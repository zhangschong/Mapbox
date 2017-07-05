package com.lib.mapbox;

import android.text.TextUtils;

import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * $desc
 */
public class SpMarkerItem extends SpItem {

    private RectD mRectD;
    protected final MarkerOptions mOptions = new MarkerOptions();

    /**
     * Creates a instance of {@link Marker} using the builder of Marker.
     *
     * @param latLng The builder used to construct the Marker.
     */
    public SpMarkerItem(LatLng latLng) {
        mOptions.setPosition(latLng);
    }

    protected void addOptions(BaseMarkerOptions options) {
        if (null != options) {
            mMapboxMap.addMarker(options);
        }
    }

    protected void removeOptions(BaseMarkerOptions options) {
        if (null != options) {
            mMapboxMap.removeMarker(options.getMarker());
        }
    }

    @Override
    public boolean isClickable() {
        if (super.isClickable()) {
            Marker marker = mOptions.getMarker();
            return !TextUtils.isEmpty(marker.getSnippet()) || !TextUtils.isEmpty(marker.getTitle());
        }
        return false;
    }

    @Override
    protected void onAddState(int state) {
        if (mStater.hasState(VISIBLE | ADDED)) {
            if (state == VISIBLE || state == ADDED) {
                addOptions(mOptions);
            }
        }
    }

    @Override
    protected void onRemoveState(int state) {
        if (mStater.hasState(VISIBLE | ADDED)) {
            if (state == VISIBLE || state == ADDED) {
                removeOptions(mOptions);
            }
        }
    }

    @Override
    public RectD getItemRect() {
        if (null == mRectD) {
            LatLng latLng = getMarker().getPosition();
            mRectD = new RectD(latLng.getLongitude(), latLng.getLatitude(), latLng.getLongitude(), latLng.getLatitude());
        }
        return mRectD;
    }

    /**
     * 获取当前的Marker
     *
     * @return
     */
    public Marker getMarker() {
        return mOptions.getMarker();
    }

    @Override
    public boolean isSameFromKey(Object key) {
        if (!super.isSameFromKey(key)) {
            return key == getMarker() || key == mOptions;
        }
        return true;
    }
}
