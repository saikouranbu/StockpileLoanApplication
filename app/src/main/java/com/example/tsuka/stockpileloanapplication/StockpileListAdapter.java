package com.example.tsuka.stockpileloanapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

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

        StockpileData data = getItem(position);

        viewHolder.stockpileName.setText(data.stockpileName);
        viewHolder.stockpileReqNum.setText(data.stockpileReqNum);
        viewHolder.stockpileNumUnitEdit.setText(data.stockpileNumUnit);
        viewHolder.stockpileNum.setText(data.stockpileNum);
        viewHolder.stockpileNumUnit.setText(data.stockpileNumUnit);

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