package com.lib.http.mock;

import android.text.TextUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lib.http.DefaultResponse;
import com.lib.http.IHttpRequester;
import com.lib.http.OkHttpRequester;
import com.lib.http.RequestCall;
import com.lib.mthdone.IMethodDone;
import com.lib.mthdone.MethodTag;
import com.lib.utils.Abandon;
import com.lib.utils.FileSource;
import com.lib.utils.GsonUtil;
import com.lib.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;

/**
 * $desc
 */

public class FileMockRequester implements IHttpRequester {
    private final static String TAG = FileMockRequester.class.getSimpleName();

    private MethodNodeControl mMethodNodeControl = new MethodNodeControl();
    private Gson mGson = new GsonBuilder().setExclusionStrategies(new GsonAbandonExclusionStrategy()).create();

    public void setFileSource(FileSource fileSource) {
        MethodDone.doIt(mMethodNodeControl, "setMethodData", fileSource);
    }

    @Override
    public void init() {
    }

    @Override
    public void recycle() {
    }

    @Override
    public void setTimeOut(int timeMills) {

    }

    @Override
    public void setRootUrl(String rootUrl) {

    }

    @Override
    public <T> RequestCall<T> request(Method method, Object callBack, Class dataCls, Object... params) {
        if (RequestCall.class.isAssignableFrom(dataCls)) {
            //从泛型中获取返回数据类型
            Type genType = method.getGenericReturnType();
            genType = ((ParameterizedType) genType).getActualTypeArguments()[0];
            if (genType instanceof ParameterizedType) {
                dataCls = (Class) ((ParameterizedType) genType).getRawType();
            } else {
                dataCls = (Class) genType;
            }
            MockRequestNode node = MockRequestNode.newRequestNode(dataCls);
            node.mCallBack = callBack;
            //在IO线程调用方法request
            MethodDone.doIt(this, "requestInner", method, node, callBack, genType, params);
            return node;
        } else {
            try {
                MethodDone.doItWithException(callBack, CALL_BACK_ERR, null, this, new RepErrMsg(CANCELED_ERR, dataCls + " is not subclass of RequestCall"));
            } catch (Exception e) {
                LogUtils.e(TAG, dataCls + " is not subclass of RequestCall");
            }
        }
        return null;
    }


    @MethodTag(threadType = IMethodDone.THREAD_TYPE_IO)
    private void requestInner(Method method, MockRequestNode reqNode, Object callBack, Type dataCls, Object... params) throws IOException, InstantiationException, IllegalAccessException {
        DefaultResponse response = DefaultResponse.createResponse(method, params);
        reqNode.mResponse = response;
        MethodNode node = mMethodNodeControl.getMethodNode(response);
        if (null != node) {
            Object data = GsonUtil.parserToJson(mGson, node.mData, dataCls);
            response.setData(data);
            MethodDone.doIt(callBack, CALL_BACK_SUCCEED, reqNode);
        } else {
            response.setData(MockRequester.createClass(dataCls));
            MethodDone.doIt(callBack, CALL_BACK_SUCCEED, reqNode);
        }
    }


    private static class MethodNodeControl {
        private final static String SKIP_LINE = "##";
        private HashMap<String, List<MethodNode>> mMethodNodes = new HashMap<>(30);


        @MethodTag(threadType = IMethodDone.THREAD_TYPE_IO)
        private void setMethodData(FileSource source) throws Exception {
            mMethodNodes.clear();
            source.init();
            InputStream is = source.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            MethodNode methodNode = null;
            while (null != (line = reader.readLine())) {
                if (line.startsWith(SKIP_LINE)) {
                    addMethodNode(methodNode);
                    methodNode = new MethodNode();
                } else {
                    methodNode.addString(line);
                }
            }
            addMethodNode(methodNode);
            source.recycle();
        }

        private MethodNode getMethodNode(DefaultResponse response) {
            final String key = response.url() + response.getParameterCounts();
            List<MethodNode> nodes = mMethodNodes.get(key);
            if (null == nodes) {
                return null;
            } else {
                for (MethodNode node : nodes) {
                    if (node.isSame(response.getParams())) {
                        return node;
                    }
                }
            }
            return null;
        }

        private void addMethodNode(MethodNode methodNode) {
            if (null == methodNode || methodNode.isEmpty()) {
                return;
            }
            final String key = methodNode.nodeKey();
            List<MethodNode> nodes = mMethodNodes.get(key);
            if (null == nodes) {
                nodes = new ArrayList<>(3);
                mMethodNodes.put(key, nodes);
            }
            nodes.add(methodNode);
        }

    }


    private static class MethodNode {

        private final static String KEY_SIGN = "::";
        private final static String URL = "url";
        private final static String PARAM = "param";
        private final static String PARAM_SIGN = "\\$\\$";
        private final static String DATA = "data";
        private final static String ALL_VALUE = "--";

        String mUrl;
        HashMap<String, String> mParams = new HashMap<>(4);
        String mData;

        String nodeKey() {
            return mUrl + mParams.size();
        }

        boolean isEmpty() {
            return TextUtils.isEmpty(mUrl);
        }

        boolean isSame(Map<String, Object> params) {
            String value;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                value = mParams.get(entry.getKey());
                if (ALL_VALUE.equals(value)) {
                    continue;
                }
                if (!entry.getValue().toString().equals(value)) {
                    return false;
                }
            }
            return true;
        }


        void addString(String line) {
            String[] urls = line.split(KEY_SIGN);
            String key = urls[0].trim();
            String data = "";
            if (urls.length > 1) {
                data = urls[1].trim();
            }
            if (key.equals(URL)) {
                mUrl = data;
            } else if (key.equals(PARAM)) {
                if (!TextUtils.isEmpty(data)) {
                    String[] keyValue = data.split(PARAM_SIGN);
                    mParams.put(keyValue[0].trim(), keyValue[1].trim());
                }
            } else if (key.equals(DATA)) {
                mData = data;
            }
        }

    }
}
