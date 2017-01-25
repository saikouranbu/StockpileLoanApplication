package com.example.tsuka.stockpileloanapplication.db;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.models.StockpileEntryModel;
import com.example.tsuka.stockpileloanapplication.utils.StockpileData;
import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StockpileTableGet extends AsyncTask<Void, Void, ArrayList> {
    private UseProperties properties;
    private StockpileEntryModel model;

    public StockpileTableGet(UseProperties properties, StockpileEntryModel model) {
        this.properties = properties;
        this.model = model;
    }

    @Override
    protected ArrayList<StockpileData> doInBackground(Void... params) {
        ArrayList<StockpileData> stockpileList = new ArrayList<StockpileData>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);
            String sql = "select * from stockpile_tbl where stockpile_point = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, properties.getStockpilePoint()); // 備蓄地点

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                stockpileList.add(new StockpileData(result.getInt("name"), String.valueOf(result.getInt("req_num")),
                        String.valueOf(result.getInt("num")), result.getInt("emergency_level")));
            }

            preparedStatement.close();
            connection.close();

            Log.d("get", "Completed");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("get", "Failed");
        } finally {
            try {
                if (connection != null) connection.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                Log.d("error", "データベースアクセスエラー");
            }
        }
        return stockpileList;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        model.updateListView(arrayList);
    }
}
