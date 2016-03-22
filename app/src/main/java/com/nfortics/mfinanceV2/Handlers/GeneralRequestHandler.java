package com.nfortics.mfinanceV2.Handlers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Message;

import com.nfortics.mfinanceV2.Handlers.BaseHandler;

/**
 * Created by bigfire on 11/16/2015.
 */

public class GeneralRequestHandler extends BaseHandler {

    String message = "";
    Context context;

    public GeneralRequestHandler(Context context, String message) {
        super(context);
        this.context = context;
        this.message = message;
    }

    @Override
    protected ProgressDialog createProgressDialog() {
        return ProgressDialog.show(context, null, message+"...", true, false);
    }

    @Override
    protected void processMessage(Message message) {
    }

    public Context getContext(){
        return this.context;
    }

}
