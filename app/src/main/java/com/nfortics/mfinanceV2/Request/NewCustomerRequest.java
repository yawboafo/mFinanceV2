package com.nfortics.mfinanceV2.Request;

import android.util.Log;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Merchant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bigfire on 12/22/2015.
 */
public class NewCustomerRequest implements Request {


    private ArrayList<NameValuePair> nameValuePairs;
    private static final String BASE_URL = Application.serverURL2 + "customers.json";
    private String url;

    public NewCustomerRequest(Map<String,String> hashMap) {
        nameValuePairs = new ArrayList<NameValuePair>();
        buildURL(hashMap);
    }
    @Override
    public String getURL() {
        return null;
    }

    private void buildURL(Map<String,String> hashMap) {
        StringBuilder urlBuffer = new StringBuilder(BASE_URL);
        buildNameValuePairs(hashMap);

        url = urlBuffer.toString();
    }

    public ArrayList<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    private void buildNameValuePairs(Map<String,String> hashMap) {

     //   Log.d("oxinbo","<<<< HEAD OF PARAMS TO SEND >>>>");
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();
            nameValuePairs.add(new BasicNameValuePair(key, value));

          //  Log.d("oxinbo","Key = "+key+"value = "+value);

        }
        nameValuePairs.add(new BasicNameValuePair("agent_msisdn", Application.getActiveAgent().getMsisdn()));
        nameValuePairs.add(new BasicNameValuePair("account_code", Merchant.getActiveMerchant("true").getCode()));


        if(Application.basse64Images.size()>3){
            nameValuePairs.add(new BasicNameValuePair("complete", "false"));
        }else{

            nameValuePairs.add(new BasicNameValuePair("complete", "true"));
        }


    }
}
