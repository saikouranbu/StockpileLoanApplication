package com.example.tsuka.stockpileloanapplication.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.db.StockpileTableSearch;
import com.example.tsuka.stockpileloanapplication.utils.PersonalData;
import com.example.tsuka.stockpileloanapplication.utils.StockpileData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class StockpileMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng latLng;
    private String stockpileName;

    ProgressDialog dialog;

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
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Please wait");
        dialog.setMessage("Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        StockpileTableSearch search = new StockpileTableSearch(stockpileName, this);
        search.execute();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        latLng = null;
    }

    public void getStockpileSearchResult(ArrayList<PersonalData> personalList) {
        StringBuilder contactBuilder = null;
        StringBuilder stockpileBuilder = null;
        for (PersonalData personal : personalList) {
            contactBuilder = new StringBuilder();
            contactBuilder.append("連絡先:");
            contactBuilder.append(personal.getContactInfo());
            contactBuilder.append("\t連絡先2:");
            contactBuilder.append(personal.getContactInfo2());

            stockpileBuilder = new StringBuilder();
            stockpileBuilder.append("貸借可能備蓄品:");
            int canLoanNum = 0;
            for (StockpileData stock : personal.getStockpileList()) {
                stockpileBuilder.append(stock.getStockpileName());
                canLoanNum = Integer.parseInt(stock.getStockpileNum()) - Integer.parseInt(stock.getStockpileReqNum());
                stockpileBuilder.append(canLoanNum);
                stockpileBuilder.append(stock.getStockpileNumUnit());
                stockpileBuilder.append(",");
            }
            stockpileBuilder.deleteCharAt(stockpileBuilder.length() - 1);
            mMap.addMarker(new MarkerOptions().position(personal.getLatLng()).title(contactBuilder.toString()).snippet(stockpileBuilder.toString()));
        }

        dialog.dismiss();
    }
}
