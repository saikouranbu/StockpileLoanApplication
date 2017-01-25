package com.example.tsuka.stockpileloanapplication.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.models.MainModel;
import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

public class MainActivity extends AppCompatActivity {
    private MainModel model;

    private static final int REQUEST_LOCATION = 1; // 識別用:位置情報の許可

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new MainModel(this);
    }

    // パーミッションの許可を求めるダイアログ選択時に呼び出される
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 位置情報の権限を求める
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 許可された場合
                // パーソナルデータ登録アクティビティに遷移
                Intent intent = new Intent(this, PersonalEntryActivity.class);
                startActivity(intent);
            } else {
                // 許可されなかった場合
                Toast.makeText(this, "位置情報の許可を出さない限りアプリを利用できません", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // パーソナルデータ登録ボタンを押下時の挙動
    public void onPersonalEntryClick(View v) {
        model.personalEntry();
    }

    // 備蓄品データ登録ボタンを押下時の挙動
    public void onStockpileEntryClick(View v) {
        model.stockpileEntry();
    }

    // 備蓄品マップボタンを押下時の挙動
    public void onStockpileMapClick(View v) {
        model.stockpileMap();
    }
    // 備蓄品データ公開スイッチを押下時の挙動
    public void onOpenSwitchClick(View v) {
        model.checkedSwitch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 備蓄品データが全削除されて戻ってきた場合スイッチをOFFにする
        if (new UseProperties(this).isOpen() == false)
            model.offOpenSwitch();
    }
}
