package com.nfortics.mfinanceV2.Services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Request.FingerPrintTemplateRequest;
import com.nfortics.mfinanceV2.Request.HTTPRequest;
import com.nfortics.mfinanceV2.Request.NewCustomerRequest;
import com.nfortics.mfinanceV2.Request.Request;
import com.nfortics.mfinanceV2.Request.UpdateCustomerRequest;
import com.nfortics.mfinanceV2.Service;
import com.nfortics.mfinanceV2.Utilities.Utils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bigfire on 12/22/2015.
 */
public class CustomerSaveService extends Service {

    public CustomerSaveService(Handler handler) {
        super(handler);
    }
    @Override
    public void processRequest(Request request) {
        SyncServiceProcessor processor=new SyncServiceProcessor();
        new Thread(processor).start();
    }




    public void processPostRequest(Map<String,String> hashMap) {
        ServiceProcessor processor = new ServiceProcessor(hashMap);
        new Thread(processor).start();
    }
    public void processPostRequest1(Map<String,String> hashMap) {
        UpdateServiceProcessor processor = new UpdateServiceProcessor(hashMap);
        new Thread(processor).start();
    }
    private class ServiceProcessor implements Runnable {

        // private final NewCustomerRequest request;

       private  Map<String,String> hashMap;
        public ServiceProcessor(Map<String,String> hashMap) {
          this.hashMap=hashMap;
        }

