package com.sheel.listeners;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.pathKey;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewStub.OnInflateListener;
import android.widget.Button;

import com.sheel.app.MyOffersActivity;
import com.sheel.app.R;
import com.sheel.utils.HTTPManager;
import com.sheel.webservices.SheelMaayaaService;


/**
 * InflateListener is the listener used when the Stub is inflated
 * 
 * It is used to save the position of the buttons inside their tags
 * 
 * @author Hossam_Amer
 *
 */

public class InflateListener implements OnInflateListener, OnClickListener {
	
	private static final String TAG = InflateListener.class.getName();

	
	/**
	 *  Position Clicked
	 */
	int mPos;
	
	/**
	 * Current context of the Activity
	 */
	Context mContext;
	
	/**
	 * Number of buttons
	 */
	int numButtons = 3;
	
	/**
	 * Array of Button IDs
	 */
	private int [] buttonIDs = new int [numButtons];
	
	/**
	 * 
	 * @param position
	 * @param mContext
	 */
	public InflateListener(int position, Context mContext)
	{
		mPos = position;
		this.mContext = mContext;
		
		// <=== List your buttons here ===>
		buttonIDs[0] = R.id.details_button_call; 
		buttonIDs[1] = R.id.details_button_confirm;
		buttonIDs[2] = R.id.details_button_sendSms;
	}
	
	/**
	 * 
	 */
	public void onInflate(ViewStub stub, View inflated) {
			
		for(int i=0; i<buttonIDs.length;i++)
		{
			Button btn = (Button) inflated.findViewById(buttonIDs[i]);
			btn.setTag(mPos);
			Log.e(TAG, "Button: " + btn.getId());
			
			btn.setOnClickListener(this);
			
			Log.e(TAG, "Button Listener done: " + btn.getId());
		}
				
	}

	/**
	 * 
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.details_button_call:
			
			Log.e("Call button", "Hello");
			
			
			break;
		case R.id.details_button_confirm:
			
			Log.e("Confirm button", "Hello");
		break;
		case R.id.details_button_sendSms:
			
			Log.e("SMS button", "Hello");
			
			break;
		default:
			break;
		}
		
		
	}
	
	private void sendSMS(String sms_content, String number)
	{
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
		sendIntent.putExtra("sms_body", sms_content);
		mContext.startActivity(sendIntent);
	}
	
	private void call(String number) {
		
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel: " + number));
		mContext.startActivity(callIntent);
	}
	
	private void confirmOffer() {
		
		HTTPManager.startHttpService("", "", mContext);
		
	}
 

}
