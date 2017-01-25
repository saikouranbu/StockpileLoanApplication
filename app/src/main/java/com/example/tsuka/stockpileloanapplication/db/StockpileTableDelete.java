package com.example.tsuka.stockpileloanapplication.db;


import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StockpileTableDelete extends AsyncTask<Void, Void, Void> {
    private UseProperties properties;

    public StockpileTableDelete(UseProperties properties) {
        this.properties = properties;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);
            String sql = "delete from stockpile_tbl where stockpile_point = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, properties.getStockpilePoint()); // 備蓄地点

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            properties.setStockpileRegistered(false); // プロパティファイルに備蓄品データを未登録であることを保存

            Log.d("delete", "Completed");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("delete", "Failed");
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
