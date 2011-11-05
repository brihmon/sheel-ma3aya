package com.sheel.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

// Sending mail: http://mobile.tutsplus.com/tutorials/android/android-email-intent/

public class PhoneCommunication extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communicate);
	}

	public void onClick_call (View v) 
	 {
		try {
	 
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			/*
			 * User x = new User()
			 * x.getInfo();
			 * String x = x.getinfo()
			 * tel: x;
			 */
			
			callIntent.setData(Uri.parse("tel: 0101577990"));
			startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			
			Log.e("PhoneCommunication.java", "Call failed!", e);
		} 
		
	}// end onClick_call
	
	public void onClick_send_SMS (View v) 
	 {
		try {
	 
			/*
			 * User x = new User()
			 * x.getInfo();
			 * String x = x.getinfo()
			 * tel: x;
			 */
						
			String number = "5556";
			Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
			sendIntent.putExtra("sms_body", "Content of the SMS goes here...");
			startActivity(sendIntent);
			
			
		} catch (ActivityNotFoundException e) {
			
			Log.e("PhoneCommunication.java", "SMS failed!", e);
		} 
		
	}// end onClick_call

	
	
	
}
