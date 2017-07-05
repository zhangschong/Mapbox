package com.lib.utils;

import android.content.Context;
import android.net.Uri;

import com.lib.mthdone.utils.IManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhanghong on 17-5-27.
 * <p>
 */
public interface FileSource extends IManager {

    /**
     * 获取输入流
     * @return
     * @throws Exception
     */
    InputStream getInputStream() throws Exception;

    class Factory {

        public static FileSource createFileSource(final Context context, final Uri uri){
            return new FileSource() {

                private InputStream mInputStream;
                @Override
                public InputStream getInputStream() throws Exception {
                    mInputStream = context.getContentResolver().openInputStream(uri);
                    return mInputStream;
                }

                @Override
                public void init() {

                }

                @Override
                public void recycle() {
                    if (null != mInputStream) {
                        try {
                            mInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }

        /**
         * 资源方案
         *
         * @param context
         * @param assetPath
         * @return
         */
        public static FileSource createAssetSource(final Context context, final String assetPath) {
            return new FileSource() {

                private InputStream mInputStream;

                @Override
                public InputStream getInputStream() throws Exception {
                    mInputStream = context.getResources().getAssets().open(assetPath);
                    return mInputStream;
                }

                @Override
                public void init() {
                }

                @Override
                public void recycle() {
                    if (null != mInputStream) {
                        try {
                            mInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
    }
}
