package com.example.tsuka.stockpileloanapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class PersonalEntryActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_entry);
    }

    // 位置情報を取得ボタンを押下時の挙動
    public void onGetLocationClick(View v) {
        // 位置情報を登録する場所の確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle("位置情報を取得");
        alertDlg.setMessage("現在位置を取得します\n備蓄している位置での取得をお願いします\n現在位置を取得してよろしいですか？");
        alertDlg.setPositiveButton(
                "はい",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // はい ボタンクリック処理
                        // 位置情報を取得する
                        getLocation();
                    }
                });
        alertDlg.setNegativeButton(
                "いいえ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // いいえ ボタンクリック処理
                        Toast.makeText(PersonalEntryActivity.this, "備蓄している位置で取得してください", Toast.LENGTH_SHORT).show();
                    }
                });

        // 表示
        alertDlg.create().show();
    }

    // 位置情報を確認ボタンを押下時の挙動
    public void onConfLocationClick(View v) {
        // 位置情報を取得済みかどうかの確認
        if(latLng == null){
            Toast.makeText(PersonalEntryActivity.this, "現在位置を取得してください", Toast.LENGTH_SHORT).show();
            return;
        }

    // 位置情報を確認する場所の確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle("位置情報を確認");
        alertDlg.setMessage("現在位置をGoogleMapで確認します");
        alertDlg.setPositiveButton(
                "はい",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // はい ボタンクリック処理
                        // 位置情報を取得する
                        Toast.makeText(PersonalEntryActivity.this, "位置を確認します", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDlg.setNegativeButton(
                "いいえ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // いいえ ボタンクリック処理
                    }
                });

        // 表示
        alertDlg.create().show();
    }

    // 位置情報を取得する関数
    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, // 位置情報更新を行う最低更新時間間隔（ms）
                0, // 位置情報更新を行う最小距離間隔（メートル）
                new LocationListener() {
                    // ロケーションが変更された時の動き
                    @Override
                    public void onLocationChanged(Location location) {
                        // 権限のチェック
                        if (ActivityCompat.checkSelfPermission(PersonalEntryActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)
                            return;
                        // テキストとログに位置情報を表示
                        String strLatitude = String.valueOf(location.getLatitude());
                        String strLongitude = String.valueOf(location.getLongitude());
                        Log.d("GPS", strLatitude + "," + strLongitude);
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        Toast.makeText(PersonalEntryActivity.this, "現在位置を取得しました", Toast.LENGTH_SHORT).show();
                        locationManager.removeUpdates(this);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                }
        );
    }
}
