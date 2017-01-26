package com.example.tsuka.stockpileloanapplication.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.db.StockpileTableSearch;
import com.example.tsuka.stockpileloanapplication.utils.PersonalData;
import com.example.tsuka.stockpileloanapplication.utils.StockpileData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class StockpileMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng latLng;
    private int stockpileNamePosition;

    ProgressDialog dialog;

    private static final int OVER_REQ = 1; // 備蓄個数が必要数を上回っている
    private static final int SAME_REQ = 0; // 備蓄個数が必要数と同等
    private static final int UNDER_REQ = -1; // 備蓄個数が必要数を下回っている

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
        stockpileNamePosition = Integer.parseInt(intent.getStringExtra("stockpileNamePosition"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        // マップに現在位置のマーカーを表示し視点を現在位置に移動
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Please wait");
        dialog.setMessage("Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        StockpileTableSearch search = new StockpileTableSearch(stockpileNamePosition, this);
        search.execute();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        latLng = null;
    }

    public void getStockpileSearchResult(ArrayList<PersonalData> personalList) {
        StringBuilder snippetBuilder = null;
        StockpileData stock = null;
        MarkerOptions personalMarker = null;
        for (PersonalData personal : personalList) {
            stock = personal.getStockpile();

            personalMarker = new MarkerOptions().position(personal.getLatLng()).title(personal.getStockpilePoint());


            snippetBuilder = new StringBuilder();
            snippetBuilder.append("連絡先1:");
            snippetBuilder.append(personal.getContactInfo());
            snippetBuilder.append("\n連絡先2:");
            snippetBuilder.append(personal.getContactInfo2());
            if (stock.compareReqNum() == OVER_REQ) {
                // 備蓄個数が必要数を上回っている場合
                snippetBuilder.append("\n余剰備蓄品:");
                snippetBuilder.append(stock);
                snippetBuilder.append("\n貸借可能備蓄数:");
                snippetBuilder.append(stock.getSubNum());
                personalMarker.snippet(snippetBuilder.toString());
                personalMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(personalMarker);
            } else if (personal.getStockpile().compareReqNum() == UNDER_REQ) {
                // 備蓄個数が必要数を下回っている場合
                snippetBuilder.append("\n不足備蓄品:");
                snippetBuilder.append(stock);
                snippetBuilder.append("\n不足備蓄数:");
                snippetBuilder.append(-stock.getSubNum());
                snippetBuilder.append("\n緊急度:");
                snippetBuilder.append(stock.getEmergencyLevel());
                personalMarker.snippet(snippetBuilder.toString());
                if(stock.getEmergencyLevelPosition() == 2){ // 緊急度低
                    personalMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }else if(stock.getEmergencyLevelPosition() == 3){ // 緊急度中
                    personalMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                }else if(stock.getEmergencyLevelPosition() == 4){ // 緊急度高
                    personalMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }else{
                    // 配送待ち
                    personalMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                mMap.addMarker(personalMarker);
            } else {
                // 備蓄個数が必要数と同等の場合マップに表示しない
            }
        }

        dialog.dismiss();
    }
}
