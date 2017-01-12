package com.example.tsuka.stockpileloanapplication.db;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.utils.StockpileData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


public class StockpileTableInsert extends AsyncTask<Void, Void, Boolean> {
    private Activity activity = null;
    private ArrayList<StockpileData> stockpileData;
    private int personalId;

    public StockpileTableInsert(Activity activity, ArrayList<StockpileData> stockpileData, int personalId) {
        this.activity = activity;
        this.stockpileData = stockpileData;
        this.personalId = personalId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean bool = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);
            String sql = "insert into stockpile_tbl (personal_id, name, req_num, num_unit, num) values (?, ?, ?, ?, ?);";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, personalId); // パーソナルテーブルのID
            for (StockpileData data : stockpileData) {
                preparedStatement.setString(2, data.getStockpileName()); // 備蓄品名
                preparedStatement.setInt(3, Integer.valueOf(data.getStockpileReqNum())); // 備蓄必要数
                preparedStatement.setString(4, data.getStockpileNumUnit()); // 備蓄品の単位
                preparedStatement.setInt(5, Integer.valueOf(data.getStockpileNum())); // 備蓄数

                preparedStatement.executeUpdate(); // データベースに挿入
            }

            preparedStatement.close();
            connection.close();

            bool = true;
        } catch (Exception e) {
            bool = false;
        } finally {
            try {
                if (connection != null) connection.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                Log.d("error", "データベースに接続できていない");
            }
        }
        return bool;
    }
}
