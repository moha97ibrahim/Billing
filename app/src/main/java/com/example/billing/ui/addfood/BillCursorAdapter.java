package com.example.billing.ui.addfood;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;


public class BillCursorAdapter extends CursorAdapter {
    private View view;

    public BillCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.add_food_listview, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {

        TextView name = view.findViewById(R.id.foodNameView);
        TextView price = view.findViewById(R.id.foodPriceView);
        TextView ingredient = view.findViewById(R.id.foodIngredientsView);
        final ImageButton deletefood = view.findViewById(R.id.deleteFoodImageButton);


        int nameColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_NAME);
        int ingredientsColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_INGREDIENTS);
        int priceColumnIndex = cursor.getColumnIndex(BillContract.addFood.COLUMN_FOOD_PRICE);


        String names = cursor.getString(nameColumnIndex);
        String prices = cursor.getString(priceColumnIndex);
        String ingredients = cursor.getString(ingredientsColumnIndex);


        name.setText(names);
        price.setText(prices);
        ingredient.setText(ingredients);
        deletefood.setTag(cursor.getPosition());
        deletefood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
//                builder.setView(view);
//                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
                        deleteFood(v,context);
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
            }
        });


    }

    private void deleteFood(View v, Context context) {
        Object object = v.getTag();
        long id = getItemId((Integer) object);
        Uri currentUri = ContentUris.withAppendedId(BillContract.addFood.CONTENT_URI, id);

        int rowsDeleted = context.getContentResolver().delete(currentUri, null, null);

        if (rowsDeleted == 0) {
            Toast.makeText(context.getApplicationContext(), "Error in Delete", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "Bill is Deleted", Toast.LENGTH_LONG).show();
        }
    }

}
