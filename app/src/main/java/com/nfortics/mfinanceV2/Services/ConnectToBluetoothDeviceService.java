package com.nfortics.mfinanceV2.Services;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Request.Request;
import com.nfortics.mfinanceV2.Service;


import java.io.IOException;
import java.util.UUID;

public class ConnectToBluetoothDeviceService extends Service {

	Context context;

	public ConnectToBluetoothDeviceService(Handler handler) {
		super(handler);
	}

	@Override
	public void processRequest(Request request) {
		// ServiceProcessor processor = new ServiceProcessor(request);
		// new Thread(processor).start();
	}

	public void processRequest(BluetoothDevice device, Context context) {
		this.context = context;
		ServiceProcessor processor = new ServiceProcessor(device);
		new Thread(processor).start();
	}

	private class ServiceProcessor implements Runnable {

		// private final Request request;
		private BluetoothDevice device;
		private BluetoothSocket deviceSocket;

		public ServiceProcessor(BluetoothDevice device) {
			this.device = device;
			BluetoothSocket tmp = null;

			try {
				UUID MY_UUID = UUID
						.fromString("00001101-0000-1000-8000-00805f9b34fb"); // RFCOMM
																				// service
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				e.printStackTrace();
			}

			deviceSocket = tmp;
			Application.setSocket(deviceSocket);

		}

		@Override
		public void run() {
			Looper.prepare();
			Message message = new Message();

			try {

				deviceSocket.connect();
				Log.i(getClass().getName(),
						"device connected successfully device name = "
								+ deviceSocket.getRemoteDevice().getName());
				Toast.makeText(context, "Device connection is successfull",
						Toast.LENGTH_LONG).show();
				message.arg1 = 0;

			} catch (Exception e) {
				message.arg1 = 111;
				e.printStackTrace();
				try {
					deviceSocket.close();
				} catch (IOException closeException) {
					Log.e(getClass().getName(), ">>>> socket close exception "
							+ closeException.getMessage());
					closeException.printStackTrace();
				}
			}
			handler.sendMessage(message);
		}

	}

}
