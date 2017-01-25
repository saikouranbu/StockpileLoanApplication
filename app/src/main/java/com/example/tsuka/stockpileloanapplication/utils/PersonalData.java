package com.example.tsuka.stockpileloanapplication.utils;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PersonalData {
    private StockpileData stockpile;

    private String stockpilePoint, contactInfo, contactInfo2;
    private double latitude, longitude;

    public PersonalData(String stockpilePoint, String contactInfo, String contactInfo2, double latitude, double longitude){
        this.stockpilePoint = stockpilePoint;
        this.contactInfo = contactInfo;
        this.contactInfo2 = contactInfo2;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setStockpile(StockpileData stockpile){
        this.stockpile = stockpile;
    }

    public StockpileData getStockpile(){
        return stockpile;
    }

    public String getStockpilePoint(){
        return stockpilePoint;
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
        return stockpile.getEmergencyLevel();
    }
}
