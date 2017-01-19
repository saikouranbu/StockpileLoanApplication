package com.example.tsuka.stockpileloanapplication.models;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.activities.MapsActivity;
import com.example.tsuka.stockpileloanapplication.activities.PersonalEntryActivity;
import com.example.tsuka.stockpileloanapplication.db.PersonalTableInsert;
import com.example.tsuka.stockpileloanapplication.db.PersonalTableUpdate;
import com.example.tsuka.stockpileloanapplication.utils.LatLngGetter;
import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

public class PersonalEntryModel {
    private PersonalEntryActivity activity;

    private RelativeLayout back;

    private EditText contactInfoEdit, contactInfoEdit2;

    private String strLatitude, strLongitude;
    private LatLngGetter latLngGetter;

    private static final int NULL_INT = -10000;

    public PersonalEntryModel(PersonalEntryActivity activity) {
        this.activity = activity;

        back = (RelativeLayout) activity.findViewById(R.id.activity_personal_entry);

        contactInfoEdit = (EditText) activity.findViewById(R.id.searchStockpileEdit);
        contactInfoEdit2 = (EditText) activity.findViewById(R.id.contactInfoEdit2);

        // 既に情報が登録されていた場合情報をEditTextに表示
        UseProperties useProperties = new UseProperties(activity.getApplicationContext());
        if (useProperties.isRegisteredProperties()) {
            contactInfoEdit.setText(useProperties.getContactInfo());
            contactInfoEdit2.setText(useProperties.getContactInfo2());
            strLatitude = String.valueOf(useProperties.getLatitude());
            strLongitude = String.valueOf(useProperties.getLongitude());
            useProperties.logProperties();
        }

        latLngGetter = new LatLngGetter(activity);

        useProperties = null;
    }

    // 位置情報を取得ボタンを押下時の挙動
    public void getLocationClick() {
        // 位置情報を登録する場所の確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(activity);
        alertDlg.setTitle("位置情報を取得");
        alertDlg.setMessage("現在位置を取得します\n備蓄している位置での取得をお願いします\n現在位置を取得してよろしいですか？");
        alertDlg.setPositiveButton(
                "はい",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // はい ボタンクリック処理
                        Toast.makeText(activity, "取得完了メッセージが出るまで待機してください", Toast.LENGTH_SHORT).show();

                        // 位置情報を取得する
                        latLngGetter.getLocation();
                    }
                });
        alertDlg.setNegativeButton(
                "いいえ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // いいえ ボタンクリック処理
                        Toast.makeText(activity, "備蓄している位置で取得してください", Toast.LENGTH_SHORT).show();
                    }
                });

        // 表示
        alertDlg.create().show();
    }

    // 位置情報を確認ボタンを押下時の挙動
    public void confLocation() {
        // 位置情報を取得済みかどうかの確認
        if (!latLngGetter.isGet() && strLatitude == null) {
            Toast.makeText(activity, "現在位置の取得が完了していません", Toast.LENGTH_SHORT).show();
            return;
        } else if (latLngGetter.isGet()) {
            strLatitude = String.valueOf(latLngGetter.getLatLng().latitude);
            strLongitude = String.valueOf(latLngGetter.getLatLng().longitude);
        }

        // 位置情報を確認する場所の確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(activity);
        alertDlg.setTitle("位置情報を確認");
        alertDlg.setMessage("現在位置をGoogleMapで確認します");
        alertDlg.setPositiveButton(
                "はい",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // はい ボタンクリック処理
                        // GoogleMapを開いて位置情報を確認する
                        Intent intent = new Intent(activity, MapsActivity.class);

                        // 位置情報をMapsActivityに受け渡す
                        intent.putExtra("lat", strLatitude);
                        intent.putExtra("lng", strLongitude);

                        // MapsActivityに遷移
                        activity.startActivity(intent);
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

    // 登録ボタンを押下時の挙動
    public void locationEntry() {
        // 登録が可能な状態かどうかの確認
        if (contactInfoEdit.getText().length() == 0) {
            Toast.makeText(activity, "連絡先1が入力されてません", Toast.LENGTH_SHORT).show();
        } else if (contactInfoEdit2.getText().length() == 0) {
            Toast.makeText(activity, "連絡先2が入力されてません", Toast.LENGTH_SHORT).show();
        } else if (contactInfoEdit.getText().toString().equals(contactInfoEdit2.getText().toString())) {
            Toast.makeText(activity, "連絡先2には連絡先1とは別の連絡先を入力してください", Toast.LENGTH_SHORT).show();
        } else if (!latLngGetter.isGet() && strLatitude == null) {
            Toast.makeText(activity, "現在位置の取得が完了していません", Toast.LENGTH_SHORT).show();
        } else {
            // パーソナルデータ登録の確認ダイアログの生成
            final AlertDialog.Builder alertDlg = new AlertDialog.Builder(activity);
            alertDlg.setTitle("登録の確認");
            alertDlg.setMessage("入力された情報を登録します\nよろしいですか？");
            alertDlg.setPositiveButton(
                    "はい",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // はい ボタンクリック処理
                            // propertiesに情報を登録する
                            UseProperties useProperties = new UseProperties(activity.getApplicationContext());

                            String contact = contactInfoEdit.getText().toString();
                            String contact2 = contactInfoEdit2.getText().toString();

                            if (latLngGetter.isGet()) {
                                strLatitude = String.valueOf(latLngGetter.getLatLng().latitude);
                                strLongitude = String.valueOf(latLngGetter.getLatLng().longitude);
                            }

                            useProperties.entryProperties(contact, contact2, strLatitude, strLongitude);

                            if(useProperties.getPersonalId() != NULL_INT){
                                // データベースにパーソナルデータが登録済みの場合は更新
                                PersonalTableUpdate update = new PersonalTableUpdate(useProperties);
                                try {
                                    update.execute();
                                } catch (Exception e) {
                                    Log.d("error", e.toString());
                                }
                            }else{
                                // データベースに未登録の場合データベースに挿入
                                PersonalTableInsert insert = new PersonalTableInsert(useProperties);
                                try {
                                    insert.execute();
                                } catch (Exception e) {
                                    Log.d("error", e.toString());
                                }
                            }

                            useProperties = null;
                            // MainActivityに戻る
                            activity.finish();
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
    public void clickFocus() {
        back.requestFocus();
    }
}
