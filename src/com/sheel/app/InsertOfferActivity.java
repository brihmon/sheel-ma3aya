package com.sheel.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * @author Mohsen
 */
public class InsertOfferActivity extends Activity {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.insert_offer_1);
	    
	    }
	 
	 
	 public void onClick_next(View v){
		 startActivity(new Intent(this, InsertFlightActivity.class));
		 
	 }
	 
	 
	 

}
