package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by bigfire on 11/14/2015.
 */


@Table(name = "User")
public class User extends Model {


    @Column(name = "firstName")
    public String firstName;

    @Column(name = "lastName")
    public String lastName;

    @Column(name = "email")
    public String email;

    @Column(name = "pin")
    public String pin;

    @Column(name = "msisdn")
    public String msisdn;

    @Column(name = "blobId")
    public String blobId;

    @Column(name = "base64Bytes")
    public String base64Bytes;

    @Column(name = "merchant_id")
    public String merchant_id;

    // @Column(name = "currentMerchant")
    // public Merchant currentMerchant;
    //  private List<Merchant> merchants = new ArrayList<Merchant>();
    //private List<NetworkProvider> networkProvider = new ArrayList<NetworkProvider>();

    @Column(name = "lastLoginTs")
    public String lastLoginTs;
    //  private User agentDetails = new User();

    //@Column(name = "fingerPrint")
    //public FingerPrintInfo fingerPrint;
    // transient private TopupResponse topupResponse = new TopupResponse();
    // transient private Airtime airtime = new Airtime();
    @Column(name = "appInstanceID")
    public String appInstanceID;
    @Column(name = "branches")
    public String branches;


    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBlobId() {
        return blobId;
    }

    public void setBlobId(String blobId) {
        this.blobId = blobId;
    }

    public String getBase64Bytes() {
        return base64Bytes;
    }

    public void setBase64Bytes(String base64Bytes) {
        this.base64Bytes = base64Bytes;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getLastLoginTs() {
        return lastLoginTs;
    }

    public void setLastLoginTs(String lastLoginTs) {
        this.lastLoginTs = lastLoginTs;
    }

    public String getAppInstanceID() {
        return appInstanceID;
    }

    public void setAppInstanceID(String appInstanceID) {
        this.appInstanceID = appInstanceID;
    }

    public String getBranches() {
        return branches;
    }

    public void setBranches(String branches) {
        this.branches = branches;
    }

    public  User getCurrentUser(){


        return new Select().from(User.class).executeSingle();


    }
    public static User aUser(String msisdn){


        return new Select().from(User.class)
                .where("msisdn = ? ", msisdn)
                .executeSingle();


    }


    public static User aUser(String msisdn,String pin){


        return new Select().from(User.class)
                .where("msisdn = ? ", msisdn)
                .and("pin = ? ", pin)
                .executeSingle();


    }

    public static Merchant userMerchant(String merchant_id){

        return new Select().from(Merchant.class)
                .where("id = ? ", merchant_id)
                .executeSingle();



    }



}
