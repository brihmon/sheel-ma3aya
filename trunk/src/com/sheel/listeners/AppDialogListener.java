package com.sheel.listeners;

import android.os.Bundle;
import android.util.Log;

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

	public void onFacebookError(FacebookError e) {
		Log.e("AppDialogListener: " , "onFacebookError: "  );
		e.printStackTrace();
	}// end onFacebookError

	public void onComplete(Bundle values) {	
		
	}// end onComplete

	public void onError(DialogError e) {	
		Log.e("AppDialogListener: " , "onError: "  );
		e.printStackTrace();
	}// end onError

	public void onCancel() {
			
	}// end onCancel

}// end class
