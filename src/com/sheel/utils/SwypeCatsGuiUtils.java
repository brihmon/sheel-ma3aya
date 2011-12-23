
package com.sheel.utils;

import android.app.Activity;
import android.content.Context;

import com.sheel.app.R;


/**
 * Class used for extracting the values from the strings file to localize the views accordingly.
 * @author Hossam_Amer
 * @author Passant El.Agroudy
 *
 */

public class SwypeCatsGuiUtils extends Activity
{
	/**
	 * Current context of the application
	 */
	Context mContext; 
	
	/**
	 * Categories display names
	 */
	public  String [] swpeCats;
	
	/**
	 * Okay word
	 */
	public  String okay;
	/**
	 * Yes word
	 */
	public  String Yes;
	
	/**
	 * No word
	 */
	public  String No;
	/**
	 * Pending confirmation Dialog
	 */
	public  String loadingConfirmation;
	
	/**
	 * Already confirmed status
	 */
	public  String alreadyConfirmed;
	
	/**
	 * Confirmed by two users status
	 */
	public  String confirmedByTwoUsers;
	
	/**
	 * Confirmed by another person status
	 */
	public  String confirmedByAnotherPerson;
	
	/**
	 * Confirmation Mail dialog box
	 */
	public  String confirmationMail;
	
	/**
	 * Constructor for creating and getting the Swyper GUI utilities.
	 * @param mContext
	 * 				Current context of the application
	 */
	
	public SwypeCatsGuiUtils(Context mContext) {
		
		this.mContext = mContext;
		swpeCats = mContext.getResources().getStringArray(R.array._swyperCats);
		okay = mContext.getResources().getString(R.string._hossamOk);
		Yes =  mContext.getResources().getString(R.string._hossamYes);
		No =  mContext.getResources().getString(R.string._hossamNo);
		loadingConfirmation =  mContext.getResources().getString(R.string._hossamConfirmPending);
		alreadyConfirmed =  mContext.getResources().getString(R.string._hossamAlreadyConfirmed);
		confirmedByTwoUsers =  mContext.getResources().getString(R.string._hossamConfirmedByTwoUsers);
		confirmedByAnotherPerson =  mContext.getResources().getString(R.string._hossamConfirmedByAnotherPerson);
		confirmationMail =  mContext.getResources().getString(R.string._hossamConfirmationMail);
		
	}
	
	
	/**
	 * 
	 * =======Some Getters=================
	 * 
	 */
	
	public String[] getSwpeCats() {
		return swpeCats;
	}
	public String getOkay() {
		return okay;
	}
	public String getYes() {
		return Yes;
	}
	public String getNo() {
		return No;
	}
	public String getLoadingConfirmation() {
		return loadingConfirmation;
	}
	public String getAlreadyConfirmed() {
		return alreadyConfirmed;
	}
	public String getConfirmedByTwoUsers() {
		return confirmedByTwoUsers;
	}
	public String getConfirmedByAnotherPerson() {
		return confirmedByAnotherPerson;
	}
	public String getConfirmationMail() {
		return confirmationMail;
	}

}//end class SwypeCatsGuiUtils