package com.example.tsuka.stockpileloanapplication;

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

public class PersonalEntryActivity extends AppCompatActivity {
    private LocationManager locationManager;

    private RelativeLayout back;

    private EditText numPeopleEdit, contactInfoEdit, contactInfoEdit2;
    private View.OnFocusChangeListener focusChangeListener;

    private Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_entry);

        back = (RelativeLayout) findViewById(R.id.activity_personal_entry);

        focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    // フォーカスが外れた場合キーボードを非表示にする
                    InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        };

        numPeopleEdit = (EditText) findViewById(R.id.numPeopleEdit);
        numPeopleEdit.setOnFocusChangeListener(focusChangeListener);
        contactInfoEdit = (EditText) findViewById(R.id.contactInfoEdit);
        contactInfoEdit.setOnFocusChangeListener(focusChangeListener);
        contactInfoEdit2 = (EditText) findViewById(R.id.contactInfoEdit2);
        contactInfoEdit2.setOnFocusChangeListener(focusChangeListener);

        // 既に情報が登録されていた場合情報をEditTextに表示
        // TODO: 登録済みの情報を表示
    }

    // 位置情報を取得ボタンを押下時の挙動
    public void onGetLocationClick(View v) {
        // 位置情報を登録する場所の確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle("位置情報を取得");
        alertDlg.setMessage("現在位置を取得します\n備蓄している位置での取得をお願いします\n現在位置を取得してよろしいですか？");
        alertDlg.setPositiveButton(
                "はい",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // はい ボタンクリック処理
                        Toast.makeText(PersonalEntryActivity.this, "取得完了メッセージが出るまで待機してください", Toast.LENGTH_SHORT).show();

                        // 位置情報を取得する
                        getLocation();
                    }
                });
        alertDlg.setNegativeButton(
                "いいえ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // いいえ ボタンクリック処理
                        Toast.makeText(PersonalEntryActivity.this, "備蓄している位置で取得してください", Toast.LENGTH_SHORT).show();
                    }
                });

        // 表示
        alertDlg.create().show();
    }

    // 位置情報を確認ボタンを押下時の挙動
    public void onConfLocationClick(View v) {
        // 位置情報を取得済みかどうかの確認
        if (latitude == null) {
            Toast.makeText(PersonalEntryActivity.this, "現在位置を取得してください", Toast.LENGTH_SHORT).show();
            return;
        }

        // 位置情報を確認する場所の確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle("位置情報を確認");
        alertDlg.setMessage("現在位置をGoogleMapで確認します");
        alertDlg.setPositiveButton(
                "はい",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // はい ボタンクリック処理
                        // GoogleMapを開いて位置情報を確認する
                        Intent intent = new Intent(PersonalEntryActivity.this, MapsActivity.class);

                        // 位置情報をMapsActivityに受け渡す
                        String strLatitude = String.valueOf(latitude);
                        String strLongitude = String.valueOf(longitude);
                        intent.putExtra("lat", strLatitude);
                        intent.putExtra("lng", strLongitude);

                        // MapsActivityに遷移
                        startActivity(intent);
                    }
                });
        alertDlg.setNegativeButton(
                "いいえ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // いいえ ボタンクリック処理
                    }
                });

        // 表示
        alertDlg.create().show();
    }

    // 位置情報を取得する関数
    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, // 位置情報更新を行う最低更新時間間隔（ms）
                0, // 位置情報更新を行う最小距離間隔（メートル）
                new LocationListener() {
                    // ロケーションが変更された時の動き
                    @Override
                    public void onLocationChanged(Location location) {
                        // 権限のチェック
                        if (ActivityCompat.checkSelfPermission(PersonalEntryActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)
                            return;
                        // 緯度と経度を取得
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        Toast.makeText(PersonalEntryActivity.this, "現在位置の取得が完了しました", Toast.LENGTH_SHORT).show();
                        locationManager.removeUpdates(this);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Toast.makeText(PersonalEntryActivity.this, "位置情報がOFFになっています", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                }
        );
    }

    // 登録ボタンを押下時の挙動
    public void onEntryClick(View v) {
        // 登録が可能な状態かどうかの確認
        if (numPeopleEdit.getText().length() == 0) {
            Toast.makeText(PersonalEntryActivity.this, "世帯人数が入力されてません", Toast.LENGTH_SHORT).show();
        } else if (contactInfoEdit.getText().length() == 0) {
            Toast.makeText(PersonalEntryActivity.this, "連絡先1が入力されてません", Toast.LENGTH_SHORT).show();
        } else if (contactInfoEdit2.getText().length() == 0) {
            Toast.makeText(PersonalEntryActivity.this, "連絡先2が入力されてません", Toast.LENGTH_SHORT).show();
        } else if (contactInfoEdit.getText().toString().equals(contactInfoEdit2.getText().toString())) {
            Toast.makeText(PersonalEntryActivity.this, "連絡先2には連絡先1とは別の連絡先を入力してください", Toast.LENGTH_SHORT).show();
        } else if (latitude == null) {
            Toast.makeText(PersonalEntryActivity.this, "現在位置を取得してください", Toast.LENGTH_SHORT).show();
        }else{
            // パーソナルデータ登録の確認ダイアログの生成
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
            alertDlg.setTitle("登録の確認");
            alertDlg.setMessage("入力された情報を登録します\nよろしいですか？");
            alertDlg.setPositiveButton(
                    "はい",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // はい ボタンクリック処理
                            // propertiesに情報を登録する


                            // TODO: サーバー側で位置情報などを保持している場合はサーバー側に変更した情報を通信

                            // MainActivityに戻る
                            finish();
                        }
                    });
            alertDlg.setNegativeButton(
                    "いいえ",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // いいえ ボタンクリック処理
                        }
                    });

            // 表示
            alertDlg.create().show();
        }
    }

    // 画面タップ時にフォーカスを背景に移すためのメソッド
    public void onBackClick(View v) {
        back.requestFocus();
    }
}
