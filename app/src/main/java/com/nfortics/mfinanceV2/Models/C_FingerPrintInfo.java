package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by bigfire on 11/16/2015.
 */

@Table(name = "C_FingerPrintInfo")
public class C_FingerPrintInfo extends Model {


    @Column(name = "customer_id")
    private String customer_id ;

    @Column(name = "_index")
    private int _index;
    @Column(name = "imageData")
    private String imageData ;
    @Column(name = "afisTemplate")
    private String afisTemplate ;
    @Column(name = "description")
    private String description;


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAfisTemplate() {
        return afisTemplate;
    }

    public void setAfisTemplate(String afisTemplate) {
        this.afisTemplate = afisTemplate;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public int getIndex() {
        return _index;
    }

    public void setIndex(int index) {
        this._index = index;
    }


    public static C_FingerPrintInfo getC_FingerPrintInfo(String customer_id){


        return new Select().from(C_FingerPrintInfo.class).where("customer_id = ?",customer_id).executeSingle();


    }


    public static C_FingerPrintInfo getC_FingerPrintInfo(String customer_id,String afisTemplate){


        return new Select().from(C_FingerPrintInfo.class).where("customer_id = ?",customer_id).and("afisTemplate = ?",afisTemplate).executeSingle();


    }
}
