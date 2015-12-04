package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by bigfire on 11/16/2015.
 */



@Table(name = "ThirdPartyIntegration",id="_id")
public class ThirdPartyIntegration extends Model {

    @Column(name = "customer_id")
    private String customer_id ;

    @Column(name = "id")
    private String id ;
    @Column(name = "name")
    private String name ;
    @Column(name = "number")
    private String number ;


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }



    public static ThirdPartyIntegration getThirdPartyIntegration(String customer_id){


        return new Select().from(ThirdPartyIntegration.class).where("customer_id = ?",customer_id).executeSingle();


    }

    public static ThirdPartyIntegration getThirdPartyIntegration(String customer_id,String number){


        return new Select().from(ThirdPartyIntegration.class).where("customer_id = ?", customer_id).and("number = ?", number).executeSingle();


    }


    public static ThirdPartyIntegration getThirdPartyIntegrationByNumber(String number){


        return new Select().from(ThirdPartyIntegration.class).where("number = ?",number).executeSingle();


    }
}
