package com.googlemapapp;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, LocationListener, LocationSource {

    // 定義這個權限要求的編號
    private final int REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION = 100;

    private GoogleMap mMap;

    private Spinner mSpnLocation;
    private static String[][] mLocations = {
            {"台北101", "25.0336110,121.5650000"},
            {"中國長城", "40.0000350,119.7672800"},
            {"紐約自由女神像", "40.6892490,-74.0445000"},
            {"巴黎鐵塔", "48.8582220,2.2945000"}	};

    private SupportMapFragment mSupportMapFragment;
    private boolean mbIsZoomFirst = true;

    private LocationManager mLocationMgr;
    private OnLocationChangedListener mLocationChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        mSpnLocation = (Spinner) this.findViewById(R.id.spnLocation);

        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item);

        for (int i = 0; i < mLocations.length; i++)
            arrAdapter.add(mLocations[i][0]);

        arrAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        mSpnLocation.setAdapter(arrAdapter);
        mSpnLocation.setOnItemSelectedListener(spnLocationOnItemSelected);

        Spinner spnMapType = (Spinner) findViewById(R.id.spnMapType);
        spnMapType.setOnItemSelectedListener(spnMapTypeOnItemSelected);

        Button btn3DMap = (Button) findViewById(R.id.btn3DMap);
        btn3DMap.setOnClickListener(btn3DMapOnClick);

        // 建立SupportMapFragment，並且設定Map的callback
        mSupportMapFragment = new SupportMapFragment();
        mSupportMapFragment.getMapAsync(this);

        // 把SupportMapFragment放到介面佈局檔裡頭的FrameLayout顯示。
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayMapContainer, mSupportMapFragment)
                .commit();

        mLocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // App從背景切換到前景執行，啟動定位功能。
        if (mMap != null)
            checkLocationPermissionAndEnableLocationUpdate(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 停止定位功能。
        checkLocationPermissionAndEnableLocationUpdate(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 檢查收到的權限要求編號是否和我們送出的相同
        if (requestCode == REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 再檢查一次，就會進入同意的狀態，並且順利啟動。
                checkLocationPermissionAndEnableLocationUpdate(true);
                return;
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.setMyLocationEnabled(true);   // 如果在模擬器執行，要加上這行程式碼
        mMap.setLocationSource(this);

        // 取得上一次定位資料。
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            Location location =
                    mLocationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null)
                location =
                        mLocationMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                Toast.makeText(MapsActivity.this, "成功取得上一次定位",
                        Toast.LENGTH_LONG).show();
                onLocationChanged(location);    // 更新地圖的定位。
            } else
                Toast.makeText(MapsActivity.this, "沒有上一次定位的資料",
                        Toast.LENGTH_LONG).show();
        }
    }

    private AdapterView.OnItemSelectedListener spnLocationOnItemSelected =
            new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView parent, View v,
                                           int position, long id) {
                    updateMapLocation();
                }

                @Override
                public void onNothingSelected(AdapterView parent) {
                }

            };

    private void updateMapLocation() {
        int iSelected = mSpnLocation.getSelectedItemPosition();
        String[] sLocation = mLocations[iSelected][1].split(",");
        double dLat = Double.parseDouble(sLocation[0]);	// 南北緯
        double dLon = Double.parseDouble(sLocation[1]);	// 東西經

        // 如果是第一次執行，把地圖放大到設定的等級。
        if (mbIsZoomFirst) {
            mbIsZoomFirst = false;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(dLat, dLon), 15));
        } else
            mMap.animateCamera(CameraUpdateFactory.newLatLng(
                    new LatLng(dLat, dLon)));
    }

    private AdapterView.OnItemSelectedListener spnMapTypeOnItemSelected =
            new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView parent,
                                           View v, int position, long id) {
                    // 依照使用者點選的項目位置，改變地圖模式。
                    switch (position) {
                        case 0:
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case 1:
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                        case 2:
                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                        case 3:
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView parent) {
                }
            };

    private View.OnClickListener btn3DMapOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // 設定地圖的俯視角度，並且放大到一定的等級，讓3D建築物出現。
            CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(mMap.getCameraPosition().target)
                            .tilt(60)
                            .zoom(18)
                            .build());
            mMap.animateCamera(camUpdate);
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        // 把新位置傳給Google Map的my-location layer。
        if (mLocationChangedListener != null)
            mLocationChangedListener.onLocationChanged(location);

        // 移動地圖到新位置。
        mMap.animateCamera(CameraUpdateFactory.newLatLng(
                new LatLng(location.getLatitude(), location.getLongitude())));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String str = provider;
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                str += "定位功能無法使用";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                str += "暫時無法定位";	// GPS正在定位中時會傳入這個值。
                break;
        }

        Toast.makeText(MapsActivity.this, str, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(MapsActivity.this, provider + "定位功能開啟",
                Toast.LENGTH_LONG).show();
        checkLocationPermissionAndEnableLocationUpdate(true);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MapsActivity.this, provider + "定位功能已經被關閉",
                Toast.LENGTH_LONG).show();
        checkLocationPermissionAndEnableLocationUpdate(false);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mLocationChangedListener = listener;
        checkLocationPermissionAndEnableLocationUpdate(true);
        Toast.makeText(MapsActivity.this, "地圖的my-location layer已經啟用",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void deactivate() {
        mLocationChangedListener = null;
        checkLocationPermissionAndEnableLocationUpdate(false);
        Toast.makeText(MapsActivity.this, "地圖的my-location layer已經關閉",
                Toast.LENGTH_LONG).show();
    }

    private void checkLocationPermissionAndEnableLocationUpdate(boolean on) {
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // 這項功能尚未取得使用者的同意
            // 開始執行徵詢使用者的流程
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    MapsActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(MapsActivity.this);
                altDlgBuilder.setTitle("提示");
                altDlgBuilder.setMessage("App需要啟動定位功能。");
                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
                altDlgBuilder.setCancelable(false);
                altDlgBuilder.setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 顯示詢問使用者是否同意功能權限的對話盒
                                // 使用者答覆後會執行callback方法onRequestPermissionsResult()
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{
                                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
                            }
                        });
                altDlgBuilder.show();

                return;
            } else {
                // 顯示詢問使用者是否同意功能權限的對話盒
                // 使用者答覆後會執行callback方法onRequestPermissionsResult()
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);

                return;
            }
        }

        // 這項功能之前已經取得使用者的同意
        // 根據on參數的值，啟動或關閉定位功能
        if (on) {
            // 如果GPS功能有開啟，優先使用GPS定位，否則使用網路定位。
            if(mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLocationMgr.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 5000, 5, this);
                Toast.makeText(MapsActivity.this, "使用GPS定位", Toast.LENGTH_LONG)
                        .show();
            } else if(mLocationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                mLocationMgr.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 5000, 5, this);
                Toast.makeText(MapsActivity.this, "使用網路定位", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            mLocationMgr.removeUpdates(this);
            Toast.makeText(MapsActivity.this, "定位功能已經停用", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
