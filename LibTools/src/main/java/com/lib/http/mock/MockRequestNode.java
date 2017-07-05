package com.lib.http.mock;


import com.lib.http.DefaultResponse;
import com.lib.http.RequestCall;

class MockRequestNode<T> implements RequestCall<T> {
    Object mCallBack;
    DefaultResponse mResponse;

    static <T> MockRequestNode<T> newRequestNode(Class<T> cls) {
        return new MockRequestNode<T>();
    }

    @Override
    public String url() {
        return mResponse.url();
    }

    @Override
    public <T1> T1 getParameter(String key) {
        return mResponse.getParameter(key);
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public <T2> T2 getData() {
        return mResponse.getData();
    }
}