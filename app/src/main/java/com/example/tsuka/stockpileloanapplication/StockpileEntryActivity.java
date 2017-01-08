package com.example.tsuka.stockpileloanapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class StockpileEntryActivity extends AppCompatActivity {
    private ListView stockpileListView;
    private Button stockpileAddButton;
    private RelativeLayout back;
    private ArrayList stockpileList;
    private StockpileListAdapter stockpileListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockpile_entry);

        stockpileListView = (ListView) findViewById(R.id.stockpileDataList);
        stockpileAddButton = (Button) findViewById(R.id.stockpileAddButton);
        back = (RelativeLayout) findViewById(R.id.activity_stockpile_entry);

        stockpileList = new ArrayList<StockpileData>();
        stockpileListAdapter = new StockpileListAdapter(this, stockpileList);

        updateListView();
    }

    // 備蓄品データの空をリストに追加する
    public void onStockpileAddClick(View v) {
        Log.d("addBoolean", String.valueOf(stockpileList.add(new StockpileData())));
        updateListView();
    }

    // 備蓄品データの末尾を削除する
    public void onStockpileRemoveClick(View v) {
        if (stockpileList.size() != 0) {
            Log.d("removeData", stockpileList.remove(stockpileList.size() - 1).toString());
            updateListView();
        } else {
            Toast.makeText(StockpileEntryActivity.this, "削除するデータがありません", Toast.LENGTH_SHORT).show();
        }
    }

    // 背景をタップしたときにキーボードを閉じる
    public void onClickFocus(View v) {
        back.requestFocus();
        updateListView();
    }

    // リストを更新する
    public void updateListView() {
        stockpileListView.setAdapter(stockpileListAdapter);
    }
}
