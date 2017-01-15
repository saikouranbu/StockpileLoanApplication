package com.example.tsuka.stockpileloanapplication.db;


import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersonalTableUpdate extends AsyncTask<Void, Void, Boolean> {
    private UseProperties properties;

    public PersonalTableUpdate(UseProperties properties) {
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
            String sql = "update personal_tbl set contact_one = ?, contact_two = ?, location = ?, longitude = ? where personal_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, properties.getContactInfo()); // 連絡先1
            preparedStatement.setString(2, properties.getContactInfo2()); // 連絡先2
            preparedStatement.setDouble(3, properties.getLatitude()); // 緯度
            preparedStatement.setDouble(4, properties.getLongitude()); // 経度

            preparedStatement.setInt(5, properties.getPersonalId()); // パーソナルID

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
