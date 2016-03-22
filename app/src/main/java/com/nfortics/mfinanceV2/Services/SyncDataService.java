package com.nfortics.mfinanceV2.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.nfortics.mfinanceV2.Adapters.SyncAdapter;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bigfire on 1/9/2016.
 */
public class SyncDataService  extends Service {

    // Storage for an instance of the sync adapter
    private static SyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    /*
     * Instantiate the sync adapter object.
     */



    int counter = 0;
    static final int UPDATE_INTERVAL = 60 * 1000; // / 1000 = 1 second
    private Timer timer = new Timer();
   // JsonParsers jsonParser = new JsonParsers();
    private static final String TAG_SUCCESS = "success";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }


    @Override
    public void onCreate() {
        Log.d("oxinbo", "Service oncreate called");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Announcement about starting
        Toast.makeText(this, "Starting the Service", Toast.LENGTH_SHORT).show();
        // Start a Background thread
        doSomethingRepeatedly();
        Log.d("oxinbo", "Service started");
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                Log.d("oxinbo","AsyncAtask runing in backgrund");


            }
        }, 0, UPDATE_INTERVAL);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
        Toast.makeText(this, "Stopping the Service", Toast.LENGTH_SHORT).show();
    }

    public void set_alarm(int year, int month, int day, String title,
                          String text, String billno) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        Intent intent = new Intent(getApplicationContext(), SyncDataService.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        intent.putExtra("billno", billno);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 1234, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        @SuppressWarnings("static-access")
        AlarmManager alarmManager = (AlarmManager) getApplicationContext()
                .getSystemService(getApplicationContext().ALARM_SERVICE);

        alarmManager.cancel(pendingIntent); // cancel any existing alarms

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,

                pendingIntent);


    }


}
