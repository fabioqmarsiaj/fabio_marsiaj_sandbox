package com.fabiomarsiaj.springbootstripe.controller;

import com.fabiomarsiaj.springbootstripe.domain.Currency;
import com.fabiomarsiaj.springbootstripe.domain.PaymentRequest;
import com.fabiomarsiaj.springbootstripe.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final StripeService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<String> payment(PaymentRequest paymentRequest, Model model)
            throws StripeException {
        paymentRequest.setDescription("Example charge");
        paymentRequest.setCurrency(Currency.EUR);

        PaymentIntent paymentIntent = paymentService.charge(paymentRequest);

        model.addAttribute("id", paymentIntent.getId());
        model.addAttribute("status", paymentIntent.getStatus());
        model.addAttribute("chargeId", paymentIntent.getId());
        model.addAttribute("amount_received", paymentIntent.getAmountReceived());
        return ResponseEntity.ok(model.toString());
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<String> handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return ResponseEntity.ok(model.toString());
    }

}
