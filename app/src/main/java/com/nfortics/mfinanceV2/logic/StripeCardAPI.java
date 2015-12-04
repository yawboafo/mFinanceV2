package com.nfortics.mfinanceV2.logic;

import java.io.UnsupportedEncodingException;

public class StripeCardAPI {
  // private BluetoothChatService chatService;
  private CommunicateManager communicateManager;

  public StripeCardAPI(CommunicateManager communicateManager) {
    this.communicateManager = communicateManager;
  }

  public String[] read() {
    byte[] command = new byte[] { 0x04, 0x1B, 0x25, 0x66, 0x4D, 0x00, 0x00 };
    communicateManager.write(command);
    byte[] buffer = new byte[1024];
    int ret = communicateManager.read(buffer, 15000, 300);
    byte[] data = new byte[ret];
    System.arraycopy(buffer, 0, data, 0, ret);
    return parseData(data);
  }

  private String[] parseData(byte[] data) {
    String decodeStr = null;
    try {
      decodeStr = new String(data, "GB2312");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    if (decodeStr == null) {
      return null;
    }
    decodeStr = decodeStr.trim();
    // Log.i("whw", "decodeStr=" + decodeStr);
    String[] strSplit = decodeStr.split(";");
    if (strSplit.length < 2) {
      return null;
    }
    return strSplit;
  }
}
