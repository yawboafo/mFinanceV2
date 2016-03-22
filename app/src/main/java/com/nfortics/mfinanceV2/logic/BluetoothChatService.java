/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */  

package com.nfortics.mfinanceV2.logic;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;


import com.nfortics.mfinanceV2.Utilities.DataUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class BluetoothChatService {
	// ����
	private static final String TAG = "BluetoothChatService";
	private static final boolean D = true;
	private Lock Mutex = null;
	/**
	 *
	 */
	public static final int CONNECTION_SUCCESS = 100;
	/**
	 *
	 */
	public static final int CONNECTION_FAIL = 101;
	/**
	 *
	 */
	public static final int CONNECTION_LOST = 102;


	private static final String NAME = "BluetoothChat";



	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static StringBuffer hexString = new StringBuffer();
	// ��������Ա
	private final BluetoothAdapter mAdapter;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CONNECTION_SUCCESS:
				if (onConnectListener != null) {
					onConnectListener.onConnectSuccess();
				}
				break;
			case CONNECTION_FAIL:
				if (onConnectListener != null) {
					onConnectListener.onConnectFail();
				}
				break;
			case CONNECTION_LOST:
				if (onConnectListener != null) {
					onConnectListener.onConnectLost();
				}
				break;
			default:
				break;
			}
		}

	};
	private AcceptThread mAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;
	// ������ָʾ��ǰ������״̬
	public static final int STATE_NONE = 0; // ��ǰû�п��õ�����
	public static final int STATE_LISTEN = 1; // �����������������
	public static final int STATE_CONNECTING = 2; // ���ڿ�ʼ������ϵ
	public static final int STATE_CONNECTED = 3; // �������ӵ�Զ���豸
	public static boolean bRun = true;
	public static boolean bConnect_State = false;

	private OnConnectListener onConnectListener;

	public void setOnConnectListener(OnConnectListener onConnectListener) {
		this.onConnectListener = onConnectListener;
	}

	public interface OnConnectListener {
		public void onConnectSuccess();

		public void onConnectFail();

		public void onConnectLost();
	};

	/**
	 *
	 * 
	 *
	 *
	 *
	 *
	 */
	public BluetoothChatService(Context context) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = STATE_NONE;
		Mutex = new ReentrantLock();
	}

	/**
	 *
	 * 
	 *
	 *
	 */
	private synchronized void setState(int state) {
		if (D)
			Log.d(TAG, "setState() " + mState + " -> " + state);
		mState = state;
	}

	/**
	 *
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 *
	 */
	public synchronized void start() {
		if (D)
			Log.d(TAG, "start");

		// ȡ���κ��߳���ͼ��������
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// ȡ���κ��߳��������е�����
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		//
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}
		setState(STATE_LISTEN);
	}

	// ���Ӱ�����Ӧ����
	/**
	 *
	 * 
	 *
	 */
	public synchronized void connect(BluetoothDevice device) {
		if (D)
			Log.d(TAG, "connect to: " + device);

		// ȡ���κ��߳���ͼ��������
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// ȡ���κ��߳��������е�����
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// �����߳����ӵ��豸
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 *
	 * 
	 *
	 *
	 */
	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {
		if (D)
			Log.d(TAG, "connected");

		//
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		//
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		//
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}

		//
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		setState(STATE_CONNECTED);
	}

	/**
	 * ֹͣ���е��߳�
	 */
	public synchronized void stop() {
		if (D)
			Log.d(TAG, "stop");
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		setState(STATE_NONE);
	}
	
	
