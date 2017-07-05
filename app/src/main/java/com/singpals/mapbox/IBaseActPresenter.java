package com.singpals.mapbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import com.singpals.manager.net.INetManager;

/**
 * $desc
 */

public interface IBaseActPresenter<T extends BaseActivity> {

    void setActivity(T activity, Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onLowMemory();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    <T extends View> T findViewById(int id);

    abstract class BaseActPresenter<T extends BaseActivity> implements IBaseActPresenter<T> {
        protected SparseArray<View> mViews = new SparseArray<>(20);
        protected T mActivity;
        protected INetManager mNetManager;
        @Override
        public final void setActivity(T activity, Bundle savedInstanceState) {
            mActivity = activity;
            mNetManager = activity.getNetManager();
            onActivityCreated(activity, savedInstanceState);
        }

        @Override
        public <T extends View> T findViewById(int id) {
            View view = mViews.get(id);
            if (null == view) {
                synchronized (mViews) {
                    view = mViews.get(id);
                    if (null == view) {
                        view = mActivity.findViewById(id);
                        mViews.put(id, view);
                    }
                }
            }
            return (T) view;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onLowMemory() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {

        }

        @Override
        public void onRestoreInstanceState(Bundle savedInstanceState) {

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

        }

        protected void onActivityCreated(T activity, Bundle savedInstanceState) {
        }


    }
}



