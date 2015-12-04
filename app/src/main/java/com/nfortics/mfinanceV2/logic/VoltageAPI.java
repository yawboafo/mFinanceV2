package com.nfortics.mfinanceV2.logic;

import android.os.Handler;
import android.util.Log;

import com.nfortics.mfinanceV2.Utilities.DataUtils;


public class VoltageAPI extends Handler {

	//����
//	private Lock Mutex = null;
	private static final int GET_BATTERY_PERCENT = 0;
	private static int				index			= 0;
		
	public static final int BATTERY_DEFAULT = 100;
	public static final int BATTERY_POWER_LOW = 101;
	public static final int BATTERY_TWOBLOCK = 102;
	public static final int BATTERY_THREEBLOCK = 103;
	public static final int BATTERY_FOURBLOCK = 104;
	public static final int BATTERY_POWER_FULL = 105;
	public static final int OFFSET = 100;
	
	//private BluetoothChatService mChatService;
	private CommunicateManager communicateManager;
	
	public VoltageAPI(CommunicateManager communicateManager) {
		this.communicateManager = communicateManager;
	}
	
	public int getBatteryPercent()
	{
		int value;
//		if(Mutex == null)
//			return -1;
//		Mutex.lock();

		value = getADCValue();

//		Mutex.unlock();
		if(value>720)
			return ((value-720)/2);
		else if(value==720)
			return 1;
		else if((value<720)&&(value>0))
			return 0;
		else if(value==0)
			return -1;
		else 
			return -1;
	}
	
	public int getADCValue()
	{
		int AdcValue;
		byte[] command = {0x03};
		byte[] buffer = new byte[10];
		//byte[] buffer = {0x31,0x32,0x33};
		String strAdc = null;
		
		if(Voltage_send_recv(command,buffer)==true)
		{
			try{
				strAdc = new String(buffer);//UTF-8
				System.out.println("strAdc="+strAdc);
				Log.i("whw", "AdcValue hex=" + DataUtils.toHexString(buffer));
				AdcValue= Integer.parseInt(strAdc.trim());
			}
			catch(NumberFormatException ex)
			{
				System.out.print("����Ĳ���������ֵ");
				AdcValue = 0;
			} 
			System.out.println("AdcValue="+AdcValue);
			
		}
		else
		{
			AdcValue = 0;
			
		}
		System.out.println("AdcValue="+AdcValue);
		return AdcValue;
	}
	
	public boolean Voltage_send_recv(byte[] RequestData, byte[] ResponseData)
	{
		//Mutex.lock();  
		if(communicateManager != null)
		{
			communicateManager.write(RequestData);
			int bytes = communicateManager.read(ResponseData, ResponseData.length);
		//	Mutex.unlock();  
			if(bytes > 0)
				return true;
			else
				return false;
		}
		else
		{
		//	Mutex.unlock();  
			return false;
		}
	}

}