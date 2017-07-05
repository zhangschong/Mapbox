package com.lib.data.kml;

import com.lib.data.DataPolygon;
import com.lib.data.Feature;
import com.lib.data.Geometry;
import com.lib.data.KMLLatLng;
import com.lib.data.LineString;
import com.lib.data.MultiGeometry;
import com.lib.data.Point;
import com.lib.mapbox.SpItem;
import com.lib.mapbox.SpItemGroup;
import com.lib.mapbox.SpLineItem;
import com.lib.mapbox.SpMarkerItem;
import com.lib.mapbox.SpPolygonItem;
import com.lib.mapbox.StyleChooser;
import com.lib.mthdone.IMethodDone;
import com.lib.mthdone.MethodTag;
import com.lib.utils.LogUtils;
import com.lib.utils.FileSource;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;

/**
 * $desc
 */

public class SpKmlLayer extends SpItemGroup {
    private final String TAG = SpKmlLayer.class.getSimpleName();


    public static void addSpItemGroup(SpItemGroup parent, HashMap<String, HashMap<String, String>> styleMap, HashMap<String, KmlStyle> styles, KmlContainer kmlContainer) {
        styleMap.putAll(kmlContainer.getStyleMap());
        styles.putAll(kmlContainer.getStyles());
        SpItemGroup group = new SpItemGroup(kmlContainer.getContainerId());
        for (KmlContainer container : kmlContainer.getContainers()) {
            addSpItemGroup(group, styleMap, styles, container);
        }

        for (Feature feature : kmlContainer.getPlacemarks()) {
            addFeature(group, styleMap, styles, feature);
        }
        parent.addChild(group);
    }

    public static void addFeature(SpItemGroup parent, HashMap<String, HashMap<String, String>> styleMap, HashMap<String, KmlStyle> styles, Feature feature) {
        if (feature instanceof KmlPlacemark) {
            KmlPlacemark kmlPlacemark = (KmlPlacemark) feature;
            KmlStyleSelector selector = new KmlStyleSelector();
            selector.setStyleId(kmlPlacemark.getStyleId());
            KmlStyle style = kmlPlacemark.getInlineStyle();
            if (null == style) {
                style = styles.get(kmlPlacemark.getStyleId());
                if (null == style) {
                    for (Map.Entry<String, String> item : styleMap.get(kmlPlacemark.getStyleId()).entrySet()) {
                        selector.addStyle(item.getKey(), styles.get(item.getValue()));
                    }
                } else {
                    selector.addStyle(KmlStyleSelector.STYLE_NORMAL, style);
                }
            } else {
                selector.addStyle(KmlStyleSelector.STYLE_NORMAL, style);
            }
            addGeometry(parent, selector, kmlPlacemark.getGeometry());
        }
    }

    public static void addGeometry(SpItemGroup parent, StyleChooser style, Geometry geometry) {
        if (null != style) {
            if (geometry instanceof Point) {
                KMLLatLng latLng = ((Point) geometry).getGeometryObject();
                SpItem item = new SpMarkerItem(new LatLng(latLng.latitude, latLng.longitude, latLng.altitude));
                item.setItemStyleChooser(style);
                parent.addChild(item);
            } else if (geometry instanceof DataPolygon) {
                DataPolygon polygon = (DataPolygon) geometry;
                parent.addChild(createSpPolygonItem(style, polygon.getOuterBoundaryCoordinates()));
                List<List<KMLLatLng>> listLatLngArray = polygon.getInnerBoundaryCoordinates();
                if (null != listLatLngArray && !listLatLngArray.isEmpty()) {
                    for (List<KMLLatLng> lists : polygon.getInnerBoundaryCoordinates()) {
                        parent.addChild(createSpPolygonItem(style, lists));
                    }
                }
            } else if (geometry instanceof LineString) {
                LineString lineString = (LineString) geometry;
                SpItem item = new SpLineItem(coverToLatLngList(lineString.getGeometryObject()));
                item.setItemStyleChooser(style);
                parent.addChild(item);
            } else if (geometry instanceof MultiGeometry) {
                MultiGeometry multiGeometry = (MultiGeometry) geometry;
                for (Geometry geo : multiGeometry.getGeometryObject()) {
                    addGeometry(parent, style, geo);
                }
            }
        }
    }


