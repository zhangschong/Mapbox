package com.singpals.datagaea;


import com.lib.mthdone.utils.IManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于生成一个动态代理
 */
public class DataGaea implements IManager {

    private Map<Class, IManager> mServiceInstance = new ConcurrentHashMap<>();


    /**
     * 添加接口,并添加代理接口实例对像
     *
     * @param impl 接口类代理实例对像
     * @param <T>
     * @return
     */
    public final <T, O extends IManager> DataGaea addImpl(Class<T> cls, O impl) {
        onAdd(impl);
        mServiceInstance.put(cls, impl);
        return this;
    }

    protected void onAdd(IManager impl) {
        if (impl instanceof AbsGaeaItem) {
            ((AbsGaeaItem) impl).onSetDataGaea(this);
        }
    }

    /**
     * 所有接口类的一个总接口
     *
     * @param cls 总接口类
     * @param <T>
     * @return
     */
    public <T> T getProxyInstance(Class<T> cls) {
        Object proxy = mServiceInstance.get(cls);
        if(null == proxy) {
            return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, mInvocationHandler);
        }else{
            return (T) proxy;
        }
    }

    private InvocationHandler mInvocationHandler = new InvocationHandler() {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            Class<?> cls = method.getDeclaringClass();
            if (cls.equals(IManager.class)) {

                return null;
            }
            Object impl = mServiceInstance.get(cls);
            if (null != impl) {
                return method.invoke(impl, objects);
            }
            return null;
        }
    };

    @Override
    public void init() {
        for (IManager iManager : mServiceInstance.values()) {
            iManager.init();
        }
    }

    @Override
    public void recycle() {
        for (IManager iManager : mServiceInstance.values()) {
            iManager.recycle();
        }
    }
}
