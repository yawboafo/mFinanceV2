package com.nfortics.mfinanceV2.logic;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;


import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.AsyncTask.AsyncVoltage;
import com.nfortics.mfinanceV2.R;

import java.util.concurrent.ExecutorService;

public class batteryComponent {
	/* ����Ҫ��ʾ��ͼƬ��Դ���� */
	private static final Integer[] imagelist = {
			//R.drawable.defaultstatus,
		//	R.drawable.batterypowerlow,
			//R.drawable.twoblock,
			//R.drawable.threeblock,
			//R.drawable.fourblock,
			//R.drawable.powerfull,
			};

	private static int index = 0;
	ExecutorService ThreadPool = null;
	private AsyncVoltage asyncVoltage = null;
	private Activity mcurrentActivity = null;
	public static final int BATTERY_DEFAULT = 100;
	public static final int BATTERY_POWER_LOW = 101;
	public static final int BATTERY_TWOBLOCK = 102;
	public static final int BATTERY_THREEBLOCK = 103;
	public static final int BATTERY_FOURBLOCK = 104;
	public static final int BATTERY_POWER_FULL = 105;
	public static final int OFFSET = 100;

	// ImageView�����ID
	private static final int ImageView_ID = 0x123458;
	// ����ImageView����
	private ImageView imageview = null;
	private TextView textview = null;
	private Handler mHandler = null;// new Handler();
	// �ؼ��߳�
	boolean isrung = false;

	public void setActivity(Activity currentActivity) {
		mcurrentActivity = currentActivity;
		updateActivity();
		updateBattery();
	}

	public void updateActivity() {
		// ����һ�����Բ���LinearLayout
		// LinearLayout main_view = new LinearLayout(this);
		// LinearLayout main_view = new LinearLayout(this);
		// ����imageview����
		// imageview = new ImageView(this);
		// ���ImageView�Ķ���
		// imageview = (ImageView) mcurrentActivity.findViewById(R.id.battery);
		// textview = (TextView)
		// mcurrentActivity.findViewById(R.id.title_left_text);
		// �����Բ��������ImageSwitcher��ͼ
		// main_view.addView(imageview);
		// ����ImageSwitcher�����ID
		// imageview.setId(ImageView_ID);
		// ����ImageSwitcher���������Դ
		// m_Switcher.setFactory(this);
		// imageview.setImageResource(imagelist[index]);
		// ������ʾ���洴�������Բ���
		// setContentView(main_view);
	}

	public batteryComponent(Application application) {
		// VolCalculate = new VoltageCalculate();

	}

	public void setVoltageListener(Application application) {

		// asyncVoltage = application.getChatService().getNewAsyncVoltage();
		//asyncVoltage = new AsyncVoltage(application.getHandlerThread().getLooper(), application.getcommunicateManager());

		asyncVoltage
				.setOnGetBatteryValueListener(new AsyncVoltage.OnGetBatteryValueListener() {
					public void onGetBatteryValue(int Percent) {
						// System.out.println("onGetBatteryValue Percent="+Percent);
						if (Percent > 80)
							index = BATTERY_POWER_FULL - OFFSET;
						else if ((Percent <= 80) && (Percent > 60))
							index = BATTERY_FOURBLOCK - OFFSET;
						else if ((Percent <= 60) && (Percent > 40))
							index = BATTERY_THREEBLOCK - OFFSET;
						else if ((Percent <= 40) && (Percent > 20))
							index = BATTERY_TWOBLOCK - OFFSET;
						else if ((Percent <= 20) && (Percent > 10))
							index = BATTERY_POWER_LOW - OFFSET;
						else if ((Percent <= 10) && (Percent > 0))
							index = BATTERY_POWER_LOW - OFFSET;
						else if (Percent <= 0)
							index = BATTERY_DEFAULT - OFFSET;
						// ������Ҫ����imageview��ͼ����Ϣ
						mHandler.sendMessage(mHandler.obtainMessage(index
								+ OFFSET));
					}
				});
	}

	public void start() {
		isrung = true;

		// ����һ���߳�����Alphaֵ�ݼ�
		new Thread(new Runnable() {
			public void run() {
				while (isrung) {
					try {
						// updateActivity();
						Thread.sleep(300000);
						// ����Alphaֵ
						updateBattery();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

		// ������Ϣ֮�����imageview��ͼ
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case BATTERY_DEFAULT:
					if (imageview != null)
						imageview.setImageResource(imagelist[BATTERY_DEFAULT
								- OFFSET]);
					break;
				case BATTERY_POWER_LOW:
					if (imageview != null)
						imageview.setImageResource(imagelist[BATTERY_POWER_LOW
								- OFFSET]);
					break;
				case BATTERY_TWOBLOCK:
					if (imageview != null)
						imageview.setImageResource(imagelist[BATTERY_TWOBLOCK
								- OFFSET]);
					break;
				case BATTERY_THREEBLOCK:
					if (imageview != null)
						imageview.setImageResource(imagelist[BATTERY_THREEBLOCK
								- OFFSET]);
					break;
				case BATTERY_FOURBLOCK:
					if (imageview != null)
						imageview.setImageResource(imagelist[BATTERY_FOURBLOCK
								- OFFSET]);
					break;
				case BATTERY_POWER_FULL:
					if (imageview != null) {
						imageview.setImageResource(imagelist[BATTERY_POWER_FULL
								- OFFSET]);
					}
					break;
				}
				if (imageview != null)
					imageview.invalidate();
			}
		};
	}

	public void getBattery() {
		if (asyncVoltage == null)
			mHandler.sendMessage(mHandler.obtainMessage(BATTERY_DEFAULT));
		else
			asyncVoltage.getBatteryValue();
		// if (index >= imagelist.length)
		// {
		// index = 0;
		// }
	}

	public void updateBattery() {
		getBattery();
	}

}
