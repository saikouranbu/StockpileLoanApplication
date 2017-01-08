package com.example.tsuka.stockpileloanapplication.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.models.PersonalEntryModel;
import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

public class PersonalEntryActivity extends AppCompatActivity {
    private PersonalEntryModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_entry);

        model = new PersonalEntryModel(this);
    }

    // 位置情報を取得ボタンを押下時の挙動
    public void onGetLocationClick(View v) {
        model.getLocationClick();
    }

    // 位置情報を確認ボタンを押下時の挙動
    public void onConfLocationClick(View v) {
        model.confLocation();
    }

    // 登録ボタンを押下時の挙動
    public void onEntryClick(View v) {
        model.locationEntry();
    }

    // 画面タップ時にフォーカスを背景に移すためのメソッド
    public void onBackClick(View v) {
        model.clickFocus();
    }
}
