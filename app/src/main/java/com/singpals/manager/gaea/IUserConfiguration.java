package com.singpals.manager.gaea;

import android.content.Context;

import com.lib.http.IHttpRequester;
import com.lib.http.RequestCall;
import com.lib.store.IClassStore;
import com.lib.store.IStringStore;
import com.singpals.manager.net.INetManager;
import com.singpals.manager.net.data.UserData;

import java.util.HashSet;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;

/**
 * $desc
 */

public interface IUserConfiguration {

    UserData getUserData();

    /**
     * 注册用户状态观察者
     *
     * @param watcher
     */
    void registerOnUserManagerWatcher(OnUserManagerWatcher watcher);

    /**
     * 注销用户状态观察者
     *
     * @param watcher
     */
    void unregisterOnUserManagerWatcher(OnUserManagerWatcher watcher);

    /**
     * 用户登录
     *
     * @param userName
     * @param psw
     */
    void userLogin(String userName, String psw);

    interface OnUserManagerWatcher {

        /**
         * 当用户登陆成功时调用
         */
        void onUserLogin();

        /**
         * 当前信息更改时调用
         *
         * @param userData
         */
        void onUserDataUpdated(UserData userData);

    }
}

/**
 * 默认的用户信息管理员
 */
class UserConfiguration extends GaeaManagerItem implements IUserConfiguration {

    private UserData mUserData = new UserData();

    private HashSet<OnUserManagerWatcher> mUserManagerWatchers = new HashSet<>(12);

    private INetManager mNetManager;

    private final IClassStore mClsStore;

    UserConfiguration(Context context){
        mClsStore = new IClassStore.ClsJsonStore(new IStringStore.SpStringStore(context,"UserConfiguration.sp"));
    }

    @Override
    protected void onSetDataGaea(GaeaManager gaea) {
        mNetManager = gaea.getProxyInstance(INetManager.class);
    }

    @Override
    public void userLogin(String userName, String psw) {
        mNetManager.userLogin(mLoginCallback, userName, psw);
    }

    @Override
    public UserData getUserData() {
        UserData userData = new UserData();
        userData.setUserData(mUserData);
        return userData;
    }

    @Override
    public void registerOnUserManagerWatcher(OnUserManagerWatcher watcher) {
        MethodDone.doIt(this, "registerOnUserManagerWatcherInner", watcher);
    }

    /**
     * 主线程中注册
     *
     * @param watcher
     */
    private void registerOnUserManagerWatcherInner(OnUserManagerWatcher watcher) {
        mUserManagerWatchers.add(watcher);
    }

    @Override
    public void unregisterOnUserManagerWatcher(OnUserManagerWatcher watcher) {
        MethodDone.doIt(this, "unregisterOnUserManagerWatcherInner", watcher);
    }

    /**
     * 主线程中注销
     *
     * @param watcher
     */
    private void unregisterOnUserManagerWatcherInner(OnUserManagerWatcher watcher) {
        mUserManagerWatchers.remove(watcher);
    }

    private IHttpRequester.Callback<UserData> mLoginCallback = new IHttpRequester.Callback<UserData>() {
        @Override
        public void onResponse(RequestCall<UserData> call) {
            mUserData.setUserData((UserData) call.getData());
            mClsStore.store(mUserData);
            for (OnUserManagerWatcher watcher : mUserManagerWatchers) {
                watcher.onUserLogin();
                watcher.onUserDataUpdated(mUserData);
            }
        }

        @Override
        public void onResponseErr(RequestCall<UserData> call, IHttpRequester.RepErrMsg msg) {

        }
    };

    @Override
    public void init() {
        UserData userData =mClsStore.get(UserData.class);
        if(null != userData){
            mUserData.setUserData(userData);
        }
    }

    @Override
    public void recycle() {

    }
}
