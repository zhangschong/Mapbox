package com.lib.mapbox;

/**
 * $desc
 */

public abstract class StyleChooser {

    public final static String STYLE_NORMAL = "normal";
    public final static String STYLE_HEIGH_LIGHT = "highlight";

    private SpItem mLayerItem;
    private String mCurrentStyle = STYLE_NORMAL;

    final void setLayerItem(SpItem item) {
        mLayerItem = item;
        onStyleChanged(mLayerItem, mCurrentStyle);
    }

    protected abstract void onStyleChanged(SpItem layerItem, String style);

    final void changeStyle(String styleType) {
        if(!mCurrentStyle.equals(styleType)){
            mCurrentStyle = styleType;
            onStyleChanged(mLayerItem,mCurrentStyle);
        }
    }

}
