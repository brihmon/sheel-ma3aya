package com.sheel.listeners;

import android.view.View;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;


/**
 * InflateListener is the listener used when the Stub is inflated
 * 
 * It is used to save the position of the buttons inside their tags
 * 
 * @author Hossam_Amer
 *
 */

public class InflateListener implements OnInflateListener {
	
	
	// Position Clicked
	int mPos;
	
	public InflateListener(int position)
	{
		mPos = position;
	}
	
	public void onInflate(ViewStub stub, View inflated) {
		// TODO Auto-generated method stub
		
//		Button x = (Button) inflated.findViewById(R.id.details_button_call);
//		x.setTag(mPos);
		
//		Log.e("mPos: ", mPos + "");
	}
 

}
