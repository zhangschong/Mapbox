package com.singpals.manager.net.data;

import android.text.TextUtils;

import com.lib.http.mock.MockRequester;

/**
 * Created by zhanghong on 17-7-3.
 */

public class UserData {

    @MockRequester.MockString("testUuid")
    private String token;
    @MockRequester.MockString("testName")
    private String userName;

    public void setUserData(UserData userData){
        token = userData.token;
        userName = userData.userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLogin(){
        return !TextUtils.isEmpty(token);
    }
}
