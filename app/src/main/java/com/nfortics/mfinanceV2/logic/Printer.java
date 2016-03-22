package com.nfortics.mfinanceV2.logic;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class Printer {
	public static final int PRINT_COMPLETE = 1;
	public static final int PRINT_NO_PAPER = 2;
	public static final int PRINT_TIMEOUT = 3;

	private byte[] buffer = new byte[1024];

	// �������£�
	// ������ӡ ��IND_PRINTER_BUSY IND_PRINTER_BUSY ....... IND_PRINTER_IDLE
	// ��ӡʱȱֽ��IND_PRINTER_BUSY IND_PRINTER_BUSY .... IND_PAPER_NONE
	// IND_PRINTER_IDLE

	/**
	 * ��ʾȱֽ
	 */
	private static final byte IND_PAPER_NONE = 0x21;
	/**
	 * ��ʾ��ӡ�����ڹ����������Ϣ���ڴ�ӡ�����г�������(ĿǰΪÿ�з���)��ֱ����������
	 */
	private static final byte IND_PRINTER_BUSY = 0x22;
	/**
	 * ��ʾ��ӡ������������ȱֽʱ��ӡ��Ҳ������״̬
	 */
	private static final byte IND_PRINTER_IDLE = 0x23;

	private CommunicateManager communicateManager;

	public Printer(CommunicateManager communicateManager) {
		this.communicateManager = communicateManager;
	}

	public int print(String printerBuffer) {
		byte[] bprinterBuffer = null;
		int currentReceiveLength = 0;
		int lastReceiveLength = 0;
		int getAckFail_count = 0 ;
		
		if (TextUtils.isEmpty(printerBuffer)) {
			return PRINT_TIMEOUT;
		} else {
			printerBuffer += "\r\n\r\n\r\n";
		}
		try {
			bprinterBuffer = printerBuffer.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int length = bprinterBuffer.length;
		Log.i("whw", "bprinterBuffer.length=" + bprinterBuffer.length);
		byte[] bytes = new byte[length + 7];
		bytes[0] = 0x01;
		bytes[1] = 0x00;
		bytes[2] = 0x00;
		bytes[3] = 0x00;

		bytes[4] = 0;// (byte) ((byte)(bprinterBuffer.length/256)+1);
		bytes[5] = (byte) (bprinterBuffer.length / 256);
		bytes[6] = (byte) (bprinterBuffer.length % 256);

		System.arraycopy(bprinterBuffer, 0, bytes, 7, bprinterBuffer.length);
		
		int count = bytes.length%45==0?bytes.length/45:(bytes.length/45+1);
		byte[] temp = new byte[45];
		for (int i = 0; i < count; i++) {
			if(i==count-1){
				temp = new byte[bytes.length-i*45];
				System.arraycopy(bytes, i * 45, temp, 0, temp.length);
			}else{
				System.arraycopy(bytes, i * 45, temp, 0, temp.length);
			}
			communicateManager.write(temp);
			SystemClock.sleep(2);
			
		}
		
		
//		communicateManager.write(bytes);
		
		
//		int receiveLength = communicateManager.read(buffer, 1000, 200);
//		if (receiveLength > 0) {
//			if (buffer[receiveLength - 1] == IND_PRINTER_IDLE) {
//				Log.i("whw", "hex=" + DataUtils.toHexString(buffer));
//				Log.i("whw", "IND_PRINTER_IDLE");
//				if (receiveLength > 2
//						&& buffer[receiveLength - 2] == IND_PAPER_NONE) {
//					Log.i("whw", "aaaaaaaaaaaaaaa");
//					return PRINT_NO_PAPER;
//				} else {
//					Log.i("whw", "bbbbbbbbbbbbbbb");
//					return PRINT_COMPLETE;
//				}
//			}
//		}
	

		while(true)
		{
	
			 currentReceiveLength = communicateManager.read(buffer, 3000, 1000);
		  
			  if(currentReceiveLength > lastReceiveLength )
			  {
				  if(buffer[currentReceiveLength -1 ] == IND_PRINTER_IDLE )
				  {
					  
					    if( currentReceiveLength >= 2 && buffer[currentReceiveLength - 2] == IND_PAPER_NONE )
					    {
					    	Log.i("liujun", "PRINT_NO_PAPER");
							  return PRINT_NO_PAPER ;
					    }
					    else
					    {
					    	 Log.i("liujun", "PRINT_COMPLETE");
							  return PRINT_COMPLETE ;
					    }	
						 
				  }
  
				  lastReceiveLength = currentReceiveLength ;
				  getAckFail_count = 0 ;
				  
			  }
			  else
			  {
				  getAckFail_count++;
				  Log.i("liujun", "getAckFail_count=" + getAckFail_count);
			  }

			  if(getAckFail_count >= 3)
			  {
				  Log.i("liujun", "getAckFail_count >= 3");
				  getAckFail_count = 0 ;
				  return PRINT_TIMEOUT ;
			  }
		}
		
		
		
	}
}