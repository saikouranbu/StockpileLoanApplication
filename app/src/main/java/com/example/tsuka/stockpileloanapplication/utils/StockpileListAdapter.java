package com.example.tsuka.stockpileloanapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

            viewHolder.stockpileName = (EditText) contentView.findViewById(R.id.stockpileName);
            viewHolder.stockpileReqNum = (EditText) contentView.findViewById(R.id.stockpileReqNum);
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

        viewHolder.stockpileName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // フォーカスが外れた場合dataを更新する
                    data.setStockpileName(finalViewHolder.stockpileName.getText().toString());
                }
            }
        });
        viewHolder.stockpileReqNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // フォーカスが外れた場合dataを更新する
                    data.setStockpileReqNum(finalViewHolder.stockpileReqNum.getText().toString());
                }
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

        viewHolder.stockpileName.setText(data.getStockpileName());
        viewHolder.stockpileReqNum.setText(data.getStockpileReqNum());
        viewHolder.stockpileNumUnitEdit.setText(data.getStockpileNumUnit());
        viewHolder.stockpileNum.setText(data.getStockpileNum());
        viewHolder.stockpileNumUnit.setText(data.getStockpileNumUnit());

        return contentView;
    }

    class ViewHolder {
        EditText stockpileName;
        EditText stockpileReqNum;
        EditText stockpileNumUnitEdit;
        EditText stockpileNum;
        TextView stockpileNumUnit;
    }
}