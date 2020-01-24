package com.example.billing.ui.cart;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
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
import com.example.billing.ui.printingActivity;

import java.text.DecimalFormat;

public class CartFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CartCursorAdapter cartCursorAdapter;
    private BillDbHelper dbHelper;
    private TextView gTotal, sGST, cGST, cTotal;
    private float grandToatal;
    private CardView cardViewtot, submitCard, clearCard;
    private ProgressDialog progressDoalog;
    int BILL_LOADER = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_cart, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        // cTotal = root.findViewById(R.id.cartTotal);
//        cGST = root.findViewById(R.id.cgstView);
//        sGST = root.findViewById(R.id.sgstView);
        gTotal = root.findViewById(R.id.grandTotal);
//        cardViewtot = root.findViewById(R.id.cardViewTotal);
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
        getActivity().getSupportLoaderManager().restartLoader(BILL_LOADER, null, this);
        submitCard = root.findViewById(R.id.submitCardView);
        cartCursorAdapter.notifyDataSetChanged();
        submitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPrint();
            }
        });



        return root;
    }

    private void checkPrint() {
        if(dbHelper.getCount()>0){
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, 0);

            } else {
                Intent i = new Intent(getActivity(), printingActivity.class);
                i.putExtra("total", grandToatal);
                startActivity(i);
            }

        }
        else{
            Toast.makeText(getActivity(),"Cart is Empty",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        update();
        super.onResume();

    }

    private void update() {
        try {
            Log.e("run", "run");
            dbHelper = new BillDbHelper(getContext());
            float cartTotal = dbHelper.getTotalSum();
            gTotal.setText(String.valueOf(cartTotal));
            reload(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void reload(int i) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                update();
            }
        };
        handler.postDelayed(runnable,i);
    }


    private void refresh(int i) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("run", "run");
                updateValue();
            }
        };
        handler.postDelayed(runnable, i);
    }

    private void updateValue() {
        DecimalFormat precision = new DecimalFormat("0.00");

        try {
            getActivity().getSupportLoaderManager().restartLoader(BILL_LOADER, null, this);
            Log.e("run", "run");
            dbHelper = new BillDbHelper(getContext());
            float cartTotal = dbHelper.getTotalSum();
            float CGST = (float) (cartTotal * (9.0 / 100));
            float SGST = (float) ((9.0 / 100) * cartTotal);
            grandToatal = cartTotal;
            cartTotal = cartTotal - CGST - SGST;
            cTotal.setText(precision.format(cartTotal));
            cGST.setText(precision.format(CGST));
            sGST.setText(precision.format(SGST));
            gTotal.setText(String.valueOf(cartTotal));
            refresh(1);
        } catch (NullPointerException ignored) {
        }

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String pro[] = {
                BillContract.addFood._ID_CART,
                BillContract.addFood.COLUMN_FOOD_NAME_CART,
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


    @Override
    public void onStop() {
        super.onStop();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}