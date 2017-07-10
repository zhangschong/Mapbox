package com.mapbox.application;

import android.app.Application;

import com.singpals.manager.gaea.GaeaManager;
//import android.support.multidex.MultiDex;

/**
 * Created by zhanghong on 17-6-2.
 */

public class SingpalsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GaeaManager.init(this);
    }

}
