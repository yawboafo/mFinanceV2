package com.nfortics.mfinanceV2.Settings;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.Serializable;

/**
 * Created by bigfire on 11/13/2015.
 */
public class AppInstanceID implements Serializable {

    private static final long serialVersionUID = 789937338575101879L;

    private static boolean synced = false;
    private static String appID = "";



    public static String getDeviceId(Context ctx) {
        TelephonyManager mngr = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);

        return mngr.getDeviceId();
    }
}
