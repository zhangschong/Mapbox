package com.singpals.mapbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.mapbox.services.commons.utils.TextUtils;
import com.singpals.manager.gaea.GaeaManager;
import com.singpals.manager.gaea.IUserManager;
import com.singpals.manager.net.data.UserData;
import com.singpals.mapbox.map.MapActivity;

/**
 * Created by zhanghong on 17-7-3.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> {

    private IUserManager mUserManager = GaeaManager.getGaeaManager().getProxyInstance(IUserManager.class);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new LoginPresenter(), savedInstanceState);
        mUserManager.registerOnUserManagerWatcher(mPresenter);
    }

    @Override
    protected void onDestroy() {
        mUserManager.unregisterOnUserManagerWatcher(mPresenter);
        super.onDestroy();
    }

    protected void userLogin(String userName, String psw) {
        mUserManager.userLogin(userName, psw);
    }

    protected void startMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

}

class LoginPresenter extends IBaseActPresenter.BaseActPresenter<LoginActivity> implements IUserManager.OnUserManagerWatcher {

    @Override
    protected void onActivityCreated(LoginActivity activity, Bundle savedInstanceState) {
        activity.setContentView(R.layout.user_login);

        TextView number = findViewById(R.id.et_phone_number);
        TextView psw = findViewById(R.id.et_psw);

        number.addTextChangedListener(mTextWatcher);
        psw.addTextChangedListener(mTextWatcher);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView number = findViewById(R.id.et_phone_number);
                TextView psw = findViewById(R.id.et_psw);
                mActivity.userLogin(number.getText().toString(), psw.getText().toString());
            }
        });
    }


    @Override
    public void onDestroy() {
        TextView number = findViewById(R.id.et_phone_number);
        TextView psw = findViewById(R.id.et_psw);

        number.removeTextChangedListener(mTextWatcher);
        psw.removeTextChangedListener(mTextWatcher);
        super.onDestroy();
    }

    @Override
    public void onUserLogin() {
        mActivity.startMapActivity();
    }

    @Override
    public void onUserDataUpdated(UserData userData) {

    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            findViewById(R.id.btn_login).setEnabled(isLoginBtnEnable());
        }
    };

    private boolean isLoginBtnEnable() {
        TextView number = findViewById(R.id.et_phone_number);
        TextView psw = findViewById(R.id.et_psw);
        return !(TextUtils.isEmpty(number.getText()) || TextUtils.isEmpty(psw.getText()));
    }
}
