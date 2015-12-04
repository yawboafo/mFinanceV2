package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by bigfire on 11/19/2015.
 */

@Table(name = "Collection")
public class Collection  extends Model{

    @Column(name = "Latitude")
    private static double Latitude ;


    @Column(name = "Longitude")
    private static double Longitude ;


    @Column(name = "cummulativeAmount")
    private double cummulativeAmount;


    @Column(name = "agent_msisdn")
    private String agent_msisdn ;


    @Column(name = "agent_name")
    private String agent_name;


    @Column(name = "type")
    private String type ;


    @Column(name = "number")
    private String number ;


    @Column(name = "name")
    private String name ;

    @Column(name = "amount")
    private String amount ;

    @Column(name = "account_code")
    private String account_code ;

    @Column(name = "merchant_name")
    private String merchant_name ;

    @Column(name = "signature")
    private String signature ;

    @Column(name = "transaction_type")
    private String transaction_type ;

    @Column(name = "datetime")
    private String datetime;

    @Column(name = "datetimeStamp")
    private String datetimeStamp;

    @Column(name = "amountCollected")
    private double amountCollected;

    @Column(name = "amountTobeCollected")
    private double amountTobeCollected;


    @Column(name = "isSynced")
    private String isSynced;


    @Column(name = "isComplete")
    private String isComplete;


    public String getDatetimeStamp() {
        return datetimeStamp;
    }

    public void setDatetimeStamp(String datetimeStamp) {
        this.datetimeStamp = datetimeStamp;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public static double getLatitude() {
        return Latitude;
    }

    public static void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public static double getLongitude() {
        return Longitude;
    }

    public static void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getCummulativeAmount() {
        return cummulativeAmount;
    }

    public void setCummulativeAmount(double cummulativeAmount) {
        this.cummulativeAmount = cummulativeAmount;
    }

    public String getAgent_msisdn() {
        return agent_msisdn;
    }

    public void setAgent_msisdn(String agent_msisdn) {
        this.agent_msisdn = agent_msisdn;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {

        String amt = "";
        if (amount.indexOf(".") != -1) {
            String parts = amount.substring(amount.indexOf(".") + 1);
            Utils.LogInfo(getClass().getName(), ">>> INFO decimal part of amount is " + parts);
            if (parts.length() == 1) {
                amount = amount + "0";
            }
            amt = amount;
        } else {
            amt = amount + ".00";
        }
        this.amount = amt;
    }

    public String getAccount_code() {
        return account_code;
    }

    public void setAccount_code(String account_code) {
        this.account_code = account_code;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public double getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(double amountCollected) {
        this.amountCollected = amountCollected;
    }

    public double getAmountTobeCollected() {
        return amountTobeCollected;
    }

    public void setAmountTobeCollected(double amountTobeCollected) {
        this.amountTobeCollected = amountTobeCollected;
    }


    public static List<Collection> getTodayCollections(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        return new Select().from(Collection.class).where("datetime = ?", formattedDate).execute();


    }

    public static List<Collection> getTodayCollectionsBySyncStatus(String status){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        return new Select().from(Collection.class).where("datetime = ?", formattedDate).and("isSynced",status).execute();


    }
}
