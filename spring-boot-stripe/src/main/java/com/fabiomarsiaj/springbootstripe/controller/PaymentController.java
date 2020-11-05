package com.fabiomarsiaj.springbootstripe.controller;

import com.fabiomarsiaj.springbootstripe.domain.Currency;
import com.fabiomarsiaj.springbootstripe.domain.PaymentRequest;
import com.fabiomarsiaj.springbootstripe.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final StripeService paymentService;

    @PostMapping("/payment")
    public String payment(PaymentRequest paymentRequest, Model model)
            throws StripeException {
        paymentRequest.setDescription("Example charge");
        paymentRequest.setCurrency(Currency.EUR);

        Charge charge = paymentService.charge(paymentRequest);

        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("amount_received", charge.getBalanceTransaction());
        return "result";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }

}
