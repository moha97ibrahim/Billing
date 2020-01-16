package com.example.billing.ui.insight;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;

public class InsightCursorAdapter extends CursorAdapter {
    public InsightCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return  LayoutInflater.from(context).inflate(R.layout.insight_listview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView orderId = view.findViewById(R.id.orderId);
        TextView orderDate = view.findViewById(R.id.orderDate);
        TextView orderValue = view.findViewById(R.id.orderValue);

        int id = cursor.getColumnIndex(BillContract.addFood._ID_DATA);
        int date = cursor.getColumnIndex(BillContract.addFood.COLUMN_DATA_ORDER_DATE);
        int month = cursor.getColumnIndex(BillContract.addFood.COLUMN_DATA_ORDER_MONTH);
        int year = cursor.getColumnIndex(BillContract.addFood.COLUMN_DATA_ORDER_YEAR);
        int value = cursor.getColumnIndex(BillContract.addFood.COLUMN_DATA_ORDER_VALUE);

        String ID = cursor.getString(id);
        String DATE = cursor.getString(date);
        String MONTH = cursor.getString(month);
        String YEAR = cursor.getString(year);
        String VALUE = cursor.getString(value);

        orderId.setText(ID);
        String DATE_FROMAT = DATE+"-"+MONTH+"-"+YEAR;
        orderDate.setText(DATE_FROMAT);
        orderValue.setText(VALUE);




    }
}
