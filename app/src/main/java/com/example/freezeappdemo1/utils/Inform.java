package com.example.freezeappdemo1.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Inform {
    public static void error(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
