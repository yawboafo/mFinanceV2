package com.nfortics.mfinanceV2.Services;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Request.Request;
import com.nfortics.mfinanceV2.Service;

import java.util.ArrayList;

/**
 * Created by bigfire on 11/19/2015.
 */
public class SMSDeliveryService extends Service {

    private Message handlerMessage = new Message();

    public SMSDeliveryService(Handler handler) {
        super(handler);
    }

    //0540256631

    @Override
    public void processRequest(Request request) {
        // EnumeratedCommoditiesProcessor processor = new
        // EnumeratedCommoditiesProcessor(request);
        // new Thread(processor).start();
    }

    public void processSMSRequest(Context context, String message, String msisdn) {
        SMSDeliveryProcessor processor = new SMSDeliveryProcessor(context,
                message, msisdn);
        new Thread(processor).start();
    }

    private class SMSDeliveryProcessor implements Runnable {

        private final String message;
        private final String msisdn;
        private final Context context;

        public SMSDeliveryProcessor(Context context, String message,
                                    String msisdn) {
            this.message = message;
            this.msisdn = msisdn;
            this.context = context;
        }

        @Override
        public void run() {
            try {

                Looper.prepare();

                String SENT = "SMS_SENT";
                String DELIVERED = "SMS_DELIVERED";

                PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                        new Intent(SENT), 0);
                PendingIntent deliveredPI = PendingIntent.getBroadcast(context,
                        0, new Intent(DELIVERED), 0);

                context.registerReceiver(createSentBroadcastReceiver(),
                        new IntentFilter(SENT));
                context.registerReceiver(createDeliveredBroadcastReceiver(),
                        new IntentFilter(DELIVERED));

                ArrayList<PendingIntent> sentList = new ArrayList<PendingIntent>();
                sentList.add(sentPI);
                ArrayList<PendingIntent> deliveredList = new ArrayList<PendingIntent>();
                sentList.add(deliveredPI);

                SmsManager sms = SmsManager.getDefault();
                ArrayList<String> parts = sms.divideMessage(message);
                //233209535752
               sms.sendMultipartTextMessage("+233209535752", null, parts, sentList, deliveredList);
               // sms.sendMultipartTextMessage("+233540256631", null, parts, sentList, deliveredList);
            } catch (Exception e) {
                handler.sendMessage(createMessage(Application.ERROR, e));
            }
        }

    }

    private BroadcastReceiver createSentBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        handlerMessage.arg1 = Activity.RESULT_OK;
                        handler.dispatchMessage(handlerMessage);
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        handlerMessage.arg1 = SmsManager.RESULT_ERROR_GENERIC_FAILURE;
                        handler.dispatchMessage(handlerMessage);
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        handlerMessage.arg1 = SmsManager.RESULT_ERROR_NO_SERVICE;
                        handler.dispatchMessage(handlerMessage);
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:

                        handlerMessage.arg1 = SmsManager.RESULT_ERROR_NULL_PDU;
                        handler.dispatchMessage(handlerMessage);
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        handlerMessage.arg1 = SmsManager.RESULT_ERROR_RADIO_OFF;
                        handler.dispatchMessage(handlerMessage);
                        break;

                    default:
                        break;

                }
            }
        };
    }

    private BroadcastReceiver createDeliveredBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        handlerMessage.arg1 = Activity.RESULT_OK;
                        handler.dispatchMessage(handlerMessage);
                        break;

                    case Activity.RESULT_CANCELED:

                        handlerMessage.arg1 = Activity.RESULT_CANCELED;
                        handler.dispatchMessage(handlerMessage);
                        break;

                }
            }
        };
    }

}
