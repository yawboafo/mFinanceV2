package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by bigfire on 11/1/2015.
 */
@Table(name = "Projects")

public class Projects extends Model {


    @Column(name = "Pid")
    public String Pid ;

    @Column(name = "merchant_id")
    public String merchant_id ;

    @Column(name = "merchant_code")
    public String merchant_code ;


    @Column(name = "project_id")
    public String project_id ;
    @Column(name = "title")
    public String title ;

    @Column(name = "receipt_template")
    public String receipt_template;


    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReceipt_template() {
        return receipt_template;
    }

    public void setReceipt_template(String receipt_template) {
        this.receipt_template = receipt_template;
    }


    public static Projects getMerchantProjects(String project_id,String title,String merchant_code){


        return new Select().from(Projects.class).where("project_id = ?",project_id).and("title = ?",title).and("merchant_code = ?",merchant_code).executeSingle();


    }
}
