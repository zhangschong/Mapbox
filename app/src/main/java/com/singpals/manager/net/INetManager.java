package com.singpals.manager.net;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.lib.http.DataNetBuilder;
import com.lib.http.IHttpRequester;
import com.lib.http.RequestCall;
import com.lib.http.mock.FileMockRequester;
import com.lib.mthdone.utils.IManager;
import com.lib.utils.FileSource;
import com.singpals.manager.net.data.AppVersionInfo;
import com.singpals.manager.net.data.PoiNode;
import com.singpals.manager.net.data.UserData;
import com.singpals.mapbox.map.clicker.INodeClicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 网络请求部份
 */
@IHttpRequester.Settings(timeOutSeconds = 15, rootUrl = "http://xxx")
public interface INetManager extends IManager {

    @IHttpRequester.Get("requestPoiNodes")
    @IHttpRequester.RequestKes("uuid")
    RequestCall<List<PoiNode>> requestPoiNodes(Object callback, String uuid);

    @IHttpRequester.Get("userLogin")
    @IHttpRequester.RequestKes({"username", "psw"})
    RequestCall<UserData> userLogin(Object callback, String user, String psw);

    @IHttpRequester.Get("checkAppVersion")
    @IHttpRequester.RequestKes("version")
    RequestCall<AppVersionInfo> checkAppVersion(Object callback, String version);



    class Factory {

        public static INetManager createMockReq(Context context) {
            FileMockRequester requester = new FileMockRequester() {
                @Override
                protected String generateData(RequestCall call, Object callback, String data) {
                    try {
                        JSONObject jData = new JSONObject(data);
                        String msg = jData.optString("msg");
                        if (TextUtils.isEmpty(msg)) {
                            return jData.optString("data");
                        } else {
                            err(call, callback, NETWORK_ERR, msg);
                        }
                    } catch (JSONException e) {
                        err(call, callback, DATA_ERR, e.toString());
                    }
                    return "";
                }
            };
            requester.setFileSource(FileSource.Factory.createAssetSource(context, "mock.req"));
            requester.init();
            return DataNetBuilder.newDataNet(INetManager.class, requester);
        }
    }

}
