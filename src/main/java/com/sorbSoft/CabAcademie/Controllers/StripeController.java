package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotAddRequestModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotsGetRequestModel;
import com.sorbSoft.CabAcademie.Services.StripeService;
import com.sorbSoft.CabAcademie.exception.EmptyValueException;
import com.sorbSoft.CabAcademie.exception.PaymentException;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
import com.sorbSoft.CabAcademie.payload.stripe.*;
import com.stripe.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/payment")
public class StripeController {

    @Autowired
    private StripeService stripeService;


    @PostMapping(value = "/createCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateCustomerBodyResponse> createCustomer(@Valid @RequestBody CreateCustomerBodyRequest postBody) throws PaymentException, UserNotFoundExcepion, EmptyValueException {

        CreateCustomerBodyResponse response = stripeService.createCustomer(postBody);

        return  new ResponseEntity<CreateCustomerBodyResponse>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/get-public-key", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPublicKey() {

        String stripePublicKey = stripeService.getPublicKey();

        return  new ResponseEntity<>(stripePublicKey, HttpStatus.OK);
    }


    @PostMapping(value = "/createSubscription", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateSubscriptionBodyResponse> createSubscription(
            @Valid @RequestBody CreateSubscriptionBodyRequest postBody) throws PaymentException {

        CreateSubscriptionBodyResponse response = stripeService.createSubscription(postBody);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping(value = "/cancelSubscription", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateSubscriptionBodyResponse> cancelSubscription(@Valid @RequestBody CancelPostBody postBody)
            throws PaymentException, UserNotFoundExcepion, EmptyValueException {

        CreateSubscriptionBodyResponse response = stripeService.cancelSubscription(postBody);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*@PostMapping(value = "/updateSubscription", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> updateSubscription(@Valid @RequestBody List<SlotAddRequestModel> timeSlotVm){

        return  new ResponseEntity<>(MessageResponse.of("Appointment Slots Saved Successfully"), HttpStatus.OK);
    }*/

    @PostMapping(value = "/retrieve-customer-payment-method", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> retreiveCustomerPaymentMethod(
            @Valid @RequestBody PaymentMethodBody postBody) throws PaymentException {

        String json = stripeService.retrieveCustomerPaymentMethod(postBody);

        return  new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping(value = "/get-subscription-status/{subscriptionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSubscriptionStatus(@PathVariable("subscriptionId") String subscriptionId) throws PaymentException {

        if(subscriptionId == null || subscriptionId.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String status = stripeService.getSubscriptionStatus(subscriptionId);

        return  new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping(value = "/get-subscription-until-date/{subscriptionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Date> getSubscriptionUntilDate(@PathVariable("subscriptionId") String subscriptionId) throws PaymentException {

        if(subscriptionId == null || subscriptionId.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Date date = stripeService.getSubscriptionUntilDate(subscriptionId);

        return  new ResponseEntity<>(date, HttpStatus.OK);
    }
}
