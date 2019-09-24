package com.coolopool.coolopool.Backend.Model;

public class Coupon {

    String code;
    int expiry;
    int amount;

    public Coupon() {}

    public Coupon(String code, int expiry, int amount) {
        this.code = code;
        this.expiry = expiry;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
