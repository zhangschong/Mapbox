package com.singpals.mapbox.map;

import android.app.Activity;
import android.os.Bundle;

import com.lib.utils.Stater;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.singpals.mapbox.BaseActivity;
import com.singpals.mapbox.IBaseActPresenter;

import static com.singpals.mapbox.map.BaseMapActivity.*;

/**
 * 用于基本的地图Activity
 */
public abstract class BaseMapActivity<T extends IBaseMapActPresenter> extends BaseActivity<T> {


    protected final static int STATE_CREATED = 1;//创建状态
    protected final static int STATE_STARTED = 2;//开始状态
    protected final static int STATE_RESUMED = 4;//返回状态
    protected final static int STATE_READY = 8;//准备完成状态

    protected Stater mStater = new Stater();
    protected MapView mMapView;
    protected MapboxMap mMapbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiemhhbmdzY2hvbmciLCJhIjoiY2oyaWtpZzBwMDFpejMzcTdrdjZ3b256MyJ9.MtZ2iu2x6aUsfByRNynw0Q");
    }

    /**
     * 当Map设置完成后会被调用,在{@link #mStater}中可以查询到状态{@link #STATE_READY}
     */
    protected void onMapReady(MapboxMap mapboxMap){}

    /**
     * 此方法需要在{@link BaseMapActPresenter#onActivityCreated(Activity, Bundle)}中调用
     *
     * @param mapView
     */
    public final void mapViewReady(MapView mapView, Bundle savedInstanceState) {
        mMapView = mapView;
        mMapView.onCreate(savedInstanceState);
        addState(STATE_CREATED);
    }

    public final void mapBoxReady(MapboxMap mapboxMap) {
        mMapbox = mapboxMap;
        onMapReady(mapboxMap);
    }

    /**
     * 添加状态
     *
     * @param state 添加的状态
     */
    protected final void addState(int state) {
        onAddingState(state);
        mStater.addState(state);
    }

    /**
     * 添加状态中,在{@link #addState(int)}中被调用,在调用{@link Stater#addState(int)}前调用
     *
     * @param state 已添加的状态
     * @return true 状态已经被处理, false 状态未被处理
     */
    protected boolean onAddingState(int state) {
        if (null != mMapView) {
            switch (state) {
                case STATE_CREATED:
                    if (mStater.hasState(STATE_STARTED)) {
                        mMapView.onStart();
                    }
                    if (mStater.hasState(STATE_RESUMED)) {
                        mMapView.onResume();
                    }
                    return true;
                case STATE_STARTED:
                    if (!mStater.hasState(STATE_STARTED)) {
                        mMapView.onStart();
                    }
                    return true;
                case STATE_RESUMED:
                    if (!mStater.hasState(STATE_RESUMED)) {
                        mMapView.onStart();
                    }
                    return true;
            }
        }
        return false;
    }

    /**
     * 移除状态后,在{@link #removeState(int)}中被调用,在调用{@link Stater#removeState(int)}前调用
     *
     * @param state 移除的state
     * @return true 状态被处理,false 状态未被处理
     */
    protected boolean onRemovingState(int state) {
        if (null != mMapView) {
            switch (state) {
                case STATE_CREATED:
                    if (mStater.hasState(STATE_CREATED)) {
                        mMapView.onDestroy();
                    }
                    return true;
                case STATE_STARTED:
                    if (mStater.hasState(STATE_STARTED)) {
                        mMapView.onStop();
                    }
                    return true;
                case STATE_RESUMED:
                    if (mStater.hasState(STATE_RESUMED)) {
                        mMapView.onResume();
                    }
                    return true;
            }
        }
        return false;
    }

    /**
     * 移除状态
     *
     * @param state 移除的状态
     */
    protected final void removeState(int state) {
        onRemovingState(state);
        mStater.removeState(state);
    }


}

class BaseMapActPresenter<T extends BaseMapActivity> extends IBaseActPresenter.BaseActPresenter<T> implements IBaseMapActPresenter<T>,OnMapReadyCallback {
    protected MapView mMapView;
    protected MapboxMap mMapbox;

    /**
     * 此方法需要在{@link #onActivityCreated(Activity, Bundle)}中调用
     *
     * @param mapView
     * @param savedInstanceState
     */
    protected final void setMapView(MapView mapView, Bundle savedInstanceState) {
        mActivity.mapViewReady(mapView, savedInstanceState);
        mMapView = mapView;
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapbox = mapboxMap;
                BaseMapActPresenter.this.onMapReady(mapboxMap);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.addState(STATE_STARTED);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.addState(STATE_RESUMED);
    }

    @Override
    public void onPause() {
        mActivity.removeState(STATE_RESUMED);
        super.onPause();
    }

    @Override
    public void onStop() {
        mActivity.removeState(STATE_STARTED);
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        mActivity.removeState(STATE_CREATED);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mActivity.addState(STATE_READY);
        mActivity.onMapReady(mapboxMap);
    }
}

