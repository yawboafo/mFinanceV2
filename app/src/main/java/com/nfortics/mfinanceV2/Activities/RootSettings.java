package com.nfortics.mfinanceV2.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Fragments.RootSettingsPages.PageOne;
import com.nfortics.mfinanceV2.Fragments.RootSettingsPages.PageOne.PageOneInteractionListener;
import com.nfortics.mfinanceV2.Fragments.RootSettingsPages.PageTwo;
import com.nfortics.mfinanceV2.Fragments.RootSettingsPages.PageTwo.PageTwoInteractionListener;
import com.nfortics.mfinanceV2.Fragments.SettingsPages.ApplicationSettingsFrag;
import com.nfortics.mfinanceV2.Fragments.SettingsPages.GeneralSettingsFrag;
import com.nfortics.mfinanceV2.Fragments.SettingsPages.ProfileSettingsFrag;
import com.nfortics.mfinanceV2.Models.AppInstanceSettings;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.ViewWidgets.SlidingTabLayout;

import java.io.IOException;

import roboguice.inject.InjectView;



public class RootSettings extends AppCompatActivity implements PageOneInteractionListener,PageTwoInteractionListener {
    Toolbar toolbar;
    TextView toolbarTitle;
    Typefacer typefacer;
    AppInstanceSettings appInstanceSettings ;

    TextView selectserver;
    RadioButton staging,production,demo;
    private CoordinatorLayout coordinatorLayout;

    ViewPager mPager;
    SlidingTabLayout mTabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_settings);
        typefacer=new Typefacer();
        //coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinate);
        appInstanceSettings  =new AppInstanceSettings();
      //  selectserver=(TextView)findViewById(R.id.selectServer);
      //  selectserver.setTypeface(typefacer.squareMedium());
       // selectserver.setText("Select Server : ");
       // SetRadioButtons();
        setToolBar();
        setPagers();

    }

/***
    void SetRadioButtons(){

                staging=(RadioButton)findViewById(R.id.radio_staging);
                 production=(RadioButton)findViewById(R.id.radio_production);
                demo=(RadioButton)findViewById(R.id.radio_demo);

        String serverMode= Application.ServerMode;

  Utils.log("server mod " + serverMode);
        if(serverMode!=null){

            if (serverMode.equalsIgnoreCase("Production"))
                production.setChecked(true);
            if (serverMode.equalsIgnoreCase("Staging"))
                staging.setChecked(true);
            if (serverMode.equalsIgnoreCase("Demo"))
                demo.setChecked(true);

        }



    }
    ***/














    private void setToolBar(){
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbarTitle= (TextView) findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(typefacer.squareLight());

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        toolbarTitle.setText("root Mode ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }

/***
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_production:
                if (checked)
                    appInstanceSettings.setServerMode("Production");

                    break;
            case R.id.radio_staging:
                if (checked)
                    appInstanceSettings.setServerMode("Staging");
                    break;
            case R.id.radio_demo:
                if (checked)
                    appInstanceSettings.setServerMode("Demo");
                    break;


        }

        try {
            Utils.insertAppInstanceSettings(appInstanceSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {


           //// AlarmManager alm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
           // alm.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, PendingIntent.getActivity(this, 0, new Intent(this, LoginActivity.class), 0));
          //  android.os.Process.killProcess(android.os.Process.myPid());


            System.exit(0);
        }
    }

    ***/
    public void SignOut()
    {



        Intent intent = new Intent(this, LoginActivity.class);
        //intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // To clean up all activities
        startActivity(intent);
        finish();


    }


    private void setPagers(){
        mPager = (ViewPager)findViewById(R.id.Viewpager);
        mTabs = (SlidingTabLayout)findViewById(R.id.tabs);

        mPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(mPager);
    }



    public void alertSingleChoiceItems(){

        AlertDialog.Builder builder = new AlertDialog.Builder(RootSettings.this);

        // Set the dialog title
        builder.setTitle("Choose One")

                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.ServerChoices, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ToastUtil.snackbar(coordinatorLayout,""+arg1 );

                    }

                })

                        // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        ToastUtil.snackbar(coordinatorLayout, "" + selectedPosition);

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }

    @Override
    public void PageOneInteraction(Uri uri) {

    }

    @Override
    public void PageTwoInteraction(Uri uri) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        Fragment fragment=null;
        String tabs[];
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.rootSettings);
        }

        @Override
        public Fragment getItem(int i) {

            if(i==0)
            {

                // actionButton.setVisibility(View.GONE);
                fragment=new PageOne();

            }     if(i==1)
            {
                // actionButton.setVisibility(View.VISIBLE);
                fragment=new PageTwo();

            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
