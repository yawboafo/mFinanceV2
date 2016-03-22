package com.nfortics.mfinanceV2.Services;

import android.os.Handler;
import android.os.Message;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Request.HTTPRequest;
import com.nfortics.mfinanceV2.Request.Request;
import com.nfortics.mfinanceV2.Service;
import com.nfortics.mfinanceV2.Utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by bigfire on 11/19/2015.
 */
public class CollectionLimitResetService extends Service {

    public CollectionLimitResetService(Handler handler) {
        super(handler);
    }

    @Override
    public void processRequest(Request request) {
        ServiceProcessor processor = new ServiceProcessor(request);
        new Thread(processor).start();
    }

    // public void processPostRequest(FieldCollectionSaveRequest request) {
    // ServiceProcessor processor = new ServiceProcessor(request);
    // new Thread(processor).start();
    // }

    private class ServiceProcessor implements Runnable {

        private final Request request;

        public ServiceProcessor(Request request) {
            this.request = request;
        }

        @Override
        public void run() {
            try {
                HTTPRequest httpRequest = new HTTPRequest();
                InputStream inputStream = httpRequest
                        .processGetRequest(request);

                JSONObject jsonObject = new JSONObject(
                        Utils.convertStreamToString(inputStream));

                int statusCode = Utils.getAPIStatus(jsonObject);

                if (statusCode == 0) {
                    // parseJsonObject(jsonObject);
                }

                Message message = new Message();
                message.arg1 = statusCode;

                handler.sendMessage(message);

            } catch (Exception e) {
                handler.sendMessage(createMessage(Application.ERROR, e));
            }
        }
    }

    private void parseJsonObject(JSONObject jsonObject) {
        try {
            String customerName = jsonObject.getString("name");
            Utils.LogInfo(getClass().getName(), ">>> INFO name of customer is "
                    + customerName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

