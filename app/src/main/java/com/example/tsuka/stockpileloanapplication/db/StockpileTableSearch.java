package com.example.tsuka.stockpileloanapplication.db;


import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.activities.StockpileMapsActivity;
import com.example.tsuka.stockpileloanapplication.models.StockpileEntryModel;
import com.example.tsuka.stockpileloanapplication.utils.StockpileData;
import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StockpileTableSearch extends AsyncTask<Void, Void, ArrayList> {
    private String stockpileName;

    private StockpileMapsActivity activity;

    public StockpileTableSearch(String stockpileName, StockpileMapsActivity activity) {
        this.stockpileName = stockpileName;
        this.activity = activity;
    }

    @Override
    protected ArrayList<StockpileData> doInBackground(Void... params) {
        ArrayList<StockpileData> stockpileList = new ArrayList<StockpileData>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);
            String sql = "select * from stockpile_tbl join personal_tbl on stockpile_tbl.personal_id = personal_tbl.personal_id where open_data = true and req_num < num and name like ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, stockpileName); // 検索ワード

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                stockpileList.add(new StockpileData(result.getString("name"), String.valueOf(result.getInt("req_num"))
                        , result.getString("num_unit"), String.valueOf(result.getInt("num"))));
            }

            preparedStatement.close();
            connection.close();

            Log.d("search", "Completed");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("search", "Failed");
        } finally {
            try {
                if (connection != null) connection.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                Log.d("error", "データベースに接続できていない");
            }
        }
        return stockpileList;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {

    }
}
