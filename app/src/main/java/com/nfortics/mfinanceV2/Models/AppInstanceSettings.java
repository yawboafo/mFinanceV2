package com.nfortics.mfinanceV2.Models;

import java.io.Serializable;

/**
 * Created by bigfire on 1/26/2016.
 */
public class AppInstanceSettings implements Serializable {
    private static final long serialVersionUID = 197969997898L;

    private String ServerMode;
    private  String deviceType;


    public String getServerMode() {
        return ServerMode;
    }

    public void setServerMode(String serverMode) {
        ServerMode = serverMode;
    }


    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
