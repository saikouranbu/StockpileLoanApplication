package com.example.tsuka.stockpileloanapplication.models;


import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.utils.StockpileData;
import com.example.tsuka.stockpileloanapplication.utils.StockpileListAdapter;
import com.example.tsuka.stockpileloanapplication.activities.StockpileEntryActivity;

import java.util.ArrayList;

public class StockpileEntryModel {
    private StockpileEntryActivity activity;
    private ListView stockpileListView;
    private RelativeLayout back;
    private ArrayList<StockpileData> stockpileList;
    private StockpileListAdapter stockpileListAdapter;

    public StockpileEntryModel(StockpileEntryActivity activity) {
        this.activity = activity;

        stockpileListView = (ListView) activity.findViewById(R.id.stockpileDataList);
        back = (RelativeLayout) activity.findViewById(R.id.activity_stockpile_entry);

        stockpileList = new ArrayList<StockpileData>();
        stockpileListAdapter = new StockpileListAdapter(activity, stockpileList);

        updateListView();
    }

    // 備蓄品データの空をリストに追加する
    public void stockpileAdd() {
        Log.d("addBoolean", String.valueOf(stockpileList.add(new StockpileData())));
        updateListView();
    }

    // 備蓄品データの末尾を削除する
    public void stockpileRemove() {
        if (stockpileList.size() != 0) {
            Log.d("removeData", stockpileList.remove(stockpileList.size() - 1).toString());
            updateListView();
        } else {
            Toast.makeText(activity, "削除するデータがありません", Toast.LENGTH_SHORT).show();
        }
    }

    // 備蓄品データをデータベースサーバに送信
    public void stockpileEntry() {
        updateListView();
        if (!isCompletedStockpileList()) {
            Toast.makeText(activity, "備蓄品データがすべて入力されていません", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(activity, "送信", Toast.LENGTH_SHORT).show();
    }

    // 備蓄品リスト内のデータがすべて入力済みか確認
    // 入力済みならtrue
    // データ数が0の場合と入力されてないデータがあればfalse
    public boolean isCompletedStockpileList() {
        if (stockpileList.size() == 0) {
            Toast.makeText(activity, "送信するデータがありません", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (StockpileData stock : stockpileList) {
            if (!stock.isCompletedStockpileData()) return false;
        }
        return true;
    }

    // 背景をタップしたときにキーボードを閉じる
    public void clickFocus() {
        back.requestFocus();
        updateListView();
    }

    // リストを更新する
    public void updateListView() {
        stockpileListView.setAdapter(stockpileListAdapter);
    }
}
