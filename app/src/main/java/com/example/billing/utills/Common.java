package com.example.billing.utills;

import android.content.Context;

import com.example.billing.R;

import java.io.File;

public class Common {

    public static String getAppPath(Context context){
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                +File.separator
                + context.getResources().getString(R.string.app_name)
        );
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir.getPath() + File.separator;
    }
}
