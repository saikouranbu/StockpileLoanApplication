package com.example.tsuka.stockpileloanapplication.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.tsuka.stockpileloanapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class StockpileMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng latLng;
    private String stockpileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 現在位置情報を受け取る
        Intent intent = getIntent();
        Double lat = Double.parseDouble(intent.getStringExtra("lat"));
        Double lng = Double.parseDouble(intent.getStringExtra("lng"));
        latLng = new LatLng(lat, lng);
        // 検索ワードを受け取る
        stockpileName = intent.getStringExtra("stockpileName");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // マップに現在位置のマーカーを表示し視点を現在位置に移動
        mMap.addMarker(new MarkerOptions().position(latLng).title("検索ワード").snippet(stockpileName+"\n\n"+stockpileName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        latLng = null;
    }
}
