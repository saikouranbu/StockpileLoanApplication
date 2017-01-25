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
import com.example.tsuka.stockpileloanapplication.db.PersonalTableOpenChange;
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
    }

    // データベースに登録済みの備蓄品データをリストに追加
    private void stockpileGetList(){
        StockpileTableGet tableGet = new StockpileTableGet(useProperties, this);

        try {
            tableGet.execute();
            Toast.makeText(activity, "登録済みの備蓄品データを取得しています", Toast.LENGTH_SHORT).show();
            // データベースに接続するまでのLoadingDialogを表示
            dialog = new ProgressDialog(activity);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle("Please wait");
            dialog.setMessage("Loading...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
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
            // 備蓄品データに未入力がある場合
            Toast.makeText(activity, "備蓄品データがすべて入力されていません", Toast.LENGTH_SHORT).show();
            return;
        }else if(!isListOverReqNum()){
            // 備蓄品データが一つも必要数を個数が上回っていない場合
            Toast.makeText(activity, "最低一つの備蓄品は貸借できるように必要数より多く備蓄してください", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Toast.makeText(activity, "備蓄品データの送信を開始しました", Toast.LENGTH_SHORT).show();
        }

        // 備蓄品データをデータベースに登録済みの場合既存のデータを削除する
        // データベースにデータが登録済みの場合
        if (useProperties.isStockpileRegistered()) {
            StockpileTableDelete delete = new StockpileTableDelete(useProperties);
            try {
                delete.execute();
            } catch (Exception e) {
                Log.d("error", e.toString());
                Toast.makeText(activity, "通信が正常に完了しませんでした", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 備蓄品データをデータベースに挿入
        StockpileTableInsert insert = new StockpileTableInsert(stockpileList, useProperties);
        try {
            insert.execute();
            Toast.makeText(activity, "備蓄品データを登録しました", Toast.LENGTH_SHORT).show();

            // MainActivityに戻る
            activity.finish();
        } catch (Exception e) {
            Log.d("error", e.toString());
            Toast.makeText(activity, "通信が正常に完了しませんでした", Toast.LENGTH_SHORT).show();
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
                            StockpileTableDelete delete = new StockpileTableDelete(useProperties);
                            try {
                                delete.execute();
                                Toast.makeText(activity, "データベースに登録済みの備蓄品データを削除しました", Toast.LENGTH_SHORT).show();

                                // MainActivityに戻る
                                activity.finish();
                            } catch (Exception e) {
                                Log.d("error", "削除失敗");
                            }
                            // プロパティファイルに保存
                            useProperties.setOpenData(false);

                            // データベースに送信
                            PersonalTableOpenChange change = new PersonalTableOpenChange(useProperties);
                            try {
                                change.execute();
                                Toast.makeText(activity, "処理が完了しました", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d("error", e.toString());
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
    private boolean isCompletedStockpileList() {
        if (stockpileList.size() == 0) {
            Toast.makeText(activity, "送信するデータがありません", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (StockpileData stock : stockpileList) {
            if (!stock.isCompletedStockpileData()) return false;
        }
        return true;
    }

    // 備蓄品リストのデータの内最低一つは個数が必要数を上回っているか確認
    // (貸借できる備蓄品があるか)
    // 上回っているならtrue
    private boolean isListOverReqNum() {
        for (StockpileData stock : stockpileList) {
            if (stock.isOverReqNum()) return true;
        }
        return false;
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

    // リストを更新する
    public void updateListView(ArrayList<StockpileData> stockpileList) {
        this.stockpileList = stockpileList;
        stockpileListAdapter = new StockpileListAdapter(activity, stockpileList);
        updateListView();
        if (stockpileList.size() != 0) {
            Toast.makeText(activity, "登録済み備蓄品データを取得しました", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "通信が正常に完了しませんでした", Toast.LENGTH_SHORT).show();
        }
        // LoadingDialogを閉じる
        dialog.dismiss();
    }
}
