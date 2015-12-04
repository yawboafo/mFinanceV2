package com.nfortics.mfinanceV2.logic;

/**
 * cpu������ִ�в������£�
 * ��1�� GetChallenge ��ȡ4�ֽ������ 
 * ��2�� authentication 4�ֽ������+4�ֽ�0 ��Կ��8���ֽ�0xFF�������ⲿ��֤  
 * ��3�� DeleteFile ɾ��MFĿ¼�������ļ�     
 * ��4�� InitMF ����MFĿ¼   //���� Ŀǰ�޷�����
 * ��5�� InitADF ����DFĿ¼ //���� ��ʱδ����
 * ��6�� InitKEF ������Կ�ļ� //����MFĿ¼����Կ�ļ�
 * ��7�� appendKEY �����Կ //Ŀǰֻ��Ҫ����ⲿ��֤��Կ Ϊ�˷�����䣬��ʱȡ8���ֽ�0xFF
 * ��8�� InitBEF �����������ļ� //
 * ��9�� Select_BinaryFile ѡ��������ļ� //ѡ���ļ���ʱ���� ��Ӱ���д
 * ��10�� UpdateBinary д�������ļ�
 * ��11�� ReadBinary ��ȡ�������ļ�  ������Ϊ0 ����ʾ��ȡ������¼��Ŀǰ��֧�����ⳤ�ȶ�ȡ
 * ��12�� InitREF ������¼�ļ� Ŀǰֻ���˽���ѭ��������¼�ļ�
 * ��13�� AppendRecord ����ѭ��������¼�ļ�  ���ȱ�����ѽ������ļ������涨�ĳ���һ��
 * ��14�� ReadRecord ��ȡ��¼�ļ�   ������Ϊ0 ����ʾ��ȡ������¼��Ŀǰ��֧�����ⳤ�ȶ�ȡ
 */
import java.io.UnsupportedEncodingException;

// Wrapper for native library

public class RFIDAPI {

	public final  int SCARD_UNKNOWN		=	0x0001;	/*!< Unknown state */
	public final int SCARD_UART_NOT_CONNECTED	=		0x0002;	/*!< UART Not connected   */
	public final int SCARD_ABSENT	=		0x0003;	/*!< Card is absent */
	//smartCOS Create_File
	public final int SCARD_COMMAND_EXECUTED_CORRECTLY	=		0x0004;	/*!< Command executed correctly  */
	public final int SCARD_WRITE_EEPROM_FAIL	=		0x0005;	/*!< Write EEPROM failure   */
	public final int SCARD_DATA_LENGTH_ERROR	=		0x0006;	/*!< Data length error    */
	public final int SCARD_ALLOW_CODE_TRANSFER_ERROR_COUNT	=		0x0007;	/*!< Allow the code transfer error count    */
	public final int SCARD_CREATE_CONDITION_NOT_SATISFIED	=		0x0008;	/*!< Create condition not satisfied     */

	public final int SCARD_SECURITY_CONDITION_NOT_SATISFIED		=	0x0007;	/*!< Security condition is not satisfied     */
	public final int SCARD_IDENTIFIER_ALREADY_EXISTS	=		0x0008;	/*!< Identifier already exists      */
	public final int SCARD_FUNCTION_NOT_SUPPORTED	=		0x0009;	/*!< Function not supported       */
	public final int SCARD_FILE_NOT_FOUND	=		0x0011;	/*!< File not found        */
	public final int SCARD_NOT_ENOUGH_SPACE		=	0x0012;	/*!< Not enough space */
	public final int SCARD_PAREMETER_IS_INCORRECT	=		0x0013;	/*!< The parameter is incorrect         */
	public final int SCARD_INS_IS_INCORRECT		=	0x0014;	/*!< The INS is incorrect         */
	public final int SCARD_CLA_IS_INCORRECT		=	0x0015;	/*!< The CLA is incorrect         */

	//Write_KEY
	public final int SCARD_CMD_NOT_MATCH_TYPES	=		0x0016;	/*!< Command file types do not match        */
	public final int SCARD_KEY_LOCK		=	0x0017;	/*!< Key lock         */
	public final int SCARD_GET_RANDOM_INVALID	=		0x0018;	/*!< From a random number is invalid         */
	public final int SCARD_CONDITION_OF_USE_NOT_SATISFIED	=		0x0019;	/*!< Conditions of use does not satisfied          */
	public final int SCARD_MAC_INCORRECT	=		0x0020;	/*!< MAC is incorrect          */
	public final int SCARD_DATA_NOT_CORRECT		=	0x0021;	/*!< Data domain is not correct           */
	public final int SCARD_CARD_LOCK	=		0x0022;	/*!< Card lock            */
	public final int SCARD_FILE_SPACE_INSUFFICIENT		=	0x0023;	/*!< File space is insufficient              */
	public final int SCARD_P1_AND_P2_NOT_CORRECT	=		0x0024;	/*!< P1 and P2 not correct            */
	public final int SCARD_APP_PERMANENT_LOCK		=	0x0025;	/*!< Application  permanent lock             */
	public final int SCARD_KEY_NOT_FOUND	=		0x0026;	/*!< KEY is not found             */

