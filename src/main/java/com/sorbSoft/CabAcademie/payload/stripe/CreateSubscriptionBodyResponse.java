package com.sorbSoft.CabAcademie.payload.stripe;

import lombok.Data;

@Data
public class CreateSubscriptionBodyResponse {

    private String subscriptionId;

    private String stripeStatus;
}
