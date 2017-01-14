package com.example.tsuka.stockpileloanapplication.models;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.activities.MainActivity;
import com.example.tsuka.stockpileloanapplication.activities.PersonalEntryActivity;
import com.example.tsuka.stockpileloanapplication.activities.StockpileEntryActivity;
import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

public class MainModel {
    private MainActivity activity;

    private static final int REQUEST_LOCATION = 1; // 識別用:位置情報の許可
    private UseProperties useProperties;

    public MainModel(MainActivity activity){
        this.activity = activity;
    }

    // パーソナルデータ登録ボタンを押下時の挙動
    public void personalEntry() {
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 位置情報の権限がある場合
            // パーソナルデータ登録アクティビティに遷移
            Intent intent = new Intent(activity.getBaseContext(), PersonalEntryActivity.class);
            activity.startActivity(intent);
        } else {
            // 位置情報の権限がない場合
            // 許可を求めるダイアログを表示
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    // 備蓄品データ登録ボタンを押下時の挙動
    public void stockpileEntry() {
        // パーソナルデータが登録されているかの確認
        if (!isPersonalDataEmpty()) return;

        // 備蓄品データ登録アクティビティに遷移
        Intent intent = new Intent(activity.getBaseContext(), StockpileEntryActivity.class);
        activity.startActivity(intent);
    }

    private boolean isPersonalDataEmpty() {
        useProperties = new UseProperties(activity.getApplicationContext());
        if (useProperties.isRegisteredProperties()) {
            useProperties = null;
            return true;
        } else {
            Toast.makeText(activity, "パーソナルデータを登録してください", Toast.LENGTH_SHORT).show();
            useProperties = null;
            return false;
        }
    }
}
