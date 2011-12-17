package com.sheel.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * InternetManager is a class that checks the availability of the Internet 
 * @author Hossam_Amer
 * @author Nada Adly
 *
 */

public class InternetManager {

	// Tag variable for logs & debugging
	private static final String TAG = InternetManager.class.getName();
	
	/**
	 * 
	 * @param mContext The current context of the activity
	 * @note You can get mContext by getApplicationContext or getContext, notsure!
	 * @return
	 * 	true   Internet connection is available
	 * 	false  Internet connection is not available
	 * @author Hossam_Amer
	 * @author Nada Adly
	 */
	
	public static boolean isInternetOn(Context mContext) 
	{
		ConnectivityManager connec =  (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		// ARE WE CONNECTED TO THE NET
		if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
		connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
		// MESSAGE TO SCREEN FOR TESTING (IF REQ)
			//System.out.println("CONNECTED");
		//Toast.makeText(this, connectionType + " connected", Toast.LENGTH_SHORT).show();
			
		Log.e(TAG, "Internet is Connected");	
		return true;
		} else if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED  ) {
		//System.out.println("Not Connected");
			Log.e(TAG, "Internet is not Connected");
		return false;
		}
		Log.e(TAG, "Internet is not Connected");
		return false;
	}
}
