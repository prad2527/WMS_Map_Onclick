package com.stp.wmsmap_onclick.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.stp.wmsmap_onclick.R;
import com.stp.wmsmap_onclick.Utils.ApplicationConstant;
import com.stp.wmsmap_onclick.Utils.UserSessionManager;
import com.stp.wmsmap_onclick.Utils.utils;
import com.stp.wmsmap_onclick.WMS.TileProviderFactory;
import com.stp.wmsmap_onclick.WMS.WMSLayerManager;
import com.stp.wmsmap_onclick.adapter.MapPopUp_ListAdaptor;
import com.stp.wmsmap_onclick.databinding.ActivityMapsBinding;
import com.stp.wmsmap_onclick.model.LayerResponse;
import com.stp.wmsmap_onclick.model.LayerResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import network.NetworkResponseHelper;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraChangeListener {

    protected static ArrayList<String> layer_label_list;
    protected static boolean[] set_visibility;
    protected static ArrayList<String> geeoserver_id;
    protected ProgressDialog mProgressDialog;
    protected RequestQueue mRequestQueue;
    protected WMSLayerManager LManager;
    protected TileOverlay[] tileOverlay;
    protected TileProvider[] wmsTileProvider;
    protected GoogleMap.OnCameraIdleListener onCameraIdleListener;
    protected ArrayList<String> final_pop_up_key = new ArrayList<>();
    protected ArrayList<String> final_pop_up_value = new ArrayList<>();
    RelativeLayout rlContainer;
    String ApiType;
    Marker marker = null;
    Map<String, String> params;
    ArrayList<LayerResponseData> layerlist = new ArrayList<>();
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    List<String> assetKey = new ArrayList<>();
    List<String> assetValue = new ArrayList<>();
    LinkedHashMap<String, String> dataJson = new LinkedHashMap<>();
    ArrayList<String> geoserver_id_layersDbname = null, attrbuteLabel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GetMapDefinition("map_group_label", "group1");

    }

    private void GetMapDefinition(String map_group_id_column, String group_name) {

        try {
            JSONObject obj = new JSONObject(utils.getData(this, "layers.json"));
            LManager = new WMSLayerManager(obj);
            LManager.AddWMSLayer(map_group_id_column, group_name);
            LManager.SetLayerArrays();
            layer_label_list = LManager.getLayer_label_list_arr();
            Log.d(TAG, "@@AK :layer_name_list :" + layer_label_list);
            geeoserver_id = LManager.getLayer_geoserver_id_arr();
            Log.d(TAG, "@@AK :geeoserver_id :" + geeoserver_id);
            set_visibility = new boolean[LManager.getLayer_onload_visible_arr().size()];
            Log.d(TAG, "@@AK :set_visibility :" + Arrays.toString(set_visibility));
            for (int i = 0; i < LManager.getLayer_onload_visible_arr().size(); i++) {
                set_visibility[i] = LManager.getLayer_onload_visible_arr().get(i);
            }
            tileOverlay = new TileOverlay[geeoserver_id.size()];
            Log.d(TAG, "@@AK" + tileOverlay.length);
            wmsTileProvider = new TileProvider[geeoserver_id.size()];
        } catch (JSONException e) {
            Log.e(TAG, "@@AK Json EXCEPTION" + e);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {/**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnCameraChangeListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);



        //for zoom map
        double first_latitude = 24.255572756457415;
        double first_longitude = 72.18788277357817;

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(first_latitude, first_longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        // end zoom map code


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                int zoomLevel = (int) mMap.getCameraPosition().zoom;
                if (zoomLevel < 17) {
                    Toast.makeText(MapsActivity.this, "Please zoom in to click", Toast.LENGTH_SHORT).show();
                    Log.d("@@AK zoomLevel", String.valueOf(zoomLevel));

                } else {
                    Log.d("@@AK zoomLevel", String.valueOf(zoomLevel));
                    if (utils.isConnected(MapsActivity.this)) {
                        params = new HashMap<>();
                        params.put("controller", "getFeatureInfo");
                        params.put("ga_id", "609");
                        params.put("click_lat", String.valueOf(latLng.latitude));
                        params.put("click_long", String.valueOf(latLng.longitude));
                        ApiType = "getFeatureInfo";


                        getOnclickInfo(MapsActivity.this);

                    } else {
                        utils.showSmallAlert(rlContainer, getString(R.string.no_internet));
                    }
                }
            }
        });



        setUpPipeLine();
    }

    private void setUpPipeLine() {
        for (int i = 0; i < geeoserver_id.size(); i++) {
            wmsTileProvider[i] = TileProviderFactory.getOsgeoWmsTileProvider(geeoserver_id.get(i), ApplicationConstant.SELECT_WMS_LINK);
            tileOverlay[i] = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(wmsTileProvider[i]));
            tileOverlay[i].setVisible(set_visibility[i]);
        }
    }

    private void getOnclickInfo(Context mContext) {

        mProgressDialog = ProgressDialog.show(this, "",
                getResources().getString(R.string.processing), true);
        String REQUEST_URL = ApplicationConstant.BASE_URL;

        Log.e(TAG, "@@URL" + REQUEST_URL);

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        NetworkResponseHelper<LayerResponse> myReq = new NetworkResponseHelper<>(
                Request.Method.POST,
                REQUEST_URL,
                LayerResponse.class,
                params,
                createReqSuccessListener(),
                createReqErrorListener());

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                ApplicationConstant.SOCKET_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(myReq);

    }

    protected Response.ErrorListener createReqErrorListener() {
        return error -> {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            Log.e(TAG, error.toString());
        };
    }


    protected Response.Listener<LayerResponse> createReqSuccessListener() {
        return response -> {
            Log.d(TAG, "@@@Ak response" + response);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            try {
                if (response.getSuccess().equals(ApplicationConstant.RESPONSE_SUCCESS)) {
                    if (response.getData().size() == 1) {

                        for (LayerResponseData layerResponseData : response.getData()) {

                            assetKey.clear();
                            assetValue.clear();
                            dataJson.clear();

                            UserSessionManager sessionManager = new UserSessionManager(MapsActivity.this);
                            sessionManager.createSelectedLayerClick(layerResponseData.getColumn_name(),
                                    layerResponseData.getTable_name(),
                                    layerResponseData.getDisplay_layer_name());
                            geoserver_id_layersDbname = LManager.getAttributeDbName(layerResponseData.getTable_name());
                            //attribute array label name
                            attrbuteLabel = LManager.getAttributeLabel(layerResponseData.getTable_name());

                            dataJson.putAll((Map<? extends String, ? extends String>) layerResponseData.getData().get(0));
                        }

                        assetValue.addAll(dataJson.values());
                        assetKey.addAll(dataJson.keySet());

                        final_pop_up_key.clear();
                        final_pop_up_value.clear();

                        for (int jj = 0; jj < assetKey.size(); jj++) {
                            for (int xx = 0; xx < geoserver_id_layersDbname.size(); xx++) {
                                if (assetKey.get(jj).equals(geoserver_id_layersDbname.get(xx))) {
                                    String key = attrbuteLabel.get(xx);

                                        final_pop_up_key.add(key);
                                        final_pop_up_value.add(String.valueOf(assetValue.get(jj)));

                                }
                            }
                        }

                        onClickPopUp(final_pop_up_key, final_pop_up_value);

                    }
                    else {
                        List<String> list = new ArrayList<>();
                        list.clear();
                        layerlist.clear();
                        for (LayerResponseData layerResponseData : response.getData()) {
                            list.add(layerResponseData.getDisplay_layer_name());

                            layerlist.add(new LayerResponseData(layerResponseData.getColumn_name(),
                                    layerResponseData.getDisplay_layer_name(),
                                    layerResponseData.getTable_name(), layerResponseData.getData()));

                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder.setTitle("Layer List");

                        builder.setItems(list.toArray(new String[0]), (dialogInterface, i) -> {

                            assetKey.clear();
                            assetValue.clear();
                            dataJson.clear();
                            final_pop_up_key.clear();
                            final_pop_up_value.clear();
                            UserSessionManager sessionManager = new UserSessionManager(MapsActivity.this);
                            sessionManager.createSelectedLayerClick(layerlist.get(i).getColumn_name(),
                                    layerlist.get(i).getTable_name(),
                                    layerlist.get(i).getDisplay_layer_name());
                            ArrayList<String> geoserver_id_layersDbname = LManager.getAttributeDbName(layerlist.get(i).getTable_name());
                            ArrayList<String> attrbuteLabel = LManager.getAttributeLabel(layerlist.get(i).getTable_name());

                            dataJson.putAll((Map<? extends String, ? extends String>) layerlist.get(i).getData().get(0));

                            assetValue.addAll(dataJson.values());
                            assetKey.addAll(dataJson.keySet());


                            //layer.json filter data according to attribute_list and replace id by its  proper name
                            for (int jj = 0; jj < assetKey.size(); jj++) {
                                Log.d("@@AK 1st for  assetkey", String.valueOf(assetKey.get(jj)));
                                for (int xx = 0; xx < geoserver_id_layersDbname.size(); xx++) {
                                    Log.d("@@AK layersDbname", String.valueOf(geoserver_id_layersDbname.get(xx)));

                                    if (geoserver_id_layersDbname.get(xx).equalsIgnoreCase(assetKey.get(jj))) {
                                        //replace id_name with proper name
                                        String key = attrbuteLabel.get(xx);
                                        Log.d("@@AK final show", key);
                                        Log.d("@@AK final show", assetValue.get(jj));

                                        final_pop_up_key.add(key);
                                        final_pop_up_value.add(String.valueOf(assetValue.get(jj)));

                                    }
                                }
                            }
                            //end of layer.json filter
                            onClickPopUp(final_pop_up_key, final_pop_up_value);

                        });


                        builder.show();
                    }
                } else if (response.getSuccess().equals(ApplicationConstant.RESPONSE_FAILURE)) {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }


    //Method for click on list item

    protected void onClickPopUp(ArrayList<String> str_popup_arr_key, final ArrayList<String> str_popup_arr_value) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = getLayoutInflater().inflate(R.layout.map_pop_up_layout, null);
        ListView lv = view.findViewById(R.id.map_pop_up_lv);
        final TextView infoModule = view.findViewById(R.id.infoModule);

        infoModule.setText(new UserSessionManager(this).getSelectedLayerClick().get("display_layer_name"));
        MapPopUp_ListAdaptor clad = new MapPopUp_ListAdaptor(MapsActivity.this, str_popup_arr_key, str_popup_arr_value);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.show();
    }


    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraChange(@NonNull CameraPosition cameraPosition) {

    }
}