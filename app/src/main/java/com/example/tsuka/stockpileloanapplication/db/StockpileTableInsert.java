package com.example.tsuka.stockpileloanapplication.db;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.utils.StockpileData;
import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


public class StockpileTableInsert extends AsyncTask<Void, Void, Void> {
    private ArrayList<StockpileData> stockpileData;
    private UseProperties properties;

    public StockpileTableInsert(ArrayList<StockpileData> stockpileData, UseProperties properties) {
        this.stockpileData = stockpileData;
        this.properties = properties;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Boolean isSuccess = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);
            String sql = "insert into stockpile_tbl (stockpile_point, name, req_num, num, emergency_level) values (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, properties.getStockpilePoint()); // 備蓄地点
            for (StockpileData data : stockpileData) {
                preparedStatement.setInt(2, data.getStockpileNamePosition()); // 備蓄品名
                preparedStatement.setInt(3, Integer.valueOf(data.getStockpileReqNum())); // 備蓄必要数
                preparedStatement.setInt(4, Integer.valueOf(data.getStockpileNum())); // 備蓄数
                preparedStatement.setInt(5, data.getEmergencyLevelPosition());

                preparedStatement.executeUpdate(); // データベースに挿入
            }

            preparedStatement.close();
            connection.close();

            properties.setStockpileRegistered(true); // プロパティファイルに備蓄品データを登録済みであることを保存

            Log.d("insert", "Completed");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("insert", "Failed");
        } finally {
            try {
                if (connection != null) connection.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                Log.d("error", "データベースアクセスエラー");
            }
        }
        return null;
    }
}
