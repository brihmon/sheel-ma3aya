package com.sheel.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.TextView;

public class SearchBySrcDesActivity extends Activity{
	
	String searchStatus;
	String selectedDate;
	String searchMethod;
	TextView textDisplay;
 	
	AutoCompleteTextView source;
	AutoCompleteTextView destination;
	
	String srcAirport;
	String desAirport;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_by_src_des);
        
        Bundle extras = getIntent().getExtras();
        
        if(extras !=null){
        	searchStatus = extras.getString("searchStatus");
        	selectedDate = extras.getString("selectedDate");
        	searchMethod = extras.getString("searchMethod");
       	}

        textDisplay = (TextView) findViewById(R.id.textView1);
        
        textDisplay.setText(
                new StringBuilder()
                        .append("I'm searching for users having ").append(searchStatus)
                        .append(" travelling on ").append(selectedDate));
        
        source = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        destination = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        
        String[] airports = getResources().getStringArray(R.array.airports_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, airports);
        
        source.setAdapter(adapter);
        destination.setAdapter(adapter);
        
       // textView1.setValidator(new Validator());
	}

	 public void onClick_filter (View v) 
	 {
		srcAirport = source.getText().toString();
		desAirport = destination.getText().toString();
		
		
		Intent intent = new Intent(getBaseContext(), FilterPreferencesActivity.class);
		intent.putExtra("searchStatus", searchStatus);
		intent.putExtra("selectedDate", selectedDate);
		intent.putExtra("searchMethod", searchMethod);
		intent.putExtra("srcAirport", srcAirport);
		intent.putExtra("desAirport", desAirport);
		startActivity(intent);
	 }	
}
