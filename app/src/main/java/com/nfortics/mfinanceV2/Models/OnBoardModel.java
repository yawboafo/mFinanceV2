package com.nfortics.mfinanceV2.Models;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by bigfire on 1/11/2016.
 */
public class OnBoardModel implements Serializable{
    private static final long serialVersionUID = 1L;

    private static OnBoardModel instance;

    private Map<String,OnBoard> onBoardMap=new TreeMap<>();


    public Map<String, OnBoard> getOnBoardMap() {
        return onBoardMap;
    }

    public void setOnBoardMap(Map<String, OnBoard> onBoardMap) {
        this.onBoardMap = onBoardMap;
    }

    public static OnBoardModel getInstance() {
        if (instance == null) {
            throw new RuntimeException("OnBoardModel not initialised");
        }
        return instance;
    }





    public static void initialize(InputStream inputStream) {
        if (instance == null) {
            if (inputStream == null) {
                instance = new OnBoardModel();

            } else {
                try {
                    instance = (OnBoardModel) new ObjectInputStream(
                            inputStream).readObject();
                    Log.i(OnBoardModel.class.getName(),
                            ">>>>>>>>>GeneralSettings initialized.<<<<<<<<");
                } catch (Exception e) {
                    instance = new OnBoardModel();
                    Log.i(OnBoardModel.class.getName(),
                            "Failed to retrieve GeneralSettings from file. Created new instance.",
                            e);
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e(OnBoardModel.class.getName(),
                                "Failed to close input stream.", e);
                    }
                }
            }
        }
    }





}
