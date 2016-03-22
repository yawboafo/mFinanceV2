package com.nfortics.mfinanceV2.logic;


public class CommunicateManager {
	public final int NO_CONNECTED = 1000;
	public final int BLUETOOTH_CONNECTED = 1001;
	public final int SERIAL_CONNECTED = 1002;

	private BluetoothChatService bluetoothchatService;
	private SerialPortService serialPortService;

	public CommunicateManager() {

	}

	public void setCommunicateService(
			BluetoothChatService bluetoothchatService,
			SerialPortService serialPortSerivce) {
		this.bluetoothchatService = bluetoothchatService;
		if (this.bluetoothchatService == null) {
			this.serialPortService = serialPortSerivce;
		} else
			this.serialPortService = null;
	}

	public synchronized void write(byte[] in) {
		if (bluetoothchatService != null) {
			if (bluetoothchatService.getState() != BluetoothChatService.STATE_CONNECTED) {
				return;
			}
			bluetoothchatService.write(in);
		} else if (serialPortService != null) {
			serialPortService.write(in);
		}
	}

	public synchronized int read(byte[] out, int bytes) {
		if (bluetoothchatService != null) {
			if (bluetoothchatService.getState() != BluetoothChatService.STATE_CONNECTED) {
				return 0;
			}
			//return bluetoothchatService.read(out, bytes);
			return bluetoothchatService.read(out, 3000,1000, bytes);
		} else if (serialPortService != null) {
			//return serialPortService.read(out, 3000, 1000);
			return serialPortService.read(out, 3000, 1000,bytes);
		}
		return 0;
	}

	/*
	 * public synchronized int readex(byte[] out, int bytes, int timeout,
	 * boolean fixSize) { if(bluetoothchatService!=null) { return
	 * bluetoothchatService.readex(out, bytes, timeout, fixSize); } else
	 * if(serialPortchatSerivce!=null) { return serialPortchatSerivce.read(out,
	 * 3000, 100); } return 0; }
	 */

	public synchronized int read(byte[] out, int waittime, int endDataTimeout) {
		// if(bluetoothchatService!=null)
		// {
		// if (bluetoothchatService.getState() !=
		// BluetoothChatService.STATE_CONNECTED) {
		// return 0;
		// }
		// return bluetoothchatService.read(out, waittime, endDataTimeout);
		// }
		// else if(serialPortService!=null)
		// {
		// return serialPortService.read(out, waittime, endDataTimeout);
		// }
		return read(out, waittime, endDataTimeout, -1);
	}

	public synchronized int read(byte[] out, int waittime, int endDataTimeout,
			int requestLength) {
		if (bluetoothchatService != null) {
			if (bluetoothchatService.getState() != BluetoothChatService.STATE_CONNECTED) {
				return 0;
			}
			return bluetoothchatService.read(out, waittime, endDataTimeout,requestLength);
		} else if (serialPortService != null) {
			return serialPortService.read(out, waittime, endDataTimeout,requestLength);
		}
		return 0;
	}
}
