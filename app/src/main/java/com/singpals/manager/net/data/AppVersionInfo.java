package com.singpals.manager.net.data;

import android.text.TextUtils;

/**
 * Created by zhanghong on 17-7-11.
 */

public class AppVersionInfo {

    private String notification;

    private String url;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean hasNotification(){
        return !TextUtils.isEmpty(notification);
    }
}
