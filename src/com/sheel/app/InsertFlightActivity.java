package com.sheel.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * @author Mohsen
 */
public class InsertFlightActivity extends Activity {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.insert_offer_2);
	        String[] airports = getResources().getStringArray(R.array.intl_airports_array);
			
			  AutoCompleteTextView textView = 
					  (AutoCompleteTextView) findViewById(R.id.sAirport);
			  ArrayAdapter<String> adapter = 
					  new ArrayAdapter<String>(this, R.layout.list_item, airports);
			  textView.setAdapter(adapter);
			 
			  AutoCompleteTextView textView2 = 
					  (AutoCompleteTextView) findViewById(R.id.dAirport);
			  ArrayAdapter<String> adapter2 = 
					  new ArrayAdapter<String>(this, R.layout.list_item, airports);
			  textView2.setAdapter(adapter2);
			  
			  
	    }
	 
	 public void onClick_back(View v){
		 //startActivity(new Intent(this, InsertOfferActivity.class));
		// setContentView(R.layout.insert_offer_1);setView1();
	 }
	 
	 
 
	 

}
