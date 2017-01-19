package com.example.tsuka.stockpileloanapplication.models;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.activities.MapSearchActivity;
import com.example.tsuka.stockpileloanapplication.activities.MapsActivity;
import com.example.tsuka.stockpileloanapplication.utils.LatLngGetter;

public class MapSearchModel {
    private RelativeLayout back;
    private MapSearchActivity activity;

    private LatLngGetter latLngGetter;

    public MapSearchModel(MapSearchActivity activity){
        back = (RelativeLayout) activity.findViewById(R.id.activity_map_search);
        this.activity = activity;

        latLngGetter = new LatLngGetter(activity);
    }

    // 位置情報を取得ボタンを押下時の挙動
    public void getLocationClick() {
        latLngGetter.getLocation();
    }

    // 備蓄品マップを表示ボタンを押下時の挙動
    public void displayStockpileMap(){
        if (!latLngGetter.isGet()) {
            Toast.makeText(activity, "現在位置の取得が完了していません", Toast.LENGTH_SHORT).show();
            return;
        }

        // 位置情報を確認する場所の確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(activity);
        alertDlg.setTitle("位置情報を確認");
        alertDlg.setMessage("現在位置をGoogleMapで確認します");
        alertDlg.setPositiveButton(
                "はい",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // はい ボタンクリック処理
                        // GoogleMapを開いて位置情報を確認する
                        Intent intent = new Intent(activity, MapsActivity.class);

                        // 位置情報をMapsActivityに受け渡す
                        intent.putExtra("lat", String.valueOf(latLngGetter.getLatLng().latitude));
                        intent.putExtra("lng", String.valueOf(latLngGetter.getLatLng().longitude));

                        // MapsActivityに遷移
                        activity.startActivity(intent);
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

    // 画面タップ時にフォーカスを背景に移すためのメソッド
    public void clickFocus() {
        back.requestFocus();
    }
}
