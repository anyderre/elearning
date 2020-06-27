package com.sorbSoft.CabAcademie.payload.stripe;

import lombok.Data;

@Data
public class CreateCustomerBodyRequest {

    private String name;

    private String email;

}
