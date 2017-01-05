package com.example.tsuka.stockpileloanapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class StockpileEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockpile_entry);

        List<StockpileData> stockpileList = new ArrayList<StockpileData>();

        for (int i = 0; i < 1; i++) {
            // stockpileList.add(new StockpileData());
            stockpileList.add(new StockpileData("tes", "1", "個", "2"));
        }

        ListView stockpileListView = (ListView) findViewById(R.id.stockpileDataList);

        stockpileListView.setAdapter(new StockpileListAdapter(this, stockpileList));
    }
}
