package com.lib.mapbox;

import android.support.annotation.NonNull;

import com.lib.data.kml.SpKmlLayer;
import com.lib.mthdone.utils.IManager;
import com.lib.utils.FileSource;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;

/**
 * $desc
 */

public class SpMapBoxHelper extends SpItemGroup implements IManager, MapboxMap.OnMarkerClickListener, MapboxMap.OnMapClickListener {

    private final static int TIME_MILLS = 1500;

    private boolean isInit;
    private InnerCameraPsn mInnerCameraPsn;

    public SpMapBoxHelper(MapView mapboxView, MapboxMap mapboxMap) {
        super("SpMapBoxHelper");
        setParent(null);
        setMapbox(mapboxView, mapboxMap);
        mRectD.left = -180.0f;
        mRectD.right = 180.0f;
        mRectD.top = 90.0f;
        mRectD.bottom = -90.0f;
        mInnerCameraPsn = new InnerCameraPsn(mMapboxMap.getProjection());
    }

    public void cameraTo(double lat, double lon, double zoom){
        cameraTo(lat,lon,zoom,TIME_MILLS);
    }

    public void cameraTo(double lat, double lon, double zoom, int timeMills){
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lon)) // Sets the new camera position
                .zoom(zoom).build(); // Creates a CameraPosition from the builder
        mMapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), timeMills);
    }

    @Override
    public void init() {
        if (!isInit) {
            isInit = true;
            mMapboxMap.setOnMarkerClickListener(this);
            mMapboxMap.setOnMapClickListener(this);
            mMapboxMap.setOnCameraChangeListener(mInnerCameraPsn);
            mInnerCameraPsn.onCameraChange(mMapboxMap.getCameraPosition());
            onInit();
        }
    }

    protected void onInit() {

    }

    protected void onRecycle() {

    }

    @Override
    public void recycle() {
        if (isInit) {
            onRecycle();
            isInit = false;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int state = dispatchMarkerClick(marker);
        if (state == DISPATCHED_NOT_DEAL) {
            clicked(marker.getPosition());
        }
        return true;
    }

    @Override
    public void addChild(SpItem child) {
        super.addChild(child);
        child.addToMapBox();
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        clicked(point);
    }


    private class InnerCameraPsn implements MapboxMap.OnCameraChangeListener {
        final LatLng latLng = new LatLng();
        double zoom;
        Projection proJection;

        InnerCameraPsn(Projection projection) {
            this.proJection = projection;
        }

        void onZoomChanged(double oldZoom, double newZoom) {
            zoomLevelChanged(oldZoom, newZoom);
        }

        void onPositionChanged(LatLng oldPosition, LatLng newPosition) {

        }

        @Override
        public void onCameraChange(CameraPosition position) {
            if (position.zoom != zoom) {
                onZoomChanged(zoom, position.zoom);
                zoom = position.zoom;
            }
            if (!position.target.equals(latLng)) {
                onPositionChanged(latLng, position.target);
                latLng.setAltitude(position.target.getAltitude());
                latLng.setLatitude(position.target.getLatitude());
                latLng.setLongitude(position.target.getLongitude());
            }
        }
    }
}

