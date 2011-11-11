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
			String sms_content1 = "Hello User2,";
			String sms_content2 = "I have seen your offer on Sheel M3aaya app that you " +
					"have an extra space (N Kilograms) in your bags." +
					" So, I would like to inform you that " +
					"I am interested in putting some of my stuff in your bags.";
			String sms_content3 = "Please contact me at this number if your space is still available.";
			String sms_content4 = "Thanks in advance :)";
			String sms_content5 = "Wish you a nice flight.";
			String sms_content6 = "Best Regards,\nUser1";
			
			String sms_content = sms_content1 
								+ "\n" + "\n" + sms_content2
								+ "\n" + "\n" + sms_content3
								+ "\n" + "\n" + sms_content4
								+ "\n" + "\n" + sms_content5
								+ "\n" + "\n" + sms_content6;
			
			Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
//			sendIntent.putExtra("sms_body", "Content of the SMS goes here...");
			sendIntent.putExtra("sms_body", sms_content);
			startActivity(sendIntent);
			
			
		} catch (ActivityNotFoundException e) {
			
			Log.e("PhoneCommunication.java", "SMS failed!", e);
		} 
		
	}// end onClick_call

	
	
	
}
