package com.example.tsuka.stockpileloanapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tsuka.stockpileloanapplication.R;

import java.util.List;

public class StockpileListAdapter extends ArrayAdapter<StockpileData> {

    private final String[] STOCKPILES = {
            "飲料水500ml", "飲料水2L", "保存食", "毛布",
            "ダンボール", "コンロ", "皿", "コップ", "箸",
            "スプーン", "ガムテープ", "はさみ", "紐", "ロープ"
    };
    private final String[] EMERGENCY_LEVEL = {
            "待機", "-", "低", "中", "高"
    };
    private LayoutInflater inflater;
    private Context context;

    public StockpileListAdapter(Context context, List<StockpileData> lists) {
        super(context, 0, lists);

        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        ViewHolder viewHolder;

        if(contentView == null){
            contentView = this.inflater.inflate(R.layout.stockpile_list, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.stockpileNameSpinner = (Spinner) contentView.findViewById(R.id.stockpileNameSpinner);
            viewHolder.stockpileNameSpinner.setAdapter(
                    new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, STOCKPILES));
            viewHolder.stockpileReqNum = (EditText) contentView.findViewById(R.id.stockpileReqNum);
            viewHolder.stockpileNum = (EditText) contentView.findViewById(R.id.stockpileNum);
            viewHolder.emergencyLevel = (Spinner) contentView.findViewById(R.id.emergencyLevel);
            viewHolder.emergencyLevel.setAdapter(
                    new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, EMERGENCY_LEVEL));

            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }

        final StockpileData data = getItem(position);

        // Listenerから参照するためにfinal修飾子のViewHolderにコピー
        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.stockpileNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;

                // 選択された備蓄品
                data.setStockpileName(spinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewHolder.stockpileReqNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // フォーカスが外れた場合dataを更新する
                    data.setStockpileReqNum(finalViewHolder.stockpileReqNum.getText().toString());
                    EditText edit = (EditText) v;
                    String strReqNum = edit.getText().toString();
                    String strNum = finalViewHolder.stockpileNum.getText().toString();
                    if (strReqNum.length() == 0 || strNum.length() == 0) return;
                    int reqNum = Integer.parseInt(strReqNum);
                    int num = Integer.parseInt(strNum);
                    if (num > reqNum) {
                        finalViewHolder.emergencyLevel.setSelection(1); // ハイフン
                    } else if (num < reqNum && finalViewHolder.emergencyLevel.getSelectedItemPosition() == 1) {
                        // 必要数より備蓄数が不足しているのに緊急度がハイフンの場合緊急度低にする
                        finalViewHolder.emergencyLevel.setSelection(2); // 緊急度低
                    }
                }
            }
        });
        viewHolder.stockpileNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // フォーカスが外れた場合dataを更新する
                    data.setStockpileNum(finalViewHolder.stockpileNum.getText().toString());
                    EditText edit = (EditText) v;
                    String strReqNum = finalViewHolder.stockpileReqNum.getText().toString();
                    String strNum = edit.getText().toString();
                    if (strReqNum.length() == 0 || strNum.length() == 0) return;
                    int reqNum = Integer.parseInt(strReqNum);
                    int num = Integer.parseInt(strNum);
                    if (num > reqNum) {
                        finalViewHolder.emergencyLevel.setSelection(1); // ハイフン
                    } else if (num < reqNum && finalViewHolder.emergencyLevel.getSelectedItemPosition() == 1) {
                        // 必要数より備蓄数が不足しているのに緊急度がハイフンの場合緊急度低にする
                        finalViewHolder.emergencyLevel.setSelection(2); // 緊急度低
                    }
                }
            }
        });
        viewHolder.emergencyLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;

                String strReqNum = finalViewHolder.stockpileReqNum.getText().toString();
                String strNum = finalViewHolder.stockpileNum.getText().toString();
                if (strReqNum.length() == 0 || strNum.length() == 0) {
                    finalViewHolder.stockpileReqNum.setText("1");
                    data.setStockpileReqNum("1");
                    finalViewHolder.stockpileNum.setText("1");
                    data.setStockpileNum("1");
                    finalViewHolder.emergencyLevel.setSelection(1); // ハイフン
                    data.setEmergencyLevelPosition(spinner.getSelectedItemPosition());
                    return;
                }
                int reqNum = Integer.parseInt(strReqNum);
                int num = Integer.parseInt(strNum);
                if (num > reqNum) {
                    finalViewHolder.emergencyLevel.setSelection(1); // ハイフン
                }
                // 選択された備蓄品
                data.setEmergencyLevelPosition(spinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewHolder.stockpileNameSpinner.setSelection(data.getStockpileNamePosition());
        viewHolder.stockpileReqNum.setText(data.getStockpileReqNum());
        viewHolder.stockpileNum.setText(data.getStockpileNum());
        viewHolder.emergencyLevel.setSelection(data.getEmergencyLevelPosition());

        return contentView;
    }

    class ViewHolder {
        Spinner stockpileNameSpinner;
        EditText stockpileReqNum;
        EditText stockpileNum;
        Spinner emergencyLevel;
    }
}