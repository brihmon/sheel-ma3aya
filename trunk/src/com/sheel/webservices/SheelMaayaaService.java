package com.sheel.webservices;


import static com.sheel.datastructures.enums.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.datastructures.enums.SheelMaayaaConstants.HTTP_STATUS;
import static com.sheel.datastructures.enums.SheelMaayaaConstants.SERVER;
import static com.sheel.datastructures.enums.SheelMaayaaConstants.pathKey;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


/**
 * SheelMaayaService is the class used for handling the HTTP requests via the 
 * 						IntentService
 * @author Hossam_Amer
 *
 */

public class SheelMaayaaService extends IntentService
{
	
	// Response got from the server upon HTTP request 
	HttpResponse httpResponse;
	
	// TAG string for debugging
	private static final String TAG = SheelMaayaaService.class.getName();
	
	
	public SheelMaayaaService()
	{
		super("SheelMaayaaService");
	}
		
	/**
	 * Used for handling the intent in three main steps:
	 * 1- Getting the path for the HTTP request from the intent
	 * 2- Executing the request by the HTTPClient
	 * 3- Broadcasting the result for the BroadcastReceivers
	 *
	 * @param intent Used for passing the path to the service
	 * @author Hossam_Amer
	 */
	
	@Override
	public void onHandleIntent(Intent intent)
	{
		
		//===================STAGE ONE=============================
		// Stage One: Retrieving the path
		
		String PATH = intent.getExtras().getString(pathKey);
		intent.removeExtra(pathKey);
		Log.e(TAG, SERVER + PATH);
		
		//================================================
		
		DefaultHttpClient CLIENT = new DefaultHttpClient();   
     	
     // This is the action or the filter to distinguish between different
	// activities 
		String action = intent.getAction();		
		Log.e(TAG, action);
		Intent broadCastIntent = new Intent(action);
		
		//===================STAGE TWO=============================		
		// Stage Two: Executing the HTTP Request
		try
		{
			httpResponse = CLIENT.execute(new HttpGet(SERVER + PATH));
			String responseString = EntityUtils.toString(httpResponse.getEntity());
			
			broadCastIntent.putExtra(HTTP_RESPONSE, responseString);
			
			Log.e(TAG, responseString);
		}
		
		catch (Exception e) {
			
			Log.e("Error: ", e.toString());
		}
		
		//================================================
		
		finally
		{
			//===================STAGE THREE=============================			
			// Stage Three: Broadcasting the result for the receivers
			
			broadCastIntent.putExtra(HTTP_STATUS, httpResponse.getStatusLine().getStatusCode());
			broadCastIntent.putExtras(intent);
			
			this.sendBroadcast(broadCastIntent);
		}
		
		//================================================
		     			
	}
	
}