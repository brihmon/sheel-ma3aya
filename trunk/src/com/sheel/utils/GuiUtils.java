/**
 * 
 */
package com.sheel.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

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
	
	/**
     * Used to add an image to the left side of a text view.
     * 
     * @param activity
     * 		Activity containing the text view
     * @param textViewId 
     * 		ID of the text view from (R.id) collection
     * @param imgId
     * 		ID of the resource image used from (R.drawable) collection
     * @param mode 
     * 		indicates size of icon.
     * 		<ul>
     * 			<li>0: small    (40X40px)</li>
     * 			<li>1: medium   (50X50px)</li>
     * 			<li>2: large    (80X80px)</li>
     * 		</ul>
     * @author 
     * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public static void setIconForATextField(Activity activity, int textViewId , int imgId , int mode){
    	Drawable img = activity.getApplicationContext().getResources().getDrawable(imgId );
    	TextView textView = (TextView)activity.findViewById(textViewId);
    	setIconForATextField(img, textView, mode);
    }// end SetIconForATextField
    
    /**
     * Used to add an image to the left side of a text view.
     * 
     * @param appContext
     * 		Application context. It can be retrieved from the activity by 
     * 		<code>activity.getApplicationContext()</code>
     * @param parent
     * 		Component (View) containing the text view. Example: the row in
     * 		list containing the text view
     * @param textViewId 
     * 		ID of the text view from (R.id) collection
     * @param imgId
     * 		ID of the resource image used from (R.drawable) collection
     * @param mode 
     * 		indicates size of icon.
     * 		<ul>
     * 			<li>0: small    (40X40px)</li>
     * 			<li>1: medium   (50X50px)</li>
     * 			<li>2: large    (80X80px)</li>
     * 		</ul>
     * 
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public static void setIconForATextField(Context appContext, View parent,  int textViewId , int imgId , int mode){
    	
    	Drawable img = appContext.getResources().getDrawable(imgId );
    	TextView textView = (TextView)parent.findViewById(textViewId);
    	setIconForATextField(img, textView, mode);
    	
    }// end SetIconForATextField
	
	/**
	 * Used to add an image to the left side of a text view.
	 * 
	 * @param img
	 * 		Image to be added to the text view
	 * @param textView
	 * 		Text view that that image will be added to its left
	 * @param mode 
     * 		indicates size of icon.
     * 		<ul>
     * 			<li>0: small    (40X40px)</li>
     * 			<li>1: medium   (50X50px)</li>
     * 			<li>2: large    (80X80px)</li>
     * 		</ul>
     * 
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
    public static void setIconForATextField(Drawable img , TextView textView , int mode) {
    	switch(mode){
    	case 0:img.setBounds( 0, 0, 40, 40 ); break;
    	case 1:img.setBounds( 0, 0, 50, 50 ); break;
    	case 2:img.setBounds( 0, 0, 80, 80 ); break;
    	default:img.setBounds( 0, 0, 50, 50 ); break;
    	}// end switch : specify size according to mode
    	textView.setCompoundDrawables(img, null, null, null);
    }// end setIconForATextField


}// end class
