package com.lib.mapbox;

import android.text.TextUtils;

import com.lib.mthdone.IMethodDone;
import com.lib.mthdone.MethodTag;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;


/**
 * $desc
 */
public abstract class AbsDataItem<T> extends SpItem {

    protected final static int DATA_SETTED = 8;

    protected T mDatas;
    private final String mName;

    public AbsDataItem(String name) {
        mName = name;
    }

    public final void setData(T data) {
        mDatas = data;
        MethodDone.doIt(this, "setDataInner", data);
    }

    @Override
    public boolean isSameFromKey(Object key) {
        if (!super.isSameFromKey(key)) {
            if (TextUtils.isEmpty(mName)) {
                return false;
            }
            return mName.equals(key);
        }
        return true;
    }

    @Override
    protected void onSetMapbox(MapView mapView, MapboxMap mapboxMap) {
        super.onSetMapbox(mapView, mapboxMap);

    }

    @Override
    protected void onAddState(int state) {
        if (mStater.hasState(VISIBLE | ADDED | DATA_SETTED)) {
            if (state == VISIBLE || state == ADDED || state == DATA_SETTED) {
                onAddToMapbox();
            }
        }
    }

    protected abstract void onAddToMapbox();

    protected abstract void onRemoveFromMapBox();

    @Override
    protected void onRemoveState(int state) {
        if (mStater.hasState(VISIBLE | ADDED | DATA_SETTED)) {
            if (state == VISIBLE || state == ADDED || state == DATA_SETTED) {
                onRemoveFromMapBox();
            }
        }
    }

    protected abstract void onSetData(T data);

    @MethodTag(threadType = IMethodDone.THREAD_TYPE_THREAD)
    private void setDataInner(T data) {
        onSetData(data);
        MethodDone.doIt(this, "addState", DATA_SETTED);
    }
}
