package com.lib.http;

import java.lang.reflect.Method;
import java.util.Map;

public class DefaultResponse {

    private final String mUrl;
    private final Map<String, Object> mParams;
    private Object mData;

    private DefaultResponse(String url, Map<String, Object> params) {
        mUrl = url;
        mParams = params;
    }

    public void setData(Object data) {
        mData = data;
    }

    public int getParameterCounts(){
        return mParams.size();
    }

    public Map<String, Object> getParams() {
        return mParams;
    }

    public String url() {
        return mUrl;
    }

    public <T> T getParameter(String key) {
        return (T) mParams.get(key);
    }

    public <T> T getData() {
        return (T) mData;
    }

    /**
     * 通过方法和参数，生成response
     *
     * @param method
     * @param objs
     * @return
     */
    public static DefaultResponse createResponse(Method method, Object[] objs) {
        IHttpRequester.Post post = method.getAnnotation(IHttpRequester.Post.class);
        String url = null;
        if (null != post) {
            url = post.value();
        } else {
            IHttpRequester.Get get = method.getAnnotation(IHttpRequester.Get.class);
            url = get.value();
        }

        if (null == url) {
            return null;
        }

        IHttpRequester.RequestKes rKeys = method.getAnnotation(IHttpRequester.RequestKes.class);
        String[] keys;
        if (null != rKeys) {
            keys = rKeys.value();
        } else {
            keys = new String[0];
        }
        final Map<String, Object> params = HttpUtils.buildHashMapParams(keys, objs);

        return new DefaultResponse(url, params);
    }


}