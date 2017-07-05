package com.lib.mapbox;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class OnListeners {

    public boolean onClicked(SpItem item, LatLng latLng) {
        return false;
    }

    public boolean onLongClicked(SpItem item) {
        return false;
    }
}