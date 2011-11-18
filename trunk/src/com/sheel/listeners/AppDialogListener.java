package com.sheel.listeners;

import android.os.Bundle;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * Quick implementation for a commonly used interface
 * in facebook SDK with different dialogues to detect
 * different actions about it
 * 
 * @author Passant
 *
 */
public class AppDialogListener implements DialogListener {

	public void onComplete(Bundle values) {	
		
	}// end onComplete

	public void onFacebookError(FacebookError e) {
		
	}// end onFacebookError

	@Override
	public void onError(DialogError e) {				
	}// end onError

	@Override
	public void onCancel() {
			
	}// end onCancel

}// end class
