package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by bigfire on 11/16/2015.
 */

@Table(name = "C_Branch")
public class C_Branch  extends Model{


    @Column(name = "customer_id")
    private String customer_id ;

    @Column(name = "name")
    public String name ;
    @Column(name = "branchCode")
    public String branchCode ;
    @Column(name = "main")
    public boolean main ;


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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



    public static C_Branch getC_Branch(String customer_id){


        return new Select().from(C_Branch.class).where("customer_id = ?",customer_id).executeSingle();


    }

    public static C_Branch getC_Branch(String customer_id,String branchCode){


        return new Select().from(C_Branch.class).where("customer_id = ?",customer_id).and("branchCode = ?",branchCode).executeSingle();


    }
}
