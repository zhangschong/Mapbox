package com.singpals.manager.net.data;

import com.lib.http.mock.MockRequester;

/**
 * Created by zhanghong on 17-7-3.
 */

public class UserData {

    @MockRequester.MockString("testUuid")
    private String uuid;
    @MockRequester.MockString("testName")
    private String userName;

    public void setUserData(UserData userData){
        uuid = userData.uuid;
        userName = userData.userName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
