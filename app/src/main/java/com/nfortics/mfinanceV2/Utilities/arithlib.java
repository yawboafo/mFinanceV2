package com.nfortics.mfinanceV2.Utilities;

public class arithlib {

	public arithlib() {
	}

	static {
		System.loadLibrary("arith");
	}

	public native void jpancount(long count_num[]);

	public native void jgetnowkey(byte now_key[], byte init_key[],
			long count_num[], byte KSNdata[]);

	public native void jvOneTwo(byte in[], int lc, byte out[]);

	public native void jvOneTwo0(byte in[], int lc, byte out[]);

	public native void jDesstring(byte in_data[], byte data_length, byte key[],
			byte key_lenth, byte out_data[], byte DES_MODE);

}
