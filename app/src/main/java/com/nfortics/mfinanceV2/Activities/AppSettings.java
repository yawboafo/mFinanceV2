package com.nfortics.mfinanceV2.Activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Activities.SettingsItemsActivity.PersonalDetails;
import com.nfortics.mfinanceV2.Models.SettingsList;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.ViewAdapters.SettingsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppSettings extends BaseActivity {

    ListView listView;
    Typefacer typefacer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_app_settings);




    typefacer=new Typefacer();
    getSupportActionBar().setTitle("");
    relativeLayout=(RelativeLayout)findViewById(R.id.compound);

    view=generateActivityView();
        generatListView();

    relativeLayout.removeAllViews();

    relativeLayout.addView(view);

    //setContentView(R.layout.settings_activity);
}


    private void  generatListView(){

        listView =(ListView)view.findViewById(R.id.settingsList);
        SettingsListAdapter menusAdapter=new SettingsListAdapter(AppSettings.this,getData());
        listView.setAdapter(menusAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView txt = (TextView) view.findViewById(R.id.settingsTitle);
                if (txt.getText().toString().equalsIgnoreCase("Contact Support")) {


                    openContactCard();
                    //openWhatsApp("+233209361021@s.whatsapp.net");
                }

                if (txt.getText().toString().equalsIgnoreCase("Personal Details")) {


                   // com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("ActivitySetting");
                    Intent intent = new Intent(AppSettings.this, PersonalDetails.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);

                   // openContactCard();
                    //openWhatsApp("+233209361021@s.whatsapp.net");
                }

            }
        });
    }



    private void  openContactCard(){

      /***  Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf("Gladis Chi"));
        intent.setData(uri);
        startActivity(intent);**/

     /**   Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts., String.valueOf(contactID));
        intent.setData(uri);
       startActivity(intent);**/

      /***  id = cursor.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        name = cur.getString( cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        Log.d(tag, "Id: " + id + "\t Name: " + name);***/

       /*** Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
        intent.setData(uri);
        startActivity(intent);***/

        ContentResolver contentResolver = getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("233240088705"));

        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if(cursor!=null) {
            while(cursor.moveToNext()){
                String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                Log.d("oxinbo", "contactMatch name: " + contactName);
                Log.d("xinbo", "contactMatch id: " + contactId);
            }
            cursor.close();
        }

    }
    private void openWhatsApp(String id) {

        Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[] { ContactsContract.Contacts.Data._ID }, ContactsContract.Data.DATA1 + " = ?",
                new String[] { id }, null);
        c.moveToFirst();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + c.getString(0)));

        startActivity(i);
        c.close();
    }
    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_app_settings, null, false);

        return activityView;
    }

    public static List<SettingsList> getData() {

        String[] Titles={
                "Personal Details",
                "Security",
                "Device",
                "Advance Features",
                "Contact Support"};

        String[] Desscrip={
                "Add or update your personal details",
                "Set and save your security settings",
                "Pair your device with printers,bio scanner etc",
                "Update profile,check location,transaction limits etc",
                "Update profile,check location,transaction limits etc"};

        Integer[] menuIcons = {
                R.drawable.identitycheck,
                R.drawable.verify,
                R.drawable.identitycheck,
                R.drawable.verify,
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

}
