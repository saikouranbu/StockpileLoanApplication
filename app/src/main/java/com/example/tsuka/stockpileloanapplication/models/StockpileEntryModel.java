package com.example.tsuka.stockpileloanapplication.models;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.activities.StockpileEntryActivity;
import com.example.tsuka.stockpileloanapplication.db.StockpileTableDelete;
import com.example.tsuka.stockpileloanapplication.db.StockpileTableGet;
import com.example.tsuka.stockpileloanapplication.db.StockpileTableInsert;
import com.example.tsuka.stockpileloanapplication.utils.StockpileData;
import com.example.tsuka.stockpileloanapplication.utils.StockpileListAdapter;
import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.util.ArrayList;

public class StockpileEntryModel {
    private StockpileEntryActivity activity;
    private ListView stockpileListView;
    private RelativeLayout back;
    private ArrayList<StockpileData> stockpileList;
    private StockpileListAdapter stockpileListAdapter;
    private UseProperties useProperties;

    private ProgressDialog dialog;

    public StockpileEntryModel(StockpileEntryActivity activity) {
        this.activity = activity;


        // データベースに接続するまでのLoadingDialogを表示
        dialog = new ProgressDialog(activity);
        dialog.setTitle("Please wait");
        dialog.setMessage("Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    public void onStart(){
        stockpileListView = (ListView) activity.findViewById(R.id.stockpileDataList);
        back = (RelativeLayout) activity.findViewById(R.id.activity_stockpile_entry);

        stockpileList = new ArrayList<StockpileData>();

        useProperties = new UseProperties(activity.getApplicationContext());

        // データベースにデータが登録済みの場合データを取得
        if(useProperties.isStockpileRegistered()) stockpileGetList();

        stockpileListAdapter = new StockpileListAdapter(activity, stockpileList);
        updateListView();

        // LoadingDialogを閉じる
        dialog.dismiss();
    }

    // データベースに登録済みの備蓄品データをリストに追加
    private void stockpileGetList(){
        StockpileTableGet tableGet = new StockpileTableGet(useProperties);

        try {
            stockpileList = tableGet.execute().get();
            if (stockpileList.size() != 0) {
                Toast.makeText(activity, "登録済み備蓄品データを取得しました", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "通信が正常に完了しませんでした", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
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
        }else{
            Toast.makeText(activity, "備蓄品データの送信を開始しました", Toast.LENGTH_SHORT).show();
        }

        // 備蓄品データをデータベースに登録済みの場合既存のデータを削除する
        boolean isSuccess = false;
        // データベースにデータが登録済みの場合
        if (useProperties.isStockpileRegistered()) {
            StockpileTableDelete delete = new StockpileTableDelete(useProperties);
            try {
                isSuccess = delete.execute().get();
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
        }
        Log.d("dataDelete", String.valueOf(isSuccess));
        // 備蓄品データをデータベースに挿入
        StockpileTableInsert insert = new StockpileTableInsert(stockpileList, useProperties);
        try {
            isSuccess = insert.execute().get();
            if (isSuccess == true) {
                Toast.makeText(activity, "備蓄品データを登録しました", Toast.LENGTH_SHORT).show();
                activity.finish();
            } else {
                Toast.makeText(activity, "通信が正常に完了しませんでした", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
    }

    // 備蓄品データをデータベースに登録済みの場合既存のデータを削除する
    public void stockpileDelete() {
        // データベースにデータが登録済みの場合
        if (useProperties.isStockpileRegistered()) {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(activity);
            alertDlg.setTitle("削除の確認");
            alertDlg.setMessage("データベースに登録済みの備蓄品データを削除します\nよろしいですか？");
            alertDlg.setPositiveButton(
                    "はい",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // はい ボタンクリック処理
                            boolean isSuccess = false;
                            StockpileTableDelete delete = new StockpileTableDelete(useProperties);
                            try {
                                isSuccess = delete.execute().get();
                            } catch (Exception e) {
                                Log.d("error", "削除失敗");
                            }

                            if (isSuccess) {
                                Toast.makeText(activity, "データベースに登録済みの備蓄品データを削除しました", Toast.LENGTH_SHORT).show();
                                // MainActivityに戻る
                                activity.finish();
                            } else {
                                Toast.makeText(activity, "通信が正常に完了しませんでした", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            alertDlg.setNegativeButton(
                    "いいえ",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // いいえ ボタンクリック処理
                        }
                    });

            // 表示
            alertDlg.create().show();
        } else {
            Toast.makeText(activity, "データベースに備蓄品データが未登録です", Toast.LENGTH_SHORT).show();
        }
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
