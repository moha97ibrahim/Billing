package com.example.billing.addFoodDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BillDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "foodlist.db";

    private static int DATABASE_VERSION = 1;


    public BillDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_FOODLIST_TABLE = "CREATE TABLE " + BillContract.addFood.TABLE_NAME + "("
                + BillContract.addFood._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BillContract.addFood.COLUMN_FOOD_NAME + " TEXT,"
                + BillContract.addFood.COLUMN_FOOD_INGREDIENTS + " TEXT,"
                + BillContract.addFood.COLUMN_FOOD_PRICE + " INTEGER);";
        String SQL_CREATE_CARTLIST_TABLE = "CREATE TABLE " + BillContract.addFood.TABLE_NAME_CART + "("
                + BillContract.addFood._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BillContract.addFood.COLUMN_FOOD_ID_CART + " INTEGER,"
                + BillContract.addFood.COLUMN_FOOD_NAME_CART + " TEXT,"
                + BillContract.addFood.COLUMN_FOOD_QUANTITY_CART + " IEXT DEFAULT '1' ,"
                + BillContract.addFood.COLUMN_FOOD_PRICE_CART + " INTEGER);";


        sqLiteDatabase.execSQL(SQL_CREATE_FOODLIST_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CARTLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public ArrayList<String> isIdExist(int id, SQLiteDatabase database) {
        boolean EXIST;
        String ids[] = {String.valueOf(id)};
        String query = " SELECT * FROM cart";
        ArrayList<String> arrayList = new ArrayList<>();
        database = getReadableDatabase();
        Cursor get = database.rawQuery(query, null);
        get.moveToFirst();
        while (get.isAfterLast() == false) {
            arrayList.add(get.getString(get.getColumnIndex(BillContract.addFood.COLUMN_FOOD_ID_CART)));
            get.moveToNext();
        }
        return arrayList;
    }

    public String getName(int id) {
        ArrayList<String> dataset = new ArrayList<>();
        String query = "SELECT * FROM food WHERE " + BillContract.addFood._ID + "=" + id;
        SQLiteDatabase database = getReadableDatabase();
        Cursor get = database.rawQuery(query, null);
        String name = "";
        if (get.getCount() > 0) {
            get.moveToFirst();
            name = get.getString(get.getColumnIndex("food_name"));
        }
        return name;
    }

    public String getprice(int id) {
        ArrayList<String> dataset = new ArrayList<>();
        String query = "SELECT * FROM food WHERE " + BillContract.addFood._ID + "=" + id;
        SQLiteDatabase database = getReadableDatabase();
        Cursor get = database.rawQuery(query, null);
        String name = "";
        if (get.getCount() > 0) {
            get.moveToFirst();
            name = get.getString(get.getColumnIndex("food_price"));
        }
        return name;
    }

    public void updateQuantity(int id, int quantity) {
        SQLiteDatabase database = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(BillContract.addFood.COLUMN_FOOD_QUANTITY_CART, quantity);
        String query = BillContract.addFood._ID_CART + " = " + id;
        database.update(BillContract.addFood.TABLE_NAME_CART, values, query, null);
    }

    public int getQuantity(int id) {
        SQLiteDatabase database = getReadableDatabase();
        String query = " SELECT "
                + BillContract.addFood.COLUMN_FOOD_QUANTITY_CART + " FROM "
                + BillContract.addFood.TABLE_NAME_CART + " WHERE "
                + BillContract.addFood._ID_CART + " = " + id;
        Cursor get = database.rawQuery(query, null);
        int quantity = 0;
        if (get.getCount() > 0) {
            get.moveToFirst();
            quantity = get.getInt(get.getColumnIndex(BillContract.addFood.COLUMN_FOOD_QUANTITY_CART));
        }
        return quantity;

    }

    public boolean isExist(int id) {
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM cart WHERE " + BillContract.addFood.COLUMN_FOOD_ID_CART + "=" + id;
        Cursor get = database.rawQuery(query, null);


        if (get.getCount() > 0) {
            get.moveToFirst();
            return true;
        } else {
            return false;
        }

    }


}
