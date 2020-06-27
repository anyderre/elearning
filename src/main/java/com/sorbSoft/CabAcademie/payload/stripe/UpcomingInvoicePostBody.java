package com.sorbSoft.CabAcademie.payload.stripe;

import lombok.Data;

@Data
public class UpcomingInvoicePostBody {

    String customerId;

    String subscriptionId;

    String newPriceId;

    String subscription_trial_end;

}
