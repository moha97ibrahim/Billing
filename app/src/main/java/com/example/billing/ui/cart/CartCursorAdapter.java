package com.example.billing.ui.cart;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;
import com.example.billing.addFoodDB.BillDbHelper;
import com.example.billing.global.CartList;

public class CartCursorAdapter extends CursorAdapter {

    private int QUANTITY = 1;
    private CartList cartList;
    private String CL;
    private BillDbHelper dbHelper;

    public CartCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.cart_food_listview, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {


        TextView name = view.findViewById(R.id.cartFoodNameView);
        TextView price = view.findViewById(R.id.cartFoodPriceView);
        //TextView ingredient = view.findViewById(R.id.cartFoodIngredientsView);
        final TextView quantity = view.findViewById(R.id.cartFoodQantityView);
        ImageButton increment = view.findViewById(R.id.imageButtonIncrement);
        ImageButton decrement = view.findViewById(R.id.imageButtonDecrement);
        TextView remove = view.findViewById(R.id.cartRemoveTextView);


        int nameColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_NAME_CART);
        int quantityColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_QUANTITY_CART);
        int priceColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_PRICE_CART);


        String names = cursor.getString(nameColumnIndex);
        String prices = cursor.getString(priceColumnIndex);
        String quantities = cursor.getString(quantityColumnIndex);


        name.setText(names);
        price.setText(prices);
        quantity.setText(quantities);


        final int mark = (int) getItemId(cursor.getPosition());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "" + mark, Toast.LENGTH_SHORT).show();
            }
        });

        // ingredient.setText(ingredients);
        increment.setTag(cursor.getPosition());
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuntity(quantity, mark, context);
            }
        });

        decrement.setTag(cursor.getPosition());
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity(quantity, mark, context);
            }
        });

        remove.setTag(cursor.getPosition());
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFood(v, context);
            }
        });

    }

    private void decrementQuantity(TextView quantity, int mark, Context context) {
        dbHelper = new BillDbHelper(context);
        QUANTITY = dbHelper.getQuantity(mark);
        if (QUANTITY > 1) {
            QUANTITY = QUANTITY - 1;
            quantity.setText(String.valueOf(QUANTITY));
            dbHelper.updateQuantity(mark, QUANTITY);
        }
    }

    private void incrementQuntity(TextView quantity, int mark, Context context) {
        dbHelper = new BillDbHelper(context);
        QUANTITY = dbHelper.getQuantity(mark);
        QUANTITY = QUANTITY + 1;
        quantity.setText(String.valueOf(QUANTITY));
        dbHelper.updateQuantity(mark, QUANTITY);
    }

    private void removeFood(View v, Context context) {
        Object object = v.getTag();
        long id = getItemId((Integer) object);
        Uri currentUri = ContentUris.withAppendedId(BillContract.addFood.CONTENT_URI_CART, id);

        int rowsDeleted = context.getContentResolver().delete(currentUri, null, null);

        if (rowsDeleted == 0) {
            Toast.makeText(context.getApplicationContext(), "Error in Delete", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "Removed", Toast.LENGTH_LONG).show();
        }
    }


}
