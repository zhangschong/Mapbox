package com.singpals.manager.gaea;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lib.http.IHttpRequester;
import com.lib.http.RequestCall;
import com.singpals.manager.net.INetManager;
import com.singpals.manager.net.data.AppVersionInfo;

/**
 * Created by zhanghong on 17-7-11.
 */

public interface IAppConfiguration {

    GaeaItemCb<AppVersionInfo> checkApkVersion(final Activity activity);
}

class AppConfigration extends GaeaManagerItem implements IAppConfiguration {

    private Context mContext;
    private INetManager mNetManager;

    AppConfigration(Context context) {
        mContext = context;
    }

    private String getApkVersion() {
        PackageManager manager = mContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            return info.versionName; // 版本名
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }

    @Override
    public GaeaItemCb<AppVersionInfo> checkApkVersion(final Activity activity) {
        final GaeaItemCb<AppVersionInfo> cb = new GaeaItemCb<>();
        mNetManager.checkAppVersion(new IHttpRequester.Callback<AppVersionInfo>() {
            @Override
            public void onResponse(RequestCall<AppVersionInfo> call) {
                AppVersionInfo info = call.getData();
                cb.dataUpdated(GaeaItemCb.DATA_TYPE_NET, info);
            }

            @Override
            public void onResponseErr(RequestCall<AppVersionInfo> call, IHttpRequester.RepErrMsg msg) {

            }
        }, getApkVersion());
        return cb;
    }

    @Override
    public void init() {

    }

    @Override
    public void recycle() {

    }

    @Override
    protected void onSetDataGaea(GaeaManager gaea) {
        mNetManager = gaea.getProxyInstance(INetManager.class);
    }
}
