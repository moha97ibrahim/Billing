package com.example.billing.addFoodDB;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BillContract {
    private BillContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.billing";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCT = "food";

    public static final String PATH_PRODUCT_CART = "cart";

    public static final class addFood implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);

        public static final Uri CONTENT_URI_CART = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT_CART);



        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        public static final String CONTENT_LIST_TYPE_CART =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT_CART;



        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        public static final String CONTENT_ITEM_TYPE_CART =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT_CART;


        public final static String TABLE_NAME = "food";


        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_FOOD_NAME = "food_name";

        public final static String COLUMN_FOOD_INGREDIENTS = "food_ingredients";

        public final static String COLUMN_FOOD_PRICE = "food_price";


        public final static String TABLE_NAME_CART = "cart";

        public final static String _ID_CART = BaseColumns._ID;

        public final static String COLUMN_FOOD_ID_CART = "food_id";

        public final static String COLUMN_FOOD_NAME_CART = "food_name";

        public final static String COLUMN_FOOD_QUANTITY_CART = "food_quantity";

        public final static String COLUMN_FOOD_PRICE_CART = "food_price";


    }

}
