package com.nfortics.mfinanceV2.Request;



import com.nfortics.mfinanceV2.Models.FingerPrintInfo;
import com.nfortics.mfinanceV2.Utilities.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class FingerPrintTemplateRequest implements Request {

	private ArrayList<NameValuePair> nameValuePairs;
	public static final String BASE_URL = "http://197.159.131.154/crypto/biometric/enroll";

	private static String url;

	public FingerPrintTemplateRequest(FingerPrintInfo fpi) {
		nameValuePairs = new ArrayList<NameValuePair>();
		buildURL(fpi);

	}

	@Override
	public String getURL() {
		return url;
	}

	private void buildURL(FingerPrintInfo fpi) {
		StringBuilder urlBuffer = new StringBuilder(BASE_URL);
		buildNameValuePairs(fpi);

		url = urlBuffer.toString();
	}

	public ArrayList<NameValuePair> getNameValuePairs() {
		return nameValuePairs;
	}

	private void buildNameValuePairs(FingerPrintInfo fpi) {
		// nameValuePairs.add(new BasicNameValuePair("fingerprint",
		// fpi.getImageData()));
		nameValuePairs.add(new BasicNameValuePair("fingerprint", Utils.getBase64Bytes(fpi.getBitmap())));
	}

}
