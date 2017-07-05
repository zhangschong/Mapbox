package com.lib.data.kml;

import com.lib.data.DataPolygon;
import com.lib.data.KMLLatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a KML Polygon. Contains a single array of outer boundary coordinates and an array of
 * arrays for the inner boundary coordinates.
 */
public class KmlPolygon implements DataPolygon{

    public static final String GEOMETRY_TYPE = "Polygon";

    private final List<KMLLatLng> mOuterBoundaryCoordinates;

    private final List<List<KMLLatLng>> mInnerBoundaryCoordinates;

    /**
     * Creates a new KmlPolygon object
     *
     * @param outerBoundaryCoordinates single array of outer boundary coordinates
     * @param innerBoundaryCoordinates multiple arrays of inner boundary coordinates
     */
    public KmlPolygon(List<KMLLatLng> outerBoundaryCoordinates,
            List<List<KMLLatLng>> innerBoundaryCoordinates) {
        if (outerBoundaryCoordinates == null) {
            throw new IllegalArgumentException("Outer boundary coordinates cannot be null");
        } else {
            mOuterBoundaryCoordinates = outerBoundaryCoordinates;
            mInnerBoundaryCoordinates = innerBoundaryCoordinates;
        }
    }

    /**
     * Gets the type of geometry
     *
     * @return type of geometry
     */
    public String getGeometryType() {
        return GEOMETRY_TYPE;
    }

    /**
     * Gets the coordinates of the Polygon
     *
     * @return ArrayList of an ArrayList of KMLLatLng points
     */
    public List<List<KMLLatLng>> getGeometryObject() {
        List<List<KMLLatLng>> coordinates = new ArrayList<>();
        coordinates.add(mOuterBoundaryCoordinates);
        //Polygon objects do not have to have inner holes
        if (mInnerBoundaryCoordinates != null) {
            coordinates.addAll(mInnerBoundaryCoordinates);
        }
        return coordinates;
    }

    /**
     * Gets an array of outer boundary coordinates
     *
     * @return array of outer boundary coordinates
     */
    public List<KMLLatLng> getOuterBoundaryCoordinates() {
        return mOuterBoundaryCoordinates;
    }

    /**
     * Gets an array of arrays of inner boundary coordinates
     *
     * @return array of arrays of inner boundary coordinates
     */
    public List<List<KMLLatLng>> getInnerBoundaryCoordinates() {
        return mInnerBoundaryCoordinates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n outer coordinates=").append(mOuterBoundaryCoordinates);
        sb.append(",\n inner coordinates=").append(mInnerBoundaryCoordinates);
        sb.append("\n}\n");
        return sb.toString();
    }
}
