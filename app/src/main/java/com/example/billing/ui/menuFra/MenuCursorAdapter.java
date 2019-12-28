package com.example.billing.ui.menuFra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.billing.MainActivity;
import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;

import com.example.billing.addFoodDB.BillDbHelper;
import com.example.billing.global.CartList;

import java.util.ArrayList;

import static com.example.billing.addFoodDB.BillContract.addFood.CONTENT_URI_CART;
import static com.example.billing.addFoodDB.BillContract.addFood.TABLE_NAME;
import static com.example.billing.addFoodDB.BillContract.addFood.TABLE_NAME_CART;
import static com.example.billing.addFoodDB.BillContract.addFood._ID_CART;

public class MenuCursorAdapter extends CursorAdapter {
    public MenuCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    private String IDS;
    private String CL;
    private Context context;
    private CartList cartList;
    private Uri mUri;
    private BillDbHelper dbHelper;
    private ArrayList<String> ids;


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.menu_food_listview, viewGroup, false);

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {


        final TextView name = view.findViewById(R.id.menuFoodNameView);
        final TextView price = view.findViewById(R.id.menuFoodPriceView);
        TextView ingredient = view.findViewById(R.id.menuFoodIngredientsView);


        final Button addBtn = view.findViewById(R.id.addBtn);

        int nameColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_NAME);
        int ID = cursor.getColumnIndex(BillContract.addFood._ID);
        int priceColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_PRICE);
        int ingredientsColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_INGREDIENTS);
        final String names = cursor.getString(nameColumnIndex);
        final String prices = cursor.getString(priceColumnIndex);
        String ingredients = cursor.getString(ingredientsColumnIndex);
        final String id = cursor.getString(ID);
        name.setText(names);
        price.setText(prices);
        ingredient.setText(ingredients);
        final int mark = (int) getItemId(cursor.getPosition());
        dbHelper = new BillDbHelper(context);
        addBtn.setTag(cursor.getPosition());
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(addBtn, context, mark);
            }
        });
        changeMarkAsAdded(mark, addBtn, context);
    }

    private void addToCart(Button addBtn, Context context, int mark) {
        dbHelper = new BillDbHelper(context);
        ContentValues values = new ContentValues();
        values.put(BillContract.addFood.COLUMN_FOOD_ID_CART, mark);
        values.put(BillContract.addFood.COLUMN_FOOD_NAME_CART, dbHelper.getName(mark));
//      values.put(BillContract.addFood.COLUMN_FOOD_QUANTITY_CART, 1);
        values.put(BillContract.addFood.COLUMN_FOOD_PRICE_CART, dbHelper.getprice(mark));
        if(!dbHelper.isExist(mark)) {
            Uri newUri = context.getContentResolver().insert(BillContract.addFood.CONTENT_URI_CART, values);
            if (newUri == null) {
                Toast.makeText(context.getApplicationContext(), "Profile Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context.getApplicationContext(), "Profile Saved", Toast.LENGTH_LONG).show();
                addBtn.setText("ADDED");
                addBtn.setTextColor(Color.GREEN);
            }
        }else{
            Toast.makeText(context.getApplicationContext(), "Already Added", Toast.LENGTH_SHORT).show();
        }
    }
    private void changeMarkAsAdded(int ID, Button addBtn, Context context) {
        dbHelper = new BillDbHelper(context);
        ids = dbHelper.isIdExist(ID, null);
        if (ids.contains(String.valueOf(ID))) {
            addBtn.setText("ADDED");
            addBtn.setTextColor(Color.GREEN);
        }else{
            addBtn.setText("ADD");
            addBtn.setTextColor(Color.BLACK);
        }
    }

}
