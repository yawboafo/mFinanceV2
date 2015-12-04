package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigfire on 11/1/2015.
 */

@Table(name = "Merchant")
public class Merchant extends Model {

    @Column(name = "userID")
    private String userID;

    @Column(name = "Agent_msisdn")
    private String Agent_msisdn;


    @Column(name = "isActive")
    private String isActive;


    @Column(name = "merchant_id")
    private String merchant_id = "";

    @Column(name = "username")
    private String username = "";

    @Column(name = "name")
    private String name = "";

    @Column(name = "code")
    private String code = "";

    @Column(name = "amount_limit")
    private Double amount_limit ;

    @Column(name = "currency")
    private String currency = "";

    @Column(name = "logo_url")
    private String logo_url = "";

    @Column(name = "password")
    private String password = "";

    @Column(name = "msisdn")
    private String msisdn = "";

    @Column(name = "email")
    private String email = "";


    @Column(name = "blob_id")
    private String blob_id = "";

    @Column(name = "signature")
    private String signature = "";

    @Column(name = "depositMsgTemplate")
    private String depositMsgTemplate = "";

    @Column(name = "serviceProvider_id")
    private String serviceProvider_id;

    @Column(name = "branches_id")
    private String branches_id;


    @Column(name = "projects_id")
    private String  projects_id;

    @Column(name = "setReceipTemplate")
    private String setReceipTemplate;


    public String getName() {
        return name;
    }


    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getAmount_limit() {
        return amount_limit;
    }

    public void setAmount_limit(Double amount_limit) {
        this.amount_limit = amount_limit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBlob_id() {
        return blob_id;
    }

    public void setBlob_id(String blob_id) {
        this.blob_id = blob_id;
    }

    public String getDepositMsgTemplate() {
        return depositMsgTemplate;
    }

    public void setDepositMsgTemplate(String depositMsgTemplate) {
        this.depositMsgTemplate = depositMsgTemplate;
    }

    public String getServiceProvider_id() {
        return serviceProvider_id;
    }

    public void setServiceProvider_id(String serviceProvider_id) {
        this.serviceProvider_id = serviceProvider_id;
    }

    public String getBranches_id() {
        return branches_id;
    }

    public void setBranches_id(String branches_id) {
        this.branches_id = branches_id;
    }

    public String getProjects_id() {
        return projects_id;
    }

    public void setProjects_id(String projects_id) {
        this.projects_id = projects_id;
    }

    public String getSetReceipTemplate() {
        return setReceipTemplate;
    }

    public void setSetReceipTemplate(String setReceipTemplate) {
        this.setReceipTemplate = setReceipTemplate;
    }


    public String getAgent_msisdn() {
        return Agent_msisdn;
    }

    public void setAgent_msisdn(String agent_msisdn) {
        Agent_msisdn = agent_msisdn;
    }

    public  static Merchant getMerchant(String user_id){

        return new Select().from(Merchant.class).where("userID = ?", user_id).executeSingle();


    }



    public static List<Merchant>getAll() {
        return new Select().from(Merchant.class).execute();
    }

    public  static Merchant getMerchantByName(String name){

        return new Select().from(Merchant.class).where("name = ?",name).executeSingle();


    }
    public  static   List<Merchant> getAllMerchant(String user_id){

        return new Select().from(Merchant.class).where("userID = ?", user_id).execute();


    }
    public  static   List<Merchant> getAllMerchantByAgentMsisdn(String msisdn){

        return new Select().from(Merchant.class).where("Agent_msisdn = ?", msisdn).execute();


    }

    public  static Merchant getMerchant(String name,String code){

        return new Select().from(Merchant.class).where("name = ?", name).and("code = ?", code).executeSingle();


    }

    public  static Merchant getMerchant(String name,String code,String agent_msisdn){

        return new Select().from(Merchant.class).where("name = ?", name).and("code = ?", code).and("agent_msisdn = ?", agent_msisdn).executeSingle();


    }


    public  static Merchant getActiveMerchant(String isActive){

        return new Select().from(Merchant.class).where("isActive = ?", isActive).executeSingle();


    }



}
