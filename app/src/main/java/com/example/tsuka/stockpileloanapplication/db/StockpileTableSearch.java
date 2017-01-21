package com.example.tsuka.stockpileloanapplication.db;


import android.os.AsyncTask;
import android.util.Log;

import com.example.tsuka.stockpileloanapplication.activities.StockpileMapsActivity;
import com.example.tsuka.stockpileloanapplication.models.StockpileEntryModel;
import com.example.tsuka.stockpileloanapplication.utils.PersonalData;
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
    protected ArrayList<PersonalData> doInBackground(Void... params) {
        ArrayList<PersonalData> personalList = new ArrayList<PersonalData>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DbConnector.getUrl(), DbConnector.USER, DbConnector.PASS);
            String sql = "select * from stockpile_tbl join personal_tbl on stockpile_tbl.personal_id = personal_tbl.personal_id where open_data = true and req_num < num and name like ? order by stockpile_tbl.personal_id, stockpile_id";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "%" + stockpileName + "%"); // 検索ワード

            ResultSet result = preparedStatement.executeQuery();

            PersonalData personalTemp = null;
            StockpileData stockpileTemp = null;
            Log.d("result", "start");
            while (result.next()) {
                Log.d("result", "next");
                if(personalList.size() == 0){
                    // 取得データの1行目
                    personalTemp = new PersonalData(result.getInt("stockpile_tbl.personal_id"),
                            result.getString("contact_one"), result.getString("contact_two"),
                            result.getDouble("latitude"), result.getDouble("longitude"));
                    stockpileTemp = new StockpileData(result.getString("name"), String.valueOf(result.getInt("req_num")),
                            result.getString("num_unit"), String.valueOf(result.getInt("num")));

                    Log.d("addStockpile", stockpileTemp.toString());
                    personalTemp.addStockpile(stockpileTemp);
                    Log.d("addPersonal", personalTemp.toString());
                    personalList.add(personalTemp);
                } else{
                    // 2行目以降
                    if(result.getInt("personal_id") == personalList.get(personalList.size() - 1).getPersonalId()){
                        // 取得した行のパーソナルデータが既にリストにある場合
                        stockpileTemp = new StockpileData(result.getString("name"), String.valueOf(result.getInt("req_num")),
                                result.getString("num_unit"), String.valueOf(result.getInt("num")));

                        Log.d("addPersonal", personalTemp.toString());
                        personalList.get(personalList.size() - 1).addStockpile(stockpileTemp);
                    }else{
                        // 取得した行のパーソナルデータが既にリストにない場合
                        personalTemp = new PersonalData(result.getInt("stockpile_tbl.personal_id"),
                                result.getString("contact_one"), result.getString("contact_two"),
                                result.getDouble("latitude"), result.getDouble("longitude"));
                        stockpileTemp = new StockpileData(result.getString("name"), String.valueOf(result.getInt("req_num")),
                                result.getString("num_unit"), String.valueOf(result.getInt("num")));

                        Log.d("addStockpile", stockpileTemp.toString());
                        personalTemp.addStockpile(stockpileTemp);
                        Log.d("addPersonal", personalTemp.toString());
                        personalList.add(personalTemp);
                    }
                }
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
                Log.d("error", "データベースに接続できていない");
            }
        }
        return personalList;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        activity.getStockpileSearchResult(arrayList);
    }
}
