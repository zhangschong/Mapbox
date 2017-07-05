package com.lib.mapbox;

import android.text.TextUtils;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

public class SpItemGroup extends SpItem {

    final static int DISPATCHED = 1;
    final static int DISPATCHED_NOT_DEAL = 2;
    final static int NOT_DISPATCH = 3;

    protected RectD mRectD = new RectD();

    private final String mName;

    private OnListeners mChildListeners;

    public SpItemGroup(String name) {
        mName = name;
    }

    public String name() {
        return mName;
    }

    /**
     * 设置子Listners
     *
     * @param l
     */
    public void setChildListeners(OnListeners l) {
        mChildListeners = l;
    }

    @Override
    protected void onSetMapbox(MapView mapView, MapboxMap mapboxMap) {
        for (SpItem item : mChildren.getItems()) {
            item.setMapbox(mapView, mapboxMap);
        }
    }

    @Override
    void zoomLevelChanged(double oldZoom, double newZoom) {
        for (SpItem item : mChildren.getItems()) {
            item.zoomLevelChanged(oldZoom, newZoom);
        }
    }

    /**
     * Marker点击事件分发
     *
     * @param marker
     * @return
     */
    final int dispatchMarkerClick(Marker marker) {
        SpMarkerItem layerMarker = (SpMarkerItem) mChildren.getItem(marker);
        if (null != layerMarker) {//如果LayerMarker不为空，则事件已经被分发
            if (isClickable() && layerMarker.clicked(layerMarker.getMarker().getPosition())) {
                //如果不能点击，则不做处理
                return DISPATCHED;
            }
            return DISPATCHED_NOT_DEAL;
        } else {
            for (SpItem child : mChildren.getItems()) {
                if (child instanceof SpItemGroup) {
                    int state = ((SpItemGroup) child).dispatchMarkerClick(marker);
                    if (state != NOT_DISPATCH) {
                        return state;
                    }
                }
            }
        }
        return NOT_DISPATCH;
    }

    @Override
    public boolean clicked(LatLng latLng) {
        if (isClickable() && contains(latLng)) {//是否在点击范围内
            List<SpItem> children = mChildren.getItems();
            SpItem child;
            for (int i = children.size() - 1; i >= 0; i--) {
                child = children.get(i);
                if (child instanceof SpMarkerItem) {//marker Item在dispatchMarker方法中已经执行过
                    continue;
                }
                if (child.clicked(latLng)) {//如果有子Item被点击，则不继续传递
                    return true;
                }
            }
            if (null != mOnListeners) {
                return mOnListeners.onClicked(this, latLng);
            }
            return onClicked(latLng);
        }
        return false;
    }

    @Override
    protected boolean onClicked(LatLng latLng) {
        return super.onClicked(latLng);
    }

    @Override
    protected void onAddState(int state) {
        if (mStater.hasState(VISIBLE | ADDED)) {
            if (state == VISIBLE || state == ADDED) {
                addChildrenToMapBoxInner();
            }
        }
    }

    protected void addChildrenToMapBoxInner() {
        for (SpItem child : mChildren.getItems()) {
            child.addToMapBox();
        }
    }

    protected void removeChildrenFromMapBoxInner() {
        for (SpItem child : mChildren.getItems()) {
            child.removeFromMapbox();
        }
    }

    @Override
    protected void onRemoveState(int state) {
        if (mStater.hasState(VISIBLE | ADDED)) {
            if (state == VISIBLE || state == ADDED) {
                removeChildrenFromMapBoxInner();
            }
        }
    }

//    @Override
//    protected void onStyleChanged(Style style) {
//        //DO noting
//    }

    public void addChild(SpItem child) {
        mChildren.addItem(child);
    }

    public void addChildren(SpItem... children) {
        for (SpItem child : children) {
            addChild(child);
        }
    }

    public void addChildren(List<SpItem> children) {
        for (SpItem child : children) {
            addChild(child);
        }
    }

    protected void onAddingItem(SpItem item) {
    }

    void childClicked(SpItem item, LatLng latLng) {
        if (null != mParent) {
            mParent.childClicked(item, latLng);
        }
        if (null != mChildListeners) {
            mChildListeners.onClicked(item, latLng);
        }
    }

    protected SpItems<Object, SpItem> mChildren = new SpItems<Object, SpItem>() {

        @Override
        public void addItem(SpItem item) {
            item.setOnListners(mChildListeners);
            item.setParent(SpItemGroup.this);
            item.setMapbox(mMapView, mMapboxMap);
            mRectD.addRect(item.getItemRect());
            onAddingItem(item);
            super.addItem(item);
        }
    };

    @Override
    public RectD getItemRect() {
        return mRectD;
    }

    @Override
    public boolean isSameFromKey(Object key) {
        if (!super.isSameFromKey(key)) {
            if(TextUtils.isEmpty(mName)){
                return false;
            }
            return mName.equals(key);
        }
        return true;
    }
}
