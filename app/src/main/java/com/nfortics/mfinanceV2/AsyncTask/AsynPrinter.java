package com.nfortics.mfinanceV2.AsyncTask;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.nfortics.mfinanceV2.logic.BluetoothChatService;
import com.nfortics.mfinanceV2.logic.CommunicateManager;
import com.nfortics.mfinanceV2.logic.Printer;


public class AsynPrinter extends Handler {

	// ����
	private static final int PRINTER_STRING = 0;

	private BluetoothChatService mChatService;

	// private Handler mHandler = null;
	private Printer printer;

	private Handler mWorkerThreadHandler;

	private OnPrinterListener onPrinterListener;

	public AsynPrinter(Looper looper, CommunicateManager communicateManager) {
		mWorkerThreadHandler = createHandler(looper);
		printer = new Printer(communicateManager);
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
			case PRINTER_STRING:
				int result = printer.print((String) msg.obj);
				if (onPrinterListener != null) {
					if (result == Printer.PRINT_COMPLETE) {
						onPrinterListener.onPrinterSuccess();
					} else {
						onPrinterListener.onPrinterFail(result);
					}
				}
				break;
			}
		}
	}

	public void Printf(String Buffer) {
		mWorkerThreadHandler.obtainMessage(PRINTER_STRING, (String) Buffer)
				.sendToTarget();
	}

	public interface OnPrinterListener {
		public void onPrinterSuccess();

		public void onPrinterFail(int code);
	}

	public void setOnPrinterListener(OnPrinterListener onPrinterListener) {
		this.onPrinterListener = onPrinterListener;
	}

}