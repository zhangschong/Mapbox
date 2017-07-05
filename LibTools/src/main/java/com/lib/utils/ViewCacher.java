package com.lib.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by zhanghong on 17-5-17.
 */

public class ViewCacher {

    private SparseArray<View> mViews = new SparseArray<>(20);

    private final  View mRoot;

    public ViewCacher(View view){
        mRoot = view;
    }

    public ViewCacher(Context context, int viewId){
        mRoot = View.inflate(context,viewId,null);
    }

    public <T extends View> T findView(int id){
        View view = mViews.get(id);
        if(null == view){
            view = mRoot.findViewById(id);
            mViews.put(id,view);
        }
        return (T) view;
    }

    public View getRootView(){
        return mRoot;
    }

}
