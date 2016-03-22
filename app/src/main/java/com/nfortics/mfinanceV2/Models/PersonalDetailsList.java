package com.nfortics.mfinanceV2.Models;

/**
 * Created by bigfire on 2/13/2016.
 */
public class PersonalDetailsList {

    int resourceID;
    String title;
    String description;


    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
