package com.singpals.manager.net.data;

import com.mapbox.services.commons.models.Position;

/**
 * $desc
 */

public class Coordinate {

    private double x;
    private double y;
    private double z;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }


    public Position adaptPosition(){
        return Position.fromCoordinates(x,y,z);
    }
}
