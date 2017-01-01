package com.example.tsuka.stockpileloanapplication;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UseProperties {
    private static final String PERSONAL_PROP = "personal.properties";

    private static final String NUM_PEOPLE = "numPeople";
    private static final String CONTACT_1 = "contactInfo";
    private static final String CONTACT_2 = "contactInfo2";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private Properties properties;

    public UseProperties() {
        properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(PERSONAL_PROP);
            properties.load(inputStream);
        } catch (IOException e) {
            Log.d("error", "プロパティファイルが読み込めてない");
        }
    }

    // 既にパーソナルデータが登録されているかの判別
    public boolean isEmpty() {
        if (properties.stringPropertyNames().size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    // パーソナルデータをプロパティファイルに登録
    public void setProperties(int numPeople, String contact, String contact2, Double lat, Double lng) {
        String strNumPeople = String.valueOf(numPeople);
        String strLat = String.valueOf(lat);
        String strLng = String.valueOf(lng);

        properties.setProperty(NUM_PEOPLE, strNumPeople);
        properties.setProperty(CONTACT_1, contact);
        properties.setProperty(CONTACT_2, contact2);
        properties.setProperty(LATITUDE, strLat);
        properties.setProperty(LONGITUDE, strLng);
    }

    // プロパティファイルからパーソナルデータを取得
    public int getNumPeople() {
        return (int) properties.get(NUM_PEOPLE);
    }

    public String getContactInfo() {
        return properties.getProperty(CONTACT_1);
    }

    public String getContactInfo2() {
        return properties.getProperty(CONTACT_2);
    }

    public Double getLatitude() {
        return (Double) properties.get(LATITUDE);
    }

    public Double getLongitude() {
        return (Double) properties.get(LONGITUDE);
    }
}
