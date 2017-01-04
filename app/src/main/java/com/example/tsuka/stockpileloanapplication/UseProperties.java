package com.example.tsuka.stockpileloanapplication;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public UseProperties(Context context) {
        properties = new Properties();
        loadProperties(context);
    }

    // 既にパーソナルデータが登録されているかの判別
    public boolean isEmpty() {
        Log.d("propertySize", String.valueOf(properties.stringPropertyNames().size()));
        if (properties.stringPropertyNames().size() != 0) {
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
    public void entryProperties(Context context, String numPeople, String contact, String contact2, String lat, String lng) {
        String strNumPeople = String.valueOf(numPeople);
        String strLat = String.valueOf(lat);
        String strLng = String.valueOf(lng);

        properties.setProperty(NUM_PEOPLE, strNumPeople);
        properties.setProperty(CONTACT_1, contact);
        properties.setProperty(CONTACT_2, contact2);
        properties.setProperty(LATITUDE, strLat);
        properties.setProperty(LONGITUDE, strLng);

        try {
            FileOutputStream outputStream = context.openFileOutput(PERSONAL_PROP, Context.MODE_APPEND);
            properties.store(outputStream, "UserProperty");
        }catch (FileNotFoundException e){
            Log.d("error", "ファイルにアクセスできていない");
        }catch (IOException e){
            Log.d("error", "ファイルに書き込めていない");
        }
    }

    // プロパティファイルからパーソナルデータを取得
    public int getNumPeople() {
        return Integer.parseInt(properties.getProperty(NUM_PEOPLE));
    }

    public String getContactInfo() {
        return properties.getProperty(CONTACT_1);
    }

    public String getContactInfo2() {
        return properties.getProperty(CONTACT_2);
    }

    public Double getLatitude() {
        return Double.parseDouble(properties.getProperty(LATITUDE));
    }

    public Double getLongitude() {
        return Double.parseDouble(properties.getProperty(LONGITUDE));
    }

    public void logProperties(){
        Log.d("num", String.valueOf(getNumPeople()));
        Log.d("contact", getContactInfo());
        Log.d("contact2", getContactInfo2());
        Log.d("latitude", String.valueOf(getLatitude()));
        Log.d("longitude", String.valueOf(getLongitude()));
    }
}
