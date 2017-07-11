package com.singpals.manager.gaea;

import com.lib.http.IHttpRequester;
import com.lib.http.RequestCall;
import com.lib.mthdone.IMethodDone;
import com.lib.mthdone.MethodTag;
import com.singpals.manager.net.INetManager;
import com.singpals.manager.net.data.PoiNode;
import com.singpals.manager.net.data.UserData;

import java.util.List;

import static com.lib.mthdone.IMethodDone.Factory.MethodDone;

/**
 * $desc
 */

public interface IPoiNodeManager {

    /**
     * 请求当前的poi点
     */
    GaeaItemCb<List<PoiNode>> requestPoiNodes();

}

class PoiNodeManager extends GaeaManagerItem implements IPoiNodeManager {

    private INetManager mNetManager;
    private UserData mUserData;

    @Override
    public GaeaItemCb<List<PoiNode>> requestPoiNodes() {
        final GaeaItemCb<List<PoiNode>> cb = new GaeaItemCb<>();
        mNetManager.requestPoiNodes(new IHttpRequester.Callback<List<PoiNode>>() {

            @Override
            @MethodTag(threadType = IMethodDone.THREAD_TYPE_THREAD)
            public void onResponse(RequestCall<List<PoiNode>> call) {
                List<PoiNode> nodes = call.getData();
                cb.dataUpdated(GaeaItemCb.DATA_TYPE_NET, nodes);
            }

            @Override
            public void onResponseErr(RequestCall<List<PoiNode>> call, IHttpRequester.RepErrMsg msg) {

            }
        }, mUserData.getUuid());
        return cb;
    }


    @Override
    public void init() {

    }

    @Override
    public void recycle() {

    }

    @Override
    protected void onSetDataGaea(GaeaManager gaea) {
        mNetManager = gaea.getProxyInstance(INetManager.class);
        mUserData = gaea.getProxyInstance(IUserManager.class).getUserData();
    }

}