    private static List<LatLng> coverToLatLngList(List<KMLLatLng> latLngs) {
        ArrayList<LatLng> latLngArray = new ArrayList<>(latLngs.size());
        for (KMLLatLng latLng : latLngs) {
            latLngArray.add(new LatLng(latLng.latitude, latLng.longitude, latLng.altitude));
        }
        return latLngArray;
    }


    public static SpItem createSpPolygonItem(StyleChooser style, List<KMLLatLng> latLngs) {
        SpItem item = new SpPolygonItem(coverToLatLngList(latLngs));
        item.setItemStyleChooser(style);
        return item;
    }


    private final static int PARSING = 0x10;
    private final static int PARSED = 0x30;

    private FileSource mXmlSource;


    public SpKmlLayer(String name, FileSource xmlSource) {
        super(name);
        mXmlSource = xmlSource;
    }

    @Override
    public void addToMapBox() {
        if (!mStater.hasState(PARSING)) {
            mStater.addState(PARSING);
            MethodDone.doIt(this, "parserXmlSource");
        }
        MethodDone.doIt(this, "addState", ADDED);
    }

    @Override
    public void removeFromMapbox() {
        MethodDone.doIt(this, "removeState", ADDED);
    }

    @Override
    protected void onAddState(int state) {
        if (state == VISIBLE || state == ADDED || state == PARSED) {
            if (mStater.hasState(VISIBLE | ADDED | PARSED)) {
                addChildrenToMapBoxInner();
            }
        }
    }

    @Override
    protected void onRemoveState(int state) {
        if (state == VISIBLE || state == ADDED || state == PARSED) {
            if (mStater.hasState(VISIBLE | ADDED | PARSED)) {
                removeChildrenFromMapBoxInner();
            }
        }
    }


    @MethodTag(threadType = IMethodDone.THREAD_TYPE_IO)
    private void parserXmlSource() throws Exception {
        KmlParser parser = KmlParser.parserKml(mXmlSource.getInputStream());
        HashMap<String, HashMap<String, String>> styleMap = parser.getStyleMaps();
        HashMap<String, KmlStyle> styles = parser.getStyles();

        for (KmlContainer container : parser.getContainers()) {
            addSpItemGroup(this, styleMap, styles, container);
        }

        for (Feature feature : parser.getPlacemarks()) {
            addFeature(this, styleMap, styles, feature);
        }
        MethodDone.doIt(this, "addState", PARSED);
    }
}

class KmlStyleSelector extends StyleChooser {

    private HashMap<String, KmlStyle> mStyles = new HashMap<>();
    private String mStyleId;


    void setStyleId(String styleId) {
        mStyleId = styleId;
    }

    void addStyle(String styleName, KmlStyle style) {
        mStyles.put(styleName, style);
    }

    private KmlStyle getStyle(String styleName) {
        KmlStyle style = mStyles.get(styleName);
        if (null == style) {
            LogUtils.d(true, KmlStyleSelector.class.getSimpleName(), " styleId: ", mStyleId, " styleName:", styleName);
            return mStyles.get(STYLE_NORMAL);
        }
        return style;
    }

    @Override
    protected void onStyleChanged(SpItem layerItem, String style) {
        if (null != layerItem) {
            if (layerItem instanceof SpPolygonItem) {
                SpPolygonItem item = (SpPolygonItem) layerItem;
                Polygon polygon = item.getPolygon();
                KmlStyle kmlStyle = getStyle(style);
                KmlStyle.LineStyle lStyle = kmlStyle.getItem(KmlStyle.LineStyle.class);
                if (null != lStyle) {
                    polygon.setStrokeColor(lStyle.getOutLineColor());
                }
                KmlStyle.PolyStyle pStyle = kmlStyle.getItem(KmlStyle.PolyStyle.class);
                if (null != pStyle) {
                    polygon.setFillColor(pStyle.getFillColor());
                }
            } else if (layerItem instanceof SpLineItem) {
                SpLineItem item = (SpLineItem) layerItem;
                Polyline line = item.getPolyline();
                KmlStyle kmlStyle = getStyle(style);
                KmlStyle.LineStyle lStyle = kmlStyle.getItem(KmlStyle.LineStyle.class);
                if (null != lStyle) {
                    line.setColor(lStyle.getOutLineColor());
                    line.setWidth(lStyle.getStokenWidth());
                }
            }
        }
    }
}
