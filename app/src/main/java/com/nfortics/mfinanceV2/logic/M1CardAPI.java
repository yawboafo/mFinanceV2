package com.nfortics.mfinanceV2.logic;

import android.os.SystemClock;
import android.util.Log;

import com.nfortics.mfinanceV2.Utilities.DataUtils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class M1CardAPI {
	public static boolean switchRFID = false;

	public static final int KEY_A = 1;
	public static final int KEY_B = 2;

	private static final byte[] SWITCH_COMMAND = "D&C00040104".getBytes();
	// �������ݰ���ǰ׺
	private static final String DATA_PREFIX = "c050605";
	private static final String FIND_CARD_ORDER = "01";// Ѱ��ָ��
	private static final String PASSWORD_SEND_ORDER = "02";// �����·�ָ��
	private static final String PASSWORD_VALIDATE_ORDER = "03";// ������֤����
	private static final String READ_DATA_ORDER = "04";// ��ָ��
	private static final String WRITE_DATA_ORDER = "05";// дָ��
	private static final String ENTER = "\r\n";// ���з�

	private static final String TURN_OFF = "c050602\r\n";// �ر����߳�

	// Ѱ����ָ���
	private static final String FIND_CARD = DATA_PREFIX + FIND_CARD_ORDER
			+ ENTER;

	// �·�����ָ���(A��B�������12����f��)
	private static final String SEND_PASSWORD = DATA_PREFIX
			+ PASSWORD_SEND_ORDER + "ffffffffffffffffffffffff" + ENTER;

	// //������ָ֤���
	// private static final static PASSWORD_VALIDATE =

	// private static final String FIND_SUCCESS = "c05060501" + ENTER + "0x00,";
	private static final String FIND_SUCCESS = "0x00,";

	private static final String WRITE_SUCCESS = " Write Success!" + ENTER;


	private byte[] buffer = new byte[100];

	public static final int WRITE = 1;
	public static final int READ_INFO = 3;

	public int inputLength = 0;
	public int switchLength = 0;

	//private BluetoothChatService chatService;
	private CommunicateManager communicateManager;

	public M1CardAPI(CommunicateManager communicateManager) {
		this.communicateManager = communicateManager;
	}


	public Result readCard() {
		//��cp810�ڲ�����������£��ܿ���������״̬���������ߺ�M1��״̬�Ͳ����ˣ����������л���m1״̬
		switchStatus();
		Log.i("whw", "!!!!!!!!!!!!readCard");
		Result result = new Result();
		byte[] command = FIND_CARD.getBytes();
		int length = receive(command, buffer);
		if (length == 0) {
			result.confirmationCode = Result.TIME_OUT;
			return result;
		}
		inputLength = length;
		String msg = "";
		msg = new String(buffer, 0, length);
		Log.i("whw", "msg hex=" + msg);
		turnOff();
		if (msg.startsWith(FIND_SUCCESS)) {
			result.confirmationCode = Result.SUCCESS;
			result.resultInfo = msg.substring(FIND_SUCCESS.length());
		} else {
			result.confirmationCode = Result.FIND_FAIL;
		}
		return result;
	}




	/**
	 * �л��ɶ�ȡRFID
	 * @return
	 */
	private boolean switchStatus() {
		sendCommand(SWITCH_COMMAND);
		Log.i("whw", "SWITCH_COMMAND hex=" + new String(SWITCH_COMMAND));
		SystemClock.sleep(700);
		return true;
	}

	private int receive(byte[] command, byte[] buffer) {
		int length = -1;
//		if (!switchRFID) {
//			switchStatus();
//		}
		sendCommand(command);

		length = communicateManager.read(buffer, 3000, 100);
		return length;
	}
	
	private void sendCommand(byte[] command){
		byte[] data = new byte[7 + command.length];
		data[0] = 2;
		data[1] = 0;
		data[2] = 0;
		data[3] = 0;
		data[4] = 1;
		data[5] = 0;
		data[6] = (byte) command.length;
		System.arraycopy(command, 0, data, 7, command.length);
		communicateManager.write(data);
	}


	/**
	 * Ѱ��
	 * 
	 * @return
	 */
	private boolean findCard() {
		//��cp810�ڲ�����������£��ܿ���������״̬���������ߺ�M1��״̬�Ͳ����ˣ����������л���m1״̬
		switchStatus();

		byte[] command = FIND_CARD.getBytes();
		int length = receive(command, buffer);

		String msg = new String(buffer, 0, length);
		Log.i("whw", "findCard msg=" + msg);
		if (msg.startsWith(FIND_SUCCESS)) {
			return true;
		}
		return false;
	}

	private String read(int position) {
		byte[] command = { 'c', '0', '5', '0', '6', '0', '5', '0', '4', '0',
				'0', '\r', '\n' };
		char[] c = getZoneId(position).toCharArray();
		command[9] = (byte) c[0];
		command[10] = (byte) c[1];
		int length = receive(command, buffer);
		String data = new String(buffer, 0, length);
		Log.i("whw", "read data=" + data);
		String[] split = data.split(";");
		String msg = "";
		if (split.length == 2) {
			int index = split[1].indexOf("\r\n");
			if (index != -1) {
				msg = split[1].substring(0, index);
			}

			Log.i("whw", "split msg=" + msg + "  msg length=" + msg.length());
		}
		return msg;
	}

	/**
	 * 
	 * @return
	 */
	private boolean validatePassword(int position) {
		// �·���֤����
		byte[] command1 = SEND_PASSWORD.getBytes();
		int tempLength = receive(command1, buffer);
		String verifyStr = new String(buffer, 0, tempLength);
		Log.i("whw", "validatePassword verifyStr=" + verifyStr);
		// ��֤����
		byte[] command2 = (DATA_PREFIX + PASSWORD_VALIDATE_ORDER + "60"
				+ getZoneId(position) + ENTER).getBytes();

		int length = receive(command2, buffer);
		String msg = new String(buffer, 0, length);
		Log.i("whw", "validatePassword msg=" + msg);
		String prefix = "0x00,\r\n";
		if (msg.startsWith(prefix)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	private boolean validatePassword(int position, int keyType, byte[] password) {
		Log.i("whw", "!!!!!!!!!!!!!!keyType=" + keyType);
		byte[] command1 = null;
		if (password == null) {
			// �·���֤����
			command1 = SEND_PASSWORD.getBytes();
		} else {
			String passwordHexStr = DataUtils.toHexString(password);
			String completePassword = getCompletePassword(keyType,
					passwordHexStr);
			command1 = (DATA_PREFIX + PASSWORD_SEND_ORDER + completePassword + ENTER)
					.getBytes();
		}

		int tempLength = receive(command1, buffer);
		String verifyStr = new String(buffer, 0, tempLength);
		Log.i("whw", "validatePassword verifyStr=" + verifyStr);
		// ��֤����
		byte[] command2 = (DATA_PREFIX + PASSWORD_VALIDATE_ORDER
				+ getKeyTypeStr(keyType) + getZoneId(position) + ENTER)
				.getBytes();

		int length = receive(command2, buffer);
		String msg = new String(buffer, 0, length);
		Log.i("whw", "validatePassword msg=" + msg);
		String prefix = "0x00,\r\n";
		if (msg.startsWith(prefix)) {
			return true;
		}
		return false;
	}

	private static final String DEFAULT_PASSWORD = "ffffffffffff";

	private String getCompletePassword(int keyType, String passwordHexStr) {
		// A.B�����볤�ȸ�Ϊ6�ֽڣ������16�����ַ��������볤�Ⱦ�Ϊ12���ַ�����
		StringBuffer passwordBuffer = new StringBuffer();
		passwordBuffer.append(passwordHexStr);
		if (passwordHexStr != null && passwordHexStr.length() < 12) {
			int length = 12 - passwordHexStr.length();
			for (int i = 0; i < length; i++) {
				passwordBuffer.append('0');
			}
		}
		passwordHexStr = passwordBuffer.toString();
		String completePasswordHexStr = "";
		switch (keyType) {
		case KEY_A:
			completePasswordHexStr = passwordHexStr + DEFAULT_PASSWORD;
			break;
		case KEY_B:
			completePasswordHexStr = DEFAULT_PASSWORD + passwordHexStr;
			break;

		default:
			break;
		}
		return completePasswordHexStr;
	}

	private String getKeyTypeStr(int keyType) {
		String keyTypeStr = null;
		switch (keyType) {
		case KEY_A:
			keyTypeStr = "60";
			break;
		case KEY_B:
			keyTypeStr = "61";
			break;
		default:
			keyTypeStr = "60";
			break;
		}
		return keyTypeStr;
	}


	// ת���������ĵ�ַΪ��λ
	private String getZoneId(int position) {
		return DataUtils.byte2Hexstr((byte) position);
	}

	// �ر����߳�
	private String turnOff() {
//		byte[] command = TURN_OFF.getBytes();
//		int length = receive(command, buffer);
//		String str = "";
//		if (length > 0) {
//			str = new String(buffer, 0, length);
//		}
//		return str;
		return "";
	}

	private int write(String hexStr, int position) {
		byte[] command = (DATA_PREFIX + WRITE_DATA_ORDER + getZoneId(position)
				+ hexStr + ENTER).getBytes();
		Log.i("whw", "***write hexStr=" + hexStr);

		int length = receive(command, buffer);
		return length;
	}

	/********************************************************************/

	/**
	 * ����Ҫд������ݱ�����ֽ�����
	 * 
	 * @param data
	 */
	public Result write(byte[][] data) {
		Result result = new Result();
		// �ֶ���
		short fieldNum = (short) data.length;
		// ��ȡÿ���ֶ������ݵĴ�С
		short[] fieldSize = new short[fieldNum];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// ÿ���ֶ�ռ2���ֽڣ��Ѹ����ֶδ�Сƴ��һ���ֽ�����
		byte[] fieldSizeData = null;
		try {
			for (int i = 0; i < fieldNum; i++) {
				fieldSize[i] = (short) data[i].length;
				baos.write(DataUtils.short2byte(fieldSize[i]));
			}
			baos.flush();
			fieldSizeData = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// �Ѹ����ֶ�����ƴ��һ���ֽ�����
		byte[] fieldData = null;
		baos = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < fieldNum; i++) {
				baos.write(data[i]);
			}
			baos.flush();
			fieldData = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// һ�������д��16���ֽ�
		int fieldSizeDataSector = fieldSizeData.length % 16 == 0 ? fieldSizeData.length / 16
				: fieldSizeData.length / 16 + 1;
		int fieldDataSector = fieldData.length % 16 == 0 ? fieldData.length / 16
				: fieldData.length / 16 + 1;
		// ��������д����Ҫ���ٸ��飬�ֶ�������ռһ����
		int piece = 1 + fieldSizeDataSector + fieldDataSector;
		byte[][] byteData = new byte[piece][];
		// ���ֶ�����shortתΪbyte[]����
		byte[] fieldNumByte = DataUtils.short2byte(fieldNum);
		// ���flag����fieldNum������Ϊ��ȷ��д�뷽ʽ
		byte[] flag = new byte[] { 0x01, 0x02, 0x03, 0x04 };
		byteData[0] = new byte[fieldNumByte.length + flag.length];
		System.arraycopy(fieldNumByte, 0, byteData[0], 0, fieldNumByte.length);
		System.arraycopy(flag, 0, byteData[0], fieldNumByte.length, flag.length);

		// �����ֶδ�Сƴ�������ֽ����鰴ÿ16���ֽڷ�Ϊһ��,��д��һ�������ֻ��д16�ֽ�
		for (int i = 0; i < fieldSizeDataSector; i++) {
			if (fieldSizeDataSector - 1 == i) {
				byteData[1 + i] = new byte[fieldSizeData.length - i * 16];
			} else {
				byteData[1 + i] = new byte[16];
			}
			System.arraycopy(fieldSizeData, i * 16, byteData[1 + i], 0,
					byteData[1 + i].length);
		}

		// �����ֶ�����ƴ�������ֽ����鰴ÿ16���ֽڷ�Ϊһ��
		for (int i = 0; i < fieldDataSector; i++) {
			if (fieldDataSector - 1 == i) {
				byteData[1 + fieldSizeDataSector + i] = new byte[fieldData.length
						- i * 16];
			} else {
				byteData[1 + fieldSizeDataSector + i] = new byte[16];
			}
			System.arraycopy(fieldData, i * 16, byteData[1
					+ fieldSizeDataSector + i], 0, byteData[1
					+ fieldSizeDataSector + i].length);
		}
		int position = 1;
		int tempPiece = piece;
		while (--tempPiece >= 0) {
			if (position == 1 || position % 4 == 3) {
				// ÿ���������ĸ��飬���ĸ��鲻����д�����ݣ�����Ҫ�����˿�
				if (position % 4 == 3) {
					++position;
				}
				int time = 0;
				while (!findCard()) {
					time++;
					if (time == 3) {
						result.confirmationCode = Result.FIND_FAIL;
						return result;
					}
					SystemClock.sleep(100);
					Log.i("whw", "findCard fail");
				}
				Log.i("whw", "findCard success");
				time = 0;
				while (!validatePassword(position)) {
					time++;
					if (time == 3) {
						result.confirmationCode = Result.VALIDATE_FAIL;
						return result;
					}
					SystemClock.sleep(100);
					Log.i("whw", "validatePassword fail");
				}
				Log.i("whw", "validatePassword success");
			}
			String hexStr = DataUtils.toHexString(byteData[piece - tempPiece
					- 1]);
			int receiveLength = write(hexStr, position);
			String writeResult = new String(buffer, 0, receiveLength);
			if (WRITE_SUCCESS.equals(writeResult)) {
				result.confirmationCode = Result.SUCCESS;
			} else {
				result.confirmationCode = Result.WRITE_FAIL;
			}
			if (position % 4 == 2) {
				turnOff();
			}
			position++;
		}
		turnOff();
		return result;

	}

	/**
	 * �ѿ�Ƭ������ݶ����������ֽ�����ķ�ʽ����
	 * 
	 * @return
	 */
	public Result read() {
		Result result = new Result();
		int position = 1;
		int piece = 1;
		short fieldNum = 0;
		int fieldSizeDataSector = 0;
		int sizeData = 0;
		short[] fieldSize = null;
		// �����ֶε�ʵ������ռ���˶��ٸ���
		int fieldDataSector;
		// �����ֶ����ݴ�С��ռ�ö��ٿռ�
		int sumSize = 0;
		// data��Ÿ����ֶε�����
		byte[][] data;
		StringBuffer strBuffer1 = new StringBuffer();
		StringBuffer strBuffer2 = new StringBuffer();
		for (int i = 0; i < piece; i++) {
			if (i == 0 || position % 4 == 3) {
				if (position % 4 == 3) {
					++position;
				}
				int time = 0;
				while (!findCard()) {
					time++;
					if (time == 3) {
						result.confirmationCode = Result.FIND_FAIL;
						return result;
					}
					SystemClock.sleep(100);
					Log.i("whw", "findCard fail");
				}
				Log.i("whw", "findCard success");
				time = 0;
				while (!validatePassword(position)) {
					time++;
					if (time == 3) {
						result.confirmationCode = Result.VALIDATE_FAIL;
						return result;
					}
					SystemClock.sleep(100);
					Log.i("whw", "validatePassword fail");
				}
				Log.i("whw", "validatePassword success");
			}
			String hexStr = read(position);
			if (i == 0) {
				// ��ȡ���ֶ���,���ֶ���ռ�����ֽڣ��������У��λ��0x01,0x02,0x03,0x04,���û��˵���˿�û���û���Ҫ������
				byte[] tempData = DataUtils.hexStringTobyte(hexStr);
				if (tempData.length > 6 && tempData[2] == 0x01
						&& tempData[3] == 0x02 && tempData[4] == 0x03
						&& tempData[5] == 0x04) {
					byte[] temp = new byte[2];
					System.arraycopy(tempData, 0, temp, 0, temp.length);
					fieldNum = DataUtils.getShort(temp[0], temp[1]);
					if (fieldNum <= 0) {
						turnOff();
						Log.i("whw", "turnOff()  111111111");
						result.confirmationCode = Result.OTHER_EXCEPTION;
						return result;
					} else {
						// ÿ���ֶ�ռ2���ֽڣ���ռ��fieldNum*2���ֽ�
						sizeData = fieldNum * 2;
						fieldSizeDataSector = sizeData % 16 == 0 ? sizeData / 16
								: sizeData / 16 + 1;
						piece += fieldSizeDataSector;
						Log.i("whw", "!!!!!!!!!!!!!piece=" + piece);
					}
				} else {
					turnOff();
					Log.i("whw", "turnOff()  222222222");
					result.confirmationCode = Result.OTHER_EXCEPTION;
					return result;
				}
			}

			// ��ȡ�����ֶ���ռ�õĿռ�
			if (i >= 1 && i < fieldSizeDataSector + 1) {
				strBuffer1.append(hexStr);
				if (i == fieldSizeDataSector) {
					byte[] temp = DataUtils.hexStringTobyte(strBuffer1
							.toString());
					if (sizeData <= temp.length) {
						fieldSize = new short[fieldNum];
						for (int j = 0; j < fieldNum; j++) {
							byte[] byteTemp = new byte[2];
							System.arraycopy(temp, j * 2, byteTemp, 0, 2);
							fieldSize[j] = DataUtils.getShort(byteTemp[0],
									byteTemp[1]);
						}
						for (int k = 0; k < fieldSize.length; k++) {
							sumSize += fieldSize[k];
						}
						if (sumSize <= 0) {
							turnOff();
							Log.i("whw", "turnOff()  33333333333333");
							result.confirmationCode = Result.OTHER_EXCEPTION;
							return result;
						} else {
							fieldDataSector = sumSize % 16 == 0 ? sumSize / 16
									: sumSize / 16 + 1;
							piece += fieldDataSector;
							Log.i("whw", "@@@@@@@@@@@@@piece=" + piece + "  fieldDataSector=" + fieldDataSector);
						}
					} else {
						turnOff();
						Log.i("whw", "turnOff()  44444444444");
						result.confirmationCode = Result.OTHER_EXCEPTION;
						return result;
					}
				}
			}

			// ��ȡ�����ֶ����е�����
			if (i >= fieldSizeDataSector + 1 && i < piece) {
				strBuffer2.append(hexStr);
				Log.i("whw", "append hexStr=" + hexStr.toString());
				if (i == piece - 1) {
					byte[] temp = DataUtils.hexStringTobyte(strBuffer2
							.toString());
					Log.i("whw", "strBuffer2 hexStr=" + strBuffer2.toString());
					if (temp.length >= sumSize) {
						data = new byte[fieldNum][];
						if (fieldSize != null) {
							int startPosition = 0;
							for (int j = 0; j < fieldSize.length; j++) {
								data[j] = new byte[fieldSize[j]];
								System.arraycopy(temp, startPosition, data[j],
										0, data[j].length);
								startPosition += fieldSize[j];
							}
							turnOff();
							Log.i("whw", "turnOff()  55555555555555555");
							result.confirmationCode = Result.SUCCESS;
							result.resultInfo = data;
							return result;
						}

					} else {
						turnOff();
						Log.i("whw", "turnOff()  66666666666666");
						result.confirmationCode = Result.OTHER_EXCEPTION;
						return result;
					}
				}
			}
			if (position % 4 == 2) {
				//ÿ�������Ŀ�3�����Զ�д����
//				turnOff();
				Log.i("whw", "turnOff()  777777777777777");
			}
			position++;
		}
		turnOff();
		return result;
	}



	public Result writeAtPosition(int position, int keyType, byte[] password,
			byte[] data) {
		Result result = new Result();
		int time = 0;
		while (!findCard()) {
			time++;
			if (time == 3) {
				result.confirmationCode = Result.FIND_FAIL;
				return result;
			}
			SystemClock.sleep(100);
			Log.i("whw", "findCard fail");
		}

		time = 0;
		while (!validatePassword(position, keyType, password)) {
			time++;
			if (time == 3) {
				result.confirmationCode = Result.VALIDATE_FAIL;
				return result;
			}
			SystemClock.sleep(100);
			Log.i("whw", "validatePassword fail");
		}

		String hexStr = DataUtils.toHexString(data);
		int receiveLength = write(hexStr, position);
		String writeResult = new String(buffer, 0, receiveLength);
		Log.i("whw", "write result=" + writeResult);
		turnOff();
		if (WRITE_SUCCESS.equals(writeResult)) {
			result.confirmationCode = Result.SUCCESS;
		} else {
			result.confirmationCode = Result.WRITE_FAIL;
		}
		return result;
	}

	public Result readAtPosition(int position, int keyType, byte[] password) {
		Result result = new Result();
		int time = 0;
		while (!findCard()) {
			time++;
			if (time == 3) {
				result.confirmationCode = Result.FIND_FAIL;
				return result;
			}
			SystemClock.sleep(100);
			Log.i("whw", "findCard fail");
		}

		time = 0;
		while (!validatePassword(position, keyType, password)) {
			time++;
			if (time == 3) {
				result.confirmationCode = Result.VALIDATE_FAIL;
				return result;
			}
			SystemClock.sleep(100);
			Log.i("whw", "validatePassword fail");
		}

		String hexStr = read(position);
		byte[] data = DataUtils.hexStringTobyte(hexStr);
		turnOff();
		result.confirmationCode = Result.SUCCESS;
		result.resultInfo = data;
		return result;
	}

	public class Result {
		public static final int SUCCESS = 1;
		public static final int FIND_FAIL = 2;
		public static final int VALIDATE_FAIL = 3;
		public static final int WRITE_FAIL = 4;
		public static final int TIME_OUT = 5;
		public static final int OTHER_EXCEPTION = 6;

		/**
		 * ȷ���� 1: �ɹ� 2��Ѱ��ʧ�� 3����֤ʧ�� 4:д��ʧ�� 5����ʱ 6�������쳣
		 */
		public int confirmationCode;

		/**
		 * �����:��ȷ����Ϊ1ʱ�����ж��Ƿ��н��
		 */
		public Object resultInfo;
	}

}
