package com.nfortics.mfinanceV2.Adapters;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.provider.SyncStateContract.Constants;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
import com.nfortics.mfinanceV2.Activities.CustomerActivity;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.Volley.MultipartRequest;
import com.nfortics.mfinanceV2.Volley.VolleySingleton;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by bigfire on 1/11/2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    private static final String BASE_URL = Application.serverURL2 + "customers.json";
    Utils utils=new Utils();
    Typefacer typefacer=new Typefacer();
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    public static final String FINGER_PRINT_URL = "http://197.159.131.154/crypto/biometric/enroll";
    static Map<String,String> map=new TreeMap<>();
    List<Map<String,String>> map1=new LinkedList<>();
    Map<String,Map<String,String>> unscyncedMap=new TreeMap<>();
    Map<String,String> hashmap;

    String customerID;
    String actionType;
    String agentPhoneNumber;
    String agentCode;
    Account account;

    Context context;
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        this.context=context;
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

         account = new Account( "mfinance", Constants.ACCOUNT_TYPE);
        AccountManager am = AccountManager.get(context);
        Bundle bundle= new Bundle();
        bundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(account, context.getApplicationContext().getPackageName(), bundle);
       // mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */

        this.context=context;
        mContentResolver = context.getContentResolver();

    }

    private void MultipartVolleyHeavy(Map<String,String> hasmap){
        Log.d("oxinbo", "Started Heavy Multipart Volley");
        //  RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();
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
                                   // Notify2("OnBoarding", msg);
                                    sendBroadcast("");
                                    Log.d("oxinbo","Utils.unscyncedMapFromFile().size()"+ Utils.unscyncedMapFromFile().size());
                                }else {
                                    //Notify3("OnBoarding", "Failed");
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



                        //Notify3("OnBoarding", "Failed");
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
        requestQueue.add(multipartRequest);




    }
    void miniSaveCustomer(String value){


        try{

            Customer customer=Customer.getCustomer(value);
            customer.setSync_status("complete");
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
    void CreateCustomer(String cusID,Customer customer,String sts){



        String first_name="";
        String last_name="";
        String other_names="";
        String gender="";
        String title="";
        String id_type="";
        String id_value="";
        String dob="";
        String address1="";
        String address2="";
        String address3="";
        String phone_number="";





        try{


            customer.setCustomer_id(cusID);


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
            customer.setSync_status(sts);
        }catch (Exception e){}


        //   customer.setSync_status(synsts);


        ActiveAndroid.beginTransaction();
        try{


            Long ID=  customer.save();

            Log.d("oxinbo","Inserted into DB = "+ID+" with cusID " +cusID);
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }



    }





    private void sendBroadcast(String value) {
        Intent intent = new Intent("myBroadcastIntent");
        intent.putExtra("response", value);
        LocalBroadcastManager.getInstance(Application.getAppContext()).sendBroadcast(intent);
    }
}
