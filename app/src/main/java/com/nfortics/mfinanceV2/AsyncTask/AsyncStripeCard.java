package com.nfortics.mfinanceV2.AsyncTask;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.nfortics.mfinanceV2.logic.CommunicateManager;
import com.nfortics.mfinanceV2.logic.StripeCardAPI;


public class AsyncStripeCard extends Handler {

	private static final int READ_STRIPE_CARD = 1;
	
	

	private Handler mWorkerThreadHandler;
	
	private StripeCardAPI stripeCardAPI;
	

	private OnReadStripeCardListener onReadStripeCardListener;
	

	public void setOnReadStripeCardListener(OnReadStripeCardListener onReadStripeCardListener) {
		this.onReadStripeCardListener = onReadStripeCardListener;
	}

	public interface OnReadStripeCardListener {
		void onReadStripeCardSuccess(String[] data);

		void onReadStripeCardFail();
	}

	public AsyncStripeCard(Looper looper,CommunicateManager communicateManager) {
		mWorkerThreadHandler = createHandler(looper);
		stripeCardAPI = new StripeCardAPI(communicateManager);
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
			case READ_STRIPE_CARD:
				String[] data = stripeCardAPI.read();
				AsyncStripeCard.this.obtainMessage(READ_STRIPE_CARD, data).sendToTarget();
				break;
			default:
				break;
			}
		}
	}

	public void readStripeCard() {
		mWorkerThreadHandler.obtainMessage(READ_STRIPE_CARD).sendToTarget();
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case READ_STRIPE_CARD:
			if(onReadStripeCardListener != null){
				if(msg.obj != null){
					onReadStripeCardListener.onReadStripeCardSuccess((String[])msg.obj);
				}else{
					onReadStripeCardListener.onReadStripeCardFail();
				}
			}
			break;
		default:
			break;
		}
	}






	

}
