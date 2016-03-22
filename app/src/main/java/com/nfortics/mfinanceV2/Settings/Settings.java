package com.nfortics.mfinanceV2.Settings;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public abstract class Settings implements Serializable {

	private static final long serialVersionUID = -2266328615261330463L;

	public void saveInstance(OutputStream outputStream) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		out.writeObject(this);
		out.flush();
		out.close();
	}
}
