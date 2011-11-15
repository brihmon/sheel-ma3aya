
package com.sheel.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This activity is used for displaying and interacting with
 * different search results
 * 
 * @author passant
 *
 */
public class ViewSearchResultsActivity extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.search_result_details);
        setIconsForDetailsItems();
    }// end onCreate
    
    /**
     * This method is used to set the default icons for the 
     * different details in the details pane
     */
    private void setIconsForDetailsItems(){
    	    	
    	/*Drawable img = getApplicationContext().getResources().getDrawable( R.drawable.details_money );
    	img.setBounds( 0, 0, 60, 60 );
    	TextView txtView = (TextView)findViewById(R.id.details_textView_price);
    	txtView.setCompoundDrawables( img, null, null, null );*/
    	
    	setIconForATextField(R.id.details_textView_price, R.drawable.details_money,1);
    	setIconForATextField(R.id.details_textView_facebookStatus, R.drawable.details_facebook,1);
    	setIconForATextField(R.id.details_textView_email, R.drawable.details_email,0);
    	setIconForATextField(R.id.details_textView_mobileNumber, R.drawable.details_phone,0);
    	
    	
    }// end setIconsForDetailsItem
    
    /**
     * Used as a helper method to add an image to the left side of a text view.
     * @param textViewId 
     * 		ID of the text view from (R.id) collection
     * @param imgId
     * 		ID of the resource image used from (R.drawable) collection
     * @param mode 
     * 		indicates size of icon.
     * 		<ul>
     * 			<li>0: small    (30X30px)</li>
     * 			<li>1: medium   (50X50px)</li>
     * 			<li>2: large    (80X80px)</li>
     * 		</ul>
     */
    private void setIconForATextField(int textViewId , int imgId , int mode){
    	
    	Drawable img = getApplicationContext().getResources().getDrawable(imgId );
    	switch(mode){
    	case 0:img.setBounds( 0, 0, 40, 40 ); break;
    	case 1:img.setBounds( 0, 0, 50, 50 ); break;
    	case 2:img.setBounds( 0, 0, 80, 80 ); break;
    	default:img.setBounds( 0, 0, 50, 50 ); break;
    	}// end switch : specify size according to mode
    	
    	TextView txtView = (TextView)findViewById(textViewId);
    	txtView.setCompoundDrawables( img, null, null, null );
    }// end SetIconForATextField
    
}// end Activity
