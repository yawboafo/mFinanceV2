package com.nfortics.mfinanceV2.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by bigfire on 11/24/2015.
 */

@Table(name = "Favorites")
public class Favorites extends Model {

    @Column(name = "iconLocation")
    private String iconLocation ;

    @Column(name = "classname")
    private String classname;


    public String getIconLocation() {
        return iconLocation;
    }

    public void setIconLocation(String iconLocation) {
        this.iconLocation = iconLocation;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }


    public static Favorites getFavorites(String classname){


        return new Select().from(Favorites.class).where("classname = ?", classname).executeSingle();


    }

    public static List<Favorites> getAllFavorites(){


        return new Select().from(Favorites.class).execute();


    }
}
