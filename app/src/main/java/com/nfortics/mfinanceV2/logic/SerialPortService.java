package com.nfortics.mfinanceV2.logic;

import android.os.SystemClock;
import android.util.Log;


import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.nfortics.mfinanceV2.Utilities.DataUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;


public class SerialPortService {
  /**
   * �����豸·��
   */
  private static final String PATH_PREFIX = "/dev/ttyUSB";

  /**
   * ���ڲ�����
   */
  private static final int BAUDRATE = 115200;
  // private static final int BAUDRATE = 230400;

  private static SerialPortService mSerialPortService = new SerialPortService();

  private final Logger logger = LoggerFactory.getLogger();

  private SerialPort mSerialPort = null;

  private OutputStream mOutputStream;

  private InputStream mInputStream;

  private byte[] mBuffer = new byte[50 * 1024];

  private int mCurrentSize = 0;

  private boolean isOpen = false;

  /**
   * �жϴ����Ƿ��
   * @return
   */
  public boolean isOpen() {
    return isOpen;
  }

  private ReadThread mReadThread;

  private SerialPortService() {
  }

  /**
   * ��ȡ�����ʵ������Ϊ����
   * @return
   */
  public static SerialPortService getInstance() {
    return mSerialPortService;
  }

  private String getSerialPath() {
    File dev = new File("/dev");
    File[] files = dev.listFiles();
    int i;
    for (i = 0; i < files.length; i++) {
      String path = files[i].getAbsolutePath();
      if (path.startsWith(PATH_PREFIX)) {
        return path;
      }
    }
    return null;
  }

  /**
   * �򿪴��� �ɹ�����true ʧ�ܷ���false
   * @throws SecurityException
   * @throws IOException
   * @throws InvalidParameterException
   */
  public boolean openSerialPort() throws SecurityException, IOException, InvalidParameterException {
    String path = getSerialPath();
    logger.debug("openSerialPort path=" + path);
    logger.debug("openSerialPort isOpen=" + isOpen);
    if (path == null) {
      return false;
    }
    if (mSerialPort == null) {
      /* Open the serial port */
      mSerialPort = new SerialPort(new File(path), BAUDRATE, 0);
      mOutputStream = mSerialPort.getOutputStream();
      mInputStream = mSerialPort.getInputStream();
      logger.debug("openSerialPort mOutputStream=" + mOutputStream);
      logger.debug("openSerialPort mInputStream=" + mInputStream);
      mReadThread = new ReadThread();
      mReadThread.start();
      isOpen = true;
      return true;
    }
    return false;
  }

  /**
   * �رմ���
   */
  public void closeSerialPort() {
    isOpen = false;
    if (mReadThread != null)
      mReadThread.interrupt();
    mReadThread = null;
    if (mSerialPort != null) {
      try {
        mOutputStream.close();
        mInputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
        logger.debug("closeSerialPort isOpen=" + isOpen);
      }
      mSerialPort.close();
      mSerialPort = null;
    }
    mCurrentSize = 0;
  }

  public synchronized int read(byte buffer[], int waittime, int endTimeout) {
    return read(buffer, waittime, endTimeout, -1);
  }

  /**
   * @param buffer
   * ��ȡ�������ݴ����buffer��
   * @param waittime
   * �ڵȴ���������ʱ���ȴ�ʱ�����waittime���͵��������ݳ�ʱ�˳�
   * @param endTimeout
   * �ڽ��յ����ݹ����У������endTimeout�������ʱ�䷶Χ�ڣ�û�н��յ������ݣ��
   * ��ж���λ�����ݽ�������
   * @return ���յ������ݳ���
   */

  public synchronized int read(byte[] buffer, int waitTime, int maxTimeout, int requestLength) {

    int lastGetBytes = mCurrentSize;

    int timeOut = waitTime;

    long lastTime = System.currentTimeMillis();

    if (mCurrentSize > 0) {
      timeOut = maxTimeout;
    }

    while (true) {

      if (requestLength > -1 && mCurrentSize >= requestLength) {
        System.arraycopy(mBuffer, 0, buffer, 0, requestLength);

        return requestLength;
      }

      if (System.currentTimeMillis() - lastTime >= timeOut) {
        System.arraycopy(mBuffer, 0, buffer, 0, mCurrentSize);
        return mCurrentSize;
      }

      if (mCurrentSize > lastGetBytes) {
        lastGetBytes = mCurrentSize;
        timeOut = maxTimeout;
        lastTime = System.currentTimeMillis();
      }

    }

  }

