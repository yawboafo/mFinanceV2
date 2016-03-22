package com.nfortics.mfinanceV2.AsyncTask;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.nfortics.mfinanceV2.logic.CommunicateManager;
import com.nfortics.mfinanceV2.logic.VoltageAPI;


public class AsyncVoltage extends Handler {

	// ����
	// private Lock Mutex = null;
	private static final int GET_BATTERY_PERCENT = 0;

	// private BluetoothChatService mChatService;
	// private CommunicateManager communicateManager;
	// private Handler mHandler = null;

	private Handler mWorkerThreadHandler;

	private VoltageAPI Voltage;

	private OnGetBatteryValueListener OnGetBatteryValueListener;

	public AsyncVoltage(Looper looper, CommunicateManager communicateManager) {
		mWorkerThreadHandler = createHandler(looper);
		Voltage = new VoltageAPI(communicateManager);
	}

	protected Handler createHandler(Looper looper) {
		return new WorkerHandler(looper);
	}

	protected class WorkerHandler extends Handler {
		public WorkerHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case GET_BATTERY_PERCENT:
				int Value = Voltage.getBatteryPercent();
				System.out.println("GET_BATTERY_PERCENT Value=" + Value);
				if (OnGetBatteryValueListener != null) {
					System.out.println("GET_BATTERY_PERCENT1 Value=" + Value);
					OnGetBatteryValueListener.onGetBatteryValue(Value);
				}
				break;

			default:
				break;
			}
		}
	}

	public void getBatteryValue() {
		mWorkerThreadHandler.obtainMessage(GET_BATTERY_PERCENT).sendToTarget();
	}

	public interface OnGetBatteryValueListener {
		public void onGetBatteryValue(int Value);
	}

	public void setOnGetBatteryValueListener(
			OnGetBatteryValueListener onGetBatteryValueListener2) {
		System.out.println("onGetBatteryValueListener="
				+ onGetBatteryValueListener2);
		this.OnGetBatteryValueListener = onGetBatteryValueListener2;
	}

}