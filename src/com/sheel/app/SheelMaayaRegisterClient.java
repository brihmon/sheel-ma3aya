package com.sheel.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author Nada
 * 
 */

// Responsible for sending HTTP requests and getting HTTP response
public abstract class SheelMaayaRegisterClient {

	// final String SERVER = "http://sheelmaaayaa.appspot.com";
	// String PATH = "";
	// HttpResponse RESPONSE = null;
	Thread THREAD = null;
	String rspStr = "";
	List<NameValuePair> PARAMETER;

	public SheelMaayaRegisterClient() {

	}

	public void runHttpRequest(List<NameValuePair> params) {

		PARAMETER = params;

		THREAD = new Thread(new Runnable()

		{
		//	@Override
			public void run() {
				try {
					// Create a new HttpClient and Post Header
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://sheelmaaayaa.appspot.com/insertuser");

					rspStr = "";


					// String paramString = URLEncodedUtils.format(params,
					// "utf-8");

					httppost.setEntity(new UrlEncodedFormEntity(PARAMETER));

					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);
					// DefaultHttpClient CLIENT = new DefaultHttpClient();
					// RESPONSE = CLIENT.execute(new HttpGet(SERVER + PATH));

					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						rspStr = EntityUtils.toString(response.getEntity());
					}

				} catch (Exception e) {
					rspStr = e.toString();
				}
				doSomething();
			}
		});

		THREAD.start();
	}

	// To be implemented by according to the activity needs
	public abstract void doSomething();

}
