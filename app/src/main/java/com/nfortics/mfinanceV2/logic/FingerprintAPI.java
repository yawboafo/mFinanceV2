package com.nfortics.mfinanceV2.logic;

import android.os.Handler;
import android.util.Log;

import com.nfortics.mfinanceV2.Utilities.DataUtils;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class FingerprintAPI extends Handler {

	// private BluetoothChatService chatService;
	private CommunicateManager communicateManager;

	/**
	 * ������:��λ���������ݴ�ŵ�
	 */
	private byte[] data = new byte[1024 * 50];

	private byte[] buffer = new byte[1024 * 50];

	private byte[] bufferImage = new byte[1024 * 50];

	/**
	 * ��Ӧ����ͼ�����ݹ�40044�ֽ�
	 */
	private static final int UP_IMAGE_RESPONSE_SIZE = 40044;

	/**
	 * ȷ���룺0x20��0xefΪ������ȷ���� ��ʾָ��ִ����ϻ�OK��
	 */
	public static final int EXEC_COMMAND_SUCCESS = 0x00;
	/**
	 * ��ʾ���ݰ����մ���
	 */
	public static final int RECEIVE_PACKAGE_ERROR = 0x01;
	/**
	 * ��ʾ��������û����ָ��
	 */
	public static final int NO_FINGER = 0x02;
	/**
	 * ��ʾ¼��ָ��ͼ��ʧ�ܣ�
	 */
	public static final int GET_IMAGE_FAIL = 0x03;
	/**
	 * ��ʾָ��ͼ��̫�ɡ�̫����������������
	 */
	public static final int FINGERPRINT_SMALL = 0x04;
	/**
	 * ��ʾָ��ͼ��̫ʪ��̫����������������
	 */
	public static final int FINGERPRINT_BLURRING = 0x05;
	/**
	 * ��ʾָ��ͼ��̫�Ҷ�������������
	 */
	public static final int FINGERPRINT_NOT_GENCHAR = 0x06;
	/**
	 * ��ʾָ��ͼ����������������̫�٣������̫С����������������
	 */
	public static final int FINGERPRINT_CHAR_LESS = 0x07;
	/**
	 * ��ʾָ�Ʋ�ƥ�䣻
	 */
	public static final int FINGERPRINT_NO_MATCH = 0x08;
	/**
	 * ��ʾû������ָ�ƣ�
	 */
	public static final int FINGERPRINT_NO_SEARCH = 0x09;
	/**
	 * ��ʾ�����ϲ�ʧ�ܣ�
	 */
	public static final int FINGERPRINT_REG_MODEL_FAIL = 0x0a;
	/**
	 * ��ʾ����ָ�ƿ�ʱ��ַ��ų���ָ�ƿⷶΧ��
	 */
	public static final int FLASH_OUT_OF_INDEX = 0x0b;
	/**
	 * ��ʾ��ָ�ƿ��ģ��������Ч��
	 */
	public static final int READ_MODEL_FROM_FLASH = 0x0c;
	/**
	 * ��ʾ�ϴ�����ʧ�ܣ� 
	 */
	public static final int UP_CHAR_FAIL = 0x0d;
	/**
	 * ��ʾģ�鲻�ܽ��ܺ������ݰ���
	 */
	public static final int NOT_RECEIVE_PACKAGE = 0x0e;
	/**
	 * ��ʾ�ϴ�ͼ��ʧ�ܣ�
	 */
	public static final int UP_IMAGE_FAIL = 0x0f;
	/**
	 * ��ʾɾ��ģ��ʧ�ܣ�
	 */
	public static final int DELETE_MODEL_FAIL = 0x10;
	/**
	 * ��ʾ���ָ�ƿ�ʧ�ܣ�
	 */
	public static final int EMPTY_FLASH = 0x11;
	/**
	 * ��ʾ���ܽ���͹���״̬��
	 */
	public static final int NOT_ENTRY_LESS_STATUS = 0x12;
	/**
	 * ��ʾ�����ȷ��
	 */
	public static final int COMMAND_FAIL = 0x13;
	public static final int ERROR1 = 0x14;// ��ʾϵͳ��λʧ�ܣ�
	public static final int ERROR2 = 0x15;// ��ʾ��������û����Чԭʼͼ��������ͼ��
	public static final int ERROR3 = 0x16;// ��ʾ��������ʧ�ܣ�
	public static final int ERROR4 = 0x17;// ��ʾ����ָ�ƻ����βɼ�֮����ָû���ƶ�����
	public static final int ERROR5 = 0x18;// ��ʾ��дFLASH ����
	public static final int ERROR6 = 0xf0;// �к������ݰ���ָ���ȷ���պ���0xf0 Ӧ��
	public static final int ERROR7 = 0xf1;// �к������ݰ���ָ��������0xf1 Ӧ��
	public static final int ERROR8 = 0xf2;// ��ʾ��д�ڲ�FLASH ʱ��У��ʹ���
	public static final int ERROR9 = 0xf3;// ��ʾ��д�ڲ�FLASH ʱ������ʶ����
	public static final int ERROR10 = 0xf4;// ��ʾ��д�ڲ�FLASH ʱ�������ȴ���
	public static final int ERROR11 = 0xf5;// ��ʾ��д�ڲ�FLASH ʱ�����볤��̫����
	public static final int ERROR12 = 0xf6;// ��ʾ��д�ڲ�FLASH ʱ����дFLASH ʧ�ܣ�
	public static final int ERROR13 = 0x19;// δ�������
	public static final int ERROR14 = 0x1a;// ��Ч�Ĵ����ţ�
	public static final int ERROR15 = 0x1b;// �Ĵ����趨���ݴ���ţ�
	public static final int ERROR16 = 0x1c;// ���±�ҳ��ָ������
	public static final int ERROR17 = 0x1d;// �˿ڲ���ʧ�ܣ�
	/**
	 * �Զ�ע�ᣨenroll��ʧ�ܣ�
	 */
	public static final int ENROLL_FAIL = 0x1e;
	/**
	 * ָ�ƿ���
	 */
	public static final int FLASH_FULL = 0x1f;
	/**
	 * ����Ӧ
	 */
	public static final int NO_RESPONSE = 0xff;

	public FingerprintAPI(CommunicateManager communicateManager) {
		this.communicateManager = communicateManager;
	}

	/**
	 * ¼��ָ��ͼ�� ����˵���� ̽����ָ��̽�⵽��¼��ָ��ͼ�����ImageBuffer�� ����ȷ�����ʾ��¼��ɹ�������ָ�ȡ�
	 * 
	 * @return ����ֵΪȷ���� ȷ����=00H��ʾ¼��ɹ� ȷ����=01H��ʾ�հ��д� ȷ����=02H��ʾ������������ָ
	 *         ȷ����=03H��ʾ¼�벻�ɹ� ȷ����=ffH ��ʾ����Ӧ
	 */
	public synchronized int PSGetImage() {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, 0x01, 0x00, 0x03, 0x01, 0x00, 0x05 };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,12);
		printlog("PSGetImage", length);
		if (length == 12) {
			return buffer[9];
		}
		return NO_RESPONSE;
	}

	/**
	 * ����˵���� ��ImageBuffer �е�ԭʼͼ������ָ�������ļ�����CharBuffer1 ��CharBuffer2 ���������
	 * BufferID(������������)
	 * 
	 * @param bufferId
	 *            ��CharBuffer1:1h,CharBuffer2:2h��
	 * @return ����ֵΪȷ���� ȷ����=00H��ʾ���������ɹ� ȷ����=01H��ʾ�հ��д� ȷ����=06H��ʾָ��ͼ��̫�Ҷ�����������
	 *         ȷ����=07H��ʾָ��ͼ����������������̫�ٶ����������� ȷ����=15H��ʾͼ�񻺳�����û����Чԭʼͼ��������ͼ��
	 *         ȷ����=ffH��ʾ����Ӧ
	 */
	public synchronized int PSGenChar(int bufferId) {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 01, (byte) 0x00, (byte) 0x04,
				(byte) 0x02, (byte) bufferId, (byte) 0x00,
				(byte) (0x7 + bufferId) };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,12);
		printlog("PSGenChar", length);
		if (length == 12) {
			return buffer[9];
		}
		return NO_RESPONSE;
	}

	/**
	 * �ϲ���������ģ�壬��CharBuffer1��CharBuffer2�е������ļ��ϲ�����ģ�壬
	 * �������CharBuffer1��CharBuffer2�С�
	 * 
	 * @return ����ֵΪȷ���� ȷ����=00H��ʾ�ϲ��ɹ� ȷ����=01H��ʾ�հ��д� ȷ����=0aH��ʾ�ϲ�ʧ�ܣ���öָ�Ʋ�����ͬһ��ָ��
	 *         ȷ����=ffH ��ʾ����Ӧ
	 */
	public synchronized int PSRegModel() {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, 0x01, 0x00, 0x03, 0x05, 0x00, 0x09 };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,12);
		printlog("PSRegModel", length);
		if (length == 12) {
			return buffer[9];
		}
		return NO_RESPONSE;
	}

	/**
	 * ��CharBuffer�е�ģ�崢�浽ָ����pageId�ŵ�flash���ݿ�λ�� bufferId:ֻ��Ϊ1h��2h
	 * pageId����ΧΪ0~1010 ��������� BufferID(��������)��PageID��ָ�ƿ�λ�úţ�
	 * 
	 * @return ����ֵΪȷ���� ȷ����=00H��ʾ����ɹ� ȷ����=01H��ʾ�հ��д� ȷ����=0bH��ʾPageID����ָ�ƿⷶΧ
	 *         ȷ����=18H��ʾдFLASH���� ȷ����=ffH ��ʾ����Ӧ
	 */
	public synchronized int PSStoreChar(int bufferId, int pageId) {
		byte[] pageIDArray = short2byte((short) pageId);
		// Log.i("whw", "pageid hex=" + DataUtils.toHexString(pageIDArray));
		int checkSum = 0x01 + 0x00 + 0x06 + 0x06 + bufferId
				+ (pageIDArray[0] & 0xff) + (pageIDArray[1] & 0xff);
		byte[] checkSumArray = short2byte((short) checkSum);
		// Log.i("whw",
		// "checkSumArray hex=" + DataUtils.toHexString(checkSumArray)
		// + "    checkSum=" + checkSum);
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x06, (byte) 0x06, (byte) bufferId,
				(byte) pageIDArray[0], (byte) pageIDArray[1],
				(byte) checkSumArray[0], (byte) checkSumArray[1] };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,12);
		printlog("PSStoreChar", length);
		if (length == 12) {
			return buffer[9];
		}
		return NO_RESPONSE;
	}

	/**
	 * ��flash ���ݿ���ָ��pageId�ŵ�ָ��ģ����뵽ģ�建����CharBuffer1��CharBuffer2
	 * bufferId:ֻ��Ϊ1h��2h pageId����ΧΪ0~1023 ��������� BufferID(��������)��PageID(ָ�ƿ�ģ���)
	 * 
	 * @param
	 *            pageId��
	 * @return ����ֵΪȷ���� ȷ����=00H��ʾ�����ɹ� ȷ����=01H��ʾ�հ��д� ȷ����=0cH��ʾ�����д��ģ���д�
	 *         ȷ����=0BH��ʾPageID����ָ�ƿⷶΧ ȷ����=ffH ��ʾ����Ӧ
	 */
	public synchronized int PSLoadChar(int bufferId, int pageId) {
		byte[] pageIDArray = short2byte((short) pageId);
		int checkSum = 0x01 + 0x00 + 0x06 + 0x07 + bufferId
				+ (pageIDArray[0] & 0xff) + (pageIDArray[1] & 0xff);
		byte[] checkSumArray = short2byte((short) checkSum);
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x06, (byte) 0x07, (byte) bufferId,
				(byte) pageIDArray[0], (byte) pageIDArray[1],
				(byte) checkSumArray[0], (byte) checkSumArray[1] };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,12);
		printlog("PSLoadChar", length);
		if (length == 12) {
			return buffer[9];
		}
		return NO_RESPONSE;
	}

	/**
	 * ��CharBuffer1 ��CharBuffer2 �е������ļ����������򲿷�ָ�ƿ⡣�����������򷵻�ҳ�롣 ��������� BufferID��
	 * StartPage(��ʼҳ)��PageNum��ҳ���� ���ز����� ȷ���֣�ҳ�루����ָ��ģ�壩
	 * 
	 * @param bufferId
	 *            ������1h��2h
	 * @param startPageId
	 *            ��ʼҳ
	 * @param pageNum
	 *            ҳ��
	 * @return ȷ����=00H ��ʾ�������� ȷ����=01H ��ʾ�հ��д� ȷ����=09H ��ʾû����������ʱҳ����÷�Ϊ0 ȷ����=ffH
	 *         ��ʾ����Ӧ
	 */
	public synchronized Result PSSearch(int bufferId, int startPageId,
			int pageNum) {
		byte[] startPageIDArray = short2byte((short) startPageId);
		byte[] pageNumArray = short2byte((short) pageNum);
		int checkSum = 0x01 + 0x00 + 0x08 + 0x04 + bufferId
				+ (startPageIDArray[0] & 0xff) + (startPageIDArray[1] & 0xff)
				+ (pageNumArray[0] & 0xff) + (pageNumArray[1] & 0xff);
		byte[] checkSumArray = short2byte((short) checkSum);
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x08, (byte) 0x04, (byte) bufferId,
				(byte) startPageIDArray[0], (byte) startPageIDArray[1],
				(byte) pageNumArray[0], (byte) pageNumArray[1],
				(byte) checkSumArray[0], (byte) checkSumArray[1] };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,16);
		printlog("PSSearch", length);
		Result result = new Result();
		if (length == 16) {
			result.code = buffer[9];
			result.pageId = getShort(buffer[10], buffer[11]);
			result.matchScore = getShort(buffer[12], buffer[13]);
		} else {
			result.code = NO_RESPONSE;
		}
		return result;
	}

	/**
	 * ��ȷ�ȶ�CharBuffer1��CharBuffer2�е������ļ� ע���:��λ�����ص��������滹��һ���÷֣����÷ִ��ڵ���50ʱ��ָ��ƥ��
	 * 
	 * @return true��ָ��ƥ��ɹ� false���ȶ�ʧ��
	 */
	public synchronized boolean PSMatch() {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x03, (byte) 0x03, (byte) 0x00, (byte) 0x07 };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,14);
		printlog("PSMatch", length);
		if (length == 14) {
			if (buffer[9] == 0x00) {
				return score(buffer[10], buffer[11]);
			}
		}
		return false;
	}

	/**
	 * �ɼ�һ��ָ��ע��ģ�壬��ָ�ƿ���������λ���洢�����ش洢pageId ���ز����� ȷ���֣�ҳ�루����ָ��ģ�壩
	 * 
	 * @return ȷ����=00H ��ʾע��ɹ��� ȷ����=01H ��ʾ�հ��д� ȷ����=1eH ��ʾע��ʧ�ܡ� ȷ����=ffH ��ʾ����Ӧ
	 */
	public synchronized Result PSEnroll() {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x03, (byte) 0x10, (byte) 0x00, (byte) 0x14 };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,14);
		printlog("PSEnroll", length);
		Result result = new Result();
		if (length == 14) {
			result.code = buffer[9];
			result.pageId = getShort(buffer[10], buffer[11]);
		} else {
			result.code = NO_RESPONSE;
		}
		return result;
	}

	/**
	 * �Զ��ɼ�ָ�ƣ���ָ�ƿ�������Ŀ��ģ�岢������������� ���Ŀ��ģ��ͬ��ǰ�ɼ���ָ�ƱȶԵ÷ִ�����߷�ֵ��
	 * ����Ŀ��ģ��Ϊ�������������Բɼ�����������Ŀ��ģ��Ŀհ����� ���ز����� ȷ���룬ҳ�루����ָ��ģ�壩
	 * 
	 * @return ȷ����=00H ��ʾ�������� ȷ����=01H ��ʾ�հ��д� ȷ����=09H ��ʾû����������ʱҳ����÷�Ϊ0 ȷ����=ffH
	 *         ��ʾ����Ӧ
	 */
	public synchronized Result PSIdentify() {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x03, (byte) 0x11, (byte) 0x00, (byte) 0x15 };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,16);
		printlog("PSIdentify", length);
		Result result = new Result();
		if (length == 16) {
			result.code = buffer[9];
			result.pageId = getShort(buffer[10], buffer[11]);
			result.matchScore = getShort(buffer[12], buffer[13]);
		} else {
			result.code = NO_RESPONSE;
		}
		return result;
	}

	/**
	 * ɾ��ģ�� ɾ��flash ���ݿ���ָ��ID �ſ�ʼ��N ��ָ��ģ�� ���������PageID(ָ�ƿ�ģ���)��N ɾ����ģ�������
	 * 
	 * @param pageIDStart
	 * @param delNum
	 * @return ȷ����=00H ��ʾɾ��ģ��ɹ��� ȷ����=01H ��ʾ�հ��д� ȷ����=10H ��ʾɾ��ģ��ʧ�ܣ� ȷ����=ffH ��ʾ����Ӧ
	 */
	public synchronized int PSDeleteChar(short pageIDStart, short delNum) {
		byte[] pageIDArray = short2byte(pageIDStart);
		byte[] delNumArray = short2byte(delNum);
		int checkSum = 0x01 + 0x07 + 0x0c + (pageIDArray[0] & 0xff)
				+ (pageIDArray[1] & 0xff) + (delNumArray[0] & 0xff)
				+ (delNumArray[1] & 0xff);
		byte[] checkSumArray = short2byte((short) checkSum);
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x07, (byte) 0x0c, pageIDArray[0], pageIDArray[1],
				delNumArray[0], delNumArray[1], checkSumArray[0],
				checkSumArray[1] };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,12);
		printlog("PSDeleteChar", length);
		if (length == 12) {
			return buffer[9];
		}
		return NO_RESPONSE;
	}

	/**
	 * ����˵���� ɾ��flash ���ݿ�������ָ��ģ��
	 * 
	 * @return ȷ����=00H ��ʾ��ճɹ��� ȷ����=01H ��ʾ�հ��д� ȷ����=11H ��ʾ���ʧ�ܣ� ȷ����=ffH ��ʾ����Ӧ
	 */
	public synchronized int PSEmpty() {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x03, (byte) 0x0d, (byte) 0x00, (byte) 0x11 };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,12);
		printlog("PSEmpty", length);
		if (length == 12) {
			return buffer[9];
		}
		return NO_RESPONSE;
	}

	/**
	 * �������������е������ļ��ϴ�����λ��(Ĭ�ϵ�����������Ϊcharbuffer1)
	 * 
	 * @return byte[]������Ϊ512�ֽڳɹ� ����ʧ�� null:�ϴ������ļ�ʧ��
	 */
	public synchronized byte[] PSUpChar() {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x04, (byte) 0x08, (byte) 0x01, (byte) 0x00, (byte) 0x0e };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 300,568);
		printlog("PSUpChar", 12);
		// ��ӦΪ12�ֽڣ���4�����ݰ���ÿ����Ϊ139�ֽڣ����Է��ص����ֽ���Ϊ568�ֽ�
		if (length == 568) {
			index = 12;// ���ݰ�����ʼ�±�
			packetNum = 0;
			byte[] packets = new byte[568];
			System.arraycopy(buffer, 0, packets, 0, 568);
			return parsePacketData(packets);
		}
		return null;

	}

	/**
	 * ��λ�����������ļ���ģ�������������(Ĭ�ϵĻ�����ΪCharBuffer2)
	 * 
	 * @param model
	 *            :ָ�Ƶ������ļ�
	 * @return ����ֵΪȷ���� ȷ����=00H ��ʾ���Խ��պ������ݰ��� ȷ����=01H ��ʾ�հ��д� ȷ����=0eH ��ʾ���ܽ��պ������ݰ���
	 *         ȷ����=ffH ��ʾ����Ӧ
	 */
	public synchronized int PSDownChar(byte[] model) {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x04, (byte) 0x09, (byte) 0x02, (byte) 0x00, (byte) 0x10 };
		sendCommand(command);
		int length = communicateManager.read(buffer, 3000, 100,12);
		printlog("PSDownChar", length);
		if (length == 12 && buffer[9] == 0x00) {
			sendData(model);
			return 0x00;
		}
		return NO_RESPONSE;
	}

	// /**
	// * ��ͼ�񻺳����������ϴ�����λ��
	// *
	// * @return ����ֵΪbmp��ʽ��ָ��ͼ�����Ϊnull�ϴ�ʧ��
	// */
	// public synchronized byte[] PSUpImage() {
	// byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
	// (byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
	// (byte) 0x03, (byte) 0x0a, (byte) 0x00, (byte) 0x0e };
	// sendCommand(command);
	// int length = communicateManager.read(buffer, 3000, 500);
	// Log.i("whw", "PSUpImage length=" + length);
	// if (length >= 40044) {
	// byte[] packets = new byte[length];
	// System.arraycopy(buffer, 0, packets, 0, length);
	// index = 12;
	// packetNum = 0;
	// byte[] data = parsePacketData(packets);
	// return getFingerprintImage(data);
	// }
	// return null;
	//
	// }

	/**
	 * ��ͼ�񻺳����������ϴ�����λ��
	 * 
	 * @return ����ֵΪbmp��ʽ��ָ��ͼ�����Ϊnull�ϴ�ʧ��
	 */
	public synchronized byte[] PSUpImage() {
		byte[] command = { (byte) 0xef, (byte) 0x01, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00,
				(byte) 0x03, (byte) 0x0a, (byte) 0x00, (byte) 0x0e };
		byte[] nextCommand = { 'O' };
		int startLength = 3348;
		int dataLength = 3336;
		boolean isStart = true;
		boolean isExit = false;
		int size = 0;
		int length = 0;
		int requestLength = 0;
		
		
		while (!isExit) {
			if (isStart) {
				sendCommand(command);
				requestLength = startLength;
				isStart = false;
			} else {
				if (startLength == size || dataLength == size) {

					System.arraycopy(buffer, 0, bufferImage, length, size);
					length += size;
					Log.i("whw", "receive length=" + length);
					if (length == UP_IMAGE_RESPONSE_SIZE) {
						sendCommand(nextCommand);
						isExit = true;
						break;
					}
	
					sendCommand(nextCommand);
					requestLength = dataLength;
				} else {
					Log.i("whw", "----------upimage fail size=" + size);
					break;
				}

			} 
			size = communicateManager.read(buffer, 5000, 100, requestLength);
			
		}
		

		
		Log.i("whw", "PSUpImage length=" + length);
		if (length == UP_IMAGE_RESPONSE_SIZE) {
			byte[] packets = new byte[length];
			System.arraycopy(bufferImage, 0, packets, 0, length);
			index = 12;
			packetNum = 0;
			byte[] data = parsePacketData(packets);
			return getFingerprintImage(data);
		}
		return null;
	}

	/**
	 * ����ָ��ģ�����ݰ�512�ֽ�,��Ϊ4�η��ͣ�3�����ݰ���һ�ν�����
	 * 
	 * @param data
	 */
	private void sendData(byte[] data) {
		// ���ݰ�ָ��ͷ
		byte[] dataPrefix = { (byte) 0xef, (byte) 0x01, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x02,
				(byte) 0x00, (byte) 0x82 };
		// ������ָ��ͷ
		byte[] endPrefix = { (byte) 0xef, (byte) 0x01, (byte) 0xff,
				(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x08,
				(byte) 0x00, (byte) 0x82 };
		byte[] command = new byte[dataPrefix.length + 128 + 2];
		for (int i = 0; i < 4; i++) {
			if (i == 3) {
				System.arraycopy(endPrefix, 0, command, 0, endPrefix.length);
			} else {
				System.arraycopy(dataPrefix, 0, command, 0, dataPrefix.length);
			}
			System.arraycopy(data, i * 128, command, dataPrefix.length, 128);
			short sum = 0;
			for (int j = 6; j < command.length - 2; j++) {
				sum += (command[j] & 0xff);
			}
			byte[] size = short2byte(sum);
			command[command.length - 2] = size[0];
			command[command.length - 1] = size[1];
			sendCommand(command);
		}

	}

	private int index;// ���ݰ�����ʼ�±�
	private int packetNum;// ���ݰ��ĸ���

	private byte[] parsePacketData(byte[] packet) {
		int dstPos = 0;
		int packageLength = 0;
		int size = 0;
		do {
			packageLength = getShort(packet[index + 7], packet[index + 8]);
			System.arraycopy(packet, index + 9, data, dstPos, packageLength - 2);
			dstPos += packageLength - 2;// 2��У���
			packetNum++;
			size += packageLength - 2;
		} while (moveToNext(index + 6, packageLength, packet));
		if (size != 0) {
			byte[] dataPackage = new byte[size];
			Log.i("whw", "**************packetNum=" + packetNum);
			System.arraycopy(data, 0, dataPackage, 0, size);
			return dataPackage;
		}
		return null;
	}

	private boolean moveToNext(int position, int packageLength, byte[] packet) {
		if (packet[position] == 0x02) {
			index += packageLength + 9;
			return true;
		}
		return false;
	}

	private byte[] getFingerprintImage(byte[] data) {
		if (data == null) {
			return null;
		}
		byte[] imageData = new byte[data.length * 2];
		// Log.i("whw", "*****************data.length="+data.length);
		for (int i = 0; i < data.length; i++) {
			imageData[i * 2] = (byte) (data[i] & 0xf0);
			imageData[i * 2 + 1] = (byte) (data[i] << 4 & 0xf0);
		}

		byte[] bmpData = toBmpByte(256, packetNum, imageData);
		return bmpData;
	}

	/**
	 * �����ݴ����ڴ�
	 */
	private byte[] toBmpByte(int width, int height, byte[] data) {
		byte[] buffer = null;
		try {
			// // ����������ļ�����
			// java.io.FileOutputStream fos = new
			// java.io.FileOutputStream(path);
			// // ����ԭʼ�������������
			// java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);

			// ���ļ�ͷ�ı�����ֵ
			int bfType = 0x424d; // λͼ�ļ����ͣ�0��1�ֽڣ�
			int bfSize = 54 + 1024 + width * height;// bmp�ļ��Ĵ�С��2��5�ֽڣ�
			int bfReserved1 = 0;// λͼ�ļ������֣�����Ϊ0��6-7�ֽڣ�
			int bfReserved2 = 0;// λͼ�ļ������֣�����Ϊ0��8-9�ֽڣ�
			int bfOffBits = 54 + 1024;// �ļ�ͷ��ʼ��λͼʵ������֮����ֽڵ�ƫ������10-13�ֽڣ�

			// �������ݵ�ʱ��Ҫע��������������ڴ���Ҫռ�����ֽڣ�
			// Ȼ����ѡ����Ӧ��д�뷽�������������Լ��������������
			// �����ļ�ͷ����
			dos.writeShort(bfType); // ����λͼ�ļ�����'BM'
			dos.write(changeByte(bfSize), 0, 4); // ����λͼ�ļ���С
			dos.write(changeByte(bfReserved1), 0, 2);// ����λͼ�ļ�������
			dos.write(changeByte(bfReserved2), 0, 2);// ����λͼ�ļ�������
			dos.write(changeByte(bfOffBits), 0, 4);// ����λͼ�ļ�ƫ����

			// ����Ϣͷ�ı�����ֵ
			int biSize = 40;// ��Ϣͷ������ֽ�����14-17�ֽڣ�
			int biWidth = width;// λͼ�Ŀ�18-21�ֽڣ�
			int biHeight = height;// λͼ�ĸߣ�22-25�ֽڣ�
			int biPlanes = 1; // Ŀ���豸�ļ��𣬱�����1��26-27�ֽڣ�
			int biBitcount = 8;// ÿ�����������λ����28-29�ֽڣ���������1λ��˫ɫ����4λ��16ɫ����8λ��256ɫ������24λ�����ɫ��֮һ��
			int biCompression = 0;// λͼѹ�����ͣ�������0����ѹ������30-33�ֽڣ���1��BI_RLEBѹ�����ͣ���2��BI_RLE4ѹ�����ͣ�֮һ��
			int biSizeImage = width * height;// ʵ��λͼͼ��Ĵ�С��������ʵ�ʻ��Ƶ�ͼ���С��34-37�ֽڣ�
			int biXPelsPerMeter = 0;// λͼˮƽ�ֱ��ʣ�ÿ����������38-41�ֽڣ��������ϵͳĬ��ֵ
			int biYPelsPerMeter = 0;// λͼ��ֱ�ֱ��ʣ�ÿ����������42-45�ֽڣ��������ϵͳĬ��ֵ
			int biClrUsed = 256;// λͼʵ��ʹ�õ���ɫ���е���ɫ����46-49�ֽڣ������Ϊ0�Ļ���˵��ȫ��ʹ����
			int biClrImportant = 0;// λͼ��ʾ��������Ҫ����ɫ��(50-53�ֽ�)�����Ϊ0�Ļ���˵��ȫ����Ҫ

			// ��Ϊjava�Ǵ�˴洢����ôҲ����˵ͬ�����������
			// ��������ǰ�С�˶�ȡ��������ǲ��ı���ֽ����ݵ�˳��Ļ�����ô�����Ͳ���������ȡ��
			// �������ȵ��÷�����int����ת��Ϊ���byte���ݣ����Ұ�С�˴洢��˳��

			// ������Ϣͷ����
			dos.write(changeByte(biSize), 0, 4);// ������Ϣͷ���ݵ����ֽ���
			dos.write(changeByte(biWidth), 0, 4);// ����λͼ�Ŀ�
			dos.write(changeByte(biHeight), 0, 4);// ����λͼ�ĸ�
			dos.write(changeByte(biPlanes), 0, 2);// ����λͼ��Ŀ���豸����
			dos.write(changeByte(biBitcount), 0, 2);// ����ÿ������ռ�ݵ��ֽ���
			dos.write(changeByte(biCompression), 0, 4);// ����λͼ��ѹ������
			dos.write(changeByte(biSizeImage), 0, 4);// ����λͼ��ʵ�ʴ�С
			dos.write(changeByte(biXPelsPerMeter), 0, 4);// ����λͼ��ˮƽ�ֱ���
			dos.write(changeByte(biYPelsPerMeter), 0, 4);// ����λͼ�Ĵ�ֱ�ֱ���
			dos.write(changeByte(biClrUsed), 0, 4);// ����λͼʹ�õ�����ɫ��
			dos.write(changeByte(biClrImportant), 0, 4);// ����λͼʹ�ù�������Ҫ����ɫ��

			// �����ɫ������
			byte[] palatte = new byte[1024];
			for (int i = 0; i < 256; i++) {
				palatte[i * 4] = (byte) i;
				palatte[i * 4 + 1] = (byte) i;
				palatte[i * 4 + 2] = (byte) i;
				palatte[i * 4 + 3] = 0;
			}
			dos.write(palatte);

			dos.write(data);
			// �ر����ݵĴ���
			dos.flush();
			buffer = baos.toByteArray();
			dos.close();
			// fos.close();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * ��һ��int����תΪ��С��˳�����е��ֽ�����
	 * 
	 * @param data
	 *            int����
	 * @return ��С��˳�����е��ֽ�����
	 */
	private byte[] changeByte(int data) {
		byte b4 = (byte) ((data) >> 24);
		byte b3 = (byte) (((data) << 8) >> 24);
		byte b2 = (byte) (((data) << 16) >> 24);
		byte b1 = (byte) (((data) << 24) >> 24);
		byte[] bytes = { b1, b2, b3, b4 };
		return bytes;
	}

	private short getShort(byte b1, byte b2) {
		short temp = 0;
		temp |= (b1 & 0xff);
		temp <<= 8;
		temp |= (b2 & 0xff);
		return temp;
	}

	private byte[] short2byte(short s) {
		byte[] size = new byte[2];
		size[1] = (byte) (s & 0xff);
		size[0] = (byte) ((s >> 8) & 0xff);
		return size;
	}

	/**
	 * ָ�Ƶ÷ֱȶ�,>=50�ַ���true���ȶԳɹ�
	 * 
	 * @return
	 */
	private boolean score(byte b1, byte b2) {
		byte[] temp = { b1, b2 };
		short score = 0;
		score |= (temp[0] & 0xff);
		score <<= 8;
		score |= (temp[1] & 0xff);
		return score >= 50;
	}

	private void sendCommand(byte[] commandBytes) {
		
	   int seglen =  commandBytes.length/32 ;
	   int remain =  commandBytes.length%32 ;	
	   
	   if(seglen > 0)
	   {   
		    byte[] command = new byte[7 + 32];
		    command[0] = 2;
			command[1] = 0;
			command[2] = 0; 
			command[3] = 0;
			command[4] = 1;
			command[5] = 0;
			command[6] = 32 ;
	   
		for(int i = 0 ; i < seglen ; i++)
		{
			System.arraycopy(commandBytes, 32 * i, command, 7, 32);
			communicateManager.write(command);
		}
		
	   }
	
	   if(remain > 0)
	   {   
		   byte[] command = new byte[7 + remain];
		    command[0] = 2;
			command[1] = 0;
			command[2] = 0;
			command[3] = 0;
			command[4] = 1;
			command[5] = 0;
			command[6] = (byte)remain ;
		   
		   System.arraycopy(commandBytes, 32 * seglen, command, 7, remain);
		   communicateManager.write(command);
	   }
	   
	 
//		byte[] command = new byte[7 + commandBytes.length];
//		command[0] = 2;
//		command[1] = 0;
//		command[2] = 0;
//		command[3] = 0;
//		command[4] = 1;
//		command[5] = 0;
//		command[6] = (byte) commandBytes.length;
//		for (int i = 0; i < commandBytes.length; i++) {
//			command[7 + i] = commandBytes[i];
//		}
//		communicateManager.write(command);
	
		    
		
	}

	private void printlog(String tag, int length) {
		byte[] temp = new byte[length];
		System.arraycopy(buffer, 0, temp, 0, length);
		Log.i("whw", tag + "=" + DataUtils.toHexString(temp));
	}

	public class Result {
		/**
		 * ȷ����
		 */
		public int code;
		/**
		 * ҳ��
		 */
		public int pageId;
		/**
		 * �÷�
		 */
		public int matchScore;
	}

}
