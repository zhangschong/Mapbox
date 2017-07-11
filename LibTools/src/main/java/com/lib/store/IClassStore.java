package com.lib.store;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lib.utils.GsonUtil;

/**
 * Created by zhanghong on 17-7-11.
 */

public interface IClassStore {

    void store(Object obj);

    void delete(Class cls);

    /**
     * 获取实例对像,支持List
     *
     * @param cls
     * @param <T>
     * @return
     */
    <T> T get(Class<T> cls);


    class ClsJsonStore implements IClassStore {

        private final IStringStore mRealStore;
        private final Gson mGson = new Gson();

        public ClsJsonStore(IStringStore store) {
            mRealStore = store;
        }

        @Override
        public void store(Object obj) {
            String data = "";
            if (null != obj) {
                data = GsonUtil.packToJson(mGson, obj);
            }
            mRealStore.store(obj.getClass().getName(), data);
        }

        @Override
        public void delete(Class cls) {
            mRealStore.delete(cls.getName());
        }

        @Override
        public <T> T get(Class<T> cls) {
            String data = mRealStore.get(cls.getName());
            if (TextUtils.isEmpty(data)) {
                return null;
            }
            return GsonUtil.parserToJson(mGson, data, cls);
        }
    }
}
