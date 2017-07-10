package com.singpals.manager.gaea;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;
/**
 * $desc
 */

public class GaeaItemCb<T> {
    private final static String METHOD_CALL_BACK_NAME = "onDataUpdated";

    private Observer<T> mObserver;

    GaeaItemCb(){
    }

    public final void observer(Observer<T> observer){
        mObserver = observer;
    }

    void dataUpdated(T data){
        if(null == mObserver){
            return;
        }
        MethodDone.doIt(mObserver, METHOD_CALL_BACK_NAME, data);
    }

    public static interface Observer<T>{
        void onDataUpdated(T data);
    }
}
