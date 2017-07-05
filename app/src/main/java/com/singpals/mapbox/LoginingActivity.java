package com.singpals.mapbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.singpals.mapbox.map.MapActivity;

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
}

class LoginingPresenter extends IBaseActPresenter.BaseActPresenter<LoginingActivity> {

    @Override
    protected void onActivityCreated(LoginingActivity activity, Bundle savedInstanceState) {
        Utils.requestPermission(mActivity, MapActivity.REQUST_READ_PERMISSION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        activity.startLoginActivity();
        activity.finish();
    }
}
