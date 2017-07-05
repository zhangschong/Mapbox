package com.singpals.manager.user;

import android.content.Context;

import com.lib.http.IHttpRequester;
import com.lib.http.RequestCall;
import com.lib.mthdone.utils.IManager;
import com.singpals.manager.net.INetManager;
import com.singpals.manager.net.data.UserData;

import java.util.HashSet;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;

/**
 * Created by zhanghong on 17-7-3.
 */

public class UserManager implements IManager {

    private static UserManager sUserManager;

    public final static void init(Context context){
        sUserManager = new UserManager();
        sUserManager.init();
    }

    public final static UserManager getUserManager(){
        return sUserManager;
    }



    private UserData mUserData = new UserData();

    private HashSet<OnUserManagerWatcher> mUserManagerWatchers = new HashSet<>(12);

    private INetManager mNetManager = INetManager.Factory.getNetManager();

    @Override
    public void init() {

    }

    /**
     * 用户登录
     * @param userName
     * @param psw
     */
    public void userLogin(String userName, String psw) {
        mNetManager.userLogin(mLoginCallback, userName, psw);
    }

    public void registerOnUserManagerWatcher(OnUserManagerWatcher watcher) {
        MethodDone.doIt(this, "registerOnUserManagerWatcherInner", watcher);
    }

    /**
     * 主线程中注册
     * @param watcher
     */
    private void registerOnUserManagerWatcherInner(OnUserManagerWatcher watcher) {
        mUserManagerWatchers.add(watcher);
    }

    public void unregisterOnUserManagerWatcher(OnUserManagerWatcher watcher) {
        MethodDone.doIt(this, "unregisterOnUserManagerWatcherInner", watcher);
    }

    /**
     * 主线程中注销
     * @param watcher
     */
    private void unregisterOnUserManagerWatcherInner(OnUserManagerWatcher watcher) {
        mUserManagerWatchers.remove(watcher);
    }

    @Override
    public void recycle() {

    }

    private IHttpRequester.Callback<UserData> mLoginCallback = new IHttpRequester.Callback<UserData>() {
        @Override
        public void onResponse(RequestCall<UserData> call) {
            mUserData.setUserData((UserData) call.getData());
            for(OnUserManagerWatcher watcher: mUserManagerWatchers){
                watcher.onUserLogin();
                watcher.onUserDataUpdated(mUserData);
            }
        }

        @Override
        public void onResponseErr(RequestCall<UserData> call, IHttpRequester.RepErrMsg msg) {

        }
    };

    public static interface OnUserManagerWatcher {

        void onUserLogin();

        void onUserDataUpdated(UserData userData);

    }
}
