package com.ecinemax.dto;

import com.ecinemax.entity.PaymentMethod;

// What checkout.html submits to "pay". This is a MOCK payment - cardNumber/
// expiry/cvv are only used transiently to do basic validation and to derive
// the last 4 digits for display; the full values are never persisted
// anywhere and no real payment gateway is contacted.
public class PaymentRequest {

    private PaymentMethod method;
    private String cardholderName;
    private String cardNumber;
    private String expiry;
    private String cvv;

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
