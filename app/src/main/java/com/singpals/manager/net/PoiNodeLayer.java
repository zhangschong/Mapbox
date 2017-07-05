package com.singpals.manager.net;

import com.lib.mapbox.AbsDataItem;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.singpals.manager.net.data.Coordinate;
import com.singpals.manager.net.data.PoiNode;

import java.util.ArrayList;
import java.util.List;



/**
 * $desc
 */
public class PoiNodeLayer extends AbsDataItem<List<PoiNode>> {
    private final static float ICON_SIZE = 1.0f;
    private final static int RADIUS = 25;
    private final static double BOUND_ANGLE = RADIUS / (111d * 1000d);

    public static PoiNodeLayer createPoiNodeLayer(String name, List<PoiNode> nodes) {
        PoiNodeLayer pni = new PoiNodeLayer(name);
        pni.setData(nodes);
        return pni;
    }

    private final String mSourceName;
    private final String mLayerName;
    private OnPoiNodesClickedListener mOnPoiNodesClickedListener;
    private RectD mRectD = new RectD();

    private Source mSource;
    private Layer[] mLayers;

    private PoiNodeLayer(String name) {
        super(name);
        mSourceName = "source-" + name;
        mLayerName = "layer-" + name;
    }

    public void hideNodeByType(int nodeType){
        final String layerName = mLayerName + "-" + nodeType;
        Layer layer = mMapboxMap.getLayer(layerName);
        if(Property.VISIBLE.equals(layer.getVisibility())){
            layer.setProperties(PropertyFactory.visibility(Property.NONE));
        }
    }

    public final Source getSource(){
        return mSource;
    }

    public void showNodeByType(int nodeType){
        final String layerName = mLayerName + "-" + nodeType;
        Layer layer = mMapboxMap.getLayer(layerName);
        if(Property.NONE.equals(layer.getVisibility())){
            layer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
        }
    }

    @Override
    protected void onAddToMapbox() {
        mMapboxMap.addSource(mSource);
        for (Layer layer : mLayers) {
            mMapboxMap.addLayer(layer);
        }
    }

    @Override
    protected void onRemoveFromMapBox() {
        for (Layer layer : mLayers) {
            mMapboxMap.removeLayer(layer);
        }
        mMapboxMap.removeSource(mSource);
    }

    @Override
    public boolean contains(LatLng latLng) {
        double radius = mMapboxMap.getProjection().getMetersPerPixelAtLatitude(0) * BOUND_ANGLE;
        return mRectD.contains(latLng.getLongitude() - radius, latLng.getLatitude() + radius,
                latLng.getLongitude() + radius, latLng.getLatitude() - radius);
    }

    @Override
    protected boolean onClicked(LatLng latLng) {
        double radius = mMapboxMap.getProjection().getMetersPerPixelAtLatitude(0) * BOUND_ANGLE;
        return onClicked(new RectD(latLng.getLongitude() - radius, latLng.getLatitude() + radius,
                latLng.getLongitude() + radius, latLng.getLatitude() - radius));
    }

    private boolean onClicked(RectD rectD) {
        if (null == mOnPoiNodesClickedListener) {
            return false;
        }
        List<PoiNode> nodes = new ArrayList<>(mDatas.size());
        for (PoiNode node : mDatas) {
            if (rectD.contains(node.getPoint().getX(), node.getPoint().getY())) {
                nodes.add(node);
            }
        }
        return mOnPoiNodesClickedListener.onPoiNodesClicked(this, rectD, nodes);
    }




    @Override
    protected void onSetData(List<PoiNode> data) {
        List<Feature> features = new ArrayList<>(data.size());
        Coordinate point;
        for (PoiNode node : data) {
            point = node.getPoint();
            mRectD.addPoint(point.getX(), point.getY());
            features.add(node.getFeature());
        }
        FeatureCollection collection = FeatureCollection.fromFeatures(features);
        mSource = new GeoJsonSource(mSourceName, collection, new GeoJsonOptions().withCluster(true).withClusterRadius(RADIUS).withMaxZoom(18));
        mLayers = PoiNode.createLayers(mSourceName, mLayerName, ICON_SIZE, PoiNode.NODE_TYPES);
    }

    @Override
    public RectD getItemRect() {
        return mRectD;
    }


    public void setOnPoiNodesClickedListener(OnPoiNodesClickedListener l) {
        mOnPoiNodesClickedListener = l;
    }


    public static interface OnPoiNodesClickedListener {
        boolean onPoiNodesClicked(PoiNodeLayer layer, RectD rectD, List<PoiNode> nodes);
    }
}
