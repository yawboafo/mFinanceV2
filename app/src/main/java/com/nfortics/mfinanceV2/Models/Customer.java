package com.nfortics.mfinanceV2.Models;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigfire on 11/3/2015.
 */

@Table(name = "Customer")
public class Customer extends Model {

    @Column(name = "local_id")
    private String local_id;

    @Column(name = "title")
    private String title ;

    @Column(name = "fullname")
    private String fullname ;

    @Column(name = "first_name")
    private String first_name ;

    @Column(name = "surname")
    private String surname ;

    @Column(name = "other_names")
    private String other_names ;

    @Column(name = "account_number")
    private String account_number ;

    @Column(name = "account_numbers")
    private String account_numbers ;

    @Column(name = "mobile_number")
    private String mobile_number ;

    @Column(name = "customer_id")
    private String customer_id ;


    @Column(name = "billCode")
    private String billCode ;

    @Column(name = "photoUrl")
    private String photoUrl ;

    @Column(name = "gender")
    private String gender ;

    @Column(name = "dob")
    private String dob ;

    @Column(name = "houseNumber")
    private String houseNumber ;// house number


    @Column(name = "streetName")
    private String streetName ; // street name


    @Column(name = "city")
    private String city; // city




    @Column(name = "locality")
    private String locality ; // town or city

    @Column(name = "identificationType")
    private String identificationType ;


    @Column(name = "identificationNumber")
    private String identificationNumber ;


    @Column(name = "identificationPhotoUrl")
    private String identificationPhotoUrl ;

    @Column(name = "signatureUrl")
    private String signatureUrl ;

    @Column(name = "biometricId")
    private String biometricId ;

    @Column(name = "electronicCardNumber")
    private String electronicCardNumber;

    @Column(name = "other_info")
    private String other_info ;


    @Column(name = "mobile_number_new")
    private String mobile_number_new ;


    @Column(name = "sync_status")
    private String sync_status ;
//sync_status :
// partial=letters synced/images not synces
// full=everything synced
// first=letters Only;


    public  static   List<ThirdPartyIntegration> getThirdPartyIntegrations(String customer_id){

        return new Select().from(ThirdPartyIntegration.class).where("customer_id = ?", customer_id).execute();


    }



    public  static   List<C_FingerPrintInfo> getFingerPrintInfos(String customer_id){

        return new Select().from(C_FingerPrintInfo.class).where("customer_id = ?", customer_id).execute();


    }



    public  static   List<Account> getAccounts(String customer_id){

        return new Select().from(Account.class).where("customer_id = ?", customer_id).execute();


    }


    public  static   List<C_Branch> getBranches(String customer_id){

        return new Select().from(Account.class).where("customer_id = ?", customer_id).execute();


    }


    public  static   List<C_Branch> getProduct(String customer_id){

        return new Select().from(C_Product.class).where("customer_id = ?", customer_id).execute();


    }


    public  static   List<Msisdn> getMsisdns(String customer_id){

        return new Select().from(Msisdn.class).where("customer_id = ?", customer_id).execute();


    }

    public String getSync_status() {
        return sync_status;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOther_names() {
        return other_names;
    }

    public void setOther_names(String other_names) {
        this.other_names = other_names;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_numbers() {
        return account_numbers;
    }

    public void setAccount_numbers(String account_numbers) {
        this.account_numbers = account_numbers;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getIdentificationPhotoUrl() {
        return identificationPhotoUrl;
    }

    public void setIdentificationPhotoUrl(String identificationPhotoUrl) {
        this.identificationPhotoUrl = identificationPhotoUrl;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public String getBiometricId() {
        return biometricId;
    }

    public void setBiometricId(String biometricId) {
        this.biometricId = biometricId;
    }

    public String getElectronicCardNumber() {
        return electronicCardNumber;
    }

    public void setElectronicCardNumber(String electronicCardNumber) {
        this.electronicCardNumber = electronicCardNumber;
    }

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info;
    }

    public String getMobile_number_new() {
        return mobile_number_new;
    }

    public void setMobile_number_new(String mobile_number_new) {
        this.mobile_number_new = mobile_number_new;
    }


    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public static List<Customer> getAllCustomers(){


        return new Select().from(Customer.class).execute();


    }


    public static List<Customer> getAllCustomers(String syncStat){


        return new Select().from(Customer.class).where("sync_status =?", syncStat).execute();


    }

    public static Customer getCustomerByFullname(String name){


        return new Select().from(Customer.class).where("fullname = ?", name).executeSingle();


    }
    public static Customer getCustomer(String customer_id){


        return new Select().from(Customer.class).where("customer_id = ?", customer_id).executeSingle();


    }
    public static Customer getCustomerByMsisdn(String msisdn){


        return new Select().from(Customer.class).where("mobile_number = ?", msisdn).executeSingle();


    }
    public static List<Customer> getAllCustomersByName(String name){


        return new Select().from(Customer.class).where("first_name LIKE ?", new String[]{'%' + name + '%'}).execute();


    }
    public static List<Customer> getCustomersByAccount(String name){


        return new Select().from(Customer.class).where("first_name LIKE ?", new String[]{'%' + name + '%'}).executeSingle();


    }
    public static void CreateCustomer(String cusID,Customer customer,String sts){



        String first_name="";
        String last_name="";
        String other_names="";
        String gender="";
        String title="";
        String id_type="";
        String id_value="";
        String dob="";
        String address1="";
        String address2="";
        String address3="";
        String phone_number="";





        try{


            customer.setCustomer_id(cusID);


            customer.setFullname(customer.getFullname());
            customer.setFirst_name(customer.getFirst_name());
            customer.setSurname(customer.getSurname());
            customer.setOther_names(customer.getOther_names());
            customer.setGender(customer.getGender());
            customer.setTitle(customer.getTitle());
            customer.setIdentificationType(customer.getIdentificationType());
            customer.setIdentificationNumber(customer.getIdentificationNumber());
            customer.setDob(customer.getDob());
            customer.setHouseNumber(customer.getHouseNumber());
            customer.setStreetName(customer.getStreetName());
            customer.setCity(customer.getCity());
            customer.setMobile_number(customer.getMobile_number());
            customer.setSync_status(sts);
        }catch (Exception e){}


        //   customer.setSync_status(synsts);


        ActiveAndroid.beginTransaction();
        try{


            Long ID=  customer.save();

            android.util.Log.d("oxinbo", "Inserted into DB = " + ID + " with cusID " + cusID);
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }



    }







}
