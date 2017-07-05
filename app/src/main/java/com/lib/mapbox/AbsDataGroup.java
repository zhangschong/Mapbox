package com.lib.mapbox;

import com.lib.mthdone.IMethodDone;
import com.lib.mthdone.MethodTag;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;


/**
 * $desc
 */
public abstract class AbsDataGroup<T> extends SpItemGroup {

    protected final static int DATA_SETTED = 8;

    private T mDatas;

    public AbsDataGroup(String name) {
        super(name);
    }

    public final void setData(T data) {
        mDatas = data;
        MethodDone.doIt(this, "setDataInner", data);
    }

    @Override
    protected void onSetMapbox(MapView mapView, MapboxMap mapboxMap) {
        super.onSetMapbox(mapView, mapboxMap);

    }

    @Override
    public RectD getItemRect() {
        return new RectD();
    }

    @Override
    protected void onAddState(int state) {
        if (mStater.hasState(VISIBLE | ADDED | DATA_SETTED)) {
            if (state == VISIBLE || state == ADDED || state == DATA_SETTED) {
                addChildrenToMapBoxInner();
            }
        }
    }

    @Override
    protected void onRemoveState(int state) {
        if (mStater.hasState(VISIBLE | ADDED | DATA_SETTED)) {
            if (state == VISIBLE || state == ADDED || state == DATA_SETTED) {
                removeChildrenFromMapBoxInner();
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
