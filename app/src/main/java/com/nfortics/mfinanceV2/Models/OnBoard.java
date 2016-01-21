package com.nfortics.mfinanceV2.Models;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by bigfire on 1/12/2016.
 */
public class OnBoard implements Serializable {
    private static final long serialVersionUID = 12L;
    private String localID="";
    private Map<String,String> textData=new TreeMap<>();
    private Map<String,String> base64data=new TreeMap<>();


    public OnBoard(String localID, Map<String, String> textData, Map<String, String> base64data) {
        this.localID = localID;
        this.textData = textData;
        this.base64data = base64data;
    }


    public String getLocalID() {
        return localID;
    }

    public void setLocalID(String localID) {
        this.localID = localID;
    }

    public Map<String, String> getTextData() {
        return textData;
    }

    public void setTextData(Map<String,String> textData) {
        this.textData = textData;
    }

    public Map<String, String> getBase64data() {
        return base64data;
    }

    public void setBase64data(Map<String, String> base64data) {
        this.base64data = base64data;
    }
}
