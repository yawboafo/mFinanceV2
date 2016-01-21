package com.nfortics.mfinanceV2.Services;


import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.OnBoard;
import com.nfortics.mfinanceV2.Models.OnBoardModel;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.Volley.MultipartRequest;
import com.nfortics.mfinanceV2.Volley.VolleySingleton;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by bigfire on 1/12/2016.
 */
public class OnBoardDataService extends IntentService {

    private static final String BASE_URL = Application.serverURL2 + "customers.json";
    Utils utils=new Utils();
    Typefacer typefacer=new Typefacer();

    public static final String FINGER_PRINT_URL = "http://197.159.131.154/crypto/biometric/enroll";
    static Map<String,String> map=new TreeMap<>();
    List<Map<String,String>> map1=new LinkedList<>();
    Map<String,Map<String,String>> unscyncedMap=new TreeMap<>();
    Map<String,String> hashmap;

    String customerID;
    String actionType;
    String agentPhoneNumber;
    String agentCode;

    OnBoardModel onBoardModel=null;
    OnBoard onBoard=null;
    Customer customer=null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public OnBoardDataService() {
        super("OnBoardDataService");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("oxinbo", "Volley services started");
        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.log("OnBoardDataService service started  ");
        Bundle extras = intent.getExtras();
        if(extras != null){
            customerID= extras.getString("cusID");
            actionType=extras.getString("actionType");




            switch (actionType){

                case "light":


                    Utils.log("light operation from OnBoardDataService stated ");
                    customer=Customer.getCustomer(customerID);
                    Utils.log("customer ID = "+customer.getCustomer_id()+"Local ID = "+customer.getLocal_id());
                    try {
                        onBoardModel=Utils.MapsDb();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    try{
                         onBoard=onBoardModel.getOnBoardMap().get(customer.getLocal_id());
                         onBoardModel=null;
                        Utils.log("onBoard.getTextData().size() = " + onBoard.getTextData().size() + "onBoard.base64().size() = "+onBoard.getBase64data().size());

                        MultipartVolley(onBoard);

                    }catch (Exception e){
                        e.printStackTrace();

                    }


                    break;
            }

        }
    }

    private void sendBroadcast(String value) {
        Intent intent = new Intent("myBroadcastIntent");
        intent.putExtra("response", value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.log("onCreate from OnBoard Data Service called ");


        agentPhoneNumber= User.User().getMsisdn();
        agentCode= Merchant.getActiveMerchant("true").getCode();
    }




    private void MultipartVolley(final OnBoard onBoard){
      //  onBoard.getTextData().put("agent_msisdn", agentPhoneNumber);
      //  onBoard.getTextData().put("account_code", agentCode);




        MultipartRequest multipartRequest = new MultipartRequest(BASE_URL, onBoard.getTextData(), new Response.Listener<String>() {
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


                            if(onBoard.getBase64data().size()>1){

                                Customer customer=Customer.getCustomer(customerID);
                                Customer.CreateCustomer(customerID, customer, "partial");
                                MultipartVolley(customerID, onBoard.getBase64data());

                            }
                            else{

                                Customer.CreateCustomer(customerID, customer, "complete");
                        }

                            sendBroadcast("");
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
    private void MultipartVolley(final String id,Map<String,String>map){


                   if(map==null)map=new TreeMap<>();
        map.put("agent_msisdn", agentPhoneNumber);
        map.put("account_code", agentCode);



        for(Map.Entry<String,String> entry:map.entrySet()){

            Utils.log("\nKey = "+entry.getKey()+"\n Value = "+entry.getValue());

        }

        String BASE_URL = Application.serverURL2 + "customers/";
        StringBuilder urlBuffer = new StringBuilder(BASE_URL);
        urlBuffer.append(id + ".json");
        Log.d("oxinbo", "URL " + urlBuffer);
        MultipartRequest multipartRequest = new MultipartRequest(BASE_URL, map, new Response.Listener<String>() {
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

                            fName = jsonObject.getString("name");
                           // name = jsonObject.getString("name");
                            //Application.activeCustomerId=jsonObject.getString("customer_uid");


                           // message= "Customer Id :"+  Application.activeCustomerId+" Created";

                            Customer customer=Customer.getCustomer(id);
                            customer.setSync_status("complete");
                            customer.save();


                            String msg="";

                            msg=fName+" data has been Successfully updated";
                          // Notify2("OnBoarding", msg);
                            Log.d("oxinbo", "data sync  message =" + msg);

                            sendBroadcast("");
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
}
