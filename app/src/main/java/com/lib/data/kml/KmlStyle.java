package com.lib.data.kml;

import android.graphics.Color;

import com.lib.data.Style;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Represents the defined styles in the KML document
 */
public class KmlStyle extends Style {

    private final static int HSV_VALUES = 3;

    private final static int HUE_VALUE = 0;

    private ArrayList<StyleItem> mStyleItems = new ArrayList<>(5);

    private String mStyleId;


    void setStyleId(String styleId) {
        mStyleId = styleId;
    }

    public String getStyleId() {
        return mStyleId;
    }

    /**
     * Creates a new KmlStyle object
     */
    /* package */ KmlStyle() {
        super();
    }

    public void addStyleItem(StyleItem item) {
        mStyleItems.add(item);
    }


    public <T extends StyleItem> T getItem(Class<T> cls) {
        for (StyleItem item : mStyleItems) {
            if (cls.isAssignableFrom(item.getClass())) {
                return (T) item;
            }
        }
        return null;
    }

    /**
     * Gets the hue value from a color
     *
     * @param integerColor Integer representation of a color
     * @return Hue value from a color
     */
    private static float getHueValue(int integerColor) {
        float[] hsvValues = new float[HSV_VALUES];
        Color.colorToHSV(integerColor, hsvValues);
        return hsvValues[HUE_VALUE];
    }

    /**
     * Converts a color format of the form AABBGGRR to AARRGGBBBalloonStyle
     *
     * @param color Color of the form AABBGGRR
     * @return Color of the form AARRGGBB
     */
    private static String convertColor(String color) {
        String newColor;
        if (color.length() > 6) {
            newColor = color.substring(0, 2) + color.substring(6, 8)
                    + color.substring(4, 6) + color.substring(2, 4);
        } else {
            newColor = color.substring(4, 6) + color.substring(2, 4) +
                    color.substring(0, 2);
        }
        // Maps exports KML colors with a leading 0 as a space.
        if (newColor.substring(0, 1).equals(" ")) {
            newColor = "0" + newColor.substring(1, newColor.length());
        }
        return newColor;
    }

    /**
     * Computes a random color given an integer. Algorithm to compute the random color can be
     * found in https://developers.google.com/kml/documentation/kmlreference#colormode
     *
     * @param color Color represented as an integer
     * @return Integer representing a random color
     */
    public static int computeRandomColor(int color) {
        Random random = new Random();
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        //Random number can only be computed in range [0, n)
        if (red != 0) {
            red = random.nextInt(red);
        }
        if (blue != 0) {
            blue = random.nextInt(blue);
        }
        if (green != 0) {
            green = random.nextInt(green);
        }
        return Color.rgb(red, green, blue);
    }

    public static class StyleItem {
        private String mColorModel;

        StyleItem() {
        }

        void setColorMode(String colorMode) {
            mColorModel = colorMode;
        }

        public String getColorModel() {
            return mColorModel;
        }
    }

    public static class IconStyle extends StyleItem {

        private String mIconUrl;
        private float mHeading;
        private float mSpotX, mSpotY;
        private String mSpotXUnits, mSpotYUnits;
        private double mScale = 1.0d;
        private int mMarkerColor = Color.BLACK;

        IconStyle() {
        }

        void setIconUrl(String iconUrl) {
            mIconUrl = iconUrl;
        }

        void setHeading(float heading) {
            mHeading = heading;
        }

        void setHotSpot(float x, float y, String xUnits, String yUnits) {
            mSpotX = x;
            mSpotY = y;
            mSpotXUnits = xUnits;
            mSpotYUnits = yUnits;
        }

        void setIconScale(double scale) {
            mScale = scale;
        }

        void setMarkerColor(String color) {
            mMarkerColor = Color.parseColor("#" + convertColor(color));
        }

        public String getIconUrl() {
            return mIconUrl;
        }

        public float getHeading() {
            return mHeading;
        }

        public float getSpotX() {
            return mSpotX;
        }

        public float getSpotY() {
            return mSpotY;
        }

        public String getSpotXUnits() {
            return mSpotXUnits;
        }

        public String getSpotYUnits() {
            return mSpotYUnits;
        }

        public double getScale() {
            return mScale;
        }

        public int getMarkerColor() {
            return mMarkerColor;
        }
    }

    public static class LineStyle extends StyleItem {
        LineStyle() {
        }

        private int mOutLineColor = Color.BLACK;
        private float mStokenWidth = 1.0f;

        /**
         * Sets the outline color for a Polyline and a Polygon using a String
         *
         * @param color Outline color for a Polyline and a Polygon represented as a String
         */
        void setOutlineColor(String color) {
            // Add # to allow for mOutline color to be parsed correctly
            mOutLineColor = Color.parseColor("#" + convertColor(color));
        }

        /**
         * Sets the line width for a Polyline and a Polygon
         *
         * @param width Line width for a Polyline and a Polygon
         */
        void setWidth(Float width) {
            mStokenWidth = width;
        }

        public int getOutLineColor() {
            return mOutLineColor;
        }

        public float getStokenWidth() {
            return mStokenWidth;
        }
    }

    public static class PolyStyle extends StyleItem {
        private boolean mFill = true;
        private boolean mOutline = true;
        private int mFillColor = Color.BLACK;

        PolyStyle() {
        }

        /**
         * Sets whether the Polygon has an outline
         *
         * @param outline True if the polygon outline is set, false otherwise
         */
        void setOutline(boolean outline) {
            mOutline = outline;
        }

        /**
         * Sets whether the Polygon has a fill
         *
         * @param fill True if the polygon fill is set, false otherwise
         */
        void setFill(boolean fill) {
            mFill = fill;
        }

        /**
         * Sets the fill color for a KML Polygon using a String
         *
         * @param color Fill color for a KML Polygon as a String
         */
        void setFillColor(String color) {
            // Add # to allow for mOutline color to be parsed correctly
            mFillColor = (Color.parseColor("#" + convertColor(color)));
        }

        public boolean isFill() {
            return mFill;
        }

        public boolean isOutline() {
            return mOutline;
        }

        public int getFillColor() {
            return mFillColor;
        }
    }

    public static class BalloonStyle extends StyleItem {
        private String mText;

        BalloonStyle() {
        }

        /**
         * Sets text found for an info window
         *
         * @param text Text for an info window
         */
        void setInfoWindowText(String text) {
            mText = text;
        }

        @Override
        @Deprecated
        void setColorMode(String colorMode) {
        }

        public String getText() {
            return mText;
        }
    }

}
