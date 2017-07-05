package com.singpals.manager.net.data;

import android.graphics.Color;

import com.google.gson.annotations.Expose;
import com.lib.utils.Abandon;
import com.mapbox.mapboxsdk.style.layers.Filter;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Point;
import com.singpals.manager.icon.IconManager;
import com.singpals.manager.net.FeatureItem;

/**
 * PoiNode　数据模型
 */
public class PoiNode implements FeatureItem {
    public final static String PROPERTY_NODE_TYPE = "nodeType";
    public final static String PROPERTY_NODE_ID = "nodeId";

    private final static String CLUSTER = "cluster";
    private final static String POINT_COUNT = "point_count";
    private final static String[] FONT_STYLE = new String[]{"DIN Offc Pro Medium", "Arial Unicode MS Bold"};

    private final static int NODE_TYPE_CLUSTER = 100;
    private final static float FONT_SIZE = 12;
    private final static Float[] FONT_OFFSET = new Float[]{0f, 0.7f};

    public final static int[] NODE_TYPES = new int[]{1, 2, NODE_TYPE_CLUSTER};

    public static String getProperty(String propertyValue) {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append(propertyValue).append("}");
        return builder.toString();
    }

    public static String getPoiNodeIconName(int nodeType) {
        return IconManager.SYMBOL + nodeType;
    }

    public static Layer createLayer(int nodeType, String sourceName, String layerName, float iconSize) {
        SymbolLayer layer = new SymbolLayer(layerName + "-" + nodeType, sourceName);
        if (nodeType < NODE_TYPE_CLUSTER) {
            layer.setFilter(Filter.all(Filter.eq(PoiNode.PROPERTY_NODE_TYPE, nodeType), Filter.neq(CLUSTER, true)));
            layer.setProperties(PropertyFactory.textField(getProperty(PoiNode.PROPERTY_NODE_ID)));
        } else {
            nodeType = NODE_TYPE_CLUSTER;
            layer.setFilter(Filter.eq(CLUSTER, true));
            layer.setProperties(PropertyFactory.textField(getProperty(POINT_COUNT)));
        }
        layer.setProperties(
                PropertyFactory.iconImage(getPoiNodeIconName(nodeType)),
                PropertyFactory.iconSize(iconSize),
                PropertyFactory.textSize(FONT_SIZE),
                PropertyFactory.textColor(Color.GRAY),
                PropertyFactory.textFont(FONT_STYLE),
                PropertyFactory.textOffset(FONT_OFFSET),
                PropertyFactory.textAnchor("top"));
        return layer;
    }

    public static Layer[] createLayers(String sourceName, String layerName, float size, int... nodeTypes) {
        Layer[] layers = new Layer[nodeTypes.length];
        for (int i = nodeTypes.length - 1; i >= 0; i--) {
            layers[i] = createLayer(nodeTypes[i], sourceName, layerName, size);
        }
        return layers;
    }


    private String nodeId;
    private Coordinate point;
    private int nodeType;
    @Abandon
    private Feature mFeature;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Coordinate getPoint() {
        return point;
    }

    public void setPoint(Coordinate point) {
        this.point = point;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public Feature getFeature() {
        if(null == mFeature) {
            Feature feature = Feature.fromGeometry(Point.fromCoordinates(point.adaptPosition()));
            feature.addNumberProperty(PROPERTY_NODE_TYPE, nodeType);
            feature.addStringProperty(PROPERTY_NODE_ID, nodeId);
            mFeature = feature;
        }
        return mFeature;
    }

}
