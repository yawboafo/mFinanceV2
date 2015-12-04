package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

/**
 * Created by bigfire on 11/3/2015.
 */

@Table(name = "Account")
public class Account extends Model {



    @Column(name = "customer_id")
    private String customer_id ;

    @Column(name = "account_number")
    private String account_number;
    @Column(name = "account_type")
    private String account_type;
    @Column(name = "number")
    private String number ;
    @Column(name = "description")
    private String description;
    @Column(name = "Isprimary")
    private boolean Isprimary;
    @Column(name = "arrears")
    private String arrears ;


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrimary() {
        return Isprimary;
    }

    public void setPrimary(boolean primary) {
        this.Isprimary = primary;
    }

    public String getArrears() {
        return arrears;
    }

    public void setArrears(String arrears) {
        this.arrears = arrears;
    }









    public static Account getAccount(String customer_id){


        return new Select().from(Account.class).where("customer_id = ?",customer_id).executeSingle();


    }
    public static Account getAccount(String customer_id,String accounr_number){


        return new Select().from(Account.class).where("customer_id = ?", customer_id).and("account_number = ? ", accounr_number).executeSingle();


    }


    public static Account getAccountByAccountNumber(String account_number){


        return new Select().from(Account.class).where("account_number = ? ",account_number).executeSingle();


    }

}
