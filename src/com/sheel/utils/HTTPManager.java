package com.sheel.utils;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.pathKey;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sheel.webservices.SheelMaayaaService;

public class HTTPManager
{
	private static final String TAG = HTTPManager.class.getName();

	/**
	 * Initiates the HTTP call to the server
	 * @param path
	 * 				Path to the server controller
	 * @param FILTER
	 * 				Filter used for the broadcastreceiver
	 * @param mContext
	 * 			Current context of the application (i.e: getApplicationContext())
	 */
	public static void startHttpService(String path, String FILTER, Context mContext) {
		
//		dialog = ProgressDialog.show(MyOffersActivity.this, "", "Getting your Offers, Please wait..", true, false);
		Intent serviceIntent = new Intent(mContext, SheelMaayaaService.class);
    	serviceIntent.setAction(HTTP_GET_MY_OFFERS_FILTER);
    	serviceIntent.putExtra(pathKey, path);
    	
    	Log.e(TAG, "Before Get HTTP Request: " + serviceIntent);
	    mContext.startService(serviceIntent);
    	
	}

	
}
		