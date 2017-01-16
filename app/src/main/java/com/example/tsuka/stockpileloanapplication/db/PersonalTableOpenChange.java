package com.example.tsuka.stockpileloanapplication.db;


import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersonalTableOpenChange extends AsyncTask<Void, Void, Boolean> {
    private UseProperties properties;

    public PersonalTableOpenChange(UseProperties properties){
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
            String sql = "update personal_tbl set open_data = ? where personal_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setBoolean(1, properties.isOpen()); // open_data

            preparedStatement.setInt(2, properties.getPersonalId()); // パーソナルID

            preparedStatement.executeUpdate(); // データベースに挿入

            preparedStatement.close();
            connection.close();

            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
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
