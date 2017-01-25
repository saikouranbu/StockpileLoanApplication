package com.example.tsuka.stockpileloanapplication.utils;

public class StockpileData {
    private int stockpileName;
    private String stockpileReqNum;
    private String stockpileNum;
    private int emergencyLevelPosition;

    private final String[] STOCKPILES = {
            "飲料水500ml", "飲料水2L", "保存食", "毛布",
            "ダンボール", "コンロ", "皿", "コップ", "箸",
            "スプーン", "ガムテープ", "はさみ", "紐", "ロープ"
    };
    private final String[] EMERGENCY_LEVEL = {
            "配送中", "-", "低", "中", "高"
    };

    private static final int OVER_REQ = 1; // 備蓄個数が必要数を上回っている
    private static final int SAME_REQ = 0; // 備蓄個数が必要数と同等
    private static final int UNDER_REQ = -1; // 備蓄個数が必要数を下回っている

    public StockpileData() {
        stockpileName = 0;
        stockpileReqNum = "1";
        stockpileNum = "1";
        emergencyLevelPosition = 4;
    }

    public StockpileData(int stockpileName, String stockpileReqNum, String stockpileNum, int emergencyLevelPosition) {
        this.stockpileName = stockpileName;
        this.stockpileReqNum = stockpileReqNum;
        this.stockpileNum = stockpileNum;
        this.emergencyLevelPosition = emergencyLevelPosition;
    }

    public int getStockpileNamePosition() {
        return stockpileName;
    }

    public String getStockpileName(){
        return STOCKPILES[stockpileName];
    }

    public void setStockpileName(int stockpileName) {
        this.stockpileName = stockpileName;
    }

    public String getStockpileReqNum() {
        return stockpileReqNum;
    }

    public void setStockpileReqNum(String stockpileReqNum) {
        this.stockpileReqNum = stockpileReqNum;
    }

    public String getStockpileNum() {
        return stockpileNum;
    }

    public void setStockpileNum(String stockpileNum) {
        this.stockpileNum = stockpileNum;
    }

    public int getEmergencyLevelPosition(){
        return emergencyLevelPosition;
    }

    public String getEmergencyLevel(){
        return EMERGENCY_LEVEL[emergencyLevelPosition];
    }

    public void setEmergencyLevelPosition(int emergencyLevelPosition){
        this.emergencyLevelPosition = emergencyLevelPosition;
    }

    // 備蓄品データがすべて入力済みかどうかの確認
    // 入力済みならtrue
    public boolean isCompletedStockpileData() {
        if (stockpileReqNum.length() != 0 &&
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

    // 備蓄数－必要数
    public int getSubNum(){
        return Integer.parseInt(getStockpileNum())-Integer.parseInt(getStockpileReqNum());
    }

    public String toString() {
        return getStockpileName();
    }
}
