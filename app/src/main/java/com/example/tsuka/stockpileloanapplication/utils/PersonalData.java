package com.example.tsuka.stockpileloanapplication.utils;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PersonalData {
    private ArrayList<StockpileData> stockpileList;

    private int personalId;
    private String contactInfo, contactInfo2;
    private double latitude, longitude;

    public PersonalData(int personalId, String contactInfo, String contactInfo2, double latitude, double longitude){
        stockpileList = new ArrayList<StockpileData>();

        this.personalId = personalId;
        this.contactInfo = contactInfo;
        this.contactInfo2 = contactInfo2;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void addStockpile(StockpileData stock){
        stockpileList.add(stock);
    }

    public ArrayList<StockpileData> getStockpileList(){
        return stockpileList;
    }

    public int getPersonalId(){
        return personalId;
    }

    public String getContactInfo(){
        return contactInfo;
    }

    public String getContactInfo2(){
        return contactInfo2;
    }

    public LatLng getLatLng(){
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return stockpileList.toString();
    }
}
