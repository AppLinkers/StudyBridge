package com.example.studybridge.Subscribe;

public class Payment {

    String paymentDetail;
    String paymentCost;
    String paymentStatus;
    String user;

    public Payment(String user, String paymentDetail, String paymentCost, String paymentStatus){
        this.user = user;
        this.paymentCost = paymentCost;
        this.paymentDetail= paymentDetail;
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPaymentDetail(String paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public String getPaymentCost() {
        return paymentCost;
    }

    public void setPaymentCost(String paymentCost) {
        this.paymentCost = paymentCost;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
