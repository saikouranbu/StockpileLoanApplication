package com.example.tsuka.stockpileloanapplication.models;


import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.activities.MapSearchActivity;

public class MapSearchModel {
    private RelativeLayout back;
    private MapSearchActivity activity;

    public MapSearchModel(MapSearchActivity activity){
        back = (RelativeLayout) activity.findViewById(R.id.activity_map_search);
        this.activity = activity;
    }

    // 備蓄品マップを表示ボタンを押下時の挙動
    public void displayStockpileMap(){
        Toast.makeText(activity, "マップを表示", Toast.LENGTH_SHORT).show();
    }

    // 画面タップ時にフォーカスを背景に移すためのメソッド
    public void clickFocus() {
        back.requestFocus();
    }
}
