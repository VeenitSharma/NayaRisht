package com.villupuram.nayarishta;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by asdf on 12/8/2017.
 */

public class globaldata extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    public SplashPersonalDetails splashPersonalDetails = new SplashPersonalDetails();
}
