package com.lib.data.kml;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Parses a given KML file into KmlStyle, KmlPlacemark, KmlGroundOverlay and KmlContainer objects
 */
public class KmlParser {

    /**
     * Creates a new XmlPullParser to allow for the KML file to be parsed
     *
     * @param stream InputStream containing KML file
     * @return XmlPullParser containing the KML file
     * @throws XmlPullParserException if KML file cannot be parsed
     */
    private static XmlPullParser createXmlParser(InputStream stream) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(stream, null);
        return parser;
    }

    public static KmlParser parserKml(InputStream stream) throws XmlPullParserException, IOException {
        if (stream == null) {
            throw new IllegalArgumentException("KML InputStream cannot be null");
        }
        XmlPullParser xmlPullParser = createXmlParser(stream);
        KmlParser parser = new KmlParser(xmlPullParser);
        parser.parseKml();
        stream.close();
        return parser;
    }

    public static KmlParser parserKml(Context context, int resourceId)
            throws XmlPullParserException, IOException {
        return parserKml(context.getResources().openRawResource(resourceId));
    }

    public static KmlParser parserKml(Context context, String assertsPath)
            throws XmlPullParserException, IOException {
        return parserKml(context.getAssets().open(assertsPath));
    }


    private final static int DEFAULT_ARRAY_SIZE = 300;


    private final static String STYLE = "Style";

    private final static String STYLE_MAP = "StyleMap";

    private final static String PLACEMARK = "Placemark";

    private final static String GROUND_OVERLAY = "GroundOverlay";

    private final static String CONTAINER_REGEX = "Folder|Document";

    private final XmlPullParser mParser;

    private final ArrayList<KmlPlacemark> mPlacemarks;

    private final ArrayList<KmlContainer> mContainers;

    private final HashMap<String, KmlStyle> mStyles;

    private final HashMap<String, HashMap<String, String>> mStyleMaps;

    private final ArrayList<KmlGroundOverlay> mGroundOverlays;

    private final static String UNSUPPORTED_REGEX = "altitude|altitudeModeGroup|altitudeMode|" +
            "begin|bottomFov|cookie|displayName|displayMode|displayMode|end|expires|extrude|" +
            "flyToView|gridOrigin|httpQuery|leftFov|linkDescription|linkName|linkSnippet|" +
            "listItemType|maxSnippetLines|maxSessionLength|message|minAltitude|minFadeExtent|" +
            "minLodPixels|minRefreshPeriod|maxAltitude|maxFadeExtent|maxLodPixels|maxHeight|" +
            "maxWidth|near|NetworkLink|NetworkLinkControl|overlayXY|range|refreshMode|" +
            "refreshInterval|refreshVisibility|rightFov|roll|rotationXY|screenXY|shape|sourceHref|" +
            "state|targetHref|tessellate|tileSize|topFov|viewBoundScale|viewFormat|viewRefreshMode|" +
            "viewRefreshTime|when";

    /**
     * Creates a new KmlParser object
     *
     * @param parser parser containing the KML file to parse
     */
    /* package */
    private KmlParser(XmlPullParser parser) {
        mParser = parser;
        mPlacemarks = new ArrayList<>(DEFAULT_ARRAY_SIZE);
        mContainers = new ArrayList<>(DEFAULT_ARRAY_SIZE);
        mStyles = new HashMap<>(DEFAULT_ARRAY_SIZE);
        mStyleMaps = new HashMap<>(DEFAULT_ARRAY_SIZE);
        mGroundOverlays = new ArrayList<>(DEFAULT_ARRAY_SIZE);
    }

    /**
     * Parses the KML file and stores the created KmlStyle and KmlPlacemark
     */
    /* package */ void parseKml() throws XmlPullParserException, IOException {
        int eventType = mParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (mParser.getName().matches(UNSUPPORTED_REGEX)) {
                    skip(mParser);
                }
                if (mParser.getName().matches(CONTAINER_REGEX)) {
                    mContainers.add(KmlContainerParser.createContainer(mParser));
                }
                if (mParser.getName().equals(STYLE)) {
                    KmlStyle style = KmlStyleParser.createStyle(mParser);
                    mStyles.put(style.getStyleId(), style);
                }
                if (mParser.getName().equals(STYLE_MAP)) {
                    mStyleMaps.putAll(KmlStyleParser.createStyleMap(mParser));
                }
                if (mParser.getName().equals(PLACEMARK)) {
                    mPlacemarks.add(KmlFeatureParser.createPlacemark(mParser));
                }
                if (mParser.getName().equals(GROUND_OVERLAY)) {
                    mGroundOverlays.add(KmlFeatureParser.createGroundOverlay(mParser));
                }
            }
            eventType = mParser.next();

        }
        //Need to put an empty new style
        mStyles.put(null, new KmlStyle());
    }

    /**
     * @return List of styles created by the parser
     */
    /* package */ HashMap<String, KmlStyle> getStyles() {
        return mStyles;
    }

    /**
     * @return A list of Kml Placemark objects
     */
    /* package */ ArrayList<KmlPlacemark> getPlacemarks() {
        return mPlacemarks;
    }

    /**
     * @return A list of Kml Style Maps
     */
    /* package */ HashMap<String, HashMap<String, String>> getStyleMaps() {
        return mStyleMaps;
    }

    /**
     * @return A list of Kml Folders
     */
    /* package */ ArrayList<KmlContainer> getContainers() {
        return mContainers;
    }

    /**
     * @return A list of Ground Overlays
     */
    /* package */ ArrayList<KmlGroundOverlay> getGroundOverlays() {
        return mGroundOverlays;
    }

    /**
     * Skips tags from START TAG to END TAG
     *
     * @param parser XmlPullParser
     */
    /*package*/
    static void skip(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
