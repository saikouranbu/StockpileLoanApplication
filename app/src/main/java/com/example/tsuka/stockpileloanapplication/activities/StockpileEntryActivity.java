package com.example.tsuka.stockpileloanapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.models.StockpileEntryModel;

public class StockpileEntryActivity extends AppCompatActivity {
    private StockpileEntryModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockpile_entry);

        model = new StockpileEntryModel(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        model.onStart();
    }

    // 備蓄品データの空をリストに追加する
    public void onStockpileAddClick(View v) {
        model.stockpileAdd();
    }

    // 備蓄品データの末尾を削除する
    public void onStockpileRemoveClick(View v) {
        model.stockpileRemove();
    }

    // 備蓄品データをデータベースサーバに送信
    public void onStockpileEntryClick(View v) {
        model.stockpileEntry();
    }

    // 背景をタップしたときにキーボードを閉じる
    public void onClickFocus(View v) {
        model.clickFocus();
    }
}
