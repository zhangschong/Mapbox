package com.lib.store;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhanghong on 17-7-11.
 */

public interface IStringStore {

    void store(String key, String value);

    void delete(String key);

    String get(String key);


    class SpStringStore implements IStringStore {

        private final SharedPreferences mSharedPreferences;

        public SpStringStore(Context context, String name) {
            mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }

        @Override
        public void store(String key, String value) {
            mSharedPreferences.edit().putString(key, value).commit();
        }

        @Override
        public void delete(String key) {
            mSharedPreferences.edit().remove(key).commit();
        }

        @Override
        public String get(String key) {
            return mSharedPreferences.getString(key,"");
        }
    }

}
