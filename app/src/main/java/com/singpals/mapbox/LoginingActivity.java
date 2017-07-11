package com.singpals.mapbox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.TextView;

import com.singpals.manager.gaea.GaeaItemCb;
import com.singpals.manager.gaea.GaeaManager;
import com.singpals.manager.gaea.IAppConfiguration;
import com.singpals.manager.gaea.IUserConfiguration;
import com.singpals.manager.net.data.AppVersionInfo;
import com.singpals.mapbox.map.MainActivity;

/**
 * $desc
 */
public class LoginingActivity extends BaseActivity<LoginingPresenter> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new LoginingPresenter(),savedInstanceState);
    }

    protected void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    protected void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

class LoginingPresenter extends IBaseActPresenter.BaseActPresenter<LoginingActivity> {

    @Override
    protected void onActivityCreated(LoginingActivity activity, Bundle savedInstanceState) {

        TextView tv = new TextView(mActivity);
        tv.setText("加载中...");
        tv.setGravity(Gravity.CENTER);
        mActivity.setContentView(tv);

        Utils.requestPermission(mActivity, MainActivity.REQUST_READ_PERMISSION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        IAppConfiguration configuration = GaeaManager.getGaeaManager().getProxyInstance(IAppConfiguration.class);
        configuration.checkApkVersion(mActivity).observer(new GaeaItemCb.Observer<AppVersionInfo>() {
            private Handler mHandler = new Handler(Looper.getMainLooper());

            private void postNextStep(){
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        IUserConfiguration userManager = GaeaManager.getGaeaManager().getProxyInstance(IUserConfiguration.class);
                        if(userManager.getUserData().isLogin()) {
                            mActivity.startMainActivity();
                            mActivity.finish();
                        }else{
                            mActivity.startLoginActivity();
                            mActivity.finish();
                        }
                    }
                },3000);
            }

            @Override
            public void onDataUpdated(int dataType, AppVersionInfo data) {
                if(null != data && data.hasNotification()){
                    //TODO add update
                }else{
                    postNextStep();
                }
            }
        });
    }
}
