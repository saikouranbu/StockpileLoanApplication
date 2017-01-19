package com.example.tsuka.stockpileloanapplication.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class LatLngGetter {
    private static LatLng latLng;
    private LocationManager locationManager;
    private Activity activity;
    private boolean isGet;

    public LatLngGetter(Activity activity) {
        this.activity = activity;
        isGet = false;
    }

    public boolean isGet(){
        return isGet;
    }

    public LatLng getLatLng(){
        return latLng;
    }

    public void getLocation() {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, // 位置情報更新を行う最低更新時間間隔（ms）
                0, // 位置情報更新を行う最小距離間隔（メートル）
                new LocationListener() {
                    // ロケーションが変更された時の動き
                    @Override
                    public void onLocationChanged(Location location) {
                        // 権限のチェック
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)
                            return;
                        // 緯度と経度を取得
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        Toast.makeText(activity, "現在位置の取得が完了しました", Toast.LENGTH_SHORT).show();
                        Log.d("LocationChanged", "Complete");
                        locationManager.removeUpdates(this);
                        isGet = true;
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Toast.makeText(activity, "位置情報がOFFになっています", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                }
        );
        Log.d("LocationChanged", "Loading");
    }
}
