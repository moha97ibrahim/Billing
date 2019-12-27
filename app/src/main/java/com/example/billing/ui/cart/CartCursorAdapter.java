package com.example.billing.ui.cart;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.billing.R;
import com.example.billing.cartDB.cartContract;
import com.example.billing.global.CartList;

public class CartCursorAdapter extends CursorAdapter {

    private int QUANTITY = 1;
    private CartList cartList;
    private String CL;

    public CartCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.cart_food_listview, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView name = view.findViewById(R.id.cartFoodNameView);
        TextView price = view.findViewById(R.id.cartFoodPriceView);
        //TextView ingredient = view.findViewById(R.id.cartFoodIngredientsView);
        final TextView quantity = view.findViewById(R.id.cartFoodQantityView);
        ImageButton increment = view.findViewById(R.id.imageButtonIncrement);
        ImageButton decrement = view.findViewById(R.id.imageButtonDecrement);
        TextView remove = view.findViewById(R.id.cartRemoveTextView);


        //cart
        cartList = (CartList) context.getApplicationContext();
        Log.e("temp", "" + cartList.getListCart());

        if (cartList.getListCart() == null) {
            CL = "";
        } else {
            CL = "" + cartList.getListCart();
        }





        int nameColumnIndex = cursor.getColumnIndex(cartContract.cartItem.COLUMN_FOOD_NAME);
        int priceColumnIndex = cursor.getColumnIndex(cartContract.cartItem.COLUMN_FOOD_PRICE);
        //int ingredientsColumnIndex = cursor.getColumnIndex(cartContract.cartItem.COLUMN_FOOD_INGREDIENTS);

        String names = cursor.getString(nameColumnIndex);
        String prices = cursor.getString(priceColumnIndex);
        //String ingredients = cursor.getString(ingredientsColumnIndex);

        name.setText(names);
        price.setText(prices);

        // ingredient.setText(ingredients);
        increment.setTag(cursor.getPosition());
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuntity(quantity);
            }
        });

        decrement.setTag(cursor.getPosition());
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity(quantity);
            }
        });

        remove.setTag(cursor.getPosition());
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromCart(v, context);
            }
        });

    }

    private void removeFromCart(View v, Context context) {


        Object object = v.getTag();
        int LIST = (int) getItemId((int) object);
        if (CL.contains(String.valueOf(LIST))) {
            CL=CL.replace(","+(LIST), "");
            Log.e("remover",""+(LIST));
            cartList.setListCart(CL);
        }

    }

    private void decrementQuantity(TextView quantity) {
        if (QUANTITY > 1) {
            QUANTITY = QUANTITY - 1;
        }
        quantity.setText(String.valueOf(QUANTITY));
    }

    private void incrementQuntity(TextView quantity) {
        QUANTITY = QUANTITY + 1;
        quantity.setText(String.valueOf(QUANTITY));
    }
}
