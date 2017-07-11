package com.singpals.mapbox.map;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.lib.data.kml.SpKmlLayer;
import com.lib.mapbox.SpItem;
import com.lib.mapbox.SpMapBoxHelper;
import com.lib.utils.FileSource;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.singpals.manager.gaea.GaeaItemCb;
import com.singpals.manager.gaea.GaeaManager;
import com.singpals.manager.gaea.IPoiNodeManager;
import com.singpals.manager.icon.IconManager;
import com.singpals.manager.net.PoiNodeLayer;
import com.singpals.manager.net.data.PoiNode;
import com.singpals.mapbox.R;
import com.singpals.mapbox.map.clicker.INodeClicker;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapActivity extends BaseMapActivity<MapActPresenter> {

    private static final int REQUST_FILE_CODE = 101;
    public static final int REQUST_READ_PERMISSION = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new MapActPresenter(), savedInstanceState);
    }


    void chooseFile() {
        MapActivityPermissionsDispatcher.chooseFileInnerWithCheck(this);
    }


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void chooseFileInner() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUST_FILE_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "暂时没有合适的文件夹浏览器！", Toast.LENGTH_SHORT);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUST_FILE_CODE) {
//            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
//            String filePath = Utils.getRealFilePath(this, uri);
//            if (!TextUtils.isEmpty(filePath)) {
//                mPresenter.addSourceFile(filePath);
//            } else {
//                Toast.makeText(this, "文件路径解析失败！", Toast.LENGTH_SHORT);
//            }
            mPresenter.addSourceFile(data.getData());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}

class MapActPresenter extends BaseMapActPresenter<MapActivity> {
    private MapBoxHelper mMapBoxHelper;
    private IconManager mIconManager;
    private IPoiNodeManager mPoiManager;

    @Override
    protected void onActivityCreated(MapActivity activity, Bundle savedInstanceState) {
        activity.setContentView(R.layout.sp_main_map);
        setMapView((MapView) findViewById(R.id.mapview_map), savedInstanceState);
        mPoiManager = GaeaManager.getGaeaManager().getProxyInstance(IPoiNodeManager.class);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        mMapView.setStyleUrl("mapbox://styles/mapbox/satellite-v9");

        mIconManager = new IconManager(mActivity, mapboxMap);
        mIconManager.init();

        mMapBoxHelper = new MapBoxHelper(mMapView, mMapbox);
        mMapBoxHelper.init();

        mMapBoxHelper.cameraTo(29.50, 106.72, 10, 5000);
        mPoiManager.requestPoiNodes().observer(new GaeaItemCb.Observer<List<PoiNode>>() {
            @Override
            public void onDataUpdated(int dataType, List<PoiNode> data) {
                mMapBoxHelper.addChild(PoiNodeLayer.createPoiNodeLayer("PoiNodes", data));
            }
        });
    }

    final void addSourceFile(Uri filePath) {
        FileSource source = FileSource.Factory.createFileSource(mActivity, filePath);
        SpKmlLayer layer = new SpKmlLayer(filePath.getPath(), source);
        mMapBoxHelper.addChild(layer);
    }

    private class MapBoxHelper extends SpMapBoxHelper {
        private INodeClicker mNodeClicker;

        public MapBoxHelper(MapView mapboxView, MapboxMap mapboxMap) {
            super(mapboxView, mapboxMap);
            mNodeClicker = INodeClicker.Factory.createNodeClicker(mMapView, mapboxMap, this);
        }

        @Override
        public void addChild(SpItem child) {
            super.addChild(child);
//            if (child instanceof PoiNodeLayer) {
//                ((PoiNodeLayer) child).setOnPoiNodesClickedListener(mNodeClicker);
//            }
        }

        @Override
        protected boolean onClicked(LatLng latLng) {
            mNodeClicker.clearAll();
            return super.onClicked(latLng);
        }
    }

}