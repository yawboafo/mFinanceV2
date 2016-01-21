package com.nfortics.mfinanceV2.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nfortics.mfinanceV2.Activities.CustomerActivity;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.OnBoard;
import com.nfortics.mfinanceV2.Models.OnBoardModel;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.Volley.MultipartRequest;
import com.nfortics.mfinanceV2.Volley.VolleySingleton;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by bigfire on 1/7/2016.
 */
public class VolleyServices extends IntentService {

    private static final String BASE_URL = Application.serverURL2 + "customers.json";
    Utils utils=new Utils();
    Typefacer typefacer=new Typefacer();

    public static final String FINGER_PRINT_URL = "http://197.159.131.154/crypto/biometric/enroll";
    static Map<String,String>map=new TreeMap<>();
    List<Map<String,String>>map1=new LinkedList<>();
    Map<String,Map<String,String>> unscyncedMap=new TreeMap<>();
    Map<String,String> hashmap;

    String customerID;
    String actionType;
    String agentPhoneNumber;
    String agentCode;
    public VolleyServices() {
        super("VolleyServices");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("oxinbo", "Volley services started");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("oxinbo", "Volley services DESTROYED");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("oxinbo", "Volley services onHandleIntent Called");
        Bundle extras = intent.getExtras();
        if(extras == null) {

        } else {
            customerID= extras.getString("cusID");
            actionType=extras.getString("actionType");
            Log.d("oxinbo","customer ID = "+customerID+"actionType = "+actionType);

            agentPhoneNumber=User.User().getMsisdn();
            agentCode= Merchant.getActiveMerchant("true").getCode();
            Log.d("oxinbo","phone number"+ User.User().getMsisdn());




        }



        switch (actionType)
        {

            case "heavy":

                Log.d("oxinbo","method called is heavy");

                OnBoardModel onBoardModel=null;


                try {
                    onBoardModel=Utils.MapsDb();
                    if(onBoardModel==null)onBoardModel=new OnBoardModel();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try{

                    Customer customer=Customer.getCustomer(customerID);
                    OnBoard onBoard=onBoardModel.getOnBoardMap().get(customer.getLocal_id());
                    MultipartVolleyHeavy(onBoard.getBase64data());
                }catch (Exception e)

                {e.printStackTrace();}

                //for(Map.Entry<String ,Map<String,String>> entry:unscyncedMap.entrySet()){

                // MultipartVolleyHeavy(entry.getKey(),entry.getValue());
                // }

                break;
            case "light":

                Log.d("oxinbo","method called is light");

                Customer customer=Customer.getCustomer(customerID);

                if (customer!=null)
                MultipartVolley(customerID, LoadCustomerToHashMap(customer));
                break;
        }





    }



    void miniSaveCustomer(String value){


        try{

            Customer customer=Customer.getCustomer(value);
            customer.setSync_status("complete");
            customer.save();
        }catch (Exception e){


        }

    }
    void miniSaveCustomer(String value,String syncStat){


        try{

            Customer customer=Customer.getCustomer(value);
            customer.setSync_status(syncStat);
            customer.save();
        }catch (Exception e){


        }

    }
    Map<String,String> LoadCustomerToHashMap(Customer customer){

        Map<String,String>map=new TreeMap<>();
        map.put("first_name",customer.getFirst_name());
        map.put("last_name",customer.getSurname());
        map.put("other_names",customer.getOther_names());
        map.put("gender",customer.getGender());
        map.put("title", customer.getTitle());
        map.put("id_type", customer.getIdentificationType());
        map.put("id_value", customer.getIdentificationNumber());
        map.put("dob", customer.getDob());
        map.put("address1", customer.getHouseNumber());
        map.put("address2", customer.getStreetName());
        map.put("address3", customer.getCity());
        map.put("phone_number", customer.getMobile_number());

        /**
         *    customer.setCustomer_id(cusID);


         customer.setFullname(customer.getFullname());
         customer.setFirst_name(customer.getFirst_name());
         customer.setSurname(customer.getSurname());
         customer.setOther_names(customer.getOther_names());
         customer.setGender(customer.getGender());
         customer.setTitle(customer.getTitle());
         customer.setIdentificationType(customer.getIdentificationType());
         customer.setIdentificationNumber(customer.getIdentificationNumber());
         customer.setDob(customer.getDob());
         customer.setHouseNumber(customer.getHouseNumber());
         customer.setStreetName(customer.getStreetName());
         customer.setCity(customer.getCity());
         customer.setMobile_number(customer.getMobile_number());
         *
         * **/


        return map;
    }

    private void MultipartVolleyHeavy(final String cusID,Map<String,String> hasmap){
        Log.d("oxinbo", "Started Heavy Multipart Volley");

        hasmap.put("agent_msisdn", agentPhoneNumber);
        hasmap.put("account_code", agentCode);


        for(Map.Entry<String,String> entry: Application.afistemplateList.entrySet()){
            hasmap.put(entry.getKey(),entry.getValue());

        }

        // hasmap.put("right_thumb_id","lsjdlsjsds7d97e92ejaoddjo029e0aldjald");

        String BASE_URL = Application.serverURL2 + "customers/";
        StringBuilder urlBuffer = new StringBuilder(BASE_URL);
        urlBuffer.append(cusID + ".json");
        Log.d("oxinbo", "URL " + urlBuffer);
        MultipartRequest multipartRequest =
                new MultipartRequest(urlBuffer.toString(), hasmap, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pDialog.hide();
                        if(response!=null){
                            String message="";
                            String status="";
                            String name="";
                            String customer_id="";
                            String msisdn="";
                            try{
                                JSONObject jsonObject=new JSONObject(response);
                                Log.d("oxinbo","response"+ response);
                                try{
                                    status = jsonObject.getString("status");
                                    //   message = jsonObject.getString("message");
                                }catch (Exception e){}
                                if(status.equals("000")){
                                    name = jsonObject.getString("name");

                                    Customer customer=Customer.getCustomer(cusID);
                                    customer.setSync_status("complete");
                                    customer.save();


                                    String msg="";

                                    msg=name+" data has been Successfully updated";
                                    Notify2("OnBoarding", msg);



                                  Map<String,Map<String,String>> _map=new TreeMap<>();
                                    _map = Utils.unscyncedMapFromFile();

                                    _map.remove(cusID);

                                    Utils.saveHashToFile(_map);

                                }else {
                                    Notify2("OnBoarding", "failed");
                                    message = jsonObject.getString("message");


                                }



                            }catch (Exception e){


                                e.printStackTrace();
                            }



                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // pDialog.hide();
                        // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                        // For AuthFailure, you can re login with user credentials.
                        // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                        // In this case you can check how client is forming the api and debug accordingly.
                        // For ServerError 5xx, you can do retry or handle accordingly.


                        Notify3("OnBoarding", "Failed");
                        Log.d("oxinbo", "" + error);
                        //getLocalHashFromFile();
                        // utils.RetryAlertDialog(Application.getAppContext(), "error", "pls retry");
                        if (error instanceof NetworkError) {

                        } else if (error instanceof ServerError) {

                        } else if (error instanceof AuthFailureError) {

                        } else if (error instanceof ParseError) {

                        } else if (error instanceof NoConnectionError) {

                        } else if (error instanceof TimeoutError) {

                        }
                    }
                });
        int socketTimeout = 999999999;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);

