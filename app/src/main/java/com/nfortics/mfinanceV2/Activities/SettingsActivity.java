package com.nfortics.mfinanceV2.Activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Handlers.GeneralRequestHandler;
import com.nfortics.mfinanceV2.MessageAlert;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Services.BluethoothDeviceSearchService;
import com.nfortics.mfinanceV2.Services.ConnectToBluetoothDeviceService;
import com.nfortics.mfinanceV2.Services.PrintInvoiceService;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.util.List;
import java.util.Set;

public class SettingsActivity extends BaseActivity {


    RelativeLayout relativeLayout;
    RelativeLayout printerSetupLayout;
    Typefacer typefacer;

    Utils utils=new Utils();
    View view;
    private BluetoothAdapter mBluetoothAdapter;

    private static int BLUETOOTH_REQUEST_CODE = 1;
    private Set<BluetoothDevice> pairedDevices;
    private long enqueue;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private DownloadManager dm;


    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_settings, null, false);

        return activityView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typefacer=new Typefacer();
        getSupportActionBar().setTitle("");
        relativeLayout=(RelativeLayout)findViewById(R.id.compound);

        view=generateActivityView();

        relativeLayout.removeAllViews();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

      SetViews(view);
      EventListeners();
        relativeLayout.addView(view);

    }


    void SetViews(View view){



        printerSetupLayout=(RelativeLayout)view.findViewById(R.id.printerSetupLayout);
    }

    void EventListeners(){

        printerSetupLayoutClickListen();
    }


    void printerSetupLayoutClickListen(){

        printerSetupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookUpBluetoothDevices();
            }
        });

    }







    private void lookUpBluetoothDevices() {
        try {
            if (mBluetoothAdapter.isEnabled()) {
                startBluetoothDiscovery();
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, BLUETOOTH_REQUEST_CODE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "There was a problem initializing bluetooth on this device.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void showBluetoothDevicesDialog() {
        CharSequence[] items = getBluetoothDevicesAsArray();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Device");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                BluetoothDevice selectedDevice = Application.getBluetoothDevices().get(which);
                connectToBluetoothDevice(selectedDevice);
                builder.create().dismiss();
            }
        });
        AlertDialog createOptions = builder.create();
        createOptions.show();
    }

    private void showPairedDevicesDialog() {
        CharSequence[] items = getPairedBluetoothDevicesAsArray();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Device");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which >= pairedDevices.size()) {
                    initBluetoothDeviceDiscovery();
                } else {
                    int index = 0;
                    for (BluetoothDevice device : pairedDevices) {
                        if (index == which) {
                            connectToBluetoothDevice(device);
                        }
                        index++;
                    }
                }
                builder.create().dismiss();
            }
        });
        AlertDialog createOptions = builder.create();
        createOptions.show();
    }

    public String[] getBluetoothDevicesAsArray() {
        String[] object = new String[Application.getBluetoothDevices().size()];
        final List<BluetoothDevice> devices = Application.getBluetoothDevices();

        for (int i = 0; i < devices.size(); i++) {
            object[i] = devices.get(i).getName();
        }

        return object;
    }

    public String[] getPairedBluetoothDevicesAsArray() {
        String[] object = new String[pairedDevices.size() + 1];

        int index = 0;
        for (BluetoothDevice device : pairedDevices) {
            object[index] = device.getName();
            index++;
        }

        object[index] = "Scan for Devices";
        return object;
    }

    private void startBluetoothDiscovery() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            showPairedDevicesDialog();
        } else {
            initBluetoothDeviceDiscovery();
        }
    }

    private void initBluetoothDeviceDiscovery() {
        GeneralRequestHandler handler = new GeneralRequestHandler(this, "searching for devices") {

            @Override
            protected void processMessage(Message message) {
                if (Application.getBluetoothDevices().size() >= 1) {
                    showBluetoothDevicesDialog();
                } else {
                    MessageAlert.showMessage("No Device found.", SettingsActivity.this);
                }
            }
        };
        handler.showProgressDialog();
        new BluethoothDeviceSearchService(handler).processRequest(mBluetoothAdapter, SettingsActivity.this);
    }

    private void connectToBluetoothDevice(BluetoothDevice device) {
        GeneralRequestHandler handler = new GeneralRequestHandler(this, "connecting to device") {

            @Override
            protected void processMessage(Message message) {
                if (message.arg1 == 0) {
                    printInvoice(Application.getSocket());
                    MessageAlert.showMessage("Bluetooth Connection was successfull.", SettingsActivity.this);

                } else {
                    MessageAlert.showMessage("Bluetooth Connection failed. Please try again.", SettingsActivity.this);
                }
            }
        };
        handler.showProgressDialog();
        new ConnectToBluetoothDeviceService(handler).processRequest(device, SettingsActivity.this);
    }

    private void printInvoice(BluetoothSocket socket) {
        GeneralRequestHandler handler = new GeneralRequestHandler(this, "") {

            @Override
            protected void processMessage(Message message) {
                Log.i(getClass().getName(), ">>>> LINsided prinvoisoi.....");
            }
        };
        new PrintInvoiceService(handler).processRequest(socket, SettingsActivity.this);
    }
}
