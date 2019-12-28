package com.example.billing.ui.addfood;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.billing.ui.AddFoodDetailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class addFoodFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private addFoodViewModel addFoodViewModel;
    private FloatingActionButton floatingActionButton;


    private ListView foodListView;
    BillCursorAdapter billCursorAdapter;
    private final int BILL_LOADER = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addFoodViewModel =
                ViewModelProviders.of(this).get(addFoodViewModel.class);

        View root = inflater.inflate(R.layout.fragment_addfood, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        foodListView = root.findViewById(R.id.addFoodListView);
        foodListView.setEmptyView(textView);
        billCursorAdapter = new BillCursorAdapter(getActivity(), null);
        foodListView.setAdapter(billCursorAdapter);
        getActivity().getSupportLoaderManager().restartLoader(BILL_LOADER, null, this);
        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("click","1");
             //   Toast.makeText(getActivity(),String.valueOf(id),Toast.LENGTH_SHORT).show();
            }
        });

        addFoodViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        floatingActionButton = getActivity().findViewById(R.id.additen_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFoodDetailsActivity.class);
                startActivity(intent);
            }
        });

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

        Log.e("on create loader 3", "onR");

        return new CursorLoader(getActivity(),
                BillContract.addFood.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        billCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        billCursorAdapter.swapCursor(null);
    }
}