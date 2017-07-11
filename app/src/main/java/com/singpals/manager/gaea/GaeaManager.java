package com.singpals.manager.gaea;

import android.content.Context;

import com.singpals.datagaea.DataGaea;
import com.singpals.manager.net.INetManager;

/**
 * $desc
 */

public class GaeaManager extends DataGaea {

    private static GaeaManager mGaeaManager;

    public final static void init(Context context) {
        mGaeaManager = new GaeaManager();

        mGaeaManager.addImpl(INetManager.class, INetManager.Factory.createMockReq(context));//添加网络请求管理员
        mGaeaManager.addImpl(IAppConfiguration.class, new AppConfigration(context));//添加应用配置
        mGaeaManager.addImpl(IUserConfiguration.class, new UserConfiguration(context));//添加用户信息管理员
        mGaeaManager.addImpl(IPoiNodeManager.class, new PoiNodeManager());//添加Poi点管理员

        mGaeaManager.init();
    }

    public final static GaeaManager getGaeaManager() {
        return mGaeaManager;
    }

    private GaeaManager() {
    }

}
