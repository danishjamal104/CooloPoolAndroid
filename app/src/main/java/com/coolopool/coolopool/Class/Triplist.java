package com.coolopool.coolopool.Class;

public class Triplist {

    private String mTripPic;
    private String mTripPlace, mNoOfTripDays;

    public Triplist(String TripPic, String TripPlace, String NoofTripDays){
        mTripPic = TripPic;
        mTripPlace = TripPlace;
        mNoOfTripDays = NoofTripDays;
    }

    public String getmTripPic(){return mTripPic;}

    public String getmTripPlace() {return mTripPlace;}

    public String getmNoOfTripDays() {return mNoOfTripDays;}

}
