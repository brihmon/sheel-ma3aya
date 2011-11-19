package com.sheel.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sheel.datastructures.enums.OwnerFacebookStatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.TextView;

public class SearchBySrcDesActivity extends UserSessionStateMaintainingActivity{
	
	int searchStatus;
	String selectedDate;
	int searchMethod;
	TextView textDisplay;
 	
	AutoCompleteTextView source;
	AutoCompleteTextView destination;
	
	String srcAirport;
	String desAirport;
	
//	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_by_src_des);
        
        Bundle extras = getIntent().getExtras();
        
        if(extras !=null){
        	searchStatus = extras.getInt("searchStatus");
        	selectedDate = extras.getString("selectedDate");
        	searchMethod = extras.getInt("searchMethod");
       	}

        String status = "less weight";
        
        if(searchStatus == 1)
        	status = "extra weight";
        
        textDisplay = (TextView) findViewById(R.id.textView1);
        
        textDisplay.setText(
                new StringBuilder()
                        .append("I'm searching for users having ").append(status)
                        .append(" travelling on ").append(selectedDate));
        
        source = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        destination = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        
        String[] airports = getResources().getStringArray(R.array.airports_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, airports);
        
        source.setAdapter(adapter);
        destination.setAdapter(adapter);
	}
	
	public String removeSpaces(String str){
		
		str = str.trim();
		
		str = str.replaceAll(" ", "%20");
		
		return str;
	}
	
	 public void onClick_filter (View v) 
	 {
		srcAirport = source.getText().toString();
		desAirport = destination.getText().toString();
		
		srcAirport = removeSpaces(srcAirport);
		desAirport = removeSpaces(desAirport);
		
		if(srcAirport.equals("") || desAirport.equals(""))
			return;
		
		//Intent intent = new Intent(getBaseContext(), FilterPreferencesActivity.class);
		Intent intent = setSessionInformationBetweenActivities(FilterPreferencesActivity.class);
		intent.putExtra("searchStatus", searchStatus);
		intent.putExtra("selectedDate", selectedDate);
		intent.putExtra("searchMethod", searchMethod);
		intent.putExtra("srcAirport", srcAirport);
		intent.putExtra("desAirport", desAirport);
		
		Bundle extras = getIntent().getExtras();
		long userId = extras.getLong("userId");
		intent.putExtra("userId", userId);
//		Toast.makeText(getApplicationContext(),"" + userId, Toast.LENGTH_SHORT).show();
		
		startActivity(intent);
	 }	
	 
	 
	 public void onClick_searchOffersByAirports (View v) {
		 
		 srcAirport = source.getText().toString();
		desAirport = destination.getText().toString();
			
		srcAirport = removeSpaces(srcAirport);
		desAirport = removeSpaces(desAirport);
	
		if(srcAirport.equals("") || desAirport.equals(""))
			return;
			
		String request =  "/filterairportsoffers/" + srcAirport + "/" + desAirport + "/" + selectedDate 
						+ "/" + searchStatus + "/0/0/both/none";
				
		//Intent intent = new Intent(getBaseContext(), ViewSearchResultsActivity.class);
		Intent intent = setSessionInformationBetweenActivities(ViewSearchResultsActivity.class);
		intent.putExtra("request", request);
		intent.putExtra("facebook", OwnerFacebookStatus.UNRELATED.name());

		Bundle extras = getIntent().getExtras();
		long userId = extras.getLong("userId");
		intent.putExtra("userId", userId);
//		Toast.makeText(getApplicationContext(),"" + userId, Toast.LENGTH_SHORT).show();
		
		startActivity(intent);
			 
		 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 public void onClick_searchOffersByAirports1 (View v) 
	 {
		srcAirport = source.getText().toString();
		desAirport = destination.getText().toString();
		
		srcAirport = removeSpaces(srcAirport);
		desAirport = removeSpaces(desAirport);
	
		if(srcAirport.equals("") || desAirport.equals(""))
			return;
		
		HTTPClient sc = new HTTPClient() {
	           
			@Override
			public void doSomething() {
				final String str = this.rspStr;
				 
							 runOnUiThread(new Runnable()
                             {
           //                      @Override
                                 public void run()
                                 {
                                     Toast.makeText(SearchBySrcDesActivity.this, str, Toast.LENGTH_LONG).show();
                                   //  startActivity(new Intent(SearchByFlightNoActivity.this, PhoneCommunication.class));
                                     
                                     try {
                                    	 
                                    	JSONArray jsonArray = new JSONArray(builder.toString());
                                    	
                                    	JSONObject offer;
                                    	JSONObject user;
                                    	JSONObject flight;
                                    	
                                    	int kgs; 
                                    	String username = "";
                                    	
                                    	String source = "";
                                    	
                                    	 for (int i = 0; i < jsonArray.length(); i++) {
                                    		 
                                    		 offer = jsonArray.getJSONObject(i);

                                    		 kgs = offer.getInt("noOfKilograms");
                                    		 
                                    		 user = offer.getJSONObject("user");
                                    		 username = user.getString("username");
                                    		 
                                    		 flight = offer.getJSONObject("flight");
                                    		 source = flight.getString("source");
										
                                    		 Log.e("mmm", username + "");
                                    		 Log.e("mmm", source + "");
                                    		 Log.e("mmm", kgs + "");
                                    		 
                                    	 }
										
									} catch (JSONException e) {
										
										e.printStackTrace();
									}
                                 }
                             });
			}
		};
        
	        sc.runHttpRequest("/getoffersbyairports/" + srcAirport + "/" + desAirport + "/" + selectedDate 
	        															+ "/" + searchStatus);
		 	
		 
	}	 
}
