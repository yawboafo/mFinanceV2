package com.nfortics.mfinanceV2.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.logic.BluetoothChatService;
import com.nfortics.mfinanceV2.logic.SerialPortService;


import java.io.IOException;
import java.security.InvalidParameterException;

public class ConnectActivity extends Activity {
  private Application application;
  private static final int REQUEST_BLUETOOTH_CONNECT_DEVICE = 1001;
  private static final int REQUEST_SERIALPORT_CONNECT_DEVICE = 1002;
  private Button bluetoothConnect;
  private Button serialConnect;

  private BluetoothAdapter mBluetoothAdapter;
  private BluetoothChatService mChatService = null;

  private final Logger logger = LoggerFactory.getLogger();

  private ProgressDialog progressDialog;

  public static final int CONNECTY_SUCCESS = 100;
  public static final int CONNECTY_FAIL = 101;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    setContentView(R.layout.activity_connect);
    // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
    // R.layout.custom_title);

    bluetoothConnect = (Button) findViewById(R.id.connectBluetooth);
    bluetoothConnect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mBluetoothAdapter == null) {
          ToastUtil.showToast(ConnectActivity.this, "no bluetooth");
          finish();
          return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
          Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
          startActivityForResult(enableBtIntent, 0);
          return;
        }
        // Intent serverIntent = new Intent(ConnectActivity.this,
        // DeviceListActivity.class);
        // startActivityForResult(serverIntent,
        // REQUEST_BLUETOOTH_CONNECT_DEVICE);
      }
    });

    serialConnect = (Button) findViewById(R.id.connectSerial);
    serialConnect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          if (!SerialPortService.getInstance().isOpen() && !SerialPortService.getInstance().openSerialPort()) {
            application.setSerialPortServiceOpen(application.NO_CONNECTED, null);
            ToastUtil.showToast(ConnectActivity.this, "is open is false");
          } else {
            application.setSerialPortServiceOpen(application.SERIAL_CONNECTED, SerialPortService.getInstance());
            application.setVoltageListener(ConnectActivity.this);
            application.setPrinterListener();
            ToastUtil.showToast(ConnectActivity.this, "Set serial port service to open");
            ConnectActivity.this.finish();
          }
        } catch (InvalidParameterException e) {
          e.printStackTrace();
          logger.debug("open InvalidParameterException");
        } catch (SecurityException e) {
          e.printStackTrace();
          logger.debug("open SecurityException");
        } catch (IOException e) {
          e.printStackTrace();
          logger.debug("open IOException");
        }
      }
    });
    initData();
  }

  public void initData() {
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    application = (Application) this.getApplicationContext();
    // if (mBluetoothAdapter == null) {
    // ToastUtil.showToast(this, R.string.no_bluetooth);
    // finish();
    // return;
    // }
    //
    // if (!mBluetoothAdapter.isEnabled()) {
    // Intent enableBtIntent = new Intent(
    // BluetoothAdapter.ACTION_REQUEST_ENABLE);
    // startActivityForResult(enableBtIntent, 0);
    // }
    // application = (MyApplication) this.getApplicationContext();
    // mChatService = new BluetoothChatService(this);
    // mChatService.setOnConnectListener(new OnConnectListener() {
    // @Override
    // public void onConnectSuccess() {
    // application.setBluetoothServiceOpen(application.BLUETOOTH_CONNECTED,mChatService);
    // cancleProgressDialog();
    // application.setVoltageListener(ConnectActivity.this);
    // application.setPrinterListener();
    // ToastUtil.showToast(ConnectActivity.this, "");
    // ConnectActivity.this.finish();
    //
    // }
    //
    // @Override
    // public void onConnectFail() {
    // application.setBluetoothServiceOpen(application.NO_CONNECTED,null);
    // cancleProgressDialog();
    // ToastUtil.showToast(ConnectActivity.this, "����ʧ��");
    // }
    //
    // @Override
    // public void onConnectLost() {
    // application.setBluetoothServiceOpen(application.NO_CONNECTED,null);
    // cancleProgressDialog();
    // ToastUtil.showToast(ConnectActivity.this, "���Ӷ�ʧ");
    // }
    //
    // });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // switch (requestCode) {
    // case REQUEST_BLUETOOTH_CONNECT_DEVICE:
    // if (resultCode == RESULT_OK) {
    // // ToastUtil.showToast(this, R.string.enable_bluetooth);
    // // Get the device MAC address
    // String address = data.getExtras().getString(
    // DeviceListActivity.EXTRA_DEVICE_ADDRESS);
    // // Get the BLuetoothDevice object
    // BluetoothDevice device = mBluetoothAdapter
    // .getRemoteDevice(address);
    // // Attempt to connect to the device
    //
    // showProgressDialog("�������ӣ����Ժ�......");
    // mChatService.connect(device);
    //
    // // Toast.makeText(this, device.getName(), Toast.LENGTH_LONG)
    // // .show();
    // } else if (resultCode == RESULT_CANCELED) {
    // ToastUtil.showToast(this, R.string.disable_bluetooth);
    // }
    // break;
    // default:
    // break;
    // }
  }

  private Handler mHandler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      // switch (msg.what) {
      // case BluetoothChatService.CONNECTION_SUCCESS:
      // application.setOpen(true);
      // cancleProgressDialog();
      // ToastUtil.showToast(ConnectActivity.this, "���ӳɹ�");
      // ConnectActivity.this.finish();
      // break;
      // case BluetoothChatService.CONNECTION_FAIL:
      // application.setOpen(false);
      // cancleProgressDialog();
      // ToastUtil.showToast(ConnectActivity.this, "����ʧ��");
      // break;
      // default:
      // break;
      // }
    }

  };

  private void showProgressDialog(String message) {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
    }
    progressDialog.setMessage(message);
    if (!progressDialog.isShowing()) {
      progressDialog.show();
    }
  }

  private void showProgressDialog(int resId) {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
    }
    progressDialog.setMessage(getResources().getString(resId));
    if (!progressDialog.isShowing()) {
      progressDialog.show();
    }
  }

  private void cancleProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.cancel();
    }
  }

}
