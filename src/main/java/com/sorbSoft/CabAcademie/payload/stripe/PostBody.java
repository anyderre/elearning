package com.sorbSoft.CabAcademie.payload.stripe;

import lombok.Data;

@Data
public class PostBody {

    private String subscriptionId;
    private String priceId;
    private String newPriceId;

}
