package com.fabiomarsiaj.springbootstripe.domain;

import lombok.Data;

@Data
public class PaymentRequest {
    private String description;
    private int amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}