	public final int SCARD_NOT_BINARY_FILE		=	0x0027;	/*!< not binary file           */
	public final int SCARD_CONDITION_OF_READ_NOT_SATISFIED	=		0x0028;	/*!< the conditions of read does not satisfied           */
	public final int SCARD_CONDITION_OF_CMD_NOT_SATISFIED	=		0x0029;	/*!< the condition of command execution does not satisfied           */
	public final int SCARD_RECORD_NOT_FOUND		=	0x0030;	/*!< record not found        */
	public final int SCARD_NO_DATA_RETURN		=	0x0031;	/*!< Card no data can be returned       */

	public final int SCARD_SECURITY_DATA_NOT_CORRECT	=		0x0032;	/*!< Security message data item is not correct        */
	public final int SCARD_P1_AND_P2_OUT_OF_GAUGE		=	0x0033;	/*!< P1 and P2 are out of gauge            */
	public final int SCARD_FILE_NOT_LINEAR_FIXED_FILE	=		0x0034;	/*!< The file is not a linear fixed length file             */
	public final int SCARD_APP_TEMPORARY_LOCED	=		0x0035;	/*!< APP Temporary locked           */
	public final int SCARD_FILE_STORAGE_SPACE_NOT_ENOUGH	=		0x0036;	/*!< File storage space is not enough          */

	public final int PROCLAIMED = 0;//����
	public final int CIPHERTEXT = 1;//����
	//mFd = open(path, baudrate);//device.getAbsolutePath()
	// JNI
	//public native int open(String path, int baudrate);
	//public native int write(int fd, byte[] buf, int count);
	//public native int read(int fd, byte[] buf, int count);
	//public native int close(int fd);
	
	//public Serialport(Context context) {
	//	final int mFd = open(path, baudrate);//device.getAbsolutePath()
   // }
	//mFd = open(path, baudrate);//device.getAbsolutePath()
	private RFID rfid = null;

	//private static BluetoothChatService mChatService = null;
	private static CommunicateManager communicateManager = null;
	//private Lock Mutex = null;

	public RFIDAPI(CommunicateManager communicateManager) {
		//String className,String MethodSend,String MethodRecv
		this.communicateManager = communicateManager;
		rfid = new RFID();
		String className = new String("com/corewise/logic/RFIDAPI");
		String MethodSend = new String("BluetoothSend");
		String MethodRecv = new String("BluetoothRecv");
		UninitRFID();
		InitRFID(className,MethodSend,MethodRecv);
    }
    
    public void InitRFID(String className,String MethodSend,String MethodRecv)
	{
		rfid.InitClassName(className.getBytes(),MethodSend.getBytes(),MethodRecv.getBytes());
	}
	
