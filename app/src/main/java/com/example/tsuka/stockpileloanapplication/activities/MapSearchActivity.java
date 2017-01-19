package com.example.tsuka.stockpileloanapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.models.MapSearchModel;

public class MapSearchActivity extends AppCompatActivity {
    private MapSearchModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        model = new MapSearchModel(this);
    }

    // 備蓄品マップを表示ボタンを押下時の挙動
    public void onDisplayStockpileMapClick(View v){
        model.displayStockpileMap();
    }

    // 画面タップ時にフォーカスを背景に移すためのメソッド
    public void onBackClick(View v) {
        model.clickFocus();
    }
}
