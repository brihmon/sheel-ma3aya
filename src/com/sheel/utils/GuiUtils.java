/**
 * 
 */
package com.sheel.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Class used to contain common methods relevant to UI
 * for showing consistent alerts and components
 * 
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public final class GuiUtils {
	
	/**
	 * Used to show consistent pop up for the user when no results are 
	 * found with the possibility of diverting to 2 different views
	 * 
	 * @param
	 * 		the activity where the pop up should be displayed over
	 * @param message
	 * 		main message displayed in the pop up
	 * @param commandRes1
	 * 		Order on the first button
	 * @param res1
	 * 		Layout that the first button should divert to upon clicking
	 * @param commandRes2
	 * 		Order on the second button
	 * @param res2
	 * 		Layout that the second button should divert to
	 * 
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 * @author 
	 * 		Hossam Amer
	 */
	public static void showAlertWhenNoResultsAreAvailable(final Activity activity, String message , String commandRes1, final Class<?> res1 , String commandRes2,final Class<?> res2) {
		System.out.println("Should show alert message");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
		       .setCancelable(true)		       
		       .setPositiveButton(commandRes1, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		        	   Intent intent = new Intent(activity.getApplicationContext(),res1);
		        	   activity.startActivity(intent);
		           }
		       
		       });
		     /*  .setNegativeButton(commandRes2, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		        	   Intent intent = new Intent(activity.getApplicationContext(),res2);
		        	   activity.startActivity(intent);
		           }
		       
		       });
		      */ 
			
		 builder.create();
		 builder.show();
	}// end showAlertWhenNoResultsAreAvailable


}// end class
