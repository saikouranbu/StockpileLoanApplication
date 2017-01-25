package com.example.tsuka.stockpileloanapplication.db;


import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.activities.StockpileMapsActivity;
import com.example.tsuka.stockpileloanapplication.utils.PersonalData;
import com.example.tsuka.stockpileloanapplication.utils.StockpileData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StockpileTableSearch extends AsyncTask<Void, Void, ArrayList> {
    private int stockpileNamePosition;

    private StockpileMapsActivity activity;

    public StockpileTableSearch(int stockpileNamePosition, StockpileMapsActivity activity) {
        this.stockpileNamePosition = this.stockpileNamePosition;
        this.activity = activity;
    }

    @Override
    protected ArrayList<PersonalData> doInBackground(Void... params) {
        ArrayList<PersonalData> personalList = new ArrayList<PersonalData>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);
            String sql = "select * from stockpile_tbl join personal_tbl on stockpile_tbl.stockpile_point = personal_tbl.stockpile_point where open_data = true and name like ? order by stockpile_tbl.stockpile_point, stockpile_id";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, stockpileNamePosition); // 検索備蓄品の配列番号

            ResultSet result = preparedStatement.executeQuery();

            PersonalData personalTemp = null;
            StockpileData stockpileTemp = null;
            Log.d("result", "start");
            while (result.next()) {
                Log.d("result", "next");
                    // 取得データの1行目
                personalTemp = new PersonalData(result.getString("stockpile_tbl.stockpile_point"),
                            result.getString("contact_one"), result.getString("contact_two"),
                            result.getDouble("latitude"), result.getDouble("longitude"));
                stockpileTemp = new StockpileData(result.getInt("name"), String.valueOf(result.getInt("req_num")),
                        String.valueOf(result.getInt("num")), result.getInt("emergency_level"));
                    Log.d("addStockpile", stockpileTemp.toString());
                personalTemp.setStockpile(stockpileTemp);
                    Log.d("addPersonal", personalTemp.toString());
                    personalList.add(personalTemp);
            }

            preparedStatement.close();
            connection.close();

            Log.d("search", "Completed");
            Log.d("result", personalList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("search", "Failed");
        } finally {
            try {
                if (connection != null) connection.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                Log.d("error", "データベースアクセスエラー");
            }
        }
        return personalList;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        activity.getStockpileSearchResult(arrayList);
    }
}
