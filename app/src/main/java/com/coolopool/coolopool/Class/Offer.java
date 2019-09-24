package com.coolopool.coolopool.Class;

public class Offer {

    String mType;
    int mAmount;
    int mExpiry;
    String code;

    public Offer(String mType, int mAmount, int mExpiry) {
        this.mType = mType;
        this.mAmount = mAmount;
        this.mExpiry = mExpiry;
    }
    public Offer(String mType, int mAmount, int mExpiry, String code) {
        this.mType = mType;
        this.mAmount = mAmount;
        this.mExpiry = mExpiry;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public int getmAmount() {
        return mAmount;
    }

    public void setmAmount(int mAmount) {
        this.mAmount = mAmount;
    }

    public void setmExpiry(int mExpiry) {
        this.mExpiry = mExpiry;
    }

    public String getmExpiry() {
        return ""+mExpiry;
    }
}
