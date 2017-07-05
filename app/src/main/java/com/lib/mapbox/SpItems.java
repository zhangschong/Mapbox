package com.lib.mapbox;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于item集的控制
 */

public class SpItems<K, T extends SpItem> {

    protected List<T> mItems = new ArrayList<>(20);

    /**
     * 通过Key值判断是否有此数据
     *
     * @param key
     * @return
     */
    public boolean hasItem(K key) {
        for (T item : mItems) {
            if (isSameOne(key, item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过Key值判断是否一样
     *
     * @param key
     * @param item
     * @return
     */
    protected boolean isSameOne(K key, T item) {
        return item.isSameFromKey(key);
    }

    /**
     * 获取当前的值
     *
     * @param key
     * @return
     */
    public T getItem(K key) {
        for (T item : mItems) {
            if (isSameOne(key, item)) {
                return item;
            }
        }
        return null;
    }


    /**
     * 添加一个Item
     *
     * @param item
     */
    public void addItem(T item) {
        mItems.add(item);
    }

    /**
     * 移除一个Item
     *
     * @param item
     */
    public void removeItem(T item) {
        mItems.remove(item);
    }

    /**
     * 获取所有的Items
     *
     * @return
     */
    public List<T> getItems() {
        return mItems;
    }

}
