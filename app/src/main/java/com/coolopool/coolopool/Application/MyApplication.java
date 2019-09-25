package com.coolopool.coolopool.Application;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.coolopool.coolopool.R;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MyApplication extends Application {


    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static Context context;
    private static FirebaseFirestoreSettings settings;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/glacial_indifference_regular");
    }

    public static Context getAppContext(){
        return MyApplication.context;
    }

    public static FirebaseFirestoreSettings getSettings() {
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        return settings;
    }
}
