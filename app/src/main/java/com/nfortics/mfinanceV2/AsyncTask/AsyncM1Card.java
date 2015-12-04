package com.nfortics.mfinanceV2.AsyncTask;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.nfortics.mfinanceV2.logic.CommunicateManager;
import com.nfortics.mfinanceV2.logic.M1CardAPI;
import com.nfortics.mfinanceV2.logic.M1CardAPI.Result;

public class AsyncM1Card extends Handler {

	private static final int READ_CARD_NUM = 1;

	private static final int WRITE_DATA = 2;

	private static final int READ_DATA = 3;

	private static final int WRITE_AT_POSITION_DATA = 4;

	private static final int READ_AT_POSITION_DATA = 5;

	private static final String POSITION_KEY = "position";
	private static final String KEY_TYPE_KEY = "keyType";
	private static final String PASSWORD_KEY = "password";
	private static final String DATA_KEY = "data";

	public static final int KEY_A = 1;
	public static final int KEY_B = 2;

	private Handler mWorkerThreadHandler;


	private M1CardAPI reader;


	public static final int WRITE = 1;
	public static final int READ_INFO = 3;



	public AsyncM1Card(Looper looper, CommunicateManager communicateManager) {
		mWorkerThreadHandler = createHandler(looper);
		reader = new M1CardAPI(communicateManager);
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
			case READ_CARD_NUM:
				Result result = reader.readCard();
				AsyncM1Card.this.obtainMessage(READ_CARD_NUM, result)
						.sendToTarget();
				break;
			case WRITE_DATA:
				Result writeResult = reader.write((byte[][]) msg.obj);
				AsyncM1Card.this.obtainMessage(WRITE_DATA, writeResult)
						.sendToTarget();
				break;
			case READ_DATA:
				AsyncM1Card.this.obtainMessage(READ_DATA, reader.read()).sendToTarget();
				break;
			case WRITE_AT_POSITION_DATA:
				Bundle writeBundle = msg.getData();
				Result writeAtPositionResult = reader.writeAtPosition(
						writeBundle.getInt(POSITION_KEY),
						writeBundle.getInt(KEY_TYPE_KEY),
						writeBundle.getByteArray(PASSWORD_KEY),
						writeBundle.getByteArray(DATA_KEY));
				AsyncM1Card.this.obtainMessage(WRITE_AT_POSITION_DATA,
						writeAtPositionResult).sendToTarget();
				break;
			case READ_AT_POSITION_DATA:
				Bundle readBundle = msg.getData();
				Result readAtPositionResult = reader.readAtPosition(
						readBundle.getInt(POSITION_KEY),
						readBundle.getInt(KEY_TYPE_KEY),
						readBundle.getByteArray(PASSWORD_KEY));
				AsyncM1Card.this.obtainMessage(READ_AT_POSITION_DATA,
						readAtPositionResult).sendToTarget();
				break;
			default:
				break;
			}
		}
	}

	private OnReadCardNumListener onReadCardNumListener;

	private OnReadDataListener onReadDataListener;

	private OnWriteDataListener onWriteDataListener;

	private OnReadAtPositionListener onReadAtPositionListener;

	private OnWriteAtPositionListener onWriteAtPositionListener;

	public void setOnReadCardNumListener(
			OnReadCardNumListener onReadCardNumListener) {
		this.onReadCardNumListener = onReadCardNumListener;
	}

	public void setOnReadDataListener(OnReadDataListener onReadDataListener) {
		this.onReadDataListener = onReadDataListener;
	}

	public void setOnWriteDataListener(OnWriteDataListener onWriteDataListener) {
		this.onWriteDataListener = onWriteDataListener;
	}

	public void setOnReadAtPositionListener(
			OnReadAtPositionListener onReadAtPositionListener) {
		this.onReadAtPositionListener = onReadAtPositionListener;
	}

	public void setOnWriteAtPositionListener(
			OnWriteAtPositionListener onWriteAtPositionListener) {
		this.onWriteAtPositionListener = onWriteAtPositionListener;
	}

	public interface OnReadCardNumListener {
		public void onReadCardNumSuccess(String num);

		public void onReadCardNumFail(int comfirmationCode);
	}

	public interface OnReadDataListener {
		public void onReadDataSuccess(byte[][] data);

		/**
		 * ȷ���� 1: �ɹ� 2��Ѱ��ʧ�� 3����֤ʧ�� 4:д��ʧ�� 5����ʱ 6�������쳣
		 * 
		 * @param comfirmationCode
		 */
		public void onReadDataFail(int comfirmationCode);
	}

	public interface OnWriteDataListener {
		public void onWriteDataSuccess();

		/**
		 * ȷ���� 1: �ɹ� 2��Ѱ��ʧ�� 3����֤ʧ�� 4:д��ʧ�� 5����ʱ 6�������쳣
		 * 
		 * @param comfirmationCode
		 */
		public void onWriteDataFail(int comfirmationCode);
	}

	public interface OnReadAtPositionListener {
		public void onReadAtPositionSuccess(byte[] data);

		/**
		 * ȷ���� 1: �ɹ� 2��Ѱ��ʧ�� 3����֤ʧ�� 4:д��ʧ�� 5����ʱ 6�������쳣
		 * 
		 * @param comfirmationCode
		 */
		public void onReadAtPositionFail(int comfirmationCode);
	}

	public interface OnWriteAtPositionListener {
		public void onWriteAtPositionSuccess();

		/**
		 * ȷ���� 1: �ɹ� 2��Ѱ��ʧ�� 3����֤ʧ�� 4:д��ʧ�� 5����ʱ 6�������쳣
		 * 
		 * @param comfirmationCode
		 */
		public void onWriteAtPositionFail(int comfirmationCode);
	}


	public void readCardNum() {
		mWorkerThreadHandler.obtainMessage(READ_CARD_NUM).sendToTarget();
	}

	public void readData() {
		mWorkerThreadHandler.obtainMessage(READ_DATA).sendToTarget();
	}

	public void writeData(byte[]... data) {
		mWorkerThreadHandler.obtainMessage(WRITE_DATA, data).sendToTarget();
	}


	

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case READ_CARD_NUM:
			Result numResult = (Result) msg.obj;
			if (onReadCardNumListener != null) {
				if (numResult != null
						&& numResult.confirmationCode == Result.SUCCESS) {
					onReadCardNumListener
							.onReadCardNumSuccess((String) numResult.resultInfo);
				} else {
					onReadCardNumListener
							.onReadCardNumFail(numResult.confirmationCode);
				}
			}
			break;
		case READ_DATA:
			Result readResult = (Result) msg.obj;
			byte[][] data = (byte[][]) readResult.resultInfo;
			if (onReadDataListener != null) {
				if (data != null) {
					onReadDataListener.onReadDataSuccess(data);
				} else {
					onReadDataListener
							.onReadDataFail(readResult.confirmationCode);
				}
			}
			break;
		case WRITE_DATA:
			if (onWriteDataListener != null) {
				Result result = (Result) msg.obj;
				if (result != null && result.confirmationCode == Result.SUCCESS) {
					onWriteDataListener.onWriteDataSuccess();
				} else {
					onWriteDataListener
							.onWriteDataFail(result.confirmationCode);
				}
			}
			break;
		case WRITE_AT_POSITION_DATA:
			if (onWriteAtPositionListener != null) {
				Result result = (Result) msg.obj;
				if (result != null && result.confirmationCode == Result.SUCCESS) {
					onWriteAtPositionListener.onWriteAtPositionSuccess();
				} else {
					onWriteAtPositionListener
							.onWriteAtPositionFail(result.confirmationCode);
				}
			}
			break;
		case READ_AT_POSITION_DATA:
			Result readPositionResult = (Result) msg.obj;
			byte[] readPositionData = (byte[]) readPositionResult.resultInfo;
			if (onReadAtPositionListener != null) {
				if (readPositionData != null) {
					onReadAtPositionListener
							.onReadAtPositionSuccess(readPositionData);
				} else {
					onReadAtPositionListener
							.onReadAtPositionFail(readPositionResult.confirmationCode);
				}
			}
			break;
		default:
			break;
		}
	}
	

	/**
	 * 
	 * @param position
	 *            д�����ݵĿ��
	 * @param password
	 *            ����Ϊ��ʱ����ʹ��Ĭ�ϵ����뼴24����f��
	 * @param keyType
	 *            �������ͣ�����A������B
	 * @param data
	 *            д������ݲ���Ϊ�գ�data�ĳ��ȱ���С�ڵ���16�ֽڣ���һ����ֻ�ܴ��16�ֽڵ�����,���鲻��16�ֽ� ��0����
	 * @return
	 */
	public void write(int position, int keyType, byte[] password, byte[] data) {
		Message msg = mWorkerThreadHandler
				.obtainMessage(WRITE_AT_POSITION_DATA);
		Bundle bundle = new Bundle();
		bundle.putInt(POSITION_KEY, position);
		bundle.putInt(KEY_TYPE_KEY, keyType);
		bundle.putByteArray(PASSWORD_KEY, password);
		bundle.putByteArray(DATA_KEY, data);
		msg.setData(bundle);
		msg.sendToTarget();
	}

	/**
	 * 
	 * @param position
	 *            д�����ݵĿ��
	 * @param password
	 *            ����Ϊ��ʱ����ʹ��Ĭ�ϵ����뼴24����f��
	 */
	public void read(int position, int keyType, byte[] password) {
		Message msg = mWorkerThreadHandler.obtainMessage(READ_AT_POSITION_DATA);
		Bundle bundle = new Bundle();
		bundle.putInt(POSITION_KEY, position);
		bundle.putInt(KEY_TYPE_KEY, keyType);
		bundle.putByteArray(PASSWORD_KEY, password);
		msg.setData(bundle);
		msg.sendToTarget();

	}



}
