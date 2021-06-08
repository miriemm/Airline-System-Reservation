package com.finalproject.payment;


import com.finalproject.payment.model.dto.PaymentDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
//@RequiredArgsConstructor
public class PaymentService {

    @Value("${baseURL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;


    // will return an object containing the currency and name (in this case, the name is represented by the flight number)
    // we multiply with 100 because cent is 1/100 of basic monetary unit
    SessionCreateParams.LineItem.PriceData createPriceData(PaymentDTO paymentDTO) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount( ((long) paymentDTO.getPrice()) * 100 )
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(paymentDTO.getFlightNr().toString())
                                .build())
                .build();
    }

    // will return an object containing the quantity (here, 1) and the price
    // paymentDTO contains the details
    SessionCreateParams.LineItem createSessionLineItem(PaymentDTO paymentDTO) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(paymentDTO))
                .setQuantity(Long.parseLong(String.valueOf(1)))
                .build();
    }


    // this is the function where we create the session id for a particular payment session
    // this method will be called in paymentController
    // I will create the session taking into account the bookingID
    public Session createSession(PaymentDTO paymentDTO, Long bookingID) throws StripeException {

        String successURL = baseURL + "#/payment/success/" + String.valueOf(bookingID);
        String failedURL = baseURL + "#/payment/failed";

        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<SessionCreateParams.LineItem>();
        sessionItemsList.add(createSessionLineItem(paymentDTO));


        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failedURL)
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(successURL)
                .build();
        return Session.create(params);
    }

}