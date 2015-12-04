package com.nfortics.mfinanceV2.Settings;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.nfortics.mfinanceV2.Models.Account;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.FingerPrintInfo;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.SyncItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bigfire on 10/29/2015.
 */
public class GeneralSettings  extends Settings {

    private static final long serialVersionUID = 6073972532204136958L;

    private static GeneralSettings instance;
    private String getCurrentImageTag;
    private List<SyncItem> syncItems = new ArrayList<SyncItem>();
    private List<Customer> customers = new ArrayList<Customer>();
    private List<Customer> searchedCustomer = new ArrayList<Customer>();
    private List<Customer> foundsearchedCustomer = new ArrayList<Customer>();
    private List<String> searchedItemsNames = new ArrayList<String>();
    private Customer activeCustomer = new Customer();
    private Location currentLocation;
    private Double totalDailyCollection = 0.0;
    private String appMode="Production";
    private String microlog;
    private String ThresholdLevel;

    private List<Customer> fingerCustomers=new ArrayList<Customer>();
    private transient String customerBalance = "";
    private transient String customerStatement = "";
    private transient String customerAmountOwed = "";
    private transient List<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();

    private String activeAgentMsisdn;
    private Merchant activemerchant;
    private transient Customer newCustomer = new Customer();
    private transient OutputStream outStream;
    private transient BluetoothSocket socket;
    private transient String pan="";
    private transient String cardExpiryDate;
    private transient String track2;
    private transient String magCardCode1 = "";
    private transient String magCardCode2 = "";
    private transient String magCardCode3 = "";
    private transient Bitmap fingerPrintBitmap;
    private transient List<Account> searchedAccounts = new ArrayList<Account>();
    private transient Account selectedAccount = new Account();
    private transient String fingerPrintImageData = "";
    private transient String fingerPrintTemplate = "";
    private List<FingerPrintInfo> fingerPrints = new ArrayList<FingerPrintInfo>(10);

    public String getActiveAgentMsisdn() {
        return activeAgentMsisdn;
    }

    public void setActiveAgentMsisdn(String activeAgentMsisdn) {
        this.activeAgentMsisdn = activeAgentMsisdn;
    }

    public String getFingerPrintImageData() {
        if (this.fingerPrintImageData == null)
            this.fingerPrintImageData = "";
        return fingerPrintImageData;
    }

    public Bitmap getFingerPrintBitmap() {
        if (this.fingerPrintBitmap == null) {
            // this.fingerPrintBitmap = Bitmap.;
        }
        return fingerPrintBitmap;
    }


    public Merchant getActivemerchant() {
        return activemerchant;
    }

    public void setActivemerchant(Merchant activemerchant) {
        this.activemerchant = activemerchant;
    }

    public List<FingerPrintInfo> getFingerPrints() {
        return fingerPrints;
    }

    public void setFingerPrints(List<FingerPrintInfo> fingerPrints) {
        this.fingerPrints = fingerPrints;
    }

    public void setFingerPrintImageData(String fingerPrintImageData) {
        this.fingerPrintImageData = fingerPrintImageData;
    }

    public void setFingerPrintBitmap(Bitmap fingerPrintBitmap) {
        this.fingerPrintBitmap = fingerPrintBitmap;
    }

    public String getGetCurrentImageTag() {
        return getCurrentImageTag;
    }

    public void setGetCurrentImageTag(String getCurrentImageTag) {
        this.getCurrentImageTag = getCurrentImageTag;
    }

    public static GeneralSettings getInstance() {
        if (instance == null) {
            throw new RuntimeException("GeneralSettings not initialised");
        }
        return instance;
    }
    public static void initialize(InputStream inputStream) {
        if (instance == null) {
            if (inputStream == null) {
                instance = new GeneralSettings();

            } else {
                try {
                    instance = (GeneralSettings) new ObjectInputStream(
                            inputStream).readObject();
                    Log.i(GeneralSettings.class.getName(),
                            ">>>>>>>>>GeneralSettings initialized.<<<<<<<<");
                } catch (Exception e) {
                    instance = new GeneralSettings();
                    Log.i(GeneralSettings.class.getName(),
                            "Failed to retrieve GeneralSettings from file. Created new instance.",
                            e);
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e(GeneralSettings.class.getName(),
                                "Failed to close input stream.", e);
                    }
                }
            }
        }
    }
}
