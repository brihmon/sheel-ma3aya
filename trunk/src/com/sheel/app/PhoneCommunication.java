package com.sheel.app;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

// Sending mail: http://mobile.tutsplus.com/tutorials/android/android-email-intent/

/**
 * @author Hossam Amer
 *
 */

public class PhoneCommunication extends UserSessionStateMaintainingActivity {
	
	
	String mobile;
	int kgs;    	
	long offerId;
	String email;
	String fullName;
	String fb_account;
	String fbId;
	long userId;
	int user_status;

	ProgressDialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communicate);
		
		Bundle extras = getIntent().getExtras();
		
		if(extras !=null)
		{
			mobile = extras.getString("mobile");
			kgs  = extras.getInt("kgs");
			offerId = extras.getLong("offerId");
			email = extras.getString("email");
			fullName = extras.getString("fullName");
			fb_account = extras.getString("fb_account");
			fbId = extras.getString("fbId");
			userId = extras.getLong("userId");
			user_status = extras.getInt("user_status");
		}

		TextView x = (TextView) findViewById(R.id.username);
		x.setText(fullName);
		
		x = (TextView) findViewById(R.id.mobile_number);
		x.setText(mobile);
		
		x = (TextView) findViewById(R.id.email);
		x.setText(email);
		
		x = (TextView) findViewById(R.id.fb_account);
		x.setText(fb_account);
		
	}
	
	
	
	/*
	 * Used for calling a given user.
	 */

	public void onClick_call (View v) 
	 {
		try {
	 
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse("tel: " + mobile));
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
			String sms_content = "";
			String sms_content1 = "";
			String sms_content2 = "";
			String sms_content3 = "";
			String sms_content4 = "";
			String sms_content5 = "";
			String sms_content6 = "";
			String number = mobile;
			
		if(user_status == 0)
		{
			sms_content1 = "Hello " + fullName +",";
			sms_content2 = "I have seen your offer on Sheel M3aaya app that you " +
					"have an extra space ("+ kgs +  "Kilograms) in your bags." +
					" So, I would like to inform you that " +
					"I am interested in putting some of my stuff in your bags.";
			sms_content3 = "Please contact me at this number if your space is still available.";
			sms_content6 = "Best Regards,\nUser1";
			
			
		}	
		
		else
		{
			
			sms_content1 = "Hello "+ fullName +",";
			sms_content2 = "I have seen your request on Sheel M3aaya app that you " +
					"need an extra space ("+ kgs + " Kilograms) in your bags." +
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
		
		dialog = ProgressDialog.show(PhoneCommunication.this, "", "Doing your request, Please wait..", true, false);
		Toast.makeText(PhoneCommunication.this, "Doing your request..", Toast.LENGTH_LONG).show();
		String path = "/insertconfirmation/" + userId+"/"+offerId+"/"+user_status;
//        SheelMaaayaClient sc = new SheelMaaayaClient() {
//			
//			@Override
//			public void doSomething() {
//				final String str = this.rspStr;
//				 
//							 runOnUiThread(new Runnable()
//                             {
//  //                               @Override
//                                 public void run()
//                                 {
//                                	 if(dialog != null)
//                                 		dialog.dismiss();
//                                	  
//                                     if(str.contains("12") || str.contains("13"))
//                                     {
//                                    	 Toast.makeText(PhoneCommunication.this, "Offer has been confirmed!", Toast.LENGTH_LONG).show(); 
//                                     }
//                                     else if(str.contains("Success"))
//                                    	 Toast.makeText(PhoneCommunication.this, "You have confirmed the offer!", Toast.LENGTH_LONG).show();
//                                     else if(str.contains("Failure"))
//                                         Toast.makeText(PhoneCommunication.this, "You could not confirm this offer anymore!", Toast.LENGTH_LONG).show();
//
//                                     // Check the response string if success and ready
//                                     // Then you go and startActivity of send SMS
////                                     startActivity(new Intent(PhoneCommunication.this, PhoneCommunication.class));
//
//                                 }
//                             });
//
//			}
//		};
//        
//        sc.runHttpRequest(path);       

	}// end onClick_send_email
	
	private void sendSMS(String sms_content, String number)
	{
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
		sendIntent.putExtra("sms_body", sms_content);
		startActivity(sendIntent);
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dialog = null;
	}
	
	
	
}//end class PhoneCommunication
