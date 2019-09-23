package com.coolopool.coolopool.Class;

import android.widget.Button;

public class followList{

    private String mUserProfilePic;
    private String mUserName, mFullName;

    public  followList(String userProfilePic, String userName, String userFullName){
        mUserProfilePic = userProfilePic;
        mUserName = userName;
        mFullName = userFullName;
    }

    public String getmUserProfilePic(){return mUserProfilePic;}
    public  String getmUserName(){return mUserName;}
    public  String getmFullName(){return  mFullName;}

    public void setmUserProfilePic(String mUserProfilePic) {
        this.mUserProfilePic = mUserProfilePic;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }
}
