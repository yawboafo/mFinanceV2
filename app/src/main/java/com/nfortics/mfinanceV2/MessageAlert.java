package com.nfortics.mfinanceV2;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by bigfire on 11/3/2015.
 */
public class MessageAlert {

    public static void showMessage(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}