package com.example.tsuka.stockpileloanapplication.db;


import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StockpileTableDelete extends AsyncTask<Void, Void, Boolean> {
    private UseProperties properties;

    public StockpileTableDelete(UseProperties properties) {
        this.properties = properties;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean isSuccess = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);
            String sql = "delete from stockpile_tbl where personal_id = ?;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, properties.getPersonalId()); // パーソナルID

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            isSuccess = true;
            properties.setRegistered(false); // プロパティファイルに備蓄品データを未登録であることを保存
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                Log.d("error", "データベースに接続できていない");
            }
        }
        return isSuccess;
    }
}
