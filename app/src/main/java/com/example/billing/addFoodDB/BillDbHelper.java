package com.example.billing.addFoodDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BillDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "foodlist.db";

    private static int DATABASE_VERSION = 1;


    public BillDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_FOODLIST_TABLE = "CREATE TABLE " + BillContract.addFood.TABLE_NAME+ "("
                + BillContract.addFood._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BillContract.addFood.COLUMN_FOOD_NAME + " TEXT,"
                + BillContract.addFood.COLUMN_FOOD_INGREDIENTS + " TEXT,"
                + BillContract.addFood.COLUMN_FOOD_PRICE + " INTEGER);";

        sqLiteDatabase.execSQL(SQL_CREATE_FOODLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
