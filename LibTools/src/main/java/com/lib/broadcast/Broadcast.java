package com.lib.broadcast;

import com.lib.mthdone.IMethodDone;
import com.lib.mthdone.MethodTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;

/**
 * $desc
 */

public interface Broadcast {

    /**
     * 注册广播
     *
     * @param action
     * @param instance
     */
    void registBroadcast(String action, Object instance);

    /**
     * 注销广播
     *
     * @param action
     * @param instance
     */
    void unregistBraodcast(String action, Object instance);

    /**
     * 发送广播
     *
     * @param action
     * @param msgs
     */
    void sendMessage(String action, Object... msgs);

    class Factory {
        public final static Broadcast BraodCast = new DefaultBroadcast();
    }

}

class DefaultBroadcast implements Broadcast {

    private Map<String, List> mRecivers = new ConcurrentHashMap<>(20);

    @Override
    public void registBroadcast(String action, Object instance) {
        MethodDone.doIt(this, "registBroadcastInner", action, instance);
    }

    @MethodTag(threadType = IMethodDone.THREAD_TYPE_THREAD)
    private void registBroadcastInner(String action, Object instance) {
        List objs = mRecivers.get(action);
        if (null == objs) {
            objs = new ArrayList();
            mRecivers.put(action, objs);
        }
        objs.add(instance);
    }

    @Override
    public void unregistBraodcast(String action, Object instance) {
        MethodDone.doIt(this, "unregistBroadcastInner", action, instance);
    }


    @MethodTag(threadType = IMethodDone.THREAD_TYPE_THREAD)
    private void unregistBroadcastInner(String action, Object instance) {
        List objs = mRecivers.get(action);
        if (null != objs) {
            objs.remove(instance);
        }
    }

    @Override
    public void sendMessage(String action, Object... msgs) {
        MethodDone.doIt(this, "sendMessageInner", action, msgs);
    }

    @MethodTag(threadType = IMethodDone.THREAD_TYPE_THREAD)
    private void sendMessageInner(String action, Object... msgs) {
        List objs = mRecivers.get(action);
        if (null != objs) {
            for (Object obj : objs) {
                MethodDone.doIt(obj, action, msgs);
            }
        }
    }
}
