package com.example.billing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.example.billing.addFoodDB.BillDbHelper;
import com.example.billing.ui.About;
import com.example.billing.ui.SettingActivity;
import com.example.billing.ui.insight.InsightActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {


    private BillDbHelper dbHelper;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_foodmenu, R.id.navigation_additem, R.id.navigation_cart)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        updateBadge();

    }

    private void updateBadge() {
        if(getItemCount()>0) {
            navView.showBadge(R.id.navigation_cart).setNumber(getItemCount());
        }
        else {
            navView.removeBadge(R.id.navigation_cart);
        }
        refresh();
    }

    private void refresh() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateBadge();
            }
        };

        handler.postDelayed(runnable,1000);
    }

    private int getItemCount() {
        dbHelper = new BillDbHelper(getApplicationContext());
        int count = dbHelper.getCount();
        return count;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.more_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
                return true;
            case R.id.insight:
                Intent j = new Intent(MainActivity.this, InsightActivity.class);
                startActivity(j);
                return true;
            case R.id.about:
                Intent k = new Intent(MainActivity.this, About.class);
                startActivity(k);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