  // public synchronized int read(byte buffer[], int waittime, int endTimeout,
  // int requestLength) {
  // int sleepTime = 10;
  // int length = waittime / sleepTime;
  // boolean shutDown = false;
  // int[] readDataLength = new int[3];
  // for (int i = 0; i < length; i++) {
  // if (mCurrentSize == 0) {
  // SystemClock.sleep(sleepTime);
  // continue;
  // } else {
  // break;
  // }
  // }
  //
  // if (mCurrentSize > 0) {
  // while (!shutDown) {
  // if (requestLength == -1) {
  // shutDown = isTimeOut(readDataLength, endTimeout);
  // } else {
  // if (mCurrentSize == requestLength) {
  // shutDown = true;
  // logger.debug("mCurrentSize == requestLength shutDown="+shutDown);
  // } else {
  // shutDown = isTimeOut(readDataLength, endTimeout);
  // logger.debug("isTimeOut(readDataLength, endTimeout)="+shutDown);
  // }
  // }
  // }
  // if (mCurrentSize <= buffer.length) {
  // System.arraycopy(mBuffer, 0, buffer, 0, mCurrentSize);
  // }
  // }
  // return mCurrentSize;
  // }

  private boolean isTimeOut(int[] readDataLength, int endDataTimeout) {
    SystemClock.sleep(endDataTimeout / 3);
    readDataLength[0] = readDataLength[1];
    readDataLength[1] = readDataLength[2];
    readDataLength[2] = mCurrentSize;
    Log.i("whw", "isTimeOut    mCurrentSize=" + mCurrentSize);
    if (readDataLength[0] == readDataLength[1] && readDataLength[1] == readDataLength[2]) {
      return true;
    } else {
      return false;
    }
  }

  private long lastTime = 0;
  private long currentTime = 0;
  private boolean isTimeOut = false;
  private static final long SLEEP_TIME = 1000 * 60 * 2;
  byte[] wake = { '#' };
  byte[] ack = { 0 };
  long ack_len = 0;

  public synchronized void write(byte[] data) {

    isTimeOut = false;

    logger.debug("data hex=" + DataUtils.toHexString(data));

    try {
      lastTime = currentTime;
      currentTime = System.currentTimeMillis();

      if (currentTime - lastTime >= SLEEP_TIME) {
        for (int i = 0; i < 6; i++) {
          Log.i("SerialPort Service:", "Send wake up char '#' ");
          mOutputStream.write(wake);
          read(ack, 300, 400);
          if (ack[0] == 'W') {
            mCurrentSize = 0;
            mBuffer[0] = 0;
            Log.i("SerialPort Service:", "Get ACK char 'W' ");
            isTimeOut = false;
            break;
          } else if (i == 6) {
            isTimeOut = true;
            break;
          }

        }
      }

      mCurrentSize = 0;

      if (!isTimeOut) {
        mOutputStream.write(data);
      }

    } catch (IOException e) {
      isOpen = false;
      e.printStackTrace();
      logger.debug("write isOpen=" + isOpen);
      logger.debug("write IOException=" + e.getMessage());
    } catch (Exception e) {
      logger.debug(e.getMessage());
    }
  }

  private class ReadThread extends Thread {

    @Override
    public void run() {
      while (!isInterrupted()) {
        int length = 0;
        try {
          byte[] buffer = new byte[100];
          if (mInputStream == null)
            return;
          length = mInputStream.read(buffer);
          if (length > 0) {
            System.arraycopy(buffer, 0, mBuffer, mCurrentSize, length);
            mCurrentSize += length;
          }
          Log.i("whw", "mCurrentSize=" + mCurrentSize + "  length=" + length);
        } catch (IOException e) {
          isOpen = false;
          e.printStackTrace();
          logger.debug("ReadThread isOpen=" + isOpen);
          return;
        }
      }
    }
  }

}
