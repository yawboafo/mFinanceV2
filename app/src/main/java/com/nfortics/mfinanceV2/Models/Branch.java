package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;

import java.util.List;

/**
 * Created by bigfire on 11/1/2015.
 */

@Table(name = "Branch")
public class Branch extends Model {


    @Column(name = "merchant_id")
    public String merchant_id ;

    @Column(name = "merchant_code")
    public String merchant_code ;

    @Column(name = "name")
    public String name ;
    @Column(name = "branchCode")
    public String branchCode ;
    @Column(name = "main")
    public boolean main ;


    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }


    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public static  Branch getCustomerBranch(String customer_id){

        //Log.d("oxinbo", "customer id from DCUSTOMER CLASX = " + customer_id);
        return new Select().from(Branch.class).where("merchant_id = ?",customer_id).executeSingle();


    }


    public static Branch getMerchantBranch(String name,String code,String merchant_code){


        return new Select().from(Branch.class).where("branchCode = ?",code).and("merchant_code = ?", merchant_code).or("name = ?", name).executeSingle();


    }

    public static List<Branch> getAllMerchantBranch(){


        return new Select().from(Branch.class).execute();


    }
    public static List<Branch> getAllMerchantBranch(String merchant_code){


        return new Select().from(Branch.class).where("merchant_code= ? ",merchant_code).execute();


    }

}
