package com.example.billing.global;

import android.app.Application;

import java.util.ArrayList;

public class CartList extends Application {
    private ArrayList<Integer> cartList;
    private String listCart;

    public ArrayList<Integer> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Integer> cList) {
        this.cartList = cList;
    }

    public String getListCart() {
        return listCart;
    }

    public void setListCart(String listCart) {
        this.listCart = listCart;
    }
}
