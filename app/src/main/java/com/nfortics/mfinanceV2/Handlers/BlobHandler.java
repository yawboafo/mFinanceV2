package com.nfortics.mfinanceV2.Handlers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Message;

import com.nfortics.mfinanceV2.Handlers.BaseHandler;

/**
 * Created by bigfire on 11/17/2015.
 */
public abstract class BlobHandler extends BaseHandler {

    public BlobHandler(Context context) {
        super(context);
    }

    @Override protected void processErrors(Message message) throws Exception {
    }

    @Override protected ProgressDialog createProgressDialog() {
        return null;
    }
}
