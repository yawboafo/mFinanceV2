package com.nfortics.mfinanceV2.Activities.SettingsItemsActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.PersonalDetailsList;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.GPSTracker;
import com.nfortics.mfinanceV2.ViewAdapters.PersonalDetailsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class PersonalDetails extends AppCompatActivity {


    Toolbar toolbar;
    TextView toolbarTitle;
    Typefacer typefacer;
    ListView listView;
    GPSTracker gpsTracker;
    public String customerName;
    public String location="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details2);


        try{

            gpsTracker=new GPSTracker(this);
            location=gpsTracker.getLatitude()+""+gpsTracker.getLongitude();
        }catch (Exception e){e.printStackTrace();}



        typefacer=new Typefacer();
        setToolBar("Personal Details");
        generatListView();

    }
    private void  generatListView(){

        listView =(ListView)findViewById(R.id.aList);
        PersonalDetailsListAdapter menusAdapter=new PersonalDetailsListAdapter(PersonalDetails.this,getData());
        listView.setAdapter(menusAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
    }
    public  List<PersonalDetailsList> getData() {

        String[] Titles={
                "Full Name",
                "Company",
                "Agent Code",
                "Primary Phone Number",
                "E-mail Address",
                "Address",
                "Identification",
                "Agent Location"};



        String[] Desscrip=new String[Titles.length];
        User user=null;
   try{
       user=User.load(User.class,1);
       if (user==null){
           Desscrip[0]="Loading...";
           Desscrip[1]="Loading...";
           Desscrip[2]="Loading...";
           Desscrip[3]="Loading...";
           Desscrip[4]="Loading...";
           Desscrip[5]="Loading...";
           Desscrip[6]="Loading...";
           Desscrip[7]=location;
       }else{
           Desscrip[0]=user.getFirstName().isEmpty()?"Loading...":user.getFirstName();
           Desscrip[1]="Loading...";
           Desscrip[2]="Loading...";
           Desscrip[3]=user.getMsisdn().isEmpty()?"Loading...":user.getMsisdn();
           Desscrip[4]=user.getEmail().isEmpty()?"Loading...":user.getEmail();
           Desscrip[5]="Loading...";
           Desscrip[6]="Loading...";
           Desscrip[7]=location.isEmpty()?"Loading...":location;
       }


          }catch (Exception e){}




        List<PersonalDetailsList> data = new ArrayList<>();

        for(int t=0;t<Titles.length;t++){

            PersonalDetailsList current = new PersonalDetailsList();
            current.setTitle(Titles[t]);
            current.setDescription(Desscrip[t]);

            data.add(current);
        }

        return data;

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
