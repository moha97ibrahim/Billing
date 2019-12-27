package com.example.billing.ui.menuFra;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;
import com.example.billing.cartDB.cartContract;
import com.example.billing.global.CartList;

public class MenuCursorAdapter extends CursorAdapter {
    public MenuCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    private String IDS;
    private String CL;
    private Context context;
    private CartList cartList;


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.menu_food_listview, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        TextView name = view.findViewById(R.id.menuFoodNameView);
        TextView price = view.findViewById(R.id.menuFoodPriceView);
        TextView ingredient = view.findViewById(R.id.menuFoodIngredientsView);


        final Button addBtn = view.findViewById(R.id.addBtn);

        int nameColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_NAME);
        int ID = cursor.getColumnIndex(BillContract.addFood._ID);
        int priceColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_PRICE);
        int ingredientsColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_INGREDIENTS);

        IDS = cursor.getString(ID);

        //cart
        cartList = (CartList) context.getApplicationContext();
        Log.e("temp", "" + cartList.getListCart());

        if (cartList.getListCart() == null) {
            CL = "";
        } else {
            CL = "" + cartList.getListCart();
        }


        // markAsAdded(cartList.getListCart());
        int mark = (int) getItemId(cursor.getPosition());
        addBtn.setTag(cursor.getPosition());
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(v,addBtn);
            }
        });


        String names = cursor.getString(nameColumnIndex);
        String prices = cursor.getString(priceColumnIndex);
        String ingredients = cursor.getString(ingredientsColumnIndex);


        name.setText(names);
        price.setText(prices);
        ingredient.setText(ingredients);


        changeMarkAsAdded(mark,addBtn);

    }

    private void addToCart(View v, Button addBtn) {
        Object object = v.getTag();
        int LIST = (int) getItemId((int) object);
        Log.e("id", "" + LIST);
        if (CL.contains(String.valueOf(LIST))) {
            Log.e("temp", "already added");
        } else {
            CL = CL + "," + LIST;
            Log.e("temp", "=added=" + LIST);
        }
        cartList.setListCart(CL);
        Log.e("new", "" + LIST);
        addBtn.setText("ADDED");
        addBtn.setTextColor(Color.GREEN);
    }

    private void changeMarkAsAdded(int mark, Button addBtn) {
        if (CL.contains(String.valueOf(mark))) {
            Log.e("comparission", CL + "=" + mark);
            addBtn.setText("ADDED");
            addBtn.setTextColor(Color.GREEN);
        }else{
            addBtn.setText("ADD");
            addBtn.setTextColor(Color.BLACK);
        }
    }

}
