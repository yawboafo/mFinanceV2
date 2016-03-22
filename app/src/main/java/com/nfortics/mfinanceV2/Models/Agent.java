package com.nfortics.mfinanceV2.Models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by bigfire on 1/25/2016.
 */
public class Agent implements Serializable {
    private static final long serialVersionUID = 1898L;

    private  byte[]   profile_pics;


    public byte[] getProfile_pics() {

        return profile_pics;
    }

    public void setProfile_pics(byte[] profile_pics) {
        this.profile_pics = profile_pics;
    }
}
