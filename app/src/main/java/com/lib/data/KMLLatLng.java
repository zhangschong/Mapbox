//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lib.data;

public final class KMLLatLng {
    public final double latitude;
    public final double longitude;
    public final double altitude;

    public KMLLatLng(double latitude, double longitude, double altitude) {
        if(-180.0D <= longitude && longitude < 180.0D) {
            this.longitude = longitude;
        } else {
            this.longitude = ((longitude - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D;
        }

        this.latitude = Math.max(-90.0D, Math.min(90.0D, latitude));
        this.altitude = altitude;
    }

    public KMLLatLng(double latitude, double longitude) {
        this(latitude, longitude,0);
    }

    public int hashCode() {
        long var3 = Double.doubleToLongBits(this.latitude);
        int var5 = 31 + (int)(var3 ^ var3 >>> 32);
        var3 = Double.doubleToLongBits(this.longitude);
        var5 = 31 * var5 + (int)(var3 ^ var3 >>> 32);
        return var5;
    }

    public boolean equals(Object var1) {
        if(this == var1) {
            return true;
        } else if(!(var1 instanceof KMLLatLng)) {
            return false;
        } else {
            KMLLatLng var2 = (KMLLatLng)var1;
            return Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(var2.latitude) && Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(var2.longitude);
        }
    }

    public String toString() {
        double var1 = this.latitude;
        double var3 = this.longitude;
        return (new StringBuilder(60)).append("lat/lng: (").append(var1).append(",").append(var3).append(")").toString();
    }
}
