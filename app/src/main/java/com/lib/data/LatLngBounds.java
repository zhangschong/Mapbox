//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lib.data;

import java.util.Arrays;

public final class LatLngBounds {

    public final KMLLatLng southwest;
    public final KMLLatLng northeast;

    public LatLngBounds(KMLLatLng southwest, KMLLatLng northeast) {
        this.southwest = southwest;
        this.northeast = northeast;
    }

    public boolean contains(KMLLatLng var1) {
        return this.zzh(var1.latitude) && this.zzi(var1.longitude);
    }

    public LatLngBounds including(KMLLatLng latlng) {
        double minLatitude = Math.min(this.southwest.latitude, latlng.latitude);
        double MaxLatitude = Math.max(this.northeast.latitude, latlng.latitude);
        double maxLongitude = this.northeast.longitude;
        double minLongitude = this.southwest.longitude;
        double longitude = latlng.longitude;
        if (!this.zzi(longitude)) {
            if (zzb(minLongitude, longitude) < zzc(maxLongitude, longitude)) {
                minLongitude = longitude;
            } else {
                maxLongitude = longitude;
            }
        }

        return new LatLngBounds(new KMLLatLng(minLatitude, minLongitude), new KMLLatLng(MaxLatitude, maxLongitude));
    }

    public KMLLatLng getCenter() {
        double var1 = (this.southwest.latitude + this.northeast.latitude) / 2.0D;
        double var3 = this.northeast.longitude;
        double var5 = this.southwest.longitude;
        double var7;
        if (var5 <= var3) {
            var7 = (var3 + var5) / 2.0D;
        } else {
            var7 = (var3 + 360.0D + var5) / 2.0D;
        }

        return new KMLLatLng(var1, var7);
    }

    private static double zzb(double var0, double var2) {
        return (var0 - var2 + 360.0D) % 360.0D;
    }

    private static double zzc(double var0, double var2) {
        return (var2 - var0 + 360.0D) % 360.0D;
    }

    private boolean zzh(double var1) {
        return this.southwest.latitude <= var1 && var1 <= this.northeast.latitude;
    }

    private boolean zzi(double var1) {
        return this.southwest.longitude <= this.northeast.longitude ? this.southwest.longitude <= var1 && var1 <= this.northeast.longitude : this.southwest.longitude <= var1 || var1 <= this.northeast.longitude;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.southwest, this.northeast});
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (!(var1 instanceof LatLngBounds)) {
            return false;
        } else {
            LatLngBounds var2 = (LatLngBounds) var1;
            return this.southwest.equals(var2.southwest) && this.northeast.equals(var2.northeast);
        }
    }

    public String toString() {
        return KmlUtils.toString("southwest-", this.southwest, " northeast-", this.northeast);
    }
}