        @Override
        public void run() {
            try {

                HTTPRequest httpRequest = new HTTPRequest();
                InputStream inputStream;

                int index = 0;
                // for (FingerPrintInfo fpi : this.fingerPrints) {
                // FingerPrintTemplateRequest request = new
                // FingerPrintTemplateRequest(
                // fpi);
                // inputStream = httpRequest.processPostRequest(request,
                // request.getNameValuePairs());
                // String template = Utils.convertStreamToString(inputStream);
                // // template = template.replace(
                // // "<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                // // template = template.replace(
                // // "<base64Binary xmlns=\"http://tempuri.org/\">", "");
                // // template = template.replace("</base64Binary>", "");
                // fpi.setAfisTemplate(template);
                // Log.i(getClass().getName(),
                // ">>> INFO finger print template is \n"
                // + fpi.getAfisTemplate());
                //
                // fingerPrints.set(index, fpi);
                // index++;
                // }

                // final ArrayList<Merchant> users = new ArrayList<Merchant>();




                NewCustomerRequest request = new NewCustomerRequest(hashMap);
                Log.d("oxinbo","<<<< HEAD OF PARAMS TO SEND >>>>");
                for (Map.Entry<String, String> entry : hashMap.entrySet()) {

                    String key = entry.getKey();
                    String value = entry.getValue();


                    Log.d("oxinbo","Key = "+key+"value = "+value);

                }
                inputStream = httpRequest.processPostRequest(request, request.getNameValuePairs());

                JSONObject jsonObject = new JSONObject(Utils.convertStreamToString(inputStream));

                int statusCode = Utils.getAPIStatus(jsonObject);

                if (statusCode == 0) {
                    parseJsonObject(jsonObject);
                } else if (statusCode == 100) {
                    try {
                        String message = jsonObject.getString("messages");
                        // temporarily storing api message in balance: just to
                        // show
                        // agent what exactly failed at the API
                       // GeneralSettings.getInstance().setCustomerBalance(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Message message = new Message();
                message.arg1 = statusCode;

                // creating a bundle to be sent to handler so it can determine
                // which Request class sent the handler. This is used within the
                // processStatusCode method so appropriate actions can be taken
                // for specific requests
                Bundle bundle = new Bundle();
                Utils.LogInfo(getClass().getName(), ">>>> INFO " + request.getClass().getName());
                bundle.putString("request_class_name", request.getClass().getName());
                message.setData(bundle);

                handler.sendMessage(message);

            } catch (ConnectException e) {

                // creating and setting message arg2 to 0 to tell the handler
                // the
                // processing had a Connection Exception error
                Message msg = new Message();
                msg.arg2 = 740;
                handler.sendMessage(msg);

            } catch (UnknownHostException e) {

                // creating and setting message arg2 to 0 to tell the handler
                // the
                // processing had a UnknownHost Exception error
                Message msg = new Message();
                msg.arg2 = 740;
                handler.sendMessage(msg);

            } catch (Exception e) {
                Message msg = new Message();
                msg.arg2 = 741;
                handler.sendMessage(msg);
                // handler.sendMessage(createMessage(Application.ERROR, e));
            }
        }
    }

    private class SyncServiceProcessor implements Runnable {

        public SyncServiceProcessor() {

        }

        @Override
        public void run() {
            try {
                Looper.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            HTTPRequest httpRequest = new HTTPRequest();
            InputStream inputStream;

            int index = 0;
         for(Map.Entry<String,String> entry: Application.getFingerPrintImages().entrySet()){


             try {

                 FingerPrintTemplateRequest request = new FingerPrintTemplateRequest(entry.getValue(),fingerPrindId(entry.getKey()));
                 inputStream = httpRequest.processPostRequest(request, request.getNameValuePairs());
                 String template = Utils.convertStreamToString(inputStream);




                 Application.afistemplateList.put(fingerPrindId(entry.getKey()),template);
                 Log.i(getClass().getName(), ">>> INFO finger print template is \n" + template);

               //  fingerPrints.set(index, fpi);
                 index++;

             } catch (Exception e) {
                 e.printStackTrace();
             }
         }



        }
    }
    private class UpdateServiceProcessor implements Runnable {

        private final  Map<String,String> map;


        public UpdateServiceProcessor(Map<String,String> map) {
            this.map = map;
            //this.fingerPrints = fingerPrints;
        }

        @Override
        public void run() {
            try {
                HTTPRequest httpRequest = new HTTPRequest();
                InputStream inputStream;

                int index = 0;

                UpdateCustomerRequest request = new UpdateCustomerRequest(map);
                inputStream = httpRequest.processPostRequest(request, request.getNameValuePairs());
                JSONObject jsonObject = new JSONObject(Utils.convertStreamToString(inputStream));

                int statusCode = Utils.getAPIStatus(jsonObject);

                if (statusCode == 0) {
                    parseJsonObject(jsonObject);
                } else if (statusCode == 100) {
                    try {
                        String message = jsonObject.getString("messages");
                        // temporarily storing api message in balance: just to
                        // show
                        // agent what exactly failed at the API

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Message message = new Message();
                message.arg1 = statusCode;

                // creating a bundle to be sent to handler so it can determine
                // which Request class sent the handler. This is used within the
                // processStatusCode method so appropriate actions can be taken
                // for specific requests
                Bundle bundle = new Bundle();
                Utils.LogInfo(getClass().getName(), ">>>> INFO " + request.getClass().getName());
                bundle.putString("request_class_name", request.getClass().getName());
                message.setData(bundle);

                handler.sendMessage(message);

            } catch (ConnectException e) {

                // creating and setting message arg2 to 0 to tell the handler
                // the
                // processing had a Connection Exception error
                Message msg = new Message();
                msg.arg2 = 740;
                handler.sendMessage(msg);

            } catch (UnknownHostException e) {

                // creating and setting message arg2 to 0 to tell the handler
                // the
                // processing had a UnknownHost Exception error
                Message msg = new Message();
                msg.arg2 = 740;
                handler.sendMessage(msg);

            } catch (Exception e) {
                Message msg = new Message();
                msg.arg2 = 741;
                handler.sendMessage(msg);
                // handler.sendMessage(createMessage(Application.ERROR, e));
            }
        }
    }

/**
 *  nameValuePairs.add(new BasicNameValuePair("right_thumb", base64Value));
 nameValuePairs.add(new BasicNameValuePair("right_thumb_id", md5Hash));


 // nameValuePairs.add(new BasicNameValuePair("left_thumb", base64Value));
 // nameValuePairs.add(new BasicNameValuePair("left_thumb_id", md5Hash));

 } else if (fgi.getIndex() == 1) {

 nameValuePairs.add(new BasicNameValuePair("right_index", base64Value));
 nameValuePairs.add(new BasicNameValuePair("right_index_id", md5Hash));
 //
 //  nameValuePairs.add(new BasicNameValuePair("left_index", base64Value));
 // nameValuePairs.add(new BasicNameValuePair("left_index_id", md5Hash));

 } else if (fgi.getIndex() == 2) {

 nameValuePairs.add(new BasicNameValuePair("right_middle", base64Value));
 nameValuePairs.add(new BasicNameValuePair("right_middle_id", md5Hash));

 // nameValuePairs.add(new BasicNameValuePair("left_middle", base64Value));
 // nameValuePairs.add(new BasicNameValuePair("left_middle_id", md5Hash));

 } else if (fgi.getIndex() == 3) {

 nameValuePairs.add(new BasicNameValuePair("right_ring", base64Value));
 nameValuePairs.add(new BasicNameValuePair("right_ring_id", md5Hash));
 *
 * **/

    String fingerPrindId(String value){
        String value2Return="";
        switch (value){

            case "right_thumb":

                value2Return="right_thumb_id";

                break;
            case "right_index":

                value2Return="right_index_id";

                break;
            case "right_middle":

                value2Return="right_middle_id";

                break;
            case "right_ring":

                value2Return="right_ring_id";

                break;
            case "right_Baby":

                value2Return="right_Baby_id";

                break;

            case "left_thumb":

                value2Return="left_thumb_id";

                break;
            case "left_index":

                value2Return="left_index_id";

                break;
            case "left_middle":

                value2Return="left_middle_id";

                break;
            case "left_ring":

                value2Return="left_ring_id";

                break;
            case "left_Baby":

                value2Return="left_Baby_id";

                break;
        }


        return value2Return;
    }
    private void parseJsonObject(JSONObject jsonObject) {
        try {
            String customerName = jsonObject.getString("fullname");
            String customerId = jsonObject.getString("customer_uid");
            String msisdn = jsonObject.getString("msisdn");

            Utils.LogInfo(getClass().getName(), ">>> INFO new customer details " + customerName + " " + customerId + " " + msisdn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
