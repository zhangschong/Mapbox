package com.lib.http;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.lib.mthdone.utils.IManager;
import com.lib.utils.Abandon;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Map;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 网络请求接口
 */
public interface IHttpRequester extends IManager {

    /**
     * 请求成功后，将会调用callback实例中的方法onResponse(RequestCall)
     */
    String CALL_BACK_SUCCEED = "onResponse";

    /**
     * 请求失败后，将会调用callback实例中的方法onResponseErr(RequestCall,RepErrMsg)
     */
    String CALL_BACK_ERR = "onResponseErr";

    /** 请求出错 */
    int REQUESTER_ERR = 10001;
    /** 网络错误 */
    int NETWORK_ERR = 10002;
    /** 数据错误 */
    int DATA_ERR = 10003;
    /** 请求取消 */
    int CANCELED_ERR = 10004;

    /**
     * 设置默认超时
     *
     * @param timeMills
     */
    void setTimeOut(int timeMills);

    /**
     * 设置根地址
     *
     * @param rootUrl
     */
    void setRootUrl(String rootUrl);

    /**
     * 请求数据时调用
     *
     * @param method
     * @param callBack
     * @param dataCls
     * @param params
     * @return
     */
    <T> RequestCall<T> request(Method method, Object callBack, Class dataCls, Object... params);


    /**
     * 默认的callback
     */
    interface Callback<CT> {
        void onResponse(RequestCall<CT> call);

        void onResponseErr(RequestCall<CT> call, RepErrMsg msg);
    }

    /**
     * 错误数据
     */
    class RepErrMsg {
        public final int type;
        public final String msg;

        public RepErrMsg(int type, String msg) {
            this.type = type;
            this.msg = msg;
        }
    }

    @Documented
    @Target(TYPE)
    @Retention(RUNTIME)
    @interface Settings {
        /**
         * 请求中断时间(秒)
         *
         * @return
         */
        int timeOutSeconds() default 20;

        /**
         * 默认的地址
         *
         * @return
         */
        String rootUrl() default "";
    }

    @Documented
    @Target(METHOD)
    @Retention(RUNTIME)
    @interface RequestKes {
        /**
         * 请求参数
         *
         * @return
         */
        String[] value() default {};
    }

    @interface Post {
        /**
         * 请求参数
         *
         * @return
         */
        String value();
    }

    @interface Get {
        /**
         * 请求参数
         *
         * @return
         */
        String value();
    }

    class GsonAbandonExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return null != f.getAnnotation(Abandon.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}


