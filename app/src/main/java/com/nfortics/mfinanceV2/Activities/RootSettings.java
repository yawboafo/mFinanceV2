package com.nfortics.mfinanceV2.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Models.AppInstanceSettings;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.io.IOException;

import roboguice.inject.InjectView;



public class RootSettings extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbarTitle;
    Typefacer typefacer;


    TextView selectserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_settings);
        typefacer=new Typefacer();
        selectserver=(TextView)findViewById(R.id.selectServer);
        selectserver.setTypeface(typefacer.squareMedium());
        selectserver.setText("Select Server :     Current Server = ");
        setToolBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_root_settings, menu);
        return true;
    }

    private void setToolBar(){
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbarTitle= (TextView) findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(typefacer.squareLight());

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        toolbarTitle.setText("root Mode ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        AppInstanceSettings appInstanceSettings  =new AppInstanceSettings();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
