package com.sheel.utils;

import static com.sheel.utils.SheelMaayaaConstants.*;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonObject;
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
	 * 
	 * @author Hossam_Amer
	 */
	public static void startHttpService(String path, String FILTER, Context mContext) {
		
		Intent serviceIntent = new Intent(mContext, SheelMaayaaService.class);
    	serviceIntent.setAction(FILTER);
    	serviceIntent.putExtra(pathKey, path);
    	
    	Log.e(TAG, "Before Get HTTP Request: " + serviceIntent);
	    mContext.startService(serviceIntent);
    	
	}

	public static void startHttpService(String path, String json, String FILTER, Context mContext) {
		
		Intent serviceIntent = new Intent(mContext, SheelMaayaaService.class);
    	serviceIntent.setAction(FILTER);
    	serviceIntent.putExtra(pathKey, path);
    	serviceIntent.putExtra(jsonObject, json);
    	
    	Log.e(TAG, "Before Get HTTP Request: " + serviceIntent);
	    mContext.startService(serviceIntent);
    	
	}
	
}
		