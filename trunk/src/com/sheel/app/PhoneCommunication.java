package com.sheel.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

// Sending mail: http://mobile.tutsplus.com/tutorials/android/android-email-intent/

/**
 * @author Hossam Amer
 *
 */


public class PhoneCommunication extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communicate);
	}
	
	/*
	 * Used for calling a given user.
	 */

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
			
			String mobile_number = "0101577990";
			callIntent.setData(Uri.parse("tel: " + mobile_number));
			startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			
			Log.e("PhoneCommunication.java", "Call failed!", e);
		} 
		
	}// end onClick_call
	
	
	/*
	 * Used for sending SMS a given user.
	 */
	
	public void onClick_send_SMS (View v) 
	 {
		try {
	 
			/*
			 * User x = new User()
			 * x.getInfo();
			 * String x = x.getinfo()
			 * tel: x;
			 */
			boolean user_type = false;
			String sms_content = "";
			String sms_content1 = "";
			String sms_content2 = "";
			String sms_content3 = "";
			String sms_content4 = "";
			String sms_content5 = "";
			String sms_content6 = "";
			String number = "5556";
			
		if(user_type)
		{
			sms_content1 = "Hello User2,";
			sms_content2 = "I have seen your offer on Sheel M3aaya app that you " +
					"have an extra space (N Kilograms) in your bags." +
					" So, I would like to inform you that " +
					"I am interested in putting some of my stuff in your bags.";
			sms_content3 = "Please contact me at this number if your space is still available.";
			sms_content6 = "Best Regards,\nUser1";
			
			
		}	
		
		else
		{
			
			sms_content1 = "Hello User1,";
			sms_content2 = "I have seen your request on Sheel M3aaya app that you " +
					"need an extra space (N Kilograms) in your bags." +
					" So, I would like to inform you that " +
					"I am interested in offering you some of my space in my bags.";
			sms_content3 = "Please contact me at this number if you are still intrested.";
			sms_content6 = "Best Regards,\nUser2";
		}
		
		sms_content4 = "Thanks in advance :)";
		sms_content5 = "Wish you a nice flight.";
		sms_content = sms_content1 
					+ "\n" + "\n" + sms_content2
					+ "\n" + "\n" + sms_content3
					+ "\n" + "\n" + sms_content4
					+ "\n" + "\n" + sms_content5
					+ "\n" + "\n" + sms_content6;
			
			sendSMS(sms_content, number);			
			
		} catch (ActivityNotFoundException e) {
			
			Log.e("PhoneCommunication.java", "SMS failed!", e);
		} 
		
	}// end onClick_call
	
// http://stackoverflow.com/questions/1930177/android-difference-in-action-send-between-api-level-2-and-5
	//http://blog.iangclifton.com/2010/05/17/sending-html-email-with-android-intent/
	public void onClick_send_email(View v)
	{
		/**
		 * 
		 * Hello User2,Ê
		 * This is an auto confirmation from Sheel M3aya app describing details of your transaction.
		 * You have requested/offered N kilograms from User1 with N euros.
		 * Have a nice flight,
		 * Sheel M3aya team
		 */
//		String path = "/insertconfirmation/8/13/0";
		
		Toast.makeText(PhoneCommunication.this, "Doing your request..", Toast.LENGTH_LONG).show();
		String path = "/insertconfirmation/15/13/1";
        SheelMaaayaClient sc = new SheelMaaayaClient() {
			
			@Override
			public void doSomething() {
				final String str = this.rspStr;
				 
							 runOnUiThread(new Runnable()
                             {
  //                               @Override
                                 public void run()
                                 {
                                     Toast.makeText(PhoneCommunication.this, str, Toast.LENGTH_LONG).show();
                                     if(str.contains("12") || str.contains("13"))
                                     {
//                                    	 sendSMS("be5", "5556");
//                                    	 sendSMS("be52", "5554");
                                    	 
                                    	 Toast.makeText(PhoneCommunication.this, "Offer has been confirmed!", Toast.LENGTH_LONG).show(); 
                                     }
                                     // Check the response string if success and ready
                                     // Then you go and startActivity of send SMS
//                                     startActivity(new Intent(PhoneCommunication.this, PhoneCommunication.class));

                                 }
                             });

			}
		};
        
        sc.runHttpRequest(path);       

	}// end onClick_send_email
	
	private void sendSMS(String sms_content, String number)
	{
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
		sendIntent.putExtra("sms_body", sms_content);
		startActivity(sendIntent);
	}
	
	
	
}//end class PhoneCommunication
