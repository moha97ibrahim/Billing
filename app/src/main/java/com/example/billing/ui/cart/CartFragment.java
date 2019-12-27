package com.example.billing.ui.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;
import com.example.billing.cartDB.cartContract;
import com.example.billing.global.CartList;

import java.util.ArrayList;

public class CartFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CartCursorAdapter cartCursorAdapter;
    private CartList cartList;
    private String query;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        CartViewModel cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        cartViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //listview
        ListView cartListView = root.findViewById(R.id.cartFoodListView);
        cartListView.setEmptyView(textView);
        cartCursorAdapter = new CartCursorAdapter(getActivity(), null);
        cartListView.setAdapter(cartCursorAdapter);
        int BILL_LOADER = 0;
        cartList = (CartList) getActivity().getApplicationContext();
        query = String.valueOf(cartList.getCartList());
        String temp = "" + cartList.getListCart();
        temp = temp.replaceFirst(",", "");
        query = "" + temp;

        Toast.makeText(getActivity(), "" + temp, Toast.LENGTH_SHORT).show();


        getActivity().getSupportLoaderManager().restartLoader(BILL_LOADER, null, this);
        return root;
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String pro[] = {
                cartContract.cartItem._ID,
                cartContract.cartItem.COLUMN_FOOD_NAME,
                cartContract.cartItem.COLUMN_FOOD_INGREDIENTS,
                cartContract.cartItem.COLUMN_FOOD_PRICE
        };

        Log.e("on create loader 1", query);


        String select = "((" + cartContract.cartItem._ID + " IN " + "(" + query + ")" + ") )";

        return new CursorLoader(getActivity(),
                cartContract.cartItem.CONTENT_URI,
                pro,
                select,
                null,
                null);
    }



    @Override
    public void onResume() {

        onCreate(null);
        super.onResume();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cartCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        cartCursorAdapter.swapCursor(null);

    }
}