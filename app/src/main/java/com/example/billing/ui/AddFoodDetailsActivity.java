package com.example.billing.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.billing.R;
import com.example.billing.addFoodDB.BillContract;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AddFoodDetailsActivity extends AppCompatActivity {


    SQLiteDatabase sqLiteDatabase;
    EditText editTextFoodName, editTextPrice, editTextIngredients;
    Button buttonAddFood;
    String FoodNameHolder, PriceHolder, SQLiteDataBaseQueryHolder, IngredientsHolder;
    Boolean EditTextEmptyHold;


    private static final int EXISTING_BILL_LOADER = 0;
    private Uri mUri;


    InterstitialAd mInterstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_details);
//        MobileAds.initialize(this,"ca-app-pub-8942221596028723~1012793998");
//
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-8942221596028723/9587936942");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.show();

        editTextFoodName = findViewById(R.id.foodName);
        editTextPrice = findViewById(R.id.foodPrice);
        editTextIngredients = findViewById(R.id.foodingredients);
        buttonAddFood = findViewById(R.id.addFoodButton);
        buttonAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextStatus();
                addData();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    private void addData() {
        if (EditTextEmptyHold) {
            ContentValues values = new ContentValues();
            values.put(BillContract.addFood.COLUMN_FOOD_NAME, FoodNameHolder);
            values.put(BillContract.addFood.COLUMN_FOOD_INGREDIENTS, IngredientsHolder);
            values.put(BillContract.addFood.COLUMN_FOOD_PRICE, PriceHolder);
            if (mUri == null) {
                Uri newUri = getContentResolver().insert(BillContract.addFood.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, "Profile Cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Profile Saved", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    public void CheckEditTextStatus() {
        FoodNameHolder = editTextFoodName.getText().toString();
        PriceHolder = editTextPrice.getText().toString();
        IngredientsHolder = editTextIngredients.getText().toString();
        if (TextUtils.isEmpty(FoodNameHolder) || TextUtils.isEmpty(PriceHolder) || TextUtils.isEmpty(IngredientsHolder)) {
            EditTextEmptyHold = false;
        } else {
            EditTextEmptyHold = true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
