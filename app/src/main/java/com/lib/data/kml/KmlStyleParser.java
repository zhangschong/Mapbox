package com.lib.data.kml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

/**
 * Parses the styles of a given KML file into a KmlStyle object
 */
/* package */ class KmlStyleParser {

    private final static String STYLE_TAG = "styleUrl";

    private final static String ICON_STYLE_HEADING = "heading";

    private final static String ICON_STYLE_URL = "Icon";

    private final static String ICON_STYLE_SCALE = "scale";

    private final static String ICON_STYLE_HOTSPOT = "hotSpot";

    private final static String COLOR_STYLE_COLOR = "color";

    private final static String COLOR_STYLE_MODE = "colorMode";

    private final static String STYLE_MAP_KEY = "key";

    private final static String STYLE_MAP_NORMAL_STYLE = "normal";

    private final static String LINE_STYLE_WIDTH = "width";

    private final static String POLY_STYLE_OUTLINE = "outline";

    private final static String POLY_STYLE_FILL = "fill";

    /**
     * Parses the IconStyle, LineStyle and PolyStyle tags into a KmlStyle object
     */
    /* package */
    static KmlStyle createStyle(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        KmlStyle styleProperties = new KmlStyle();
        setStyleId(parser.getAttributeValue(null, "id"), styleProperties);
        int eventType = parser.getEventType();
        while (!(eventType == END_TAG && parser.getName().equals("Style"))) {
            if (eventType == START_TAG) {
                if (parser.getName().equals("IconStyle")) {
                    createIconStyle(parser, styleProperties);
                } else if (parser.getName().equals("LineStyle")) {
                    createLineStyle(parser, styleProperties);
                } else if (parser.getName().equals("PolyStyle")) {
                    createPolyStyle(parser, styleProperties);
                } else if (parser.getName().equals("BalloonStyle")) {
                    createBalloonStyle(parser, styleProperties);
                }
            }
            eventType = parser.next();
        }
        return styleProperties;
    }

    /**
     * @param styleProperties
     */
    private static void setStyleId(String id, KmlStyle styleProperties) {
        if (id != null) {
            // Append # to a local styleUrl
            String styleId = "#" + id;
            styleProperties.setStyleId(styleId);
        }
    }

    /**
     * Recieves input from an XMLPullParser and assigns relevant properties to a KmlStyle.
     *
     * @param style Style to apply properties to
     * @return true if icon style has been set
     */
    private static void createIconStyle(XmlPullParser parser, KmlStyle style)
            throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        KmlStyle.IconStyle item = new KmlStyle.IconStyle();
        while (!(eventType == END_TAG && parser.getName().equals("IconStyle"))) {
            if (eventType == START_TAG) {
                if (parser.getName().equals(ICON_STYLE_HEADING)) {
                    item.setHeading(Float.parseFloat(parser.nextText()));
                } else if (parser.getName().equals(ICON_STYLE_URL)) {
                    setIconUrl(parser, item);
                } else if (parser.getName().equals(ICON_STYLE_HOTSPOT)) {
                    setIconHotSpot(parser, item);
                } else if (parser.getName().equals(ICON_STYLE_SCALE)) {
                    item.setIconScale(Double.parseDouble(parser.nextText()));
                } else if (parser.getName().equals(COLOR_STYLE_COLOR)) {
                    item.setMarkerColor(parser.nextText());
                } else if (parser.getName().equals(COLOR_STYLE_MODE)) {
                    item.setColorMode(parser.nextText());
                }
            }
            eventType = parser.next();
        }
        style.addStyleItem(item);
    }

    /**
     * Parses the StyleMap property and stores the id and the normal style tag
     */
    /* package */
    static HashMap<String, HashMap<String, String>> createStyleMap(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        HashMap<String, HashMap<String, String>> styleMaps = new HashMap();
//        // Indicates if a normal style is to be stored
//        Boolean isNormalStyleMapValue = false;
        // Append # to style id
        String styleId = "#" + parser.getAttributeValue(null, "id");
        int eventType = parser.getEventType();
        HashMap<String, String> styles = new HashMap<>();
        String key = "", value;
        while (!(eventType == END_TAG && parser.getName().equals("StyleMap"))) {
            if (eventType == START_TAG) {
                if (parser.getName().equals(STYLE_MAP_KEY)) {
                    key = parser.nextText();
                } else if (parser.getName().equals(STYLE_TAG)) {
                    value = parser.nextText();
                    if (!"".equals(key)) {
                        styles.put(key, value);
                        key = "";
                    }
                }
            }
            eventType = parser.next();
        }
        styleMaps.put(styleId, styles);
        return styleMaps;
    }

    /**
     * Sets relevant styling properties to the KmlStyle object that are found in the IconStyle tag
     * Supported tags include scale, heading, Icon, href, hotSpot
     *
     * @param style Style object to add properties to
     */
    private static void createBalloonStyle(XmlPullParser parser, KmlStyle style)
            throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        KmlStyle.BalloonStyle item = new KmlStyle.BalloonStyle();
        while (!(eventType == END_TAG && parser.getName().equals("BalloonStyle"))) {
            if (eventType == START_TAG && parser.getName().equals("text")) {
                item.setInfoWindowText(parser.nextText());
            }
            eventType = parser.next();
        }
        style.addStyleItem(item);
    }

    /**
     * Sets the icon url for the style
     *
     * @param style Style to set the icon url to
     */
    private static void setIconUrl(XmlPullParser parser, KmlStyle.IconStyle style)
            throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (!(eventType == END_TAG && parser.getName().equals(ICON_STYLE_URL))) {
            if (eventType == START_TAG && parser.getName().equals("href")) {
                style.setIconUrl(parser.nextText());
            }
            eventType = parser.next();
        }
    }

    /**
     * Sets the hot spot for the icon
     *
     * @param style Style object to apply hotspot properties to
     */
    private static void setIconHotSpot(XmlPullParser parser, KmlStyle.IconStyle style) {
        Float xValue, yValue;
        String xUnits, yUnits;
        xValue = Float.parseFloat(parser.getAttributeValue(null, "x"));
        yValue = Float.parseFloat(parser.getAttributeValue(null, "y"));
        xUnits = parser.getAttributeValue(null, "xunits");
        yUnits = parser.getAttributeValue(null, "yunits");
        style.setHotSpot(xValue, yValue, xUnits, yUnits);
    }

    /**
     * Sets relevant styling properties to the KmlStyle object that are found in the LineStyle tag
     * Supported tags include color, width
     *
     * @param style Style object to add properties to
     */
    private static void createLineStyle(XmlPullParser parser, KmlStyle style)
            throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        KmlStyle.LineStyle item = new KmlStyle.LineStyle();
        while (!(eventType == END_TAG && parser.getName().equals("LineStyle"))) {
            if (eventType == START_TAG) {
                if (parser.getName().equals(COLOR_STYLE_COLOR)) {
                    item.setOutlineColor(parser.nextText());
                } else if (parser.getName().equals(LINE_STYLE_WIDTH)) {
                    item.setWidth(Float.valueOf(parser.nextText()));
                } else if (parser.getName().equals(COLOR_STYLE_MODE)) {
                    item.setColorMode(parser.nextText());
                }
            }
            eventType = parser.next();
        }
        style.addStyleItem(item);
    }

    /**
     * Sets relevant styling properties to the KmlStyle object that are found in the PolyStyle tag
     * Supported tags include color, outline, fill
     *
     * @param style Style object to add properties to
     */
    private static void createPolyStyle(XmlPullParser parser, KmlStyle style)
            throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        KmlStyle.PolyStyle item = new KmlStyle.PolyStyle();
        while (!(eventType == END_TAG && parser.getName().equals("PolyStyle"))) {
            if (eventType == START_TAG) {
                if (parser.getName().equals(COLOR_STYLE_COLOR)) {
                    item.setFillColor(parser.nextText());
                } else if (parser.getName().equals(POLY_STYLE_OUTLINE)) {
                    item.setOutline(KmlBoolean.parseBoolean(parser.nextText()));
                } else if (parser.getName().equals(POLY_STYLE_FILL)) {
                    item.setFill(KmlBoolean.parseBoolean(parser.nextText()));
                } else if (parser.getName().equals(COLOR_STYLE_MODE)) {
                    item.setColorMode(parser.nextText());
                }
            }
            eventType = parser.next();
        }
        style.addStyleItem(item);
    }

}
