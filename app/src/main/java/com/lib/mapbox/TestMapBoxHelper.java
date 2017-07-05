package com.lib.mapbox;

import android.support.annotation.NonNull;

import com.lib.data.kml.SpKmlLayer;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.lib.utils.FileSource;

/**
 * $desc
 */

public class TestMapBoxHelper extends SpMapBoxHelper {

    private SpItem mLastSelctedItem;

    public TestMapBoxHelper(MapView mapboxView, MapboxMap mapboxMap) {
        super(mapboxView, mapboxMap);
    }

    @Override
    protected void onInit() {
        FileSource kmlSource = FileSource.Factory.createAssetSource(mMapView.getContext(), "test2_filted.kml");

        SpItem layer = new SpKmlLayer("testLayer", kmlSource);
        setChildListeners(mListeners);
        addChildren(layer);

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(29.50, 106.72)) // Sets the new camera position
                .zoom(10)
                .build(); // Creates a CameraPosition from the builder
        mMapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (null != mLastSelctedItem) {
            mLastSelctedItem.changeItemStyle(StyleChooser.STYLE_NORMAL);
        }
        return super.onMarkerClick(marker);
    }

    private OnListeners mListeners = new OnListeners() {
        @Override
        public boolean onClicked(SpItem item, LatLng latLng) {
            item.changeItemStyle(StyleChooser.STYLE_HEIGH_LIGHT);
            mLastSelctedItem = item;
            return true;
        }
    };
}
