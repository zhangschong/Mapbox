package com.singpals.manager.net;

import android.content.Context;

import com.lib.http.DataNetBuilder;
import com.lib.http.IHttpRequester;
import com.lib.http.RequestCall;
import com.lib.http.mock.FileMockRequester;
import com.lib.mthdone.utils.IManager;
import com.lib.utils.FileSource;
import com.singpals.manager.net.data.PoiNode;
import com.singpals.manager.net.data.UserData;
import com.singpals.mapbox.map.clicker.INodeClicker;

import java.util.List;

/**
 * 网络请求部份
 */
@IHttpRequester.Settings(timeOutSeconds = 15, rootUrl = "http://xxx")
public interface INetManager extends IManager {

    @IHttpRequester.Get("requestPoiNodes")
    @IHttpRequester.RequestKes("uuid")
    RequestCall<List<PoiNode>> requestPoiNodes(Object callback, String uuid);

    @IHttpRequester.Post("userLogin")
    @IHttpRequester.RequestKes({"userName","password"})
    RequestCall<UserData> userLogin(Object callback, String user, String psw);

    class Factory {

        private static INetManager sNetManager;

        public static void init(Context context) {
            FileMockRequester requester = new FileMockRequester();
            requester.setFileSource(FileSource.Factory.createAssetSource(context, "mock.req"));
            requester.init();
            sNetManager = DataNetBuilder.newDataNet(INetManager.class, requester);
        }

        public static INetManager getNetManager(){
            return sNetManager;
        }
    }

}
