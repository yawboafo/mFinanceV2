package com.nfortics.mfinanceV2.Request;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

public class HTTPRequest {

	// public InputStream processRequest(Request urlBuilder) throws Exception {
	// Log.d(getClass().getName(), urlBuilder.getURL());
	//
	// // String PROXY = "208.52.90.6";
	// // // proxy host
	// // final HttpHost PROXY_HOST = new HttpHost(PROXY, 80);
	// // HttpParams httpParameters = new BasicHttpParams();
	// // DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
	// // httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
	// // PROXY_HOST);
	//
	// DefaultHttpClient httpClient = new DefaultHttpClient();
	// // addCookies(httpClient.getCookieStore());
	// HttpPost request = new HttpPost(urlBuilder.getURL());
	// request.setHeader("User-Agent", "Android User Agent");
	// HttpResponse response = httpClient.execute(request);
	// // processCookies(httpClient.getCookieStore());
	// StatusLine statusLine = response.getStatusLine();
	// switch (statusLine.getStatusCode()) {
	// case HttpStatus.SC_OK:
	// return response.getEntity().getContent();
	// default: {
	// throw new HttpStatusException(statusLine.getReasonPhrase(),
	// statusLine.getStatusCode());
	// }
	// }
	// }

	public InputStream processGetRequest(Request urlBuilder) throws Exception {
		Log.d(getClass().getName(), urlBuilder.getURL());

		// String PROXY = "208.52.90.6";
		// // proxy host
		// final HttpHost PROXY_HOST = new HttpHost(PROXY, 80);
		// HttpParams httpParameters = new BasicHttpParams();
		// DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		// httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
		// PROXY_HOST);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		// addCookies(httpClient.getCookieStore());
		HttpGet request = new HttpGet(urlBuilder.getURL());
		request.setHeader("User-Agent", "Android User Agent");
		HttpResponse response = httpClient.execute(request);
		// processCookies(httpClient.getCookieStore());
		StatusLine statusLine = response.getStatusLine();
		switch (statusLine.getStatusCode()) {
		case HttpStatus.SC_OK:
			return response.getEntity().getContent();
		default: {
			throw new HttpStatusException(statusLine.getReasonPhrase(),
					statusLine.getStatusCode());
		}
		}
	}

	public InputStream processPostRequest(Request urlBuilder,
			ArrayList<NameValuePair> keyValuePairs) throws Exception {
		Log.d(getClass().getName(), urlBuilder.getURL());

		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = keyValuePairs;

		for (NameValuePair nvp : nameValuePairs) {
			Log.d(getClass().getName(), ">>> INFO Parameter " + nvp.getName() + "=" + nvp.getValue());
		}

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlBuilder.getURL());
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		is = entity.getContent();

		return is;
	}

	// private void addCookies(CookieStore cookieStore) {
	// cookieStore.clear();
	// Collection<Cookie> cookies = Settings.session.getCookies();
	// for (Cookie cookie : cookies) {
	// cookieStore.addCookie(cookie);
	// }
	// }
	//
	// private void processCookies(CookieStore cookieStore) {
	// List<Cookie> cookies = cookieStore.getCookies();
	// Settings.session.addNewCookies(cookies);
	// }
}
