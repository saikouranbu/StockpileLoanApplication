package com.example.tsuka.stockpileloanapplication.utils;

public class StockpileData {
    private String stockpileName;
    private String stockpileReqNum;
    private String stockpileNumUnit;
    private String stockpileNum;

    private static final int OVER_REQ = 1; // 備蓄個数が必要数を上回っている
    private static final int SAME_REQ = 0; // 備蓄個数が必要数と同等
    private static final int UNDER_REQ = -1; // 備蓄個数が必要数を下回っている

    public StockpileData() {
        stockpileName = "";
        stockpileReqNum = "1";
        stockpileNumUnit = "";
        stockpileNum = "1";
    }

    public StockpileData(String stockpileName, String stockpileReqNum, String stockpileNumUnit, String stockpileNum) {
        this.stockpileName = stockpileName;
        this.stockpileReqNum = stockpileReqNum;
        this.stockpileNumUnit = stockpileNumUnit;
        this.stockpileNum = stockpileNum;
    }

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

    // 備蓄品データがすべて入力済みかどうかの確認
    // 入力済みならtrue
    public boolean isCompletedStockpileData() {
        if (stockpileName.length() != 0 &&
                stockpileReqNum.length() != 0 &&
                stockpileNumUnit.length() != 0 &&
                stockpileNum.length() != 0)
            return true;
        else return false;
    }

    // 備蓄品データの個数が必要数を上回っているかどうかの確認
    // 上回っているならtrue
    public int compareReqNum() {
        int reqNum = Integer.parseInt(getStockpileReqNum());
        int num = Integer.parseInt(getStockpileNum());
        if (num > reqNum) {
            return OVER_REQ;
        } else if (num < reqNum) {
            return UNDER_REQ;
        } else {
            return SAME_REQ;
        }
    }

    public String toString() {
        return stockpileName;
    }
}
