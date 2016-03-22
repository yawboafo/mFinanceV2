package com.nfortics.mfinanceV2.Request;



import com.nfortics.mfinanceV2.Models.FingerPrintInfo;
import com.nfortics.mfinanceV2.Utilities.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Map;

public class FingerPrintTemplateRequest implements Request {

	private ArrayList<NameValuePair> nameValuePairs;
	public static final String BASE_URL = "http://197.159.131.154/crypto/biometric/enroll";

	private static String url;

	public FingerPrintTemplateRequest(String string,String s) {
		nameValuePairs = new ArrayList<NameValuePair>();
		buildURL(string);

	}

	@Override
	public String getURL() {
		return url;
	}

	private void buildURL(String fpi) {
		StringBuilder urlBuffer = new StringBuilder(BASE_URL);
		buildNameValuePairs(fpi);

		url = urlBuffer.toString();
	}

	public ArrayList<NameValuePair> getNameValuePairs() {
		return nameValuePairs;
	}

	private void buildNameValuePairs(String fpi) {
		// nameValuePairs.add(new BasicNameValuePair("fingerprint",
		// fpi.getImageData()));
		nameValuePairs.add(new BasicNameValuePair("fingerprint", fpi));
	}

}
