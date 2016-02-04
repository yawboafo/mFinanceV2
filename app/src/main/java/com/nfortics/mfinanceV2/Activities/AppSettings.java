package com.nfortics.mfinanceV2.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
