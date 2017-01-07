package com.example.tsuka.stockpileloanapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class StockpileEntryActivity extends AppCompatActivity {
    private ListView stockpileListView;
    private Button stockpileAddButton;
    private RelativeLayout back;
    private ArrayList stockpileList;
    private StockpileListAdapter stockpileListAdapter;

    private ArrayAdapter testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockpile_entry);

        stockpileListView = (ListView) findViewById(R.id.stockpileDataList);
        stockpileAddButton = (Button) findViewById(R.id.stockpileAddButton);
        back = (RelativeLayout) findViewById(R.id.activity_stockpile_entry);

        stockpileList = new ArrayList<StockpileData>();
        stockpileListAdapter = new StockpileListAdapter(this, stockpileList);
        //stockpileList=new ArrayList<String>();
        //testAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, stockpileList);
        //stockpileListView.setAdapter(testAdapter);
        stockpileListView.setAdapter(stockpileListAdapter);
    }

    public void onStockpileAddClick(View v) {
        Log.d("addBoolean", String.valueOf(stockpileList.add(new StockpileData())));
        //Log.d("listSize",String.valueOf(stockpileListAdapter.getCount()));
        //stockpileList.add(String.valueOf(stockpileList.size()+1));
        //stockpileListView.setAdapter(testAdapter);
        stockpileListView.setAdapter(stockpileListAdapter);
    }
}
