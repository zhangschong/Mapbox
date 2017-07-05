package com.singpals.manager.icon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lib.mthdone.utils.IManager;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.io.IOException;
import java.io.InputStream;

/**
 * $desc
 */

public class IconManager implements IManager {

    public final static String SYMBOL = "symbol_";

    private final static String[] ICONS = new String[]{"", "symbol_1", "symbol_2", "symbol_100"};

    private final MapboxMap mMapboxMap;
    private Bitmap[] mBitmaps;
    private final Context mContext;


    public IconManager(Context context,MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;
        mContext = context;
    }


    @Override
    public void init() {
        final int size = ICONS.length;

        mBitmaps = new Bitmap[size];
        for(int i = 1; i < size;i++){
            InputStream is = null;
            try {
                is = mContext.getAssets().open(ICONS[i]);
                mBitmaps[i] = BitmapFactory.decodeStream(is);
                mMapboxMap.addImage(ICONS[i], mBitmaps[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(null != is){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    @Override
    public void recycle() {
        for(int i = mBitmaps.length -1; i> 0; i--){
            mBitmaps[i].recycle();
        }
    }
}
