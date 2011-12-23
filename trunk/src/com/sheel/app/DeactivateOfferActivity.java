package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.*;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sheel.app.MyOffersActivity.SheelMaayaaBroadCastRec;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.SheelMaayaaConstants;

/**
 * 
 * @author mohsen
 * Temporary activity, used for testing only
 */
/*
public class DeactivateOfferActivity extends UserSessionStateMaintainingActivity {
	IntentFilter filter;
	BroadcastReceiver br;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.deactivate_offer);
		
	}
	
	public void onClick_delete(View v){
		EditText oid = (EditText) findViewById(R.id.offer_id);
		TextView tv = (TextView) findViewById(R.id.deactivate_response);
		//long id = Integer.parseInt(oid.getText().toString());
		//tv.setText("Offer with id = "+oid.getText().toString()+"\n");
		deactivateOffer(oid.getText().toString());
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		filter = new IntentFilter();
		//filter.addAction(HTTP_DEACTIVATE_OFFER);		
		br = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				String responseStr = intent.getExtras().getString(HTTP_RESPONSE);
				((TextView) findViewById(R.id.deactivate_response)).setText(responseStr);
				
				
			}
		};
		
		 
		registerReceiver(br, filter);
	}
	
	void deactivateOffer(String id){
		//HTTPManager.startHttpService("/deactivateoffer/xx",id , SheelMaayaaConstants.HTTP_DEACTIVATE_OFFER, getApplicationContext());
		filter = new IntentFilter();
	//	filter.addAction(SheelMaayaaConstants.HTTP_DEACTIVATE_OFFER);	
		registerReceiver(br, filter);
	}
	

}*/
