package com.lib.mapbox;

import com.lib.mthdone.IMethodDone;
import com.lib.mthdone.MethodTag;
import com.lib.utils.LogUtils;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;

/**
 * $desc
 */

public class SpPolygonItem extends SpItem {

    private final static double PIXEL_20_LEVEL = 0.00000067055189 * 1.0f;
    private final static double PIXEL_20_LEVEL_POW2 = PIXEL_20_LEVEL * PIXEL_20_LEVEL;

    private final static int MAX_LEVEL = 20;
    private final static int LEVEL_STEP = 2;

    private final static int LEVEL_COUNTS = MAX_LEVEL / LEVEL_STEP;

    private RectD mRectD;

    private List<LatLng> mDefaultPoints;
    private PolygonOptions mPolygonOptions = new PolygonOptions();
    private List<LatLng>[] mPoints = new List[LEVEL_COUNTS];

    private int mCurrentLevel = LEVEL_COUNTS - 1;

    public SpPolygonItem(List<LatLng> points) {
        mDefaultPoints = points;
        mPolygonOptions.getPolygon().setPoints(mDefaultPoints);
    }


    public final Polygon getPolygon() {
        return mPolygonOptions.getPolygon();
    }

    @Override
    public RectD getItemRect() {
        if (null == mRectD) {
            mRectD = new RectD();
            for (LatLng latLng : mDefaultPoints) {
                mRectD.addPoint(latLng.getLongitude(), latLng.getLatitude());
            }
        }
        return mRectD;
    }

    @Override
    protected void onAddState(int state) {
        if (mStater.hasState(VISIBLE | ADDED)) {
            if (state == VISIBLE || state == ADDED) {
                mMapboxMap.addPolygon(mPolygonOptions);
            }
        }
    }

    @Override
    protected void onRemoveState(int state) {
        if (mStater.hasState(VISIBLE | ADDED)) {
            if (state == VISIBLE || state == ADDED) {
                mMapboxMap.removePolygon(mPolygonOptions.getPolygon());
            }
        }
    }

    @Override
    void zoomLevelChanged(double oldZoom, double newZoom) {
//        int currentLevel = (((int) newZoom) >>> 1); //newZoom/2
//        if (currentLevel > MAX_LEVEL) {
//            currentLevel = MAX_LEVEL;
//        }
//        if (currentLevel != mCurrentLevel) {
//            mCurrentLevel = currentLevel;
//            MethodDone.doIt(this, "changeZoom", currentLevel);
//        }
    }

//    @Override
//    protected void onStyleChanged(Style style) {
//
//    }


    @MethodTag(threadType = IMethodDone.THREAD_TYPE_THREAD)
    private void changeZoom(int level) {
        List<LatLng> latLngs = mPoints[level];
        if (null == latLngs) {
            synchronized (mDefaultPoints) {
                latLngs = mPoints[level];
                if (null == latLngs) {
//                    final int scalePow2 = (1 << (MAX_LEVEL - (level << 1) + 1));
//                    final double distancePow2 = scalePow2 * PIXEL_20_LEVEL_POW2;
//                    RectD rect = getItemRect();
//                    if (rect.distancePower2() < distancePow2) {
//                        latLngs = new ArrayList<>(5);
//                        latLngs.add(new LatLng(rect.top, rect.left));
//                        latLngs.add(new LatLng(rect.top, rect.right));
//                        latLngs.add(new LatLng(rect.bottom, rect.right));
//                        latLngs.add(new LatLng(rect.bottom, rect.left));
//                        latLngs.add(new LatLng(rect.top, rect.left));
//                    } else {
//                        latLngs = new ArrayList<>(mDefaultPoints.size());
//                        int index = 0;
//                        final int size = mDefaultPoints.size() - 1;
//                        LatLng p1 = mDefaultPoints.get(index++);
//                        latLngs.add(p1);
//                        LatLng p2;
//                        for (; index < size; index++) {
//                            p2 = mDefaultPoints.get(index);
//                            if (distancePOW2(p1, p2) > distancePow2) {
//                                latLngs.add(p2);
//                                p1 = p2;
//                            }
//                        }
//                        latLngs.add(mDefaultPoints.get(size));
//                    }
                }
            }
        }
        mPoints[level] = latLngs;
        if (level == mCurrentLevel) {
            MethodDone.doIt(this, "updatePolygon", latLngs);
        }

        LogUtils.e("Leon", "level: ", level, " pointSize:", mDefaultPoints.size(), " dealPointSize:", latLngs.size());
    }

    private void updatePolygon(List<LatLng> latLns) {
        mPolygonOptions.getPolygon().setPoints(latLns);
    }

}
