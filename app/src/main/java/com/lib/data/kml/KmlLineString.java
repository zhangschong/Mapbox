package com.lib.data.kml;

import com.lib.data.KMLLatLng;
import com.lib.data.LineString;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a KML LineString. Contains a single array of coordinates.
 */
public class KmlLineString extends LineString {

    /**
     * Creates a new KmlLineString object
     *
     * @param coordinates array of coordinates
     */
    public KmlLineString(ArrayList<KMLLatLng> coordinates) {
        super(coordinates);
    }

    /**
     * Gets the coordinates
     *
     * @return ArrayList of KMLLatLng
     */
    public ArrayList<KMLLatLng> getGeometryObject() {
        List<KMLLatLng> coordinatesList = super.getGeometryObject();
        return new ArrayList<>(coordinatesList);
    }
}
