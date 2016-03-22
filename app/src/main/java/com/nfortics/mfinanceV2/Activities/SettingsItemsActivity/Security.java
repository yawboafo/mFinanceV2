package com.nfortics.mfinanceV2.Activities.SettingsItemsActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

public class Security extends AppCompatActivity {


    Toolbar toolbar;
    TextView toolbarTitle;
    Typefacer typefacer;

    public String customerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        typefacer=new Typefacer();
        setToolBar("Security");
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
