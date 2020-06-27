package com.sorbSoft.CabAcademie.payload.stripe;

import lombok.Data;

@Data
public class CreateSubscriptionBodyRequest {

    private String customerId;
    private String priceId;
    private String paymentMethodId;
}
