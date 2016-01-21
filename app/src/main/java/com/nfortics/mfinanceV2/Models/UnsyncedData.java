package com.nfortics.mfinanceV2.Models;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by bigfire on 1/7/2016.
 */
public class UnsyncedData implements Serializable {

    private static final long serialVersionUID = 1L;
   private Map<String,String> map;
    private static UnsyncedData instance;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public static UnsyncedData getInstance() {
        if (instance == null) {
            throw new RuntimeException("UnsyncedData not initialised");
        }
        return instance;
    }
    public static void initialize(InputStream inputStream) {
        if (instance == null) {
            if (inputStream == null) {
                instance = new UnsyncedData();

            } else {
                try {
                    instance = (UnsyncedData) new ObjectInputStream(inputStream).readObject();
                    Log.i(UnsyncedData.class.getName(), ">>>>>>>>>GeneralSettings initialized.<<<<<<<<");
                } catch (Exception e) {instance = new UnsyncedData();
                    Log.i(UnsyncedData.class.getName(), "Failed to retrieve GeneralSettings from file. Created new instance.", e);
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e(UnsyncedData.class.getName(),
                                "Failed to close input stream.", e);
                    }
                }
            }
        }
    }
}
