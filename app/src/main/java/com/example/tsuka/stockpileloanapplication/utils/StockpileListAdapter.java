package com.example.tsuka.stockpileloanapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tsuka.stockpileloanapplication.R;

import java.util.List;

public class StockpileListAdapter extends ArrayAdapter<StockpileData> {

    LayoutInflater inflater;

    public StockpileListAdapter(Context context, List<StockpileData> lists) {
        super(context, 0, lists);

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        ViewHolder viewHolder;

        if(contentView == null){
            contentView = this.inflater.inflate(R.layout.stockpile_list, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.stockpileNameSpinner = (Spinner) contentView.findViewById(R.id.stockpileNameSpinner);
            viewHolder.stockpileReqNum = (NumberPicker) contentView.findViewById(R.id.stockpileReqNum);
            viewHolder.stockpileReqNum.setMaxValue(100); // 入力上限
            viewHolder.stockpileReqNum.setMinValue(1); // 入力下限
            viewHolder.stockpileReqNum.setValue(1); // 入力初期値
            viewHolder.stockpileNumUnitEdit = (EditText) contentView.findViewById(R.id.stockpileNumUnitEdit);
            viewHolder.stockpileNum = (EditText) contentView.findViewById(R.id.stockpileNum);
            viewHolder.stockpileNumUnit = (TextView) contentView.findViewById(R.id.stockpileNumUnit);

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
                String stock = (String) spinner.getSelectedItem();
                data.setStockpileName(stock);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewHolder.stockpileReqNum.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                data.setStockpileReqNum(String.valueOf(finalViewHolder.stockpileReqNum.getValue()));
            }
        });
        viewHolder.stockpileNumUnitEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // フォーカスが外れた場合dataを更新する
                    data.setStockpileNumUnit(finalViewHolder.stockpileNumUnitEdit.getText().toString());
                }
            }
        });
        viewHolder.stockpileNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // フォーカスが外れた場合dataを更新する
                    data.setStockpileNum(finalViewHolder.stockpileNum.getText().toString());
                }
            }
        });

        viewHolder.stockpileReqNum.setValue(Integer.parseInt(data.getStockpileReqNum()));
        viewHolder.stockpileNumUnitEdit.setText(data.getStockpileNumUnit());
        viewHolder.stockpileNum.setText(data.getStockpileNum());
        viewHolder.stockpileNumUnit.setText(data.getStockpileNumUnit());

        return contentView;
    }

    class ViewHolder {
        Spinner stockpileNameSpinner;
        NumberPicker stockpileReqNum;
        //EditText stockpileReqNum;
        EditText stockpileNumUnitEdit;
        EditText stockpileNum;
        TextView stockpileNumUnit;
    }
}