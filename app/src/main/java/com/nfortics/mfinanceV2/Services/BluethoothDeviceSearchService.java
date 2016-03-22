package com.nfortics.mfinanceV2.Services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;


import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Request.Request;
import com.nfortics.mfinanceV2.Service;


import java.util.ArrayList;

public class BluethoothDeviceSearchService extends Service {

	Context context;
	BroadcastReceiver bluetoothBroadCastReceiver;

	public BluethoothDeviceSearchService(Handler handler) {
		super(handler);
	}

	@Override
	public void processRequest(Request request) {
		// ServiceProcessor processor = new ServiceProcessor(request);
		// new Thread(processor).start();
	}

	public void processRequest(BluetoothAdapter bluetoothAdapter,
			Context context) {
		this.context = context;
		ServiceProcessor processor = new ServiceProcessor(bluetoothAdapter);
		new Thread(processor).start();
	}

	private class ServiceProcessor implements Runnable {

		// private final Request request;
		private BluetoothAdapter bluetoothAdapter;

		public ServiceProcessor(BluetoothAdapter bluetoothAdapter) {
			this.bluetoothAdapter = bluetoothAdapter;
		}

		@Override
		public void run() {
			try {

				lookUpBluetoothDevices();
			} catch (Exception e) {
				// Message msg = new Message();
				// msg.arg2 = 740;
				// handler.sendMessage(msg);
				e.printStackTrace();
				handler.sendMessage(createMessage(Application.ERROR, e));
			}
		}

		private void lookUpBluetoothDevices() {
			try {
				startBluetoothDevicesDiscovery();
			} catch (Exception e) {
				Toast.makeText(
						context,
						"There was a problem initializing bluetooth on this device.",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}

		private BroadcastReceiver getBroadCastReceiver() {
			// Create a BroadcastReceiver for ACTION_FOUND
			final BroadcastReceiver mReceiver = new BroadcastReceiver() {
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					// When discovery finds a device
					if (BluetoothDevice.ACTION_FOUND.equals(action)) {
						// Get the BluetoothDevice object from the Intent
						BluetoothDevice device = intent
								.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						Application.getBluetoothDevices()
								.add(device);
						Log.i(getClass().getName(), ">>>> FOUND DEVICE ... "
								+ device.getName());
					} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
							.equals(action)) {

						Log.i(getClass().getName(),
								">>>> ACTION DISCOVERY FINISHED ... ");
						bluetoothAdapter.cancelDiscovery();
						context.unregisterReceiver(bluetoothBroadCastReceiver);
						handler.sendEmptyMessage(0);

					} else if ("android.bluetooth.device.action.UUID"
							.equals(action)) {

						BluetoothDevice device = intent
								.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						Log.i(getClass().getName(),
								">>>> DISCOVWRED UUID INTENT ... ");
						Log.i(getClass().getName(),
								">>>> INFO got uuid for ... "
										+ device.getName());
						Parcelable[] uuidExtra = intent
								.getParcelableArrayExtra("android.bluetooth.device.extra.UUID");
						for (int i = 0; i < uuidExtra.length; i++) {
							Log.i(getClass().getName(), ">>>> INFO uuid for "
									+ device.getName() + " is " + uuidExtra[i]);
						}

					}
				}
			};
			return mReceiver;
		}

		private void startBluetoothDevicesDiscovery() {
			if (!bluetoothAdapter.isDiscovering()) {
				Application.setBluetoothDevices(
						new ArrayList<BluetoothDevice>());
				bluetoothAdapter.startDiscovery();
				bluetoothBroadCastReceiver = getBroadCastReceiver();
				IntentFilter filter = new IntentFilter();
				filter.addAction("android.bluetooth.device.action.UUID");
				filter.addAction(BluetoothDevice.ACTION_FOUND);
				filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
				context.registerReceiver(bluetoothBroadCastReceiver, filter);
			}
		}
	}

}
