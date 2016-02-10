package com.nfortics.mfinanceV2.Activities.SettingsItemsActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

public class PersonalDetails extends AppCompatActivity {


    Toolbar toolbar;
    TextView toolbarTitle;
    Typefacer typefacer;

    public String customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details2);


        typefacer=new Typefacer();
        setToolBar("Personal Details");

    }


    private void setToolBar(String ti){
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbarTitle= (TextView) findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(typefacer.squareLight());

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        toolbarTitle.setText("" + ti);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }

}
