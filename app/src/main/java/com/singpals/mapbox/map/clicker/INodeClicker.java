package com.singpals.mapbox.map.clicker;

import com.lib.mapbox.ILayerItem;
import com.lib.mapbox.SpMapBoxHelper;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.singpals.manager.net.PoiNodeLayer;
import com.singpals.manager.net.data.PoiNode;

import java.util.List;

/**
 * Created by zhanghong on 17-7-1.
 */

public interface INodeClicker extends PoiNodeLayer.OnPoiNodesClickedListener {

    void clearAll();

    class Factory {
        public final static INodeClicker createNodeClicker(MapView mapView, MapboxMap mapboxMap, SpMapBoxHelper boxHelper) {
            return new NodeClicker(mapView, mapboxMap, boxHelper);
        }
    }

}

class NodeClicker implements INodeClicker {
    private final MapView mMapView;
    private final MapboxMap mMapbox;
    private final SpMapBoxHelper mMapBoxHelper;

    private final GeoJsonSource mSource;
    private final Layer mLayer;

    NodeClicker(MapView mapView, MapboxMap mapboxMap, SpMapBoxHelper boxHelper) {
        mMapView = mapView;
        mMapbox = mapboxMap;
        mMapBoxHelper = boxHelper;
        mSource = new GeoJsonSource(NodeClicker.class.getSimpleName());
        mLayer = new CircleLayer("layer-" + NodeClicker.class.getSimpleName(), mSource.getId());
        init();
    }

    private void init() {
        mMapbox.addSource(mSource);
        mLayer.setProperties(PropertyFactory.visibility(Property.NONE),
                PropertyFactory.circleRadius(25.0f),
                PropertyFactory.circleColor(0xffffffff),
                PropertyFactory.circleOpacity(0.2f));
        mMapbox.addLayer(mLayer);
    }

    private void clicked() {
        if (Property.NONE.equals(mLayer.getVisibility().getValue())) {
            mLayer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
        }
    }

    @Override
    public boolean onPoiNodesClicked(PoiNodeLayer layer, ILayerItem.RectD rectD, List<PoiNode> nodes) {
        if (nodes.isEmpty()) {
            return false;
        }
        if (nodes.size() > 1) {
            int zoom = (int) (mMapbox.getCameraPosition().zoom + 1d + 0.05d);
            mMapBoxHelper.cameraTo(rectD.centerY(), rectD.centerX(), zoom);
        } else {
            mSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{nodes.get(0).getFeature()}));
            clicked();
        }
        return true;
    }

    @Override
    public void clearAll() {
        if (Property.VISIBLE.equals(mLayer.getVisibility().getValue())) {
            mLayer.setProperties(PropertyFactory.visibility(Property.NONE));
        }
    }
}