	public void UninitRFID()
	{
		rfid.DeInitClassName();
	}
	
//����MF�ļ�
	public int InitMF()
	{
		byte TransCode = (byte) 0xFF;
		char Authority = 0xF0F0;
		byte FileId = 0x01;

		return rfid.InitMF(TransCode, Authority, FileId);
	}
//����DF�ļ�
	public int InitADF()
	{
		byte FileId = (byte) 0x95;
		char Authority = 0xF0F0;
		int NameLen = 9;
		byte[] ADFName = {(byte) 0xA0,0x00,0x00,0x00,0x03,(byte) 0x86,(byte) 0x98,0x07,0x01};
		
		return  rfid.InitADF(FileId,Authority,NameLen,ADFName);
	}
	//����MF�ļ�����Կ�ļ�
	public int InitMKEF()
	{
		char FileId = (0x00<<8)|0x00;
		byte FileType = (byte) 0x3F;
		char Authority = 0x01F0;
		char FileLen = 0x00B0;
		
		return rfid.InitBEF(FileId, FileType, Authority, FileLen);
	}
//�����������ļ�
	public int InitBEF()
	{
		char FileId = (0x00<<8)|0x16;
		byte FileType = (byte) 0xA8;
		char Authority = 0xF0F0;
		char FileLen = 0x0027;
		
		return rfid.InitBEF(FileId, FileType, Authority, FileLen);
	}
//����ѭ����¼�ļ�
	public int InitREF()
	{
		char FileId = (0x00<<8)|0x18;
		byte FileType = 0x2E;
		char Authority = 0xF0F0;
		char FileLen = 0x0A17;//��ʾ����0x0A����¼��ÿһ����¼�ĳ�����0x17
		
		return rfid.InitREF(FileId,FileType,Authority,FileLen);
	}
//���������ļ�
	/**
	 * 
	 * @param RecBuf
	 * @param Count ���Ȳ�Ҫ�����ѽ����Ķ������ļ��ĳ��ȣ� ��ʱ��Ϊ0x27
	 * @return ����0x04��ʾ��ȷ
	 */
	public int ReadBinary(byte[] RecBuf,int Count)
	{
		byte FileId = (0x00<<8)|0x16;
		byte Offset = 0x00;
		return rfid.ReadBinary(FileId, Offset, Count, RecBuf);
	}
//���¶������ļ�
	public int UpdateBinary(byte[] SendBuf,int Count)
	{
		byte FileId = (0x00<<8)|0x16;
		byte Offset = 0x00;
		int level = 0x00;
		return rfid.UpdateBinary(FileId,Offset,level,SendBuf,Count);
	}
//��ѭ����¼�ļ�
	public int ReadRecord(byte[] RecBuf,int Count)
	{
		byte FileId = (0x00<<8)|0x18;
        int Index = 0x01;
        int level = 0x00;

		return rfid.ReadRecord(FileId, level, Index, RecBuf, 0);
	}
//����ѭ��������¼�ļ�
	/**
	 * 
	 * @param SendBuf  
	 * @param Count  ��������ܳ����ѽ����ļ��涨�ĵ�����¼�ĳ��� ��ʱΪ0x17
	 * @return ����0x04��ʾ��ȷ
	 */
	public int AppendRecord(
        byte[] SendBuf,
        int Count
    )
	{
		byte FileId = (0x00<<8)|0x18;
        int level = 0x00;
        byte[] tempBuf = new byte[0x17];
        
        if(Count>tempBuf.length)
        	Count = tempBuf.length;
        
       	System.arraycopy(SendBuf, 0, tempBuf, 0, Count);

		return rfid.AppendRecord(FileId,level,tempBuf,tempBuf.length);
	}
//����ѭ����¼�ļ�
	/**
	 * 
	 * @param SendBuf
	 * @param Count ��������ܳ����ѽ����ļ��涨�ĵ�����¼�ĳ��� ��ʱΪ0x17
	 * @return ����0x04��ʾ��ȷ
	 */
	public int UpdateRecord(byte[] SendBuf,int Count)
	{
		byte FileId = (0x00<<8)|0x18;
        int level = 0x00;
        int Index = 0x00;//index Ϊ��¼�ţ������ļ���N����¼�����¼�ſ�����1-N��
        
        byte[] tempBuf = new byte[0x17];
        
        if(Count>tempBuf.length)
        	Count = tempBuf.length;
        
       	System.arraycopy(SendBuf, 0, tempBuf, 0, Count);
       	
		return rfid.UpdateRecord(FileId, level, Index, tempBuf, tempBuf.length);
	}
//ѡ��������ļ�
	/**
	 * ��Ŀ¼�´��ڶ���������ļ�ʱ����Ҫѡ���Ӧ�Ķ������ļ���Ȼ����ܽ��ж�д
	 * @return ����0x04��ʾ��ȷ
	 */
	public int Select_BinaryFile() {
		int FileType = 0x02;
		//int FileIndex = 0x00;
		int Idlen = 0x02;
		byte[] FileId = {0x00,0x16};

		return rfid.SelectFile(FileType, Idlen, FileId);
	}
//ѡ��ѭ����¼�ļ�	
	/**
	 * ��Ŀ¼�´��ڶ��ѭ����¼�ļ�ʱ����Ҫѡ���Ӧ�ļ�¼�ļ���Ȼ����ܽ��ж�д
	 * @return ����0x04��ʾ��ȷ
	 */
	public int Select_RecordFile() {
		int FileType = 0x02;
		//int FileIndex = 0x00;
		int Idlen = 0x02;
		byte[] FileId = {0x00,0x18};

		return rfid.SelectFile(FileType,Idlen,FileId);
	}
	
	//ѡ��MFĿ¼
	/**
	 * ѡ����Ŀ¼������ֻ��һ����Ŀ¼���ɲ���ѡ��
	 * @return ����0x04��ʾ��ȷ
	 */
	public int Select_MasterFile() {
		int FileType = 0x00;
		//int FileIndex = 0x00;
		int Idlen = 0x00;
		byte[] FileId = {0x00,0x00};
		
		return rfid.SelectFile(FileType,Idlen,FileId);
	}
	
