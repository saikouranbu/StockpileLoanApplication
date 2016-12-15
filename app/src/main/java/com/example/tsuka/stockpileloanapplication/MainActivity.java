package com.example.tsuka.stockpileloanapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1; // 識別用:位置情報の許可

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // パーソナルデータ登録ボタンを押下時の挙動
    public void onPersonalEntryClick(View v) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 位置情報の権限がある場合
            // パーソナルデータ登録アクティビティに遷移
            Intent intent = new Intent(this.getBaseContext(), PersonalEntryActivity.class);
            startActivity(intent);
        } else {
            // 位置情報の権限がない場合
            // 許可を求めるダイアログを表示
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    // パーミッションの許可を求めるダイアログ選択時に呼び出される
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 位置情報の権限を求める
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 許可された場合
                // パーソナルデータ登録アクティビティに遷移
                Intent intent = new Intent(this.getBaseContext(), PersonalEntryActivity.class);
                startActivity(intent);
            } else {
                // 許可されなかった場合
                Toast.makeText(this, "位置情報の許可を出さない限りアプリを利用できません", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
