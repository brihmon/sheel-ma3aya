package com.sheel.listeners;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_CHECK_REGISTERED;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;

import org.apache.http.HttpStatus;

import com.sheel.utils.InternetManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Class used to handle the checking of the results returned by the user
 * whether he is a registered user or needs to register
 * 
 * @author 
 * 		Nada Emad (nada.adly@gmail.com)
 * @author 
 * 		Passant El.Agroudy (passant.elagroudy@gmail.coms)
 *
 */
public abstract class LoginDataBaseListener extends BroadcastReceiver {
	private static final String TAG = LoginDataBaseListener.class.getName();
	
	public LoginDataBaseListener(IntentFilter filterToAddActionTo){
		filterToAddActionTo.addAction(HTTP_CHECK_REGISTERED);
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		int httpStatus = intent.getExtras().getInt(HTTP_STATUS);
		Log.e(TAG, "HTTPSTATUS: " + httpStatus);

		if (httpStatus == HttpStatus.SC_OK) {
			if (action.equals(HTTP_CHECK_REGISTERED)) {
				String responseStr = intent.getExtras()
						.getString(HTTP_RESPONSE);
				if (responseStr.equals("false")) {
					System.out.println("Not found");
					doActionUserIsNotRegistered();
				}// end if: user is not registered
				else {
					System.out.println("found in database");
					doActionUserIsRegistered();
				}// end else: user is registered
				context.unregisterReceiver(this);
			}// end if: this is the response of the required controller
		}// end if: HTTP request was successful
	}// end onReceive

	/**
	 * Performed if the check on the registration of the user returned that the
	 * user is already registered
	 */
	public abstract void doActionUserIsRegistered();

	/**
	 * Performed if the check on the registration of the user returned that the
	 * user is not registered
	 */
	public abstract void doActionUserIsNotRegistered();
}// end LoginDataBaseListener class

