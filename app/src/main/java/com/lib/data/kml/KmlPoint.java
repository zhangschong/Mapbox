package com.lib.data.kml;

import com.lib.data.KMLLatLng;
import com.lib.data.Point;

/**
 * Represents a KML Point. Contains a single coordinate.
 */
public class KmlPoint extends Point {

    /**
     * Creates a new KmlPoint
     *
     * @param coordinates coordinates of the KmlPoint
     */
    public KmlPoint(KMLLatLng coordinates) {
        super(coordinates);
    }

}