package com.nfortics.mfinanceV2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.nfortics.mfinanceV2.Request.Request;

import java.io.Serializable;

/**
 * Created by bigfire on 11/16/2015.
 */
public abstract class Service {

    protected Handler handler;

    public Service(Handler handler) {
        this.handler = handler;
    }

    public abstract void processRequest(Request request);

    protected Message createMessage(String messageType, Serializable data) {
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putSerializable(messageType, data);
        message.setData(bundle);
        return message;
    }
}
