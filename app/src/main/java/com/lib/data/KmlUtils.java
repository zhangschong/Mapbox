package com.lib.data;

/**
 * $desc
 */

public class KmlUtils {

    public static String toString(Object... objs) {
        StringBuilder builder = new StringBuilder();
        for (Object obj : objs) {
            builder.append(obj);
        }
        return builder.toString();
    }

}