	//ѡ��DF�ļ�	
	/**
	 * ����Ҫ���Ѵ�����DFĿ¼�²���ʱ��Ҫ��ѡ���ӦDFĿ¼
	 * @return ����0x04��ʾ��ȷ
	 */
	public int Select_DedicatedFile() {
		int FileType = 0x04;
		//int FileIndex = 0x00;
		int Idlen = 0x09;
		//char FileId = (0x3F<<8)|0x00;
		byte[] FileId = {(byte) 0xA0,0x00,0x00,0x00,0x03,(byte) 0x86,(byte) 0x98,0x07,0x01};
		return rfid.SelectFile(FileType,Idlen,FileId);
	}
	
//��ȡ�����
	public int GetChallenge(int Count,byte[] RecBuf)
	{
		return rfid.GetChallenge(Count, RecBuf);
	}
	//�ⲿ��֤
	/**
	 * 4�ֽ����������4�ֽ�0x00��8�ֽ���Կ��Ӧ������8�ֽ��������16�ֽ���Կ��Ӧ
	 * @param random �����
	 * @param Keycode  ��Կ
	 * @param KeyLen  ��Կ����
	 * @return ����0x04��ʾ��ȷ
	 */
	public int authentication(byte[] random , byte[] Keycode,int KeyLen)
	{
		byte KeyID = 0x00;
		//byte[] Keycode = {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
		//int KeyLen = 16;
		//byte[] Keycode = {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
		//int KeyLen = 8;
		return rfid.authentication(KeyID, random,Keycode,KeyLen);
	}
//��ȡ����ֵ
	/**
	 * Count ����
	 * RecBuf ��ȡCPU��������
	 */
	public int GetResponse(
        int Count,
        byte[] RecBuf
    )
	{
		return rfid.GetResponse(Count, RecBuf);
	}
//ɾ���ļ�
	/**
	 * ɾ��MF���ļ��ļ�
	 * @return ����0x04��ʾ��ȷ
	 */
	public int DeleteFile()
	{
		return rfid.DeleteFile();
	}
	//1��	���ö�����ģʽ
	public int setReaderMode(byte[] RecBuf)
	{
		return rfid.setReaderMode(RecBuf);
	}
	//���ݿ�Ƭ�������ö���Э��ģʽ
	public int setProtocolMode(byte[] RecBuf)
	{
		return rfid.setProtocolMode(RecBuf);
	}
//���ö�������У���뷽ʽ �Զ����ճ�ʱ�б�
	public int setCheckMode(byte[] RecBuf)
	{
		return rfid.setCheckMode(RecBuf);
	}
//Ѱ��
	public int SearchCard(byte[] RecBuf)
	{
		return rfid.SearchCard(RecBuf);
	}
//��ײѡ��
	public int Anticoll(byte[] RecBuf)
	{
		return rfid.Anticoll(RecBuf);
	}
//ѡ��
	/**
	 * 
	 * @param Count ����Ĭ��4λ����1λУ��λ
	 * @param SendBuf ����Ŀ���
	 * @return ����0x04��ʾ��ȷ
	 */
	public int SelectCard(int Count,byte[] SendBuf)
	{
		return rfid.SelectCard(Count,SendBuf);
	}
//��λ
	/**
	 * 
	 * @param Count �������ݵĳ��� Ĭ��16�ֽ�
	 * @param RecBuf ��λ���ص�����
	 * @return ����0x04��ʾ��ȷ
	 */
	public int ResetCard(byte[] RecBuf)
	{
		return rfid.ResetCard(RecBuf);
	}
	//�����Կ
	/**
 	 * 
	 * @param Keycode 8�ֽڻ�10�ֽ���Կ
	 * @param KeyLen ��Կ����
	 * @return ����0x04��ʾ��ȷ
	 */
	public int appendKEY(byte[] Keycode,int KeyLen)
	{
		byte KEY_InMode = PROCLAIMED;
		byte KEY_Opt = 0x01;
		byte KEY_ID = 0x00;
		int Key_Msglen = 5+KeyLen;
		byte[] Key_MsgData = new byte[Key_Msglen] ;
		Key_MsgData[0] = 0x39;
		Key_MsgData[1] = (byte)0xF0;
		Key_MsgData[2] = (byte)0xF0;
		Key_MsgData[3] = (byte)0xAA;
		Key_MsgData[4] = 0x55;
		System.arraycopy(Keycode, 0, Key_MsgData, 5, KeyLen);
		
		return rfid.WriteKEY(KEY_InMode,KEY_Opt,KEY_ID,Key_MsgData,Key_Msglen);
	}
	
	public static int BluetoothSend(byte[] send) {
		communicateManager.write(send);
		return 1;

	}

	public static int BluetoothRecv(byte[] Recv, int bytes) {
		bytes = communicateManager.read(Recv, 4000, 300);
		if(bytes <=0)
		{
			String rec = "No respose from A602";
			byte[] tempRecv = null;
			try {
				tempRecv = rec.getBytes("GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("from A602 bytes="+bytes);
			System.arraycopy(tempRecv, 0, Recv, 0, tempRecv.length);
			bytes = tempRecv.length;
		}
		return bytes;
	}
}