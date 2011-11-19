package com.sheel.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchByFlightNoActivity extends UserSessionStateMaintainingActivity{
	
	int searchStatus;
	String selectedDate;
	int searchMethod;
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
        	searchStatus = extras.getInt("searchStatus");
        	selectedDate = extras.getString("selectedDate");
        	searchMethod = extras.getInt("searchMethod");
        }
        
        Log.e("passant integrate: " , "SearchByFlightNoActivity: " + searchStatus + "  " + selectedDate + "  " + searchMethod);
        
        textDisplay = (TextView) findViewById(R.id.flightTextView);
        
        String status = "less weight";
        
        if(searchStatus == 1)
        	status = "extra weight";
        
        textDisplay.setText(
                new StringBuilder()
                        .append("I'm searching for users having ").append(status)
                        .append(" travelling on ").append(selectedDate).append(" using"));
          
	}
	
	public String removeSpaces(String str){
		
		str = str.trim();
		
		str = str.replaceAll(" ", "%20");
		
		return str;
	}
	
	 public void onClick_filter (View v) 
	 {
		flightNo = flightNumber.getText().toString();
		flightNo = removeSpaces(flightNo);
		
		if(flightNo.equals(""))
			return;
		
		//Intent intent = new Intent(getBaseContext(), FilterPreferencesActivity.class);
		Intent intent = setSessionInformationBetweenActivities(FilterPreferencesActivity.class);
		intent.putExtra("searchStatus", searchStatus);
		intent.putExtra("selectedDate", selectedDate);
		intent.putExtra("searchMethod", searchMethod);
		intent.putExtra("flightNo", flightNo);
		startActivity(intent);
	 }
	 
	 public void onClick_searchOffersByFlight (View v) {
		 
		 flightNo = flightNumber.getText().toString();
		 flightNo = removeSpaces(flightNo);
		 
		 if(flightNo.equals(""))
				return;
		 
		 String request = "/filterflightnumberoffers/" + flightNo + "/" + selectedDate + "/" + searchStatus +
				 															"/0/0/both/none";
		 
		//Intent intent = new Intent(getBaseContext(), ViewSearchResultsActivity.class);
		Intent intent = setSessionInformationBetweenActivities(ViewSearchResultsActivity.class);
		intent.putExtra("request", request);
		startActivity(intent);
		 
	 }
	 
	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 /*public void onClick_searchOffersByFlight (View v) 
	 {
		 flightNo = flightNumber.getText().toString();
		 flightNo = removeSpaces(flightNo);
		 
		 if(flightNo.equals(""))
				return;

	      HTTPClient sc = new HTTPClient() {
	           
				@Override
				public void doSomething() {
					final String str = this.rspStr;
					 
								 runOnUiThread(new Runnable()
	                             {
	                                 @Override
	                                 public void run()
	                                 {
	                                     Toast.makeText(SearchByFlightNoActivity.this, str, Toast.LENGTH_LONG).show();
	                                   //  startActivity(new Intent(SearchByFlightNoActivity.this, PhoneCommunication.class));
	                                     
	                                     try {
	                                    	 
	                                    	// Log.e("mmm", "mmm");
	                                    //	System.out.println("ya rab tekoon passant msh betefty :D" + builder);
	                                    	 
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
	        
	        sc.runHttpRequest("/getoffersbyflightnumber/" + flightNo + "/" + selectedDate + "/" + searchStatus);
		 	
		 
	}	*/ 

}
