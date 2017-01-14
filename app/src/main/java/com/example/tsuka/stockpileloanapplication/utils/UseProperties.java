package com.example.tsuka.stockpileloanapplication.utils;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UseProperties {
    private static final String PERSONAL_PROP = "personal.properties";

    private static final int NULL_INT = -10000;
    private static final double NULL_DOUBLE = -10000;
    private static final String PERSONAL_ID = "personalId";
    private static final String CONTACT_1 = "contactInfo";
    private static final String CONTACT_2 = "contactInfo2";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String REGISTERED = "registered";
    private static final String OPEN = "openData";

    private Properties properties;
    private Context context;

    public UseProperties(Context context) {
        properties = new Properties();
        this.context = context;
        loadProperties(context);
    }

    // 既にパーソナルデータが登録されているかの判別
    public boolean isRegisteredProperties() {
        Log.d("propertySize", String.valueOf(properties.stringPropertyNames().size()));
        if (getPersonalId() != NULL_INT) {
            return true;
        } else {
            return false;
        }
    }

    public void loadProperties(Context context){
        InputStream inputStream;
        try {
            inputStream = context.openFileInput(PERSONAL_PROP);
            properties.load(inputStream);
        } catch (Exception e) {
            Log.d("error", "プロパティファイルが読み込めてない");
        }
    }

    // パーソナルデータをプロパティファイルに登録
    public void entryProperties(String contact, String contact2, String lat, String lng) {
        String strLat = String.valueOf(lat);
        String strLng = String.valueOf(lng);

        properties.setProperty(CONTACT_1, contact);
        properties.setProperty(CONTACT_2, contact2);
        properties.setProperty(LATITUDE, strLat);
        properties.setProperty(LONGITUDE, strLng);

        propertyOutput(properties);
    }

    private void propertyOutput(Properties properties) {
        try {
            FileOutputStream outputStream = context.openFileOutput(PERSONAL_PROP, Context.MODE_APPEND);
            properties.store(outputStream, "UserProperty");
        } catch (FileNotFoundException e) {
            Log.d("error", "ファイルにアクセスできていない");
        } catch (IOException e) {
            Log.d("error", "ファイルに書き込めていない");
        }
    }

    // プロパティファイルからパーソナルデータを取得
    public int getPersonalId() {
        String temp = properties.getProperty(PERSONAL_ID);

        // プロパティファイルに未登録の場合の処理用
        if(temp == null) return NULL_INT;

        return Integer.parseInt(temp);
    }

    public String getContactInfo() {
        return properties.getProperty(CONTACT_1);
    }

    public String getContactInfo2() {
        return properties.getProperty(CONTACT_2);
    }

    public Double getLatitude() {
        String temp = properties.getProperty(LATITUDE);

        // プロパティファイルに未登録の場合の処理用
        if(temp == null) return NULL_DOUBLE;

        return Double.parseDouble(temp);
    }

    public Double getLongitude() {
        String temp = properties.getProperty(LONGITUDE);

        // プロパティファイルに未登録の場合の処理用
        if(temp == null) return NULL_DOUBLE;

        return Double.parseDouble(temp);
    }

    public void setPersonalId(int personalId) {
        String strId = String.valueOf(personalId);
        properties.setProperty(PERSONAL_ID, strId);

        propertyOutput(properties);
    }

    public boolean isStockpileRegistered() {
        return Boolean.valueOf(properties.getProperty(REGISTERED));
    }

    public boolean isOpen() {
        return Boolean.valueOf(properties.getProperty(OPEN));
    }

    public void setRegistered(boolean isRegistered) {
        String strRegistered = String.valueOf(isRegistered);
        properties.setProperty(REGISTERED, strRegistered);

        propertyOutput(properties);
    }

    public void setOpenData(boolean isOpen) {
        String strOpen = String.valueOf(isOpen);
        properties.setProperty(OPEN, strOpen);

        propertyOutput(properties);
    }

    // デバッグ用
    public void logProperties(){
        Log.d("id", String.valueOf(getPersonalId()));
        Log.d("contact", getContactInfo());
        Log.d("contact2", getContactInfo2());
        Log.d("latitude", String.valueOf(getLatitude()));
        Log.d("longitude", String.valueOf(getLongitude()));
        Log.d("isRegistered", String.valueOf(isStockpileRegistered()));
        Log.d("isOpen", String.valueOf(isOpen()));
    }
}
