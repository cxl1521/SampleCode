package com.googlemapapp;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Spinner mSpnLocation;
    private static String[][] mLocations = {
            {"台北101", "25.0336110,121.5650000"},
            {"中國長城", "40.0000350,119.7672800"},
            {"紐約自由女神像", "40.6892490,-74.0445000"},
            {"巴黎鐵塔", "48.8582220,2.2945000"}	};

    private SupportMapFragment mSupportMapFragment;
    private boolean mbIsZoomFirst = true;

    private Marker mMarker1, mMarker2, mMarker3, mMarker4;
    private Polyline mPolylineRoute;

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

        // 設定4個新按鈕的OnClickListener。
        Button btnAddMarker = (Button) findViewById(R.id.btnAddMarker);
        btnAddMarker.setOnClickListener(btnAddMarkerOnClick);
        Button btnRemoveMarker = (Button) findViewById(R.id.btnRemoveMarker);
        btnRemoveMarker.setOnClickListener(btnRemoveMarkerOnClick);
        Button btnShowRoute = (Button) findViewById(R.id.btnShowRoute);
        btnShowRoute.setOnClickListener(btnShowRouteOnClick);
        Button btnHideRoute = (Button) findViewById(R.id.btnHideRoute);
        btnHideRoute.setOnClickListener(btnHideRouteOnClick);
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

        // 設定Google Map的Info Window。
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                View v = getLayoutInflater()
                        .inflate(R.layout.map_info_window, null);
                TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
                txtTitle.setText(marker.getTitle());
                TextView txtSnippet = (TextView) v.findViewById(R.id.txtSnippet);
                txtSnippet.setText(marker.getSnippet());
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        // 設定Info Window的OnClickListener。
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                // TODO Auto-generated method stub
                marker.hideInfoWindow();
            }
        });

        // 建立Polyline，並且先將它隱藏。
        PolylineOptions polylineOpt = new PolylineOptions()
                .width(15)
                .color(Color.BLUE);
        ArrayList<LatLng> listLatLng = new ArrayList<LatLng>();
        listLatLng.add(new LatLng(25.0336110, 121.5650000));
        listLatLng.add(new LatLng(25.037, 121.5650000));
        listLatLng.add(new LatLng(25.037, 121.5630000));
        polylineOpt.addAll(listLatLng);
        mPolylineRoute = mMap.addPolyline(polylineOpt);
        mPolylineRoute.setVisible(false);
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

    private View.OnClickListener btnAddMarkerOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // 在4個景點加上Marker。
            if (mMarker1 == null) {
                String[] sLocation = mLocations[0][1].split(",");
                double dLat = Double.parseDouble(sLocation[0]);
                double dLon = Double.parseDouble(sLocation[1]);
                mMarker1 = mMap.addMarker(new MarkerOptions()
                        .title(mLocations[0][0])
                        .snippet("2004年完工,高509公尺")
                        .position(new LatLng(dLat, dLon))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrows))
                        .anchor(0.5f, 0.5f));
            }

            if (mMarker2 == null) {
                String[] sLocation = mLocations[1][1].split(",");
                double dLat = Double.parseDouble(sLocation[0]);
                double dLon = Double.parseDouble(sLocation[1]);
                mMarker2 = mMap.addMarker(new MarkerOptions()
                        .title(mLocations[1][0])
                        .snippet("東起山海關,西至嘉峪關,全長6000多公里")
                        .position(new LatLng(dLat, dLon))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle))
                        .anchor(0.5f, 0.5f));
            }

            if (mMarker3 == null) {
                String[] sLocation = mLocations[2][1].split(",");
                double dLat = Double.parseDouble(sLocation[0]);
                double dLon = Double.parseDouble(sLocation[1]);
                mMarker3 = mMap.addMarker(new MarkerOptions()
                        .title(mLocations[2][0])
                        .snippet("1886年完工,高93公尺")
                        .position(new LatLng(dLat, dLon))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.square))
                        .anchor(0.5f, 0.5f));
            }

            if (mMarker4 == null) {
                String[] sLocation = mLocations[3][1].split(",");
                double dLat = Double.parseDouble(sLocation[0]);
                double dLon = Double.parseDouble(sLocation[1]);
                mMarker4 = mMap.addMarker(new MarkerOptions()
                        .title(mLocations[3][0])
                        .snippet("又稱為艾菲爾鐵塔,高324公尺")
                        .position(new LatLng(dLat, dLon))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.star))
                        .anchor(0.5f, 0.5f));
            }
        }
    };

    private View.OnClickListener btnRemoveMarkerOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mMarker1 != null) {
                mMarker1.remove();
                mMarker1 = null;
            }
            if (mMarker2 != null) {
                mMarker2.remove();
                mMarker2 = null;
            }
            if (mMarker3 != null) {
                mMarker3.remove();
                mMarker3 = null;
            }
            if (mMarker4 != null) {
                mMarker4.remove();
                mMarker4 = null;
            }
        }
    };

    private View.OnClickListener btnShowRouteOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mPolylineRoute.setVisible(true);
        }
    };

    private View.OnClickListener btnHideRouteOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mPolylineRoute.setVisible(false);
        }
    };
}