        Application.requestQueue.add(multipartRequest);




    }
    private void MultipartVolley(final String customerID, final Map<String,String> hasmap){



        hasmap.put("agent_msisdn", agentPhoneNumber);
        hasmap.put("account_code", agentCode);

        try {
            if(Utils.unscyncedMapFromFile().get(customerID)!=null){
                hasmap.put("complete", "false");
            }else{
                hasmap.put("complete", "true");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MultipartRequest multipartRequest = new MultipartRequest(BASE_URL, hasmap, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response!=null){
                    Log.d("oxinbo", "response message from  =" + response);
                    String message="";
                    String fName="";
                    JSONObject jsonObject=null;
                    String status="";
                    try{
                        jsonObject=new JSONObject(response);
                        status= jsonObject.getString("status");



                        if(status.equals("000")){

                            fName = jsonObject.getString("fullname");

                            Application.activeCustomerId=jsonObject.getString("customer_uid");


                            message= "Customer Id :"+  Application.activeCustomerId+" Created";

                      /// Toast.makeText(VolleyServices.this, "Success", Toast.LENGTH_LONG).show();
                            // DownloadCustomerBackgroundS(Application.activeCustomerId);

                            if(Utils.unscyncedMapFromFile().get(customerID)!=null){

                                Customer customer=Customer.getCustomer(customerID);
                                Customer.CreateCustomer(customerID,customer,"partial");
                                Thread t = new Thread(new Runnable() {
                                    public void run() {
                                      //  combineMap(Application.activeCustomerId);

                                        MultipartVolleyHeavy(customerID,hasmap);
                                    }
                                });

                                t.start();



                            }else{

                            //    CreateCustomer(Application.activeCustomerId, fName, Application.getCollectionItems(),"complete");
                            }
                         //   finish();

                        }else{


                            try{
                                message = jsonObject.getString("message");
                                if(message==null)
                                    Utils.getMessageFromAPIStatus(Integer.valueOf(status));
                            }catch (Exception e){}

                            Log.d("oxinbo", "response message =" + message);


                           // utils.ErrorAlertDialog(OnBoardCustomerActivity.this, message, "New Error Message ! ").show();

                        }


                    }catch (Exception e){


                        e.printStackTrace();
                    }



                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

              //  pDialog.dismiss();
                // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                // For AuthFailure, you can re login with user credentials.
                // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                // In this case you can check how client is forming the api and debug accordingly.
                // For ServerError 5xx, you can do retry or handle accordingly.
                // utils.RetryAlertDialog(OnBoardCustomerActivity.this, "error", "pls retry");

                //  utils.ErrorAlertDialog(OnBoardCustomerActivity.this, message, " Failed").show();
                if (error instanceof NetworkError) {
                    String msg="Network Error Occurred !";
                  //  RetryAlertDialog(msg);
                } else if (error instanceof ServerError) {
                    String msg="Server Error Occurred !";
                   // RetryAlertDialog(msg);
                } else if (error instanceof AuthFailureError) {
                    String msg="Application Error Occurred !";
                   // RetryAlertDialog(msg);

                } else if (error instanceof ParseError) {


                    String msg="A Parser Error Occurred !";
                 //   RetryAlertDialog(msg);
                } else if (error instanceof NoConnectionError) {


                    String msg="No Internet Connection !";
                 //   RetryAlertDialog(msg);

                } else if (error instanceof TimeoutError) {
                    String msg="Syncing Timed Out !";
                  //  RetryAlertDialog(msg);
                }
            }
        });
        int socketTimeout = 999999999;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        Application.requestQueue.add(multipartRequest);



    }
    private void MultipartVolleyHeavy(Map<String,String> hasmap){
        Log.d("oxinbo", "Started Heavy Multipart Volley");

        hasmap.put("agent_msisdn", agentPhoneNumber);
        hasmap.put("account_code", agentCode);


        for(Map.Entry<String,String> entry: Application.afistemplateList.entrySet()){
            hasmap.put(entry.getKey(),entry.getValue());

        }


//        Log.d("oxinbo","Params  "+hasmap.toString()+"   size  "+hasmap.size());
        // hasmap.put("right_thumb_id","lsjdlsjsds7d97e92ejaoddjo029e0aldjald");

        String BASE_URL = Application.serverURL2 + "customers/";
        StringBuilder urlBuffer = new StringBuilder(BASE_URL);
        urlBuffer.append(customerID + ".json");
        Log.d("oxinbo", "URL " + urlBuffer);
        MultipartRequest multipartRequest =
                new MultipartRequest(urlBuffer.toString(), hasmap, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pDialog.hide();
                        if(response!=null){
                            String message="";
                            String status="";
                            String name="";
                            String customer_id="";
                            String msisdn="";
                            try{
                                JSONObject jsonObject=new JSONObject(response);
                                Log.d("oxinbo", response);
                                try{
                                    status = jsonObject.getString("status");
                                    //   message = jsonObject.getString("message");
                                }catch (Exception e){}



                                if(status.equals("000")){

                                    String id=  customerID;



                                    name = jsonObject.getString("name");
                                    // DownloadCustomerBackgroundS(id);

                                    String msg="";
                                    unscyncedMap.remove(customerID);
                                    miniSaveCustomer(customerID);
                                    Utils.saveHashToFile(unscyncedMap);
                                    msg=name+" data has been Successfully updated";
                                    Notify2("OnBoarding", msg);
                                    sendBroadcast("success");
                                    Log.d("oxinbo","Utils.unscyncedMapFromFile().size()"+ Utils.unscyncedMapFromFile().size());
                                }else {
                                    miniSaveCustomer(customerID,"failed");
                                    sendBroadcast("failed");
                                    Notify3("OnBoarding", "Failed");
                                    //message = jsonObject.getString("message");
                                    // combineMap();

                                }



                            }catch (Exception e){


                                e.printStackTrace();
                            }



                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {



                        miniSaveCustomer(customerID, "failed");
                        sendBroadcast("failed");
                        Notify3("OnBoarding", "Failed");
                        Log.d("oxinbo", "" + error);
                        //getLocalHashFromFile();
                        // utils.RetryAlertDialog(Application.getAppContext(), "error", "pls retry");
                        if (error instanceof NetworkError) {

                        } else if (error instanceof ServerError) {

                        } else if (error instanceof AuthFailureError) {

                        } else if (error instanceof ParseError) {

                        } else if (error instanceof NoConnectionError) {

                        } else if (error instanceof TimeoutError) {

                        }
                    }
                });
        int socketTimeout = 999999999;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        Application.requestQueue.add(multipartRequest);




    }


    public static String serializeWithJSON(Object o, Type genericType) {
        Gson gson = new Gson();
        return gson.toJson(o, genericType);
    }

    void combineMap(){

        for(Map.Entry<String ,String >entry:Application.getCollectionItems().entrySet()){

            map.put(entry.getKey(),entry.getValue());
        }

        Application.setCollectionItems(new HashMap<String, String>());

        for(Map.Entry<String ,String >entry:Application.basse64Images.entrySet()){

            map.put(entry.getKey(),entry.getValue());
        }

          // map1.add(map);

        try{
            unscyncedMap=Utils.unscyncedMapFromFile();
            if(unscyncedMap==null)
                unscyncedMap=new TreeMap<>();

        }catch (Exception w){

        }
        unscyncedMap.put(Application.activeCustomerId,map);
        ClearMemory();



        try {

            //Saved Hashmap to file
            Utils.saveHashToFile(unscyncedMap);
            unscyncedMap=new TreeMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }





        try {

            Log.d("oxinbo", "READING MAP FROM FILE <<<<<<<<<<<>>>>>>....TO SERIALIZE INCAse") ;
            for(Map.Entry<String,Map<String,String>> entry:Utils.unscyncedMapFromFile().entrySet()){
                Log.d("oxinbo", "PRINTING MAP FROM  "+entry.getKey()+"  LOCATION") ;

                for (Map.Entry<String,String>InnerEntry:entry.getValue().entrySet()){

                    Log.d("oxinbo", "Key "+ InnerEntry.getKey()+" Value "+InnerEntry.getValue()) ;

                }
            }
            Log.d("oxinbo","UnsyncedFile Size = "+Utils.unscyncedMapFromFile().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ClearMemory(){


        Application. galleryImages=new HashMap<>();
        Application. fingerPrintImages=new HashMap<>();
        Application. signatureImages=new HashMap<>();


        Application. CollectionItems=new HashMap<>();
        Application.basse64Images=new HashMap<>();
        // Application. activeSignatureLabel;
        //  public static String activePictureLabel;

        Application. listOfKeysChecked=new ArrayList<>();
        //Application. SignatureLabelsList=new ArrayList<>();
        // Application. PictureLabelsList=new ArrayList<>();

        // Application. checkedImageLabels=new HashMap<>();
        // Application. SignatureMap=new ArrayList<>();
        //  Application. PicturesImageMap=new ArrayList<>();
        //  private static List<Map<String,Bitmap>> galleryImageList=new ArrayList<>();
        // private static List<Map<String,Bitmap>> fingerPrintList=new ArrayList<>();
        Application.otherImages= new HashMap<>();
        Application.setCollectionItems(new HashMap<String, String>());
    }
    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(VolleyServices.this.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.d("Exception", "File write suceess: ");
        }
        catch (IOException e) {
            Log.d("Exception", "File write failed: " + e.toString());
        }
    }
    private void Notify(String notificationTitle, String notificationMessage){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")

        Notification notification = new Notification(R.drawable.mfinance_logo_small,"mFinance "+Merchant.getActiveMerchant("true").getCode(), System.currentTimeMillis());
        Intent notificationIntent = new Intent(this,CustomerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);

        notification.setLatestEventInfo(VolleyServices.this, notificationTitle, notificationMessage, pendingIntent);

        notificationManager.notify(9999, notification);
    }
    private void Notify2(String title,String message){

        Intent intent = new Intent(this, CustomerActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle("mFinance "+Merchant.getActiveMerchant("true").getCode())
                .setContentText(message)
                .setSmallIcon(R.drawable.mfinance_logo_small)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);



    }
    private void Notify3(String title,String message){

        Intent intent = new Intent(this, CustomerActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.mfinance_logo_small)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .addAction(R.drawable.ic_close, "Retry", pIntent)
                .addAction(R.drawable.ic_edit, "Clear", pIntent).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);



    }
    private static NotificationCompat.Builder buildNotificationCommon(Context _context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context).setWhen(System.currentTimeMillis());
        //Vibration
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        //LED
        builder.setLights(Color.RED, 3000, 3000);

        //Ton
        //builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

        return builder;
    }

    private void sendBroadcast(String value) {
        Intent intent = new Intent("myBroadcastIntent");
        intent.putExtra("response", value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
