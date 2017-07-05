package com.lib.mapbox;

import com.lib.utils.Stater;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public abstract class SpItem implements ILayerItem {
    protected final static int CLICK_ABLE = 1;
    protected final static int VISIBLE = 2;
    protected final static int ADDED = 4;

    protected final Stater mStater = new Stater();
    protected SpItemGroup mParent;
    protected MapboxMap mMapboxMap;
    protected MapView mMapView;
    protected OnListeners mOnListeners;

    private StyleChooser mItemStyleChooser;

    /**
     * 设置是否能点击,默认为true
     *
     * @param isEnable
     */
    public void setClickable(boolean isEnable) {
        if (isEnable) {
            mStater.addState(CLICK_ABLE);
        } else {
            mStater.removeState(CLICK_ABLE);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            addState(VISIBLE);
        } else {
            removeState(VISIBLE);
        }
    }

    final void setParent(SpItemGroup parent) {
        mParent = parent;
        setClickable(true);
        setVisible(true);
        onSetParent(parent);
    }

    protected void onSetParent(SpItemGroup parent) {

    }

    /**
     * 添加到Mapbox
     */
    public void addToMapBox() {
        addState(ADDED);
    }

    /**
     * 从Mapbox移除
     */
    public void removeFromMapbox() {
        removeState(ADDED);
    }

    final void setMapbox(MapView mapView, MapboxMap mapboxMap) {
        mMapView = mapView;
        mMapboxMap = mapboxMap;
        onSetMapbox(mapView, mapboxMap);
        addState(VISIBLE);
    }

    protected void onSetMapbox(MapView mapView, MapboxMap mapboxMap) {
    }

    @Override
    public boolean isClickable() {
        return mStater.hasState(CLICK_ABLE);
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    public MapboxMap getMapboxMap() {
        return mMapboxMap;
    }

    void zoomLevelChanged(double oldZoom, double newZoom){

    }

    @Override
    public boolean clicked(LatLng latLng) {
        if (isClickable() && contains(latLng)) {
            boolean isClicked = false;
            if (null != mOnListeners) {
                isClicked = mOnListeners.onClicked(this, latLng);
            }
            if (!isClicked) {
                isClicked = onClicked(latLng);
            }
            if (isClicked) {
                mParent.childClicked(this,latLng);
                return true;
            }
        }
        return false;
    }

    protected boolean onClicked(LatLng latLng) {
        return false;
    }

    @Override
    public boolean isLongClickable() {
        return false;
    }

    @Override
    public boolean LongClicked() {
        return false;
    }

    @Override
    public boolean contains(LatLng latLng) {
        return getItemRect().contains(latLng.getLongitude(), latLng.getLatitude());
    }

    protected abstract void onAddState(int state);

    /**
     * 添加状态
     *
     * @param state
     */
    protected void addState(int state) {
        if (!mStater.hasState(state)) {
            mStater.addState(state);
            onAddState(state);
        }
    }

    /**
     * @param state
     */
    protected abstract void onRemoveState(int state);


    /**
     * 移除状态
     *
     * @param state
     */
    protected final void removeState(int state) {
        if (mStater.hasState(state)) {
            onRemoveState(state);
            mStater.removeState(state);
        }
    }

    @Override
    public void setItemStyleChooser(StyleChooser chooser) {
        mItemStyleChooser = chooser;
        if (null != mItemStyleChooser) {
            mItemStyleChooser.setLayerItem(this);
        }
    }

    @Override
    public void changeItemStyle(String style) {
        if (null != mItemStyleChooser) {
            mItemStyleChooser.changeStyle(style);
        }
    }

    public void setOnListners(OnListeners l) {
        mOnListeners = l;
    }


    /**
     * 是否与Key相同
     *
     * @param key
     * @return
     */
    public boolean isSameFromKey(Object key) {
        return equals(key);
    }

}