package com.example.billing.ui.addfood;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class addFoodViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public addFoodViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("No Data");
    }

    public LiveData<String> getText() {
        return mText;
    }
}