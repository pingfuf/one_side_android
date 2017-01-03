package com.kuaipao.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.VisibleRegion;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.manager.R;
import com.kuaipao.model.LocationCoordinate2D;
import com.kuaipao.manager.CardDataManager;
import com.kuaipao.manager.CardDataManager.DataResultListener;
import com.kuaipao.manager.CardLocationManager;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.JumpCenter;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.utils.WebUtils;

public class MapOfMerchantsActivity extends BaseActivity implements OnMapLoadedListener,
        OnMarkerClickListener, OnMapClickListener, OnCameraChangeListener {

    private static final int MAP_ZOOM_LEVEL = 14;
    private static final LatLng WU_DAO_KOU_POSTION = new LatLng(39.9928904908, 116.3377848782);

    private MapView mMapView;
    private AMap aMap;
    private UiSettings mUiSettings;

    private RelativeLayout layoutMerchantInfo;
    private LinearLayout layoutGoToMerchant;
    private TextView tvDistance;
    private TextView tvMerchantName;
    private TextView tvMerchantType;

    private long mSelectedMerchantID = -1;
    private Marker mSelectedMarker;
    private int mLastSelectedDrawableID;
    private int mSelectedDrawableID;

    private String mCity;
    private LatLng mUserLocation;
    private volatile VisibleRegion mVisibleRegion;

    private volatile boolean bOnPause = false;
    private volatile boolean bIsMapviewChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_of_merchants);
        setTitle("", true);
        mTitleBar.setBackPressedImageResource(R.drawable.ic_back_btn);

        if (savedInstanceState != null) {
            this.finish();
            return;
        }

        LocationCoordinate2D location = CardLocationManager.getInstance().getLocation();
        if (location != null)
            mUserLocation = LocationCoordinate2D.toMapData(location);

        mCity = CardLocationManager.getInstance().getCityName();

        initUI();
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setScaleControlsEnabled(false);
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setScrollGesturesEnabled(true);
            mUiSettings.setCompassEnabled(false);

            aMap.setOnMapLoadedListener(this);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnMapClickListener(this);
            aMap.setOnCameraChangeListener(this);
        }
        LogUtils.d(">>>> onCreate()");
    }


    @Override
    protected boolean isTitleBarOverlay() {
        return true;
    }

    ;

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null)
            mMapView.onResume();
        LogUtils.d(">>>> onResume()");
        bOnPause = false;
    }

    protected void onPause() {
        super.onPause();

        LogUtils.d(">>>> onPause()");
        bOnPause = true;
        if (mMapView != null)
            mMapView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null)
            mMapView.onDestroy();
        LogUtils.d(">>>> onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null)
            mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onMapLoaded() {
        LogUtils.d(">>>> onMapLoaded");
        initMarkersToMap();
    }

    private void initUI() {
        mMapView = (MapView) findViewById(R.id.map_merchants);
        layoutMerchantInfo = (RelativeLayout) findViewById(R.id.layout_merchant);
        layoutGoToMerchant = (LinearLayout) findViewById(R.id.layout_go_to_merchant);
        layoutGoToMerchant.setOnClickListener(this);
        tvDistance = (TextView) findViewById(R.id.tv_distance);
        tvMerchantName = (TextView) findViewById(R.id.tv_merchant_name);
        tvMerchantType = (TextView) findViewById(R.id.tv_merchant_type);

        layoutMerchantInfo.setVisibility(View.GONE);
    }

    private void selectMerchant(Marker marker) {
        String markerInfo = marker.getSnippet();
        LogUtils.d(">>>> selectMerchant marker.getSnippet=%s", markerInfo);

        MerchantMapModel merchant = MerchantMapModel.parseFromInfoString(markerInfo);

        if (merchant == null)
            return;

        LogUtils.d(">>>> selectMerchant merchant=%s", merchant);

        mSelectedMerchantID = merchant.getMerchantID();

        mSelectedDrawableID = R.drawable.ic_zonghe_action;
        mLastSelectedDrawableID = R.drawable.ic_zonghe;

        String types = merchant.getTypes();
        String[] typeArray = types.split(ViewUtils.getString(R.string.comma_tip));
        if (typeArray == null || typeArray.length != 1) {
            mSelectedDrawableID = R.drawable.ic_zonghe_action;
            mLastSelectedDrawableID = R.drawable.ic_zonghe;
        } else {
            String type = typeArray[0];
            if (type.equals(getResources().getString(R.string.gym_type_qi_xie))) {
                mSelectedDrawableID = R.drawable.ic_qixie_action;
                mLastSelectedDrawableID = R.drawable.ic_qixie;
            } else if (type.equals(getResources().getString(R.string.gym_type_you_yong))) {
                mSelectedDrawableID = R.drawable.ic_youyong_action;
                mLastSelectedDrawableID = R.drawable.ic_youyong;
            } else if (type.equals(getResources().getString(R.string.gym_type_yu_jia))) {
                mSelectedDrawableID = R.drawable.ic_yujia_action;
                mLastSelectedDrawableID = R.drawable.ic_yujia;
            } else if (type.equals(getResources().getString(R.string.gym_type_wu_dao))) {
                mSelectedDrawableID = R.drawable.ic_wudao_action;
                mLastSelectedDrawableID = R.drawable.ic_wudao;
            } else if (type.equals(getResources().getString(R.string.gym_type_dan_che))) {
                mSelectedDrawableID = R.drawable.ic_donggandanche_action;
                mLastSelectedDrawableID = R.drawable.ic_donggandanche;
            } else if (type.equals(getResources().getString(R.string.gym_type_wu_shu))) {
                mSelectedDrawableID = R.drawable.ic_wushu_action;
                mLastSelectedDrawableID = R.drawable.ic_wushu;
            }
        }

        mSelectedMarker = marker;

        marker.setIcon(BitmapDescriptorFactory.fromResource(mSelectedDrawableID));
        aMap.invalidate();

        layoutMerchantInfo.setVisibility(View.VISIBLE);
        tvDistance.setText(formatDistance(merchant.getDistance()));
        tvMerchantName.setText(marker.getTitle());
//    LogUtils.d(">>>> types=%s", types);
        tvMerchantType.setText(/*LangUtils.isEmpty(types) || types.equals(" ") ? "综合" :*/ types);
    }

    private void initMarkersToMap() {
        if (aMap == null)
            return;

        if (mUserLocation != null) {
            MarkerOptions mUserMarkerOptions =
                    new MarkerOptions().anchor(0.5f, 0.5f).position(mUserLocation)
                            .title(getResources().getString(R.string.my_location_tip))/*.draggable(true)*/;
            aMap.addMarker(mUserMarkerOptions);
            // marker.showInfoWindow();
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mUserLocation, MAP_ZOOM_LEVEL));
        } else {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WU_DAO_KOU_POSTION, MAP_ZOOM_LEVEL));// default:
            // WuDaoKou
            // aMap.moveCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM_LEVEL));
        }
    }

    private void updateMarkersToMap(final List<MerchantMapModel> merchantList) {
        if (aMap == null)
            return;
        aMap.clear();

        if (!LangUtils.isEmpty(merchantList)) {
            LogUtils.d(">>>> updateMarkersToMap merchantList=%s", merchantList.size());

            // List<Marker> lastMarkers = aMap.getMapScreenMarkers();
            // List<MarkerOptions> markerOptions = new ArrayList<MarkerOptions>(mAllMerchants.size());

            for (MerchantMapModel merchant : merchantList) {

                String types = merchant.getTypes();
                String[] typeArray = types.split(ViewUtils.getString(R.string.comma_tip));
                int drawableID = R.drawable.ic_zonghe;
                if (typeArray == null || typeArray.length != 1) {
                    drawableID = R.drawable.ic_zonghe;
                } else {
                    String type = typeArray[0];
                    if (type.equals(getResources().getString(R.string.gym_type_qi_xie))) {
                        drawableID = R.drawable.ic_qixie;
                    } else if (type.equals(getResources().getString(R.string.gym_type_you_yong))) {
                        drawableID = R.drawable.ic_youyong;
                    } else if (type.equals(getResources().getString(R.string.gym_type_yu_jia))) {
                        drawableID = R.drawable.ic_yujia;
                    } else if (type.equals(getResources().getString(R.string.gym_type_wu_dao))) {
                        drawableID = R.drawable.ic_wudao;
                    } else if (type.equals(getResources().getString(R.string.gym_type_dan_che))) {
                        drawableID = R.drawable.ic_donggandanche;
                    } else if (type.equals(getResources().getString(R.string.gym_type_wu_shu))) {
                        drawableID = R.drawable.ic_wushu;
                    }
                }
                LatLng mapLocation = new LatLng(merchant.getLatitude(), merchant.getLongitude());
                aMap.addMarker(new MarkerOptions().title(merchant.getName()).position(mapLocation)
            /*.draggable(true)*/.snippet(merchant.toInfoString())
                        .icon(BitmapDescriptorFactory.fromResource(drawableID)));

                // boolean bAlreadyExisted = false;
                // if (!LangUtils.isEmpty(lastMarkers)) {
                // for (Marker marker : lastMarkers) {
                // if (marker.getPosition().latitude == mapLocation.latitude
                // && marker.getPosition().longitude == mapLocation.longitude
                // && marker.getTitle().equals(mSelectedMarker.getTitle())) {
                // bAlreadyExisted = true;
                // markerOptions.add(new MarkerOptions().title(mSelectedMarker.getTitle())
                // .position(new LatLng(mSelectedLocationLat, mSelectedLocationLng)).draggable(true)
                // .icon(BitmapDescriptorFactory.fromResource(mSelectedDrawableID)));
                // LogUtils.d(">>>> bAlreadyExisted = true");
                // break;
                // }
                // }
                // }
                // if (!bAlreadyExisted) {
                // markerOptions.add(new MarkerOptions().title(merchant.getName()).position(mapLocation)
                // .draggable(true).icon(BitmapDescriptorFactory.fromResource(drawableID)));
                // }
                // }
                //
                // for (MarkerOptions mo : markerOptions) {
                // aMap.addMarker(mo);
                // }
            }

            merchantList.clear();
        }

        if (mUserLocation != null) {
            MarkerOptions mUserMarkerOptions =
                    new MarkerOptions().anchor(0.5f, 0.5f).position(mUserLocation)
                            .title(getResources().getString(R.string.my_location_tip))/*.draggable(true)*/;
            aMap.addMarker(mUserMarkerOptions);
        }

        aMap.invalidate();
    }


    private long lastTime;

    private void fetchAllMerchants(final LatLng userLocation, final VisibleRegion visibleRegion) {
//    showLoadingDialog();

        LocationCoordinate2D location;
        if (userLocation == null)
            location = null;
            //new LocationCoordinate2D(WU_DAO_KOU_POSTION.latitude, WU_DAO_KOU_POSTION.longitude);// default: WuDaoKou
        else
            location = new LocationCoordinate2D(userLocation.latitude, userLocation.longitude);

        lastTime = System.currentTimeMillis();
        if (visibleRegion == null)
            return;

        CardDataManager
                .fetchMerchantsInMapData(location, mCity, new LocationCoordinate2D(
                                visibleRegion.nearLeft.latitude, visibleRegion.nearLeft.longitude),
                        new LocationCoordinate2D(visibleRegion.farRight.latitude,
                                visibleRegion.farRight.longitude), new DataResultListener() {
                            @SuppressWarnings("unchecked")
                            @Override
                            public void onFinish(boolean ret, Object... params) {
                                LogUtils.d(">>>> passed ＝ %s ms", System.currentTimeMillis() - lastTime);

                                if (ret) {
                                    if (mVisibleRegion.nearLeft.latitude != visibleRegion.nearLeft.latitude
                                            || mVisibleRegion.nearLeft.longitude != visibleRegion.nearLeft.longitude
                                            || mVisibleRegion.farRight.latitude != visibleRegion.farRight.latitude
                                            || mVisibleRegion.farRight.longitude != visibleRegion.farRight.longitude) {
                                        LogUtils.d(">>>> out of date!");
                                        return;
                                    }

                                    if (params != null && params.length >= 1) {
                                        // @SuppressWarnings("unchecked")
                                        final List<MerchantMapModel> merchantList = new ArrayList<MerchantMapModel>();
                                        LogUtils.d(">>>> merchantList ＝ %s", merchantList.size());
                                        final JSONArray data = (JSONArray) params[0];
                                        if (!LangUtils.isEmpty(data)) {
                                            int len = data.size();
                                            for (int i = 0; i < len; i++) {
                                                JSONObject json = data.getJSONObject(i);
                                                merchantList.add(MerchantMapModel.fromJson(json));
                                            }
                                        }
                                        ViewUtils.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                updateMarkersToMap(merchantList);
                                                // if (mVisibleRegion != null) {
                                                // LatLngBounds latLngBounds = mVisibleRegion.latLngBounds;
                                                // if (mSelectedMarker != null) {
                                                // LatLng location = mSelectedMarker.getPosition();
                                                // if(location == null)
                                                // location = new LatLng(mSelectedLocationLat, mSelectedLocationLng);
                                                // if (!latLngBounds.contains(location)) {
                                                hideMerchantInfoLayout();
                                                // }
                                                // }
                                                // }
                                            }
                                        });
                                    }

                                } else {
                                    // network failed!
                                    ViewUtils.showToast(getResources().getString(R.string.fetch_merchants_fail_tip),
                                            Toast.LENGTH_LONG);
                                }

//                ViewUtils.post(new Runnable() {
//                  @Override
//                  public void run() {
//                    dismissLoadingDialog();
//                  }
//                });
                            }
                        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // LogUtils.d(">>>> onMarkerClick marker=%s; name=%s", marker.getPosition(), marker.getTitle());

        if (marker.getTitle().equals(getResources().getString(R.string.my_location_tip)))
            return true;

        if (mSelectedMarker == null || !marker.getTitle().equals(mSelectedMarker.getTitle())) {
            if (mSelectedMarker != null) {
                mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(mLastSelectedDrawableID));
                if (aMap != null) {
                    aMap.invalidate();
                }
            }
            selectMerchant(marker);
        }
        return true;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        hideMerchantInfoLayout();
    }

    private void hideMerchantInfoLayout() {
        if (mSelectedMarker != null) {
            mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(mLastSelectedDrawableID));
            if (aMap != null) {
                aMap.invalidate();
            }
            mSelectedMarker = null;
        }
        layoutMerchantInfo.setVisibility(View.GONE);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//     LogUtils.d(">>>> onCameraChange:" + cameraPosition.toString());
        bIsMapviewChanged = true;
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (bOnPause || !bIsMapviewChanged) {
            bOnPause = false;
            bIsMapviewChanged = false;
            return;
        }

        Projection proj = aMap.getProjection();
        mVisibleRegion = proj.getVisibleRegion();

        // ViewUtils.showToast("vr.nearLeft" + vr.nearLeft.toString() + ";vr.nearRight"
        // + vr.nearRight.toString() + ";vr.farLeft" + vr.farLeft.toString() + ";vr.farRight"
        // + vr.farRight.toString() + "; onCameraChangeFinish:" + cameraPosition.toString(),
        // Toast.LENGTH_LONG);

        LogUtils.d(">>>> onCameraChangeFinish=%s ; bOnPause=%s", cameraPosition.toString(), bOnPause);
        fetchAllMerchants(mUserLocation, mVisibleRegion);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(layoutGoToMerchant)) {
            if (mSelectedMerchantID >= 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.CARD_MERCHANT_SELECT_DATA, new Date());
                bundle.putSerializable(Constant.SINGLE_CARD_MERCHANT_ID, mSelectedMerchantID);
            }
        }
    }


    private DecimalFormat mFormat = new DecimalFormat("#0.0");

    private String formatDistance(double distance) {
        if (distance <= 0.000001) {
            return "";
        } else if (distance > 0.000001 && distance < 1000) {
            return "<1km";
        } else if (distance >= 1000 && distance < 10000) {
            return mFormat.format(distance / 1000) + "km";
        } else
            return String.format("%dkm", (int) (distance / 1000));
    }


    public static class MerchantMapModel {
        private static final String KEY_MERCHANT_ID = "gid";// id of merchant
        private static final String KEY_NAME = "name";
        private static final String KEY_GEO_LAT = "lat";
        private static final String KEY_GEO_lNG = "lng";
        private static final String KEY_DISTANCE = "dist";
        private static final String KEY_CATE_LIST = "cate_list";

        private long merchantID;
        private String name;
        private String types;
        private double distance;
        private double latitude;
        private double longitude;


        public MerchantMapModel(Long merchantID) {
            this.setMerchantID(merchantID);
        }


        public static MerchantMapModel fromJson(JSONObject j) {
            if (j == null || j.size() == 0) {
                return null;
            }
            long merchantID = WebUtils.getJsonLong(j, KEY_MERCHANT_ID, 0l);
            String name = WebUtils.getJsonString(j, KEY_NAME, "");
            double distance = WebUtils.getJsonDouble(j, KEY_DISTANCE, -1d);
            JSONArray cateArray = WebUtils.getJsonArray(j, KEY_CATE_LIST);
            ArrayList<String> cateList = (ArrayList<String>) WebUtils.jsonToArrayString(cateArray);
            double lat = WebUtils.getJsonDouble(j, KEY_GEO_LAT, 360d);
            double lng = WebUtils.getJsonDouble(j, KEY_GEO_lNG, 360d);

            MerchantMapModel cm = new MerchantMapModel(merchantID);
            cm.setName(name);
            cm.setDistance(distance);
            String types = formatCateList(cateList);
            cm.setTypes(types);
            cm.setLatitude(lat);
            cm.setLongitude(lng);

            return cm;
        }

        private static MerchantMapModel parseFromInfoString(String strInfo) {
            if (LangUtils.isEmpty(strInfo)) {
                return null;
            }

            String strConnector = "~-~";
            String[] infos = strInfo.split(strConnector);
            LogUtils.d(">>>> strInfo=%s; infos.length=%s", strInfo, infos.length);

            if (infos == null || infos.length < 3) {
                return null;
            }
            LogUtils.d(">>>> infos.lenght=%s", infos.length);

            long merchantID = LangUtils.parseLong(infos[0], -1l);
            LogUtils.d(">>>> merchantID=%s", merchantID);

            if (merchantID != -1l) {
                MerchantMapModel mmm = new MerchantMapModel(merchantID);

                double distance = LangUtils.parseDouble(infos[1], .0f);
                mmm.setDistance(distance);

                String types = infos[2];
                mmm.setTypes(types);
                return mmm;
            }
            return null;
        }

        private String toInfoString() {
            String strConnector = "~-~";
            StringBuilder strInfo = new StringBuilder();
            strInfo.append(merchantID);
            strInfo.append(strConnector);
            strInfo.append(distance);
            strInfo.append(strConnector);
            strInfo.append(LangUtils.isEmpty(types) ? " " : types);

            return strInfo.toString();
        }

        private static String formatCateList(List<String> cateList) {
            StringBuilder strType = new StringBuilder("");
            if (!LangUtils.isEmpty(cateList)) {
                for (int i = 0; i < cateList.size(); i++) {
                    strType.append(cateList.get(i));
                    if (i != cateList.size() - 1)
                        strType.append(ViewUtils.getString(R.string.comma_tip));
                }
            }
            return strType.toString();
        }

        public JSONObject toJson() {
            try {
                JSONObject json = new JSONObject();
                json.put(KEY_MERCHANT_ID, this.merchantID);
                json.put(KEY_NAME, this.name);
                json.put(KEY_DISTANCE, this.distance);
                json.put(KEY_CATE_LIST, this.types);
                json.put(KEY_GEO_LAT, this.latitude);
                json.put(KEY_GEO_lNG, this.longitude);
                return json;
            } catch (Exception e) {
                LogUtils.w(e, " parse MerchantMapModel serial error");

            }
            return null;
        }

        public String toString() {
            JSONObject j = toJson();
            return j == null ? super.toString() : j.toString();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o instanceof MerchantMapModel) {
                if (this.merchantID == ((MerchantMapModel) o).getMerchantID()) {
                    return true;
                }
            }
            return false;
        }

        public long getMerchantID() {
            return merchantID;
        }


        public void setMerchantID(long merchantID) {
            this.merchantID = merchantID;
        }


        public String getName() {
            return name;
        }


        public void setName(String name) {
            this.name = name;
        }


        public Double getDistance() {
            return distance;
        }


        public void setDistance(double distance) {
            this.distance = distance;
        }


        public String getTypes() {
            return types;
        }


        public void setTypes(String types) {
            this.types = types;
        }


        public double getLatitude() {
            return latitude;
        }


        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }


        public double getLongitude() {
            return longitude;
        }


        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

    }

}
