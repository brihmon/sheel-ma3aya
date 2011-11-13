package com.sheel.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class SearchByFlightNoActivity extends Activity{
	
	String searchStatus;
	String selectedDate;
	String searchMethod;
	TextView textDisplay;
	
	String flightNo;
	AutoCompleteTextView flightNumber; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_by_flight);
        
        flightNumber = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        
        Bundle extras = getIntent().getExtras();
        
        if(extras !=null){
        	searchStatus = extras.getString("searchStatus");
        	selectedDate = extras.getString("selectedDate");
        	searchMethod = extras.getString("searchMethod");
        }
        
        textDisplay = (TextView) findViewById(R.id.textView);
        
        textDisplay.setText(
                new StringBuilder()
                        .append("I'm searching for users having ").append(searchStatus)
                        .append(" travelling on ").append(selectedDate).append("using"));
          
	}
	
	 public void onClick_filter (View v) 
	 {
		flightNo = flightNumber.getText().toString();
		
		Intent intent = new Intent(getBaseContext(), FilterPreferencesActivity.class);
		intent.putExtra("searchStatus", searchStatus);
		intent.putExtra("selectedDate", selectedDate);
		intent.putExtra("searchMethod", searchMethod);
		intent.putExtra("flightNo", flightNo);
		startActivity(intent);
	 }	

}
