package com.example.billing.ui.menuFra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.billing.global.CartList;

import java.util.ArrayList;


public class MenuFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MenuViewModel menuViewModel;

    private ListView menuFoodListView;
    MenuCursorAdapter menuCursorAdapter;
    private final int CART_LOADER = 1;
    private ArrayList<Integer> cart = new ArrayList<>();
    private CartList cartList;
    private String CL;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button addBtn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        menuViewModel =
                ViewModelProviders.of(this).get(MenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        menuViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Toast.makeText(getActivity(), "" + cart, Toast.LENGTH_SHORT).show();

        //list view
        menuFoodListView = root.findViewById(R.id.menuFoodListView);
        addBtn = root.findViewById(R.id.addBtn);
        menuFoodListView.setEmptyView(textView);
        menuCursorAdapter = new MenuCursorAdapter(getActivity(), null);
        menuFoodListView.setAdapter(menuCursorAdapter);
        getActivity().getSupportLoaderManager().initLoader(CART_LOADER, null, this);

        //cart
        cartList = (CartList) getActivity().getApplicationContext();
        if (cartList.getListCart() == null) {
            CL = "";
        } else {
            CL = "" + cartList.getListCart();
        }

//        CL = cartList.getListCart();
//        if(CL != null) {
//            String a[] = CL.split(",");
//


        //adding to cart
//        menuFoodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Button button = view.findViewById(R.id.addBtn);
//                button.setText("ADDED");

//                if (cart.contains((int) id)) {
//                    Log.e("notadded", (id) + "" + cart);
//                    Toast.makeText(getActivity(), "Already Added", Toast.LENGTH_SHORT).show();
//                } else {
//                    cart.add((int) id);
//                    Log.e("added", String.valueOf(id));
//                    cartList.setCartList(cart);
//                    Toast.makeText(getActivity(), "" + cart, Toast.LENGTH_SHORT).show();
//                }
//
//                if(CL.contains(String.valueOf(id))){
//                    Log.e("temp",CL);
//                    Toast.makeText(getActivity(), "Already Added", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                   CL=CL+","+id;
//                    Log.e("temp",CL);
//                }
//                cartList.setListCart(CL);
//            }
//        });
//
//

        Log.e("temp", CL);
        return root;
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String projection[] = {
                BillContract.addFood._ID,
                BillContract.addFood.COLUMN_FOOD_NAME,
                BillContract.addFood.COLUMN_FOOD_INGREDIENTS,
                BillContract.addFood.COLUMN_FOOD_PRICE
        };


        Log.e("on create loader 2", "onR");


        CursorLoader s = new CursorLoader(getActivity(),
                BillContract.addFood.CONTENT_URI,
                projection,
                null,
                null,
                null);

        return s;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        menuCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        menuCursorAdapter.swapCursor(null);

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}