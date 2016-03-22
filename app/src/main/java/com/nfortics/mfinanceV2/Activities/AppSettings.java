package com.nfortics.mfinanceV2.Activities;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Activities.SettingsItemsActivity.AdvanceFeatures;
import com.nfortics.mfinanceV2.Activities.SettingsItemsActivity.ContactDetails;
import com.nfortics.mfinanceV2.Activities.SettingsItemsActivity.Device;
import com.nfortics.mfinanceV2.Activities.SettingsItemsActivity.PersonalDetails;
import com.nfortics.mfinanceV2.Activities.SettingsItemsActivity.Security;
import com.nfortics.mfinanceV2.Models.Agent;
import com.nfortics.mfinanceV2.Models.SettingsList;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ImagePicker;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.ViewAdapters.SettingsListAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppSettings extends BaseActivity {
    CircleImageView profile_image;
    ListView listView;
    TextView textViewUserLog,agentLastlogin;
    Typefacer typefacer;
    User user;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_app_settings);




    typefacer=new Typefacer();
    getSupportActionBar().setTitle("");
    relativeLayout=(RelativeLayout)findViewById(R.id.compound);

       view=generateActivityView();
        AgentProfileImages();
        generatListView();
        loadSetUserAgent();
    relativeLayout.removeAllViews();

    relativeLayout.addView(view);

    //setContentView(R.layout.settings_activity);
}

    void loadSetUserAgent(){
        textViewUserLog=(TextView)view.findViewById(R.id.agentLog);
        agentLastlogin=(TextView)view.findViewById(R.id.agentLastlogin);
        textViewUserLog.setTypeface(typefacer.squareRegular());
        agentLastlogin.setTypeface(typefacer.squareLight());
        textViewUserLog.setText("Don Robot");
        user=User.load(User.class,1);

        if(user!=null){
            textViewUserLog.setText(""+user.getFirstName());
            if(user.getLastLoginTs()==null){
                agentLastlogin.setText(Utils.getCurrentDate());
            }else {
                agentLastlogin.setText("Last Login : "+user.getLastLoginTs());
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);


                if(bitmap!=null){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Agent agent = new Agent();
                    agent.setProfile_pics(byteArray);

                    try {
                        Utils.insertAgentData(agent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        final Bitmap aa = BitmapFactory.decodeByteArray(byteArray, 0,
                                byteArray.length);


                        profile_image.post(new Runnable() {
                            @Override
                            public void run() {
                                profile_image.setImageBitmap(aa);

                            }
                        });
                    }


                }


                // TODO use bitmap
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter

    public void onPickImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
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

                    Intent sendIntent = new Intent(AppSettings.this, ContactDetails.class);

                    startActivity(sendIntent);
                    //  openContactCard();
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

                if (txt.getText().toString().equalsIgnoreCase("Security")) {


                    // com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("ActivitySetting");
                    Intent intent = new Intent(AppSettings.this, Security.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);

                    // openContactCard();
                    //openWhatsApp("+233209361021@s.whatsapp.net");
                }
                if (txt.getText().toString().equalsIgnoreCase("Device")) {


                    // com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("ActivitySetting");
                    Intent intent = new Intent(AppSettings.this, Device.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);

                    // openContactCard();
                    //openWhatsApp("+233209361021@s.whatsapp.net");
                }
                if (txt.getText().toString().equalsIgnoreCase("Advance Features")) {


                    // com.nfortics.mfinanceV2.Application.Application.setCurrentActivityState("ActivitySetting");
                    Intent intent = new Intent(AppSettings.this, AdvanceFeatures.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);

                    // openContactCard();
                    //openWhatsApp("+233209361021@s.whatsapp.net");
                }

            }
        });
    }
    void AgentProfileImages(){
        profile_image=(CircleImageView)view.findViewById(R.id.agentImage);
            profile_image.post(new Runnable() {
                @Override
                public void run() {

                    try {
                        Agent agent = Utils.AgentData();

                        if (agent.getProfile_pics() != null) {


                            final Bitmap bitmap = BitmapFactory.decodeByteArray(agent.getProfile_pics(), 0,
                                    agent.getProfile_pics().length);

                            profile_image.setImageBitmap(bitmap);
                        }


                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


       profile_image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onPickImage();

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



        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(getContactId()));
        intent.setData(uri);
       startActivity(intent);

    }

    String getContactId(){


        String contactID="";
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
                contactID = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                Log.d("oxinbo", "contactMatch name: " + contactName);
                Log.d("xinbo", "contactMatch id: " + contactID);
            }
            cursor.close();
        }

        return contactID;
    }
    private void openWhatsApp(String id) {

        Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[] { ContactsContract.Contacts.Data._ID }, ContactsContract.Data.DATA1 + " = ?",
                new String[] { id }, null);
        c.moveToFirst();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + c.getString(0)));

        startActivity(i);
        c.close();



        Intent sendIntent = new Intent(this, ContactDetails.class);

        startActivity(sendIntent);

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
                "Contact Support","Commissions"};

        String[] Desscrip={
                "Add or update your personal details",
                "Set and save your security settings",
                "Pair your device with printers,bio scanner etc",
                "Update profile,check location,transaction limits etc",
                "We Are Listening","View your commission "};

        Integer[] menuIcons = {
                R.drawable.identitycheck,
                R.drawable.verify,
                R.drawable.identitycheck,
                R.drawable.verify,
                R.drawable.verify, R.drawable.identitycheck

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
