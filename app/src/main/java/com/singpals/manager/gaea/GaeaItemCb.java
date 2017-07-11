package com.singpals.manager.gaea;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;

/**
 * $desc
 */

public class GaeaItemCb<T> {

    public final static int DATA_TYPE_NET = 1;
    public final static int DATA_TYPE_CACHED = 2;

    private final static String METHOD_CALL_BACK_NAME = "onDataUpdated";

    private Observer<T> mObserver;

    GaeaItemCb() {
    }

    public final void observer(Observer<T> observer) {
        mObserver = observer;
    }

    void dataUpdated(int dataType, T data) {
        if (null == mObserver) {
            return;
        }
        MethodDone.doIt(mObserver, METHOD_CALL_BACK_NAME, dataType, data);
    }

    public static interface Observer<T> {
        void onDataUpdated(int dataType, T data);
    }
}
