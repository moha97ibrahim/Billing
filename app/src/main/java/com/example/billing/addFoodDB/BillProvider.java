package com.example.billing.addFoodDB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BillProvider extends ContentProvider {
    public static final String LOG_TAG = BillProvider.class.getSimpleName();

    private static final int BILL = 1000;

    private static final int BILL_ID = 1001;

    private static final int CART = 2000;

    private static final int CART_ID = 2001;

    private static final int SETTING = 3000;

    private static final int SETTING_ID = 3001;

    private static final int DATA = 4000;

    private static final int DATA_ID = 4001;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_PRODUCT, BILL);
        mUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_PRODUCT + "/#", BILL_ID);

        mUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_PRODUCT_CART, CART);
        mUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_PRODUCT_CART + "/#", CART_ID);

        mUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_PRODUCT_SETTING, SETTING);
        mUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_PRODUCT_SETTING + "/#", SETTING_ID);

        mUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_PRODUCT_DATA, DATA);
        mUriMatcher.addURI(BillContract.CONTENT_AUTHORITY, BillContract.PATH_PRODUCT_DATA + "/#", DATA_ID);
    }




    private BillDbHelper billDbHelper;

    @Override
    public boolean onCreate() {
        billDbHelper = new BillDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = billDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = mUriMatcher.match(uri);

        switch (match) {
            case BILL:
                cursor = db.query(BillContract.addFood.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BILL_ID:
                selection = BillContract.addFood._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = db.query(BillContract.addFood.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CART:
                cursor = db.query(BillContract.addFood.TABLE_NAME_CART,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CART_ID:
                selection = BillContract.addFood._ID_CART + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = db.query(BillContract.addFood.TABLE_NAME_CART,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case SETTING:
                cursor = db.query(BillContract.addFood.TABLE_NAME_SETTING,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SETTING_ID:
                selection = BillContract.addFood._ID_SETTING + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = db.query(BillContract.addFood.TABLE_NAME_SETTING,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case DATA:
                cursor = db.query(BillContract.addFood.TABLE_NAME_DATA,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DATA_ID:
                selection = BillContract.addFood._ID_DATA + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = db.query(BillContract.addFood.TABLE_NAME_DATA,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query Unknown Uri " + getType(uri));
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case BILL:
                return BillContract.addFood.CONTENT_LIST_TYPE;
            case BILL_ID:
                return BillContract.addFood.CONTENT_ITEM_TYPE;
            case CART:
                return BillContract.addFood.CONTENT_LIST_TYPE_CART;
            case CART_ID:
                return BillContract.addFood.CONTENT_ITEM_TYPE_CART;
            case SETTING:
                return BillContract.addFood.CONTENT_LIST_TYPE_SETTING;
            case SETTING_ID:
                return BillContract.addFood.CONTENT_ITEM_TYPE_SETTING;
            case DATA:
                return BillContract.addFood.CONTENT_LIST_TYPE_DATA;
            case DATA_ID:
                return BillContract.addFood.CONTENT_ITEM_TYPE_DATA;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri + " with match" + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case BILL:
                return insertData(uri, contentValues);
            case CART:
                return insertData_cart(uri,contentValues);
            case SETTING:
                return insertData_setting(uri,contentValues);
            case DATA:
                return insertData_data(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    private Uri insertData_data(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(BillContract.addFood.COLUMN_DATA_ORDER_DATE);

        if (name == null) {
            throw new IllegalArgumentException("Setting requires name");
        }

        SQLiteDatabase db = billDbHelper.getWritableDatabase();

        long rowsInserted = db.insert(BillContract.addFood.TABLE_NAME_DATA, null, contentValues);

        if (rowsInserted == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, rowsInserted);
    }

    private Uri insertData_setting(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(BillContract.addFood.COLUMN_SETTING_NAME);

        if (name == null) {
            throw new IllegalArgumentException("Setting requires name");
        }

        SQLiteDatabase db = billDbHelper.getWritableDatabase();

        long rowsInserted = db.insert(BillContract.addFood.TABLE_NAME_SETTING, null, contentValues);

        if (rowsInserted == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, rowsInserted);
    }

    private Uri insertData_cart(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(BillContract.addFood.COLUMN_FOOD_NAME_CART);

        if (name == null) {
            throw new IllegalArgumentException("Bill requires name");
        }

        SQLiteDatabase db = billDbHelper.getWritableDatabase();

        long rowsInserted = db.insert(BillContract.addFood.TABLE_NAME_CART, null, contentValues);

        if (rowsInserted == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, rowsInserted);
    }

    private Uri insertData(Uri uri, ContentValues values) {

        String name = values.getAsString(BillContract.addFood.COLUMN_FOOD_NAME);

        if (name == null) {
            throw new IllegalArgumentException("Bill requires name");
        }

        SQLiteDatabase db = billDbHelper.getWritableDatabase();

        long rowsInserted = db.insert(BillContract.addFood.TABLE_NAME, null, values);

        if (rowsInserted == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, rowsInserted);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = billDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case BILL:
                rowsDeleted = db.delete(BillContract.addFood.TABLE_NAME, null, null);
                break;
            case BILL_ID:
                selection = BillContract.addFood._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                rowsDeleted = db.delete(BillContract.addFood.TABLE_NAME, selection, selectionArgs);
                break;
            case CART:
                rowsDeleted = db.delete(BillContract.addFood.TABLE_NAME_CART, null, null);
                break;
            case CART_ID:
                selection = BillContract.addFood._ID_CART + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                rowsDeleted = db.delete(BillContract.addFood.TABLE_NAME_CART, selection, selectionArgs);
                break;
            case SETTING:
                rowsDeleted = db.delete(BillContract.addFood.TABLE_NAME_SETTING, null, null);
                break;
            case SETTING_ID:
                selection = BillContract.addFood._ID_SETTING + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                rowsDeleted = db.delete(BillContract.addFood.TABLE_NAME_SETTING, selection, selectionArgs);
                break;
            case DATA:
                rowsDeleted = db.delete(BillContract.addFood.TABLE_NAME_DATA, null, null);
                break;
            case DATA_ID:
                selection = BillContract.addFood._ID_DATA + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                rowsDeleted = db.delete(BillContract.addFood.TABLE_NAME_DATA, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Setting Error for Uri:" + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case BILL:
                return updateBill(uri, contentValues, selection, selectionArgs);
            case BILL_ID:
                selection = BillContract.addFood._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                return updateBill(uri, contentValues, selection, selectionArgs);
            case CART:
                return updateCart(uri, contentValues, selection, selectionArgs);
            case CART_ID:
                selection = BillContract.addFood._ID_CART + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                return updateCart(uri, contentValues, selection, selectionArgs);
            case SETTING:
                return updateSetting(uri, contentValues, selection, selectionArgs);
            case SETTING_ID:
                selection = BillContract.addFood._ID_SETTING + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                return updateSetting(uri, contentValues, selection, selectionArgs);
            case DATA:
                return updateData(uri, contentValues, selection, selectionArgs);
            case DATA_ID:
                selection = BillContract.addFood._ID_DATA + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                return updateData(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for" + uri);
        }
    }

    private int updateData(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = billDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(BillContract.addFood.TABLE_NAME_DATA, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private int updateSetting(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = billDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(BillContract.addFood.TABLE_NAME_SETTING, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private int updateCart(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = billDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(BillContract.addFood.TABLE_NAME_CART, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    private int updateBill(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = billDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(BillContract.addFood.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

}