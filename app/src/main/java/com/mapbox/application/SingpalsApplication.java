package com.mapbox.application;

import android.app.Application;

import com.singpals.manager.net.INetManager;
import com.singpals.manager.user.UserManager;
//import android.support.multidex.MultiDex;

/**
 * Created by zhanghong on 17-6-2.
 */

public class SingpalsApplication extends Application {

    private INetManager mNetManager;

    @Override
    public void onCreate() {
        super.onCreate();
//        MultiDex.install(this);
        INetManager.Factory.init(this);
        UserManager.init(this);
    }

}
