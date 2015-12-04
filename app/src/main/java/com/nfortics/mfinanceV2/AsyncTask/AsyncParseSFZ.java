package com.nfortics.mfinanceV2.AsyncTask;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.nfortics.mfinanceV2.logic.CommunicateManager;
import com.nfortics.mfinanceV2.logic.ParseSFZAPI;

import static com.nfortics.mfinanceV2.logic.ParseSFZAPI.*;


public class AsyncParseSFZ extends Handler {

	private static final int READ_SFZ = 1000;
	private static final int FIND_CARD_SUCCESS = 1001;
	private static final int FIND_CARD_FAIL = 1002;

	public static final int DATA_SIZE = 1295;

	private ParseSFZAPI parse;

	private Handler mWorkerThreadHandler;

	private OnReadSFZListener onReadSFZListener;

	public void setOnReadSFZListener(OnReadSFZListener onReadSFZListener) {
		this.onReadSFZListener = onReadSFZListener;
	}

	public interface OnReadSFZListener {
		void onReadSuccess(People people);

		void onReadFail(int confirmationCode);
	}

	public AsyncParseSFZ(Looper looper, CommunicateManager communicateManager) {
		mWorkerThreadHandler = createHandler(looper);
		parse = new ParseSFZAPI(communicateManager);
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
			case READ_SFZ:
				Result resultSFZ = parse.read();
				if (resultSFZ.confirmationCode == Result.SUCCESS) {
					AsyncParseSFZ.this.obtainMessage(FIND_CARD_SUCCESS,
							resultSFZ.resultInfo).sendToTarget();
				} else {
					AsyncParseSFZ.this.obtainMessage(FIND_CARD_FAIL,
							resultSFZ.confirmationCode, -1).sendToTarget();
				}
				break;
			default:
				break;
			}
		}
	}

	public void readSFZ() {
		mWorkerThreadHandler.obtainMessage(READ_SFZ).sendToTarget();
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case FIND_CARD_SUCCESS:
			if (onReadSFZListener != null) {
				onReadSFZListener.onReadSuccess((People) msg.obj);
			}
			break;
		case FIND_CARD_FAIL:
			if (onReadSFZListener != null) {
				onReadSFZListener.onReadFail(msg.arg1);
			}
			break;
		default:
			break;
		}
	}

}
