package com.nfortics.mfinanceV2.Request;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.nfortics.mfinanceV2.Application.Application;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bigfire on 12/22/2015.
 */
public class UpdateCustomerRequest  implements Request{
    private ArrayList<NameValuePair> nameValuePairs;
    private static final String BASE_URL = Application.serverURL2 + "customers/";
    private String url;

    public UpdateCustomerRequest(Map<String,String> hashMap) {
        nameValuePairs = new ArrayList<NameValuePair>();
        buildURL(hashMap);
    }
    @Override
    public String getURL() {
        return null;
    }

    private void buildURL(Map<String,String> hashMap) {
        StringBuilder urlBuffer = new StringBuilder(BASE_URL);
       urlBuffer.append(Application.activeCustomerId + ".json");
        buildNameValuePairs(hashMap);

        url = urlBuffer.toString();
    }

    public ArrayList<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    private void buildNameValuePairs(Map<String,String> hashMap) {


        for (Map.Entry<String, String> entry : Application.afistemplateList.entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();
            nameValuePairs.add(new BasicNameValuePair(key,value));

        }



        for (Map.Entry<String, String> entry : Application.getCollectionItems().entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();
            nameValuePairs.add(new BasicNameValuePair(key,value));

        }


        for (Map.Entry<String, String> entry : Application.getFingerPrintImages().entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();
            nameValuePairs.add(new BasicNameValuePair(key,value));

        }
       // nameValuePairs.add(new BasicNameValuePair("complete", "true"));

    }
}
