package com.sorbSoft.CabAcademie.payload.stripe;

import lombok.Data;

@Data
public class UpdatePostBody {

    private String subscriptionId;
    private String newPriceId;

}
