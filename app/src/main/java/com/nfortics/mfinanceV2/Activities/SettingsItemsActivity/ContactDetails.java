package com.nfortics.mfinanceV2.Activities.SettingsItemsActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nfortics.mfinanceV2.Models.SettingsList;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.ViewAdapters.SettingsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactDetails extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbarTitle;
    Typefacer typefacer;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details2);


        typefacer=new Typefacer();
        setToolBar("Customer Service");
        generatListView();

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

    private void  generatListView(){

        listView =(ListView)findViewById(R.id.aList);
        SettingsListAdapter menusAdapter=new SettingsListAdapter(ContactDetails.this,getData());
        listView.setAdapter(menusAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView txt = (TextView) view.findViewById(R.id.settingsTitle);
                if (txt.getText().toString().equalsIgnoreCase("Voice Call 1")) {
                    call("233240088705");

                }

                if (txt.getText().toString().equalsIgnoreCase("Voice Call 2")) {

                    call("233542389021");
                }

                if (txt.getText().toString().equalsIgnoreCase("Chat")) {

                    openWhatsappContact("233240088705");
                    // onClickWhatsApp();
                }

                if(txt.getText().toString().equalsIgnoreCase("Email")){

                    sendEmail();
                }

            }
        });
    }
    void openWhatsappContact(String number) {

        try{

            Uri uri = Uri.parse("smsto:" + number);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(i, ""));
        }catch (Exception e){

            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public static List<SettingsList> getData() {

        String[] Titles={
                "Chat",
                "Voice Call 1",
                "Voice Call 2",
                "Email"};

        String[] Desscrip={
                "Chat With Us",
                "Call mFinance Support Line One",
                "Call mFinance Support Line Two",
                "Send Us  mail"};

        Integer[] menuIcons = {
                R.drawable.identitycheck,
                R.drawable.verify,
                R.drawable.identitycheck,
                R.drawable.verify


        };

        List<SettingsList> data = new ArrayList<>();

        for(int t=0;t<Titles.length;t++){

            SettingsList current = new SettingsList();
            current.setTitle(Titles[t]);
            current.setDescription(Desscrip[t]);
            current.setResourceID(menuIcons[t]);
            data.add(current);
        }

        return data;

    }

    public void call(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    public void chat(String number) {
        Intent callIntent = new Intent(Intent.ACTION_SEND);
        //callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    public void onClickWhatsApp() {

        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    void sendEmail(){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: abc@xyz.com"));
        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
    }
}
