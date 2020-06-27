package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.SubscriptionPlan;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.SubscriptionPlanRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.exception.EmptyValueException;
import com.sorbSoft.CabAcademie.exception.PaymentException;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
import com.sorbSoft.CabAcademie.payload.stripe.*;
import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.PaymentMethodAttachParams;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log4j2
@Transactional
public class StripeService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenericValidator validator;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Value("${stripe.keys.secret}")
    private String API_SECRET_KEY;

    @Value("${stripe.keys.public}")
    private String API_PUBLIC_KEY;


    public StripeService() {
    }

    public CreateCustomerBodyResponse createCustomer(CreateCustomerBodyRequest postBody) throws PaymentException, UserNotFoundExcepion, EmptyValueException {

        Stripe.apiKey = API_SECRET_KEY;

        if(postBody == null) {
            throw new EmptyValueException("Create customer request can't be null");
        }

        validator.validateNull(postBody.getEmail(), "email");
        validator.validateNull(postBody.getName(), "name");


        String email = postBody.getEmail();
        User user = userRepository.findByEmail(email);
        validator.validateNull(user, "email", email);

        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("name", postBody.getName());
        customerParams.put("email", postBody.getEmail());
        Customer customer = null;
        try {

            customer = Customer.create(customerParams);
        } catch (StripeException e) {
            log.error("Fail to create Stripe customer. Stacktrace {}", e.getMessage());
            throw new PaymentException("Fail to create Stripe customer. Stacktrace "+e.getMessage());
        }

        user.setStripeId(customer.getId());
        userRepository.save(user);

        CreateCustomerBodyResponse response = new CreateCustomerBodyResponse();
        response.setCustomerId(customer.getId());
        return response;

    }

    public CreateSubscriptionBodyResponse createSubscription(CreateSubscriptionBodyRequest postBody) throws PaymentException {
        Stripe.apiKey = API_SECRET_KEY;
        Customer customer = null;
        Subscription subscription = null;
        try {
            customer = Customer.retrieve(postBody.getCustomerId());
            // Attach the payment method to the customer
            PaymentMethod pm = PaymentMethod.retrieve(postBody.getPaymentMethodId());
            pm.attach(PaymentMethodAttachParams.builder().setCustomer(customer.getId()).build());

            // Change the default invoice settings on the customer to the new payment method
            Map<String, Object> customerParams = new HashMap<String, Object>();
            Map<String, String> invoiceSettings = new HashMap<String, String>();
            invoiceSettings.put("default_payment_method", postBody.getPaymentMethodId());
            customerParams.put("invoice_settings", invoiceSettings);
            customer.update(customerParams);

            // Create the subscription
            Map<String, Object> item = new HashMap<>();
            item.put("price", postBody.getPriceId());
            Map<String, Object> items = new HashMap<>();
            items.put("0", item);
            Map<String, Object> params = new HashMap<>();
            params.put("customer", postBody.getCustomerId());
            params.put("items", items);

            List<String> expandList = new ArrayList<>();
            expandList.add("latest_invoice.payment_intent");
            params.put("expand", expandList);

            subscription = Subscription.create(params);
        } catch (CardException e) {
            throw new PaymentException("Card Exception. "+e.getMessage());
        } catch (StripeException e1) {
            throw new PaymentException("Stripe Exception. "+e1.getMessage());
        }


        if(subscription.getStatus().equalsIgnoreCase("active")) {
            String email = customer.getEmail();
            User user = userRepository.findByEmail(email);

            user.setSubscriptionId(subscription.getId());

            SubscriptionPlan plan = planRepository.findOneByPriceId(postBody.getPriceId());
            user.setSubscriptionLevel(plan.getLevel());
            user.setOrganizationType(plan.getType());
            user.setPriceId(plan.getPriceId());
            user.setProductId(plan.getProductId());

            userRepository.save(user);
        } else {
            log.error("Subscription is not active");
            throw new PaymentException("Subscription is not active");
        }

        CreateSubscriptionBodyResponse response = new CreateSubscriptionBodyResponse();
        response.setSubscriptionId(subscription.getId());
        response.setStripeStatus(subscription.getStatus());

        return response;
    }

    public CreateSubscriptionBodyResponse cancelSubscription(CancelPostBody postBody) throws PaymentException, UserNotFoundExcepion, EmptyValueException {

        Stripe.apiKey = API_SECRET_KEY;
        Subscription subscription = null;
        Subscription deletedSubscription = null;

        validator.validateNull(postBody.getSubscriptionId(), "subscriptionID");

        User user = userRepository.findBySubscriptionId(postBody.getSubscriptionId());
        validator.validateNull(user, "subscriptionID", postBody.getSubscriptionId());
        try {
            subscription = Subscription.retrieve(postBody.getSubscriptionId());
            deletedSubscription = subscription.cancel();
        } catch (StripeException e) {
            log.error("Can't cancel subscription, ID {}", postBody.getSubscriptionId());
            throw new PaymentException("Can't cancel subscription, ID" + postBody.getSubscriptionId()+ ". Stacktrace "+e.getMessage());
        }

        user.setProductId("");
        user.setPriceId("");
        user.setOrganizationType(null);
        user.setSubscriptionLevel(null);
        userRepository.save(user);

        CreateSubscriptionBodyResponse response = new CreateSubscriptionBodyResponse();
        response.setSubscriptionId(deletedSubscription.getId());
        response.setStripeStatus(deletedSubscription.getStatus());

        return response;
    }

    public String retrieveCustomerPaymentMethod(PaymentMethodBody postBody) throws PaymentException {

        PaymentMethod paymentMethod = null;
        try {
            paymentMethod = PaymentMethod.retrieve(postBody.getPaymentMethodId());
        } catch (StripeException e) {
            log.error("Can't retrieve payment method. Payment ID: {}. Stacktrace ", postBody.getPaymentMethodId(), e.getMessage());
            throw new PaymentException("Can't retrieve payment method. Payment ID: "+postBody.getPaymentMethodId()+". Stacktrace "+e.getMessage());
        }
        return paymentMethod.toJson();
    }

    public Coupon retrieveCoupon(String code) {
        try {
            Stripe.apiKey = API_SECRET_KEY;
            return Coupon.retrieve(code);
        } catch (Exception ex) {
            //ex.printStackTrace();
            log.error(ex.getMessage());
        }
        return null;
    }

    public Date getSubscriptionUntilDate(String subscriptionId) {
        Date endAtDate = null;
        try {
            Stripe.apiKey = API_SECRET_KEY;
            Subscription sub = Subscription.retrieve(subscriptionId);
            endAtDate = new Date(sub.getEndedAt());
        } catch (Exception ex) {

            //ex.printStackTrace();
            log.error(ex.getMessage());
            endAtDate = null;
        }
        return endAtDate;
    }

    public String getSubscriptionStatus(String subscriptionId) throws PaymentException {
        String status = null;
        try {
            Stripe.apiKey = API_SECRET_KEY;
            Subscription sub = Subscription.retrieve(subscriptionId);
            status = sub.getStatus();
        } catch (Exception ex) {

            //ex.printStackTrace();
           log.error("Can't get subscription status. Subscription ID:"+subscriptionId+". Stacktrace"+ex.getMessage());
           throw new PaymentException("Can't get subscription status. Subscription ID:"+subscriptionId+". Stacktrace"+ex.getMessage());
        }
        return status;
    }

    public String createCharge(String email, String token, int amount) {
        String id = null;
        try {
            Stripe.apiKey = API_SECRET_KEY;
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", amount);
            chargeParams.put("currency", "usd");
            chargeParams.put("description", "Charge for " + email);
            chargeParams.put("source", token); // ^ obtained with Stripe.js

            //create a charge
            Charge charge = Charge.create(chargeParams);
            id = charge.getId();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            //ex.printStackTrace();
        }
        return id;
    }

    public String getPublicKey() {
        return API_PUBLIC_KEY;
    }
}