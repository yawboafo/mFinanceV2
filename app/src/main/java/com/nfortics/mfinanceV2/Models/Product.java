package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by bigfire on 11/14/2015.
 */
@Table(name = "Product")
public class Product extends Model {


    @Column(name = "merchant_id")
    public String merchant_id ;

    @Column(name = "merchant_code")
    public String merchant_code ;

    @Column(name = "productName")
    public String productName ;
    @Column(name = "productCode")
    public String productCode ;


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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


    public static Product getMerchantProduct(String name,String code,String merchant_code){


        return new Select().from(Product.class).where("productName = ?",name).and("productCode = ?",code).and("merchant_code = ?",merchant_code).executeSingle();


    }
}