//	private long lastTime;
//	private long currentTime;
//	private static final long SLEEP_TIME = 1000 * 60 * 2;
	
	
	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * 
	 * @param out
	 *            The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public synchronized void write(byte[] out) {

		// ������ʱ����

		ConnectedThread r;
		if (Mutex == null)
			return;
		Mutex.lock();
		// ͬ��������connectedthread
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		

		r.write(out);
		Mutex.unlock();
	}

	public synchronized int read(byte[] out, int bytes) {
		// ������ʱ����
		ConnectedThread r;
		if (Mutex == null)
			return -1;
		Mutex.lock();
		// ͬ��������connectedthread
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return 0;
			r = mConnectedThread;
		}
		// ִ��дͬ��
		bytes = r.read(out, bytes);
		Mutex.unlock();
		return bytes;
	}

	public synchronized int readex(byte[] out, int bytes, int timeout,
			boolean fixSize) {
		// ������ʱ����
		ConnectedThread r;
		if (Mutex == null)
			return -1;
		Mutex.lock();
		// ͬ��������connectedthread
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return 0;
			r = mConnectedThread;
		}
		// ִ��дͬ��
		bytes = r.readex(out, bytes, timeout, fixSize);
		Mutex.unlock();
		return bytes;
	}

	public synchronized int read(byte[] out, int waittime, int endDataTimeout) {
		return read(out, waittime, endDataTimeout, -1);
	}

	public synchronized int read(byte[] out, int waittime, int endDataTimeout,
			int requestLength) {
		// ������ʱ����
		ConnectedThread r;
		Mutex.lock();
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return 0;
			r = mConnectedThread;
		}
		int length = r.read(out, waittime, endDataTimeout, requestLength);
		Mutex.unlock();
		return length;
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		setState(STATE_LISTEN);
		mHandler.sendEmptyMessage(CONNECTION_FAIL);
		bConnect_State = false;
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		setState(STATE_LISTEN);
		mHandler.sendEmptyMessage(CONNECTION_LOST);
		bConnect_State = false;
	}

	/**
	 * ����ͬʱ������������ӡ�������Ϊ ϲ��һ���������˵Ŀͻ��ˡ�������ֱ�����ӱ����� ����ȡ������
	 */
	private class AcceptThread extends Thread {
		// ���ط������׽���
		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {

			BluetoothServerSocket tmp = null;

			// ����һ���µ������������׽���
			try {
				tmp = mAdapter
						.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "listen() failed", e);
			}
			mmServerSocket = tmp;
		}

		public void run() {
			if (D)
				Log.d(TAG, "BEGIN mAcceptThread" + this);
			setName("AcceptThread");
			BluetoothSocket socket = null;

			// ���������׽����������û������
			while (mState != STATE_CONNECTED) {
				try {
					// ����һ���������úͽ�ֻ����һ��
					// �ɹ������ӻ�����
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					Log.e(TAG, "accept() failed", e);
					break;
				}

				// ������ӱ�����
				if (socket != null) {
					synchronized (BluetoothChatService.this) {
						switch (mState) {
						case STATE_LISTEN:
						case STATE_CONNECTING:
							// ��������������������ơ�
							connected(socket, socket.getRemoteDevice());
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							// û��׼���������ӡ��²�����ֹ��
							try {
								socket.close();
							} catch (IOException e) {
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
			if (D)
				Log.i(TAG, "END mAcceptThread");
		}

		public void cancel() {
			if (D)
				Log.d(TAG, "cancel " + this);
			try {
				mmServerSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of server failed", e);
			}
		}
	}

	/**
	 * ��������ͼʹ������ϵ ���豸������ֱ�������ӣ����� �ɹ���ʧ�ܡ�
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			// �õ�һ��bluetoothsocketΪ������
			// ���������豸
			try {
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectThread");
			setName("ConnectThread");

			// ����ȡ���ķ��֣���Ϊ�����������
			mAdapter.cancelDiscovery();

			// ʹһ�����ӵ�bluetoothsocket
			try {
				// ����һ���������úͽ�ֻ����һ��
				// �ɹ������ӻ�����
				mmSocket.connect();
				mHandler.sendEmptyMessage(CONNECTION_SUCCESS);
				bConnect_State = true;
			} catch (IOException e) {
				connectionFailed();
				// �ر����socket
				try {
					mmSocket.close();
				} catch (IOException e2) {
					Log.e(TAG,
							"unable to close() socket during connection failure",
							e2);
				}
				// ����������������������ģʽ
				BluetoothChatService.this.start();
				return;
			}

			// ��Ϊ����������connectthread��λ
			synchronized (BluetoothChatService.this) {
				mConnectThread = null;
			}

			// ���������߳�
			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	/**
	 * ������������Զ���豸�� ���������д���ʹ����Ĵ��䡣
	 */
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		byte[] mmRecvbuffer = new byte[1024 * 40];
		int mmbytes = 0;
		int time = 0;

		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// ���bluetoothsocket���������
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "û�д�����ʱsockets", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;

		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			byte[] buffer = new byte[1024 * 2];
			int bytes;

			// ������InputStreamͬʱ����
			while (true) {
				try {
					// ��ȡ������
					bytes = mmInStream.read(buffer);
					// mmRecvbuffer += buffer;

					System.arraycopy(buffer, 0, mmRecvbuffer, mmbytes, bytes);
					mmbytes += bytes;
					Log.i("whw", "input stream mmbytes=" + mmbytes
							+ "     current read=" + bytes);
					// ι��
					time = 0;
					// ���ͻ�õ��ֽڵ��û�����
					// Log.i("whw", "mmRecvbuffer hex="+new
					// String(mmRecvbuffer));
				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
					connectionLost();
					break;
				}
			}
		}

		/**
		 * д��������ӡ�
		 * 
		 * @param buffer
		 *            ����һ���ֽ���
		 */
		
		private long lastTime;
		private long currentTime;
		private boolean isTimeOut;
		private static final long SLEEP_TIME = 1000*60*2;
		
		
		public synchronized void write(byte[] buffer) {
			mmbytes = 0;
			lastTime = currentTime;
			currentTime = System.currentTimeMillis();
			
			byte[] wake = { '#' };
			byte[] ack  = {0};
			isTimeOut = false ;
			
			if(currentTime - lastTime >= SLEEP_TIME  ){
				for (int i = 0; i < 6; i++) {
					try
					  {
					   Log.i(TAG, "Send wake up char '#' ");
					   mmOutStream.write(wake);
					    read(ack, 300, 400,1);
						if(ack[0] == 'W') 
						{
							
							mmRecvbuffer[0] = 0 ;
							mmbytes = 0;
							
							if (D)
								Log.i(TAG, "Get ACK char 'W' ");
							isTimeOut = false ;
							break ;
						}
						else if( i == 6 )
						{
							isTimeOut = true ;
							break;
						}
					}
					catch (IOException e) {
						Log.e(TAG, "Exception during write", e);
				}
		
			 }
				
				
				
			}	
			
			if(!isTimeOut)
			{
				mmbytes = 0;
				
				try {
					Log.i("whw", "write hex=" + DataUtils.toHexString(buffer));
					mmOutStream.write(buffer);
				} catch (IOException e) {
					Log.e(TAG, "Exception during write", e);
				}
			}

		}

		public synchronized int read(byte[] buffer, int bytes) {
			// int time = 0;
			int count = 0;
			boolean shutdown = false;
			mmbytes = 0;

			for (int i = 0; i < 1024; i++)
				mmRecvbuffer[i] = 0;
			// �������Ź�����ʼ��ʱ
			try {
				while (!shutdown) {
					Thread.sleep(100);
					time++;
					if (mmbytes > 1) {
						if (time > 15)
							shutdown = true;
					} else {
						if (time > 30)
							shutdown = true;

					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// ����

			// bytes = mmInStream.read(buffer);
			// ʱ�䵽�ˣ���ȡ����
			if (bytes > mmbytes)
				bytes = mmbytes;

			// System.out.println("read mmRecvbuffer="+mmRecvbuffer+
			// " mmbytes=  "+mmbytes+"mmRecvbuffer[0]="+mmRecvbuffer[0]+"mmRecvbuffer[1] "+mmRecvbuffer[1]);

			System.arraycopy(mmRecvbuffer, 0, buffer, 0, bytes);
			// System.out.println("read buffer="+buffer+
			// " buffer=  "+buffer+"buffer[0]="+buffer[0]+"buffer[1] "+buffer[1]);

			System.out.println("read mmRecvbuffer=" + mmRecvbuffer
					+ " mmbytes=  " + mmbytes + "mmRecvbuffer[0]="
					+ mmRecvbuffer[0] + "mmRecvbuffer[1] " + mmRecvbuffer[1]);
			try {
				System.out.println("read mmRecvbuffer=" + mmRecvbuffer
						+ " mmbytes=  " + mmbytes + "buffer="
						+ new String(buffer, 1, buffer.length - 1, "GB2312"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return bytes;
		}

		public synchronized int readex(byte[] buffer, int bytes, int timeout,
				boolean fixSize) {
			int time = 0;
			boolean shutdown = false;
			mmbytes = 0;
			// while (mmbytes < bytes) {
			// �������Ź�����ʼ��ʱ
			try {
				while (!shutdown) {
					Thread.sleep(100);
					time++;

					if (mmbytes > 1) {
						if (time > timeout / 1000)
							shutdown = true;
					} else {
						if (time > timeout / 100)
							shutdown = true;

					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// ʱ�䵽�ˣ���ȡ����
			if (bytes > mmbytes)
				bytes = mmbytes;
			System.arraycopy(mmRecvbuffer, 0, buffer, 0, bytes);
			return bytes;
		}

		
	public synchronized int read(byte[] buffer, int waitTime ,int waitTimeout, int requestLength ) {
		    
			int lastGetBytes = mmbytes ;
			int timeOut = 0 ;
		
			long lastTime = System.currentTimeMillis();
			
			
			if(mmbytes > 0 )
			{
				timeOut = waitTimeout ;
			}
			else
			{
				timeOut = waitTime ;
			}	
			
			
			while(true)
			{
 
				
				if(requestLength > -1 &&  mmbytes >= requestLength  )
				{
					System.arraycopy(mmRecvbuffer, 0, buffer, 0, requestLength);
					
					return requestLength ;
				}
				
				if( System.currentTimeMillis()- lastTime >= timeOut  )
				{
					System.arraycopy(mmRecvbuffer, 0, buffer, 0, mmbytes);
					return  mmbytes ;
				}
				
				if( mmbytes > lastGetBytes )
				{
					lastGetBytes = mmbytes ;
					timeOut = waitTimeout ;
					lastTime = System.currentTimeMillis() ;
				}
				
			}

		}


		
		
		
		
		
//		public synchronized int read(byte[] buffer, int waittime,
//				int endDataTimeout, int requestLength) {
//			int sleepTime = 10;
//			int length = waittime / sleepTime;
//			boolean shutDown = false;
//			int[] readDataLength = new int[3];
//			for (int i = 0; i < length; i++) {
//				if (mmbytes == 0) {
//					SystemClock.sleep(sleepTime);
//					continue;
//				} else {
//					break;
//				}
//			}
//
//			if (mmbytes > 0) {
//				while (!shutDown) {
//					if(requestLength ==-1){
////						SystemClock.sleep(endDataTimeout / 3);
////						readDataLength[0] = readDataLength[1];
////						readDataLength[1] = readDataLength[2];
////						readDataLength[2] = mmbytes;
////						Log.i("whw", "read2    mmbytes=" + mmbytes);
////						if (readDataLength[0] == readDataLength[1]
////								&& readDataLength[1] == readDataLength[2]) {
////							shutDown = true;
////						}
//						shutDown = isTimeOut(readDataLength, endDataTimeout);
//					}else{
//						if(mmbytes == requestLength){
//							shutDown = true;
//						}else{
//							shutDown = isTimeOut(readDataLength, endDataTimeout);
//						}
//					}
//
//				}
//				if (mmbytes <= buffer.length) {
//					System.arraycopy(mmRecvbuffer, 0, buffer, 0, mmbytes);
//				}
//			}
//			return mmbytes;
//		}
		
		private boolean isTimeOut(int[] readDataLength,int endDataTimeout){
			SystemClock.sleep(endDataTimeout / 3);
			readDataLength[0] = readDataLength[1];
			readDataLength[1] = readDataLength[2];
			readDataLength[2] = mmbytes;
			Log.i("whw", "read2    mmbytes=" + mmbytes);
			if (readDataLength[0] == readDataLength[1]
					&& readDataLength[1] == readDataLength[2]) {
				return true;
			}else{
				return false;
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

}
