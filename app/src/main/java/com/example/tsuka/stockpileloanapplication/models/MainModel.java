package com.example.tsuka.stockpileloanapplication.models;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.example.tsuka.stockpileloanapplication.R;
import com.example.tsuka.stockpileloanapplication.activities.MainActivity;
import com.example.tsuka.stockpileloanapplication.activities.PersonalEntryActivity;
import com.example.tsuka.stockpileloanapplication.activities.StockpileEntryActivity;
import com.example.tsuka.stockpileloanapplication.db.PersonalTableOpenChange;
import com.example.tsuka.stockpileloanapplication.utils.UseProperties;

public class MainModel {
    private MainActivity activity;

    private static final int REQUEST_LOCATION = 1; // 識別用:位置情報の許可
    private UseProperties useProperties;

    private Switch openSwitch;

    public MainModel(MainActivity activity){
        this.activity = activity;

        useProperties = new UseProperties(activity.getApplicationContext());

        openSwitch = (Switch) activity.findViewById(R.id.stockpileOpenSwitch);
    }

    // パーソナルデータ登録ボタンを押下時の挙動
    public void personalEntry() {
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 位置情報の権限がある場合
            // パーソナルデータ登録アクティビティに遷移
            Intent intent = new Intent(activity.getBaseContext(), PersonalEntryActivity.class);
            activity.startActivity(intent);
        } else {
            // 位置情報の権限がない場合
            // 許可を求めるダイアログを表示
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    // 備蓄品データ登録ボタンを押下時の挙動
    public void stockpileEntry() {
        // パーソナルデータが登録されているかの確認
        if (!isPersonalDataEmpty()) return;

        // 備蓄品データ登録アクティビティに遷移
        Intent intent = new Intent(activity.getBaseContext(), StockpileEntryActivity.class);
        activity.startActivity(intent);
    }

    // パーソナルデータが登録されていたらtrue
    private boolean isPersonalDataEmpty() {
        if (useProperties.isRegisteredProperties()) {
            return true;
        } else {
            Toast.makeText(activity, "パーソナルデータを登録してください", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // 備蓄品データ公開スイッチのon,off時の処理
    public void checkedSwitch() {
        if (!isPersonalDataEmpty()) {
            // パーソナルデータ未登録の場合
            // 何も処理せずにスイッチを戻す
            openSwitch.toggle();
            return;
        } else if (!useProperties.isRegisteredProperties()) {
            // 備蓄品データ未登録の場合
            // 何も処理せずにスイッチを戻す
            Toast.makeText(activity, "備蓄品データを登録してください", Toast.LENGTH_SHORT).show();
            openSwitch.toggle();
            return;
        } else {
            if (openSwitch.isChecked()) {
                // on時の処理
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(activity);
                alertDlg.setTitle("備蓄品データを公開");
                alertDlg.setMessage("備蓄品データを公開することで備蓄品マップが利用可能になります\n" +
                        "備蓄品データを公開中はパーソナルデータに登録された連絡先を他ユーザーに公開してしまうので災害時だけにご利用ください\n\n" +
                        "備蓄品データを公開しますか？");
                alertDlg.setPositiveButton(
                        "はい",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // はい ボタンクリック処理
                                Toast.makeText(activity, "処理完了メッセージが出るまで待機してください", Toast.LENGTH_SHORT).show();

                                // プロパティファイルに保存
                                useProperties.setOpenData(true);

                                // データベースに送信
                                PersonalTableOpenChange change = new PersonalTableOpenChange(useProperties);
                                try {
                                    boolean isSuccess = change.execute().get();
                                    if (isSuccess == true) {
                                        Toast.makeText(activity, "処理が完了しました", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "通信が正常に完了しませんでした", Toast.LENGTH_SHORT).show();
                                        // スイッチを元に戻す
                                        openSwitch.toggle();
                                    }
                                } catch (Exception e) {
                                    Log.d("error", e.toString());
                                }
                            }
                        });
                alertDlg.setNegativeButton(
                        "いいえ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // いいえ ボタンクリック処理
                                // スイッチを元に戻す
                                openSwitch.toggle();
                            }
                        });

                // 表示
                alertDlg.create().show();
            } else {
                // off時の処理
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(activity);
                alertDlg.setTitle("備蓄品データを非公開");
                alertDlg.setMessage("備蓄品データを非公開にします\n" +
                        "備蓄品データを非公開にすると備蓄品マップを利用できなくなります\n\n" +
                        "備蓄品データを非公開にしますか？");
                alertDlg.setPositiveButton(
                        "はい",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // はい ボタンクリック処理
                                Toast.makeText(activity, "処理完了メッセージが出るまで待機してください", Toast.LENGTH_SHORT).show();

                                // プロパティファイルに保存
                                useProperties.setOpenData(false);

                                // データベースに送信
                                PersonalTableOpenChange change = new PersonalTableOpenChange(useProperties);
                                try {
                                    boolean isSuccess = change.execute().get();
                                    if (isSuccess == true) {
                                        Toast.makeText(activity, "処理が完了しました", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "通信が正常に完了しませんでした", Toast.LENGTH_SHORT).show();
                                        // スイッチを元に戻す
                                        openSwitch.toggle();
                                    }
                                } catch (Exception e) {
                                    Log.d("error", e.toString());
                                }
                            }
                        });
                alertDlg.setNegativeButton(
                        "いいえ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // いいえ ボタンクリック処理
                                // スイッチを元に戻す
                                openSwitch.toggle();
                            }
                        });

                // 表示
                alertDlg.create().show();
            }
        }


    }
}
