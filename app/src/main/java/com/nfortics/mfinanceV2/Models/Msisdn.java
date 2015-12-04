package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by bigfire on 11/16/2015.
 */

@Table(name = "Msisdn")
public class Msisdn  extends Model{



    @Column(name = "customer_id")
    private String customer_id ;
    @Column(name = "number")
    private String number;
    @Column(name = "type")
    private String type;


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }



    public static Msisdn getMsisdn(String customer_id){


        return new Select().from(Msisdn.class).where("customer_id = ?",customer_id).executeSingle();


    }

    public static Msisdn getMsisdn(String customer_id,String number){


        return new Select().from(Msisdn.class).where("customer_id = ?", customer_id).and("number = ?", number).executeSingle();


    }

    public static Msisdn getMsisdnByNumber(String number){


        return new Select().from(Msisdn.class).where("number = ?", number).executeSingle();


    }
}
