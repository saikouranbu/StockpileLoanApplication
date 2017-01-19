package com.example.tsuka.stockpileloanapplication.db;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            String sql = "insert into personal_tbl (contact_one, contact_two, location, longitude) values (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, properties.getContactInfo()); // 連絡先1
            preparedStatement.setString(2, properties.getContactInfo2()); // 連絡先2
            preparedStatement.setDouble(3, properties.getLatitude()); // 緯度
            preparedStatement.setDouble(4, properties.getLongitude()); // 経度

            preparedStatement.executeUpdate(); // データベースに挿入

            // 挿入したパーソナルデータのIDを取得
            sql = "select personal_id from personal_tbl where location = ? and longitude = ?";

            // ID取得用に再接続
            connection.close();
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setDouble(1, properties.getLatitude()); // 緯度
            preparedStatement.setDouble(2, properties.getLongitude()); // 経度

            ResultSet result = preparedStatement.executeQuery();

            int personalId = -100; // 仮代入
            while(result.next()) {
                personalId = result.getInt(1); // カラムの1列目(personal_id)を取得
            }
            // 取得したパーソナルIDをプロパティに保存
            properties.setPersonalId(personalId);

            preparedStatement.close();
            connection.close();
            Log.d("insert", "Completed");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("insert", "Failed");
        } finally {
            try {
                if (connection != null) connection.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                Log.d("error", "データベースに接続できていない");
            }
        }
        return null;
    }
}
