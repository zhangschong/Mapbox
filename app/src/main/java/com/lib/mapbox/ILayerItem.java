package com.lib.mapbox;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * $desc
 */

public interface ILayerItem {
    /**
     * 当前元素是否能被点击
     *
     * @return
     */
    boolean isClickable();

    /**
     * 当前元素点被击时调用
     *
     * @return true 消费此点击事件，false 不消费此点击事件
     */
    boolean clicked(LatLng latLng);

    /**
     * 不前元素是否能长按点击
     *
     * @return
     */
    boolean isLongClickable();

    /**
     * 长按时回调
     *
     * @return true 消费此点击事件，false 不消费此点击事件
     */
    boolean LongClicked();

    /**
     * 是否包含点
     *
     * @param latLng
     * @return
     */
    boolean contains(LatLng latLng);

    /**
     * 获取所占用的位置
     *
     * @return
     */
    RectD getItemRect();

    /**
     * 获取MapView
     *
     * @return
     */
    MapView getMapView();

    /**
     * 添加到Mapbox
     */
    void addToMapBox();

    /**
     * 从Mapbox移除
     */
    void removeFromMapbox();

    /**
     * 获取MapboxMap
     *
     * @return
     */
    MapboxMap getMapboxMap();

    /**
     * 设置是否可点击
     *
     * @param isEnable
     */
    void setClickable(boolean isEnable);

    /**
     * 设置是否可见
     *
     * @param visible
     */
    void setVisible(boolean visible);

    /**
     * 设置一个风格选择者
     *
     * @param chooser
     */
    void setItemStyleChooser(StyleChooser chooser);

    /**
     * 改变item的风格
     *
     * @param style
     */
    void changeItemStyle(String style);

    /**
     * 地图坐标x:[-180,180],y:[90:-90]
     */
    class RectD {
        public double left = Double.MAX_VALUE;
        public double top = Double.MIN_VALUE;
        public double right = Double.MIN_VALUE;
        public double bottom = Double.MAX_VALUE;

        public RectD() {
        }

        public RectD(double left, double top, double right, double bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public void setRounds(double left, double top, double right, double bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public boolean contains(double x, double y) {
            return x >= this.left && x <= this.right && y <= this.top && y >= this.bottom;
        }

        public boolean contains(RectD rectD) {
            return contains(rectD.left, rectD.right, rectD.top, rectD.bottom);
        }

        public boolean contains(double l, double t, double r, double b) {
            return l <= right && r >= left && t >= bottom && b <= top;
        }

        public double centerY() {
            return (top + bottom) / 2d;
        }

        public double centerX() {
            return (right + left) / 2d;
        }

        public void addPoint(double x, double y) {
            left = x < left ? x : left;
            right = x > right ? x : right;
            top = y > top ? y : top;
            bottom = y < bottom ? y : bottom;
        }

        public void addRect(RectD rect) {
            left = rect.left < left ? rect.left : left;
            right = rect.right > right ? rect.right : right;
            top = rect.top > top ? rect.top : top;
            bottom = rect.bottom < bottom ? rect.bottom : bottom;
        }

        public double distancePower2() {
            double x = right - left;
            double y = top - bottom;
            return x * x + y * y;
        }
    }
}

