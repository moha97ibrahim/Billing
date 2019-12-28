package com.example.billing.ui.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;
import com.example.billing.addFoodDB.BillDbHelper;
import com.example.billing.global.CartList;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CartCursorAdapter cartCursorAdapter;
    private CartList cartList;
    private String query;
    private BillDbHelper dbHelper;
    private TextView gTotal,sGST,cGST,cTotal;
    private float grandToatal;
    private CardView cardViewtot;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        CartViewModel cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        cTotal = root.findViewById(R.id.cartTotal);
        cGST = root.findViewById(R.id.cgstView);
        sGST = root.findViewById(R.id.sgstView);
        gTotal = root.findViewById(R.id.grandTotalView);
        cardViewtot = root.findViewById(R.id.cardViewTotal);
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
        getActivity().getSupportLoaderManager().restartLoader(BILL_LOADER, null, this);
        return root;
    }

    @Override
    public void onResume() {
            updateValue();
        super.onResume();

    }

    private void refresh(int i) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateValue();
            }
        };

        handler.postDelayed(runnable,i);
    }

    private void updateValue() {
        DecimalFormat precision = new DecimalFormat("0.00");

        try {
            Log.e("run","run");
            dbHelper = new BillDbHelper(getContext());
            float cartTotal = dbHelper.getTotalSum();
            float CGST = (float) (cartTotal * (9.0 / 100));
            float SGST = (float) ((9.0 / 100) * cartTotal);
            grandToatal = cartTotal + CGST + SGST;

            cTotal.setText(precision.format(cartTotal));
            cGST.setText(precision.format(CGST));
            sGST.setText(precision.format(SGST));
            gTotal.setText(precision.format(grandToatal));
            refresh(1);
        }catch (NullPointerException ignored){}

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String pro[] = {
                BillContract.addFood._ID_CART,
                BillContract.addFood.COLUMN_FOOD_NAME_CART,
                //cartContract.cartItem.COLUMN_FOOD_INGREDIENTS,
                BillContract.addFood.COLUMN_FOOD_QUANTITY_CART,
                BillContract.addFood.COLUMN_FOOD_PRICE_CART
        };

        return new CursorLoader(getActivity(),
                BillContract.addFood.CONTENT_URI_CART,
                pro,
                null,
                null,
                null);
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