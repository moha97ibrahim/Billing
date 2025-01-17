package com.example.billing.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.billing.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SettingActivity extends AppCompatActivity {

    private EditText shopName, shopAddredd,bluetoothName;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button saveButton;
    private String name, address, bluetooth;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences = getApplicationContext().getSharedPreferences("SETTING", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        shopName = findViewById(R.id.shopName);
        shopAddredd = findViewById(R.id.shopAddress);
        saveButton = findViewById(R.id.saveButton);
        bluetoothName = findViewById(R.id.bluetoothName);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        shopName.setText(sharedPreferences.getString("SHOP_NAME", null));
        shopAddredd.setText(sharedPreferences.getString("SHOP_ADDRESS",null));
        bluetoothName.setText(sharedPreferences.getString("BLUETOOTH_NAME",null));


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void saveData() {
        if (checkData()) {
            editor.putString("SHOP_NAME", name);
            editor.putString("SHOP_ADDRESS", address);
            editor.putString("BLUETOOTH_NAME", bluetooth);
            editor.apply();
            Toast.makeText(SettingActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean checkData() {
        name = shopName.getText().toString().trim();
        address = shopAddredd.getText().toString().trim();
        bluetooth = bluetoothName.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(bluetooth)) {
            Toast.makeText(SettingActivity.this, "Field is empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
