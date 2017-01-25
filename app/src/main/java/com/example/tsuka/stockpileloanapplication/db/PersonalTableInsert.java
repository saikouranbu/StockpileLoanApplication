package com.example.tsuka.stockpileloanapplication.db;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersonalTableInsert  extends AsyncTask<Void, Void, Void> {
    private UseProperties properties;

    public PersonalTableInsert(UseProperties properties) {
        this.properties = properties;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);
            String sql = "insert into personal_tbl (stockpile_point, contact_one, contact_two, latitude, longitude) values (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, properties.getStockpilePoint()); // 備蓄地点
            preparedStatement.setString(2, properties.getContactInfo()); // 連絡先1
            preparedStatement.setString(3, properties.getContactInfo2()); // 連絡先2
            preparedStatement.setDouble(4, properties.getLatitude()); // 緯度
            preparedStatement.setDouble(5, properties.getLongitude()); // 経度

            preparedStatement.executeUpdate(); // データベースに挿入

            preparedStatement.close();
            connection.close();
            Log.d("insert", "Completed");
        } catch (SQLException e) {
            // 既に同名の備蓄地点が登録されている
            properties.setStockpileRegistered(true);
            Log.d("insert", "");
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
