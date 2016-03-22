package com.nfortics.mfinanceV2.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

public class CustomerDetails extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbarTitle;
    Typefacer typefacer;

    public String customerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        typefacer=new Typefacer();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            customerName=extras.getString("name");

        }
         setToolBar(customerName);
       // getSupportActionBar.set
    }

    private void setToolBar(String ti){
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbarTitle= (TextView) findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(typefacer.squareLight());

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        toolbarTitle.setText(""+ti);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }
}
