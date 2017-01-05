package com.example.tsuka.stockpileloanapplication;

public class StockpileData {
    String stockpileName;
    String stockpileReqNum;
    String stockpileNumUnit;
    String stockpileNum;

    public StockpileData(){
        stockpileName = "";
        stockpileReqNum = "";
        stockpileNumUnit = "";
        stockpileNum = "";
    }

    public StockpileData(String stockpileName, String stockpileReqNum, String stockpileNumUnit, String stockpileNum){
        this.stockpileName = stockpileName;
        this.stockpileReqNum = stockpileReqNum;
        this.stockpileNumUnit = stockpileNumUnit;
        this.stockpileNum = stockpileNum;
    }
}
