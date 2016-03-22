package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by bigfire on 11/16/2015.
 */

@Table(name = "C_Product")
public class C_Product extends Model{



    @Column(name = "customer_id")
    private String customer_id ;

    @Column(name = "productName")
    public String productName ;
    @Column(name = "productCode")
    public String productCode ;


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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
}
