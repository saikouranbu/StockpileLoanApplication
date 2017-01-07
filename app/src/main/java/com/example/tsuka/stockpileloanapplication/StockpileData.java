package com.example.tsuka.stockpileloanapplication;

public class StockpileData extends Object{
    private String stockpileName;
    private String stockpileReqNum;
    private String stockpileNumUnit;
    private String stockpileNum;

    public String getStockpileName() {
        return stockpileName;
    }

    public void setStockpileName(String stockpileName) {
        this.stockpileName = stockpileName;
    }

    public String getStockpileReqNum() {
        return stockpileReqNum;
    }

    public void setStockpileReqNum(String stockpileReqNum) {
        this.stockpileReqNum = stockpileReqNum;
    }

    public String getStockpileNumUnit() {
        return stockpileNumUnit;
    }

    public void setStockpileNumUnit(String stockpileNumUnit) {
        this.stockpileNumUnit = stockpileNumUnit;
    }

    public String getStockpileNum() {
        return stockpileNum;
    }

    public void setStockpileNum(String stockpileNum) {
        this.stockpileNum = stockpileNum;
    }

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

    public String toString(){
        return stockpileName;
    }
}
