package com.nfortics.mfinanceV2.Services;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Request.Request;
import com.nfortics.mfinanceV2.Service;

import java.io.InputStream;
import java.io.OutputStream;

public class PrintInvoiceService extends Service {

	Context context;

	public PrintInvoiceService(Handler handler) {
		super(handler);
	}

	@Override
	public void processRequest(Request request) {
		// ServiceProcessor processor = new ServiceProcessor(request);
		// new Thread(processor).start();
	}

	public void processRequest(BluetoothSocket socket, Context context) {
		this.context = context;
		ServiceProcessor processor = new ServiceProcessor(socket);
		new Thread(processor).start();
	}

	private class ServiceProcessor implements Runnable {

		// private final Request request;
		private BluetoothSocket socket;
		private InputStream inStream;
		private OutputStream outStream;

		public ServiceProcessor(BluetoothSocket socket) {
			this.socket = socket;

			InputStream inTmp = null;
			OutputStream outTmp = null;

			try {
				inTmp = socket.getInputStream();
				outTmp = socket.getOutputStream();
			} catch (Exception e) {
				e.printStackTrace();
			}

			inStream = inTmp;
			outStream = outTmp;
			Application.setOutStream(outStream);
		}

		@Override
		public void run() {
			// Looper.prepare();
			Log.i(getClass().getName(),
					">>>> Logging start of print invoice service thread.");
			byte[] buffer = new byte[1024]; // buffer store for the stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			// while (true) {
			// try {
			// Read from the InputStream
			// bytes = inStream.read(buffer);
			// Send the obtained bytes to the UI activity
			// handler.obtainMessage(1, bytes, -1, buffer).sendToTarget();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			Log.i(getClass().getName(),
					">>>> Logging start of printtttttttttttt invoice service thread.");
			Message msg = new Message();
			msg.arg2 = 740;
			handler.sendMessage(msg);

		}
	}

}
