package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by bigfire on 11/1/2015.
 */
@Table(name = "ServiceProvider")
public class ServiceProvider extends Model {


    @Column(name = "merchant_id")
    public String merchant_id = "";


    @Column(name = "merchant_code")
    public String merchant_code ;


    @Column(name = "name")
    public String name ;
    @Column(name = "code")
    public String code ;
    @Column(name = "logo_url")
    public String logo_url;
    @Column(name = "description")
    public String description ;


    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public static ServiceProvider getMerchantServiceProvider(String name,String code,String merchant_code){


        return new Select().from(ServiceProvider.class).where("name = ?",name).and("code = ?",code).and("merchant_code = ?",merchant_code).executeSingle();


    }
}