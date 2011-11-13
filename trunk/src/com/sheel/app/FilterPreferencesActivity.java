package com.sheel.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ToggleButton;

public class FilterPreferencesActivity extends Activity {
	
	String searchStatus;
	String selectedDate;
	String searchMethod;
	String flightNumber;
	String srcAirport;
	String desAirport;
	
	ToggleButton toggleButton1; //both
	ToggleButton toggleButton2; //male
	ToggleButton toggleButton3; //female

	AutoCompleteTextView numOfKgs;
	AutoCompleteTextView price;
	AutoCompleteTextView nationalityView;
	
	String availableKgs;
	String pricePerKg;
	String gender;
	String nationality;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_preferences);
        
        Bundle extras = getIntent().getExtras();
        
        	if(extras !=null){
        		searchStatus = extras.getString("searchStatus");
        		selectedDate = extras.getString("selectedDate");
        		searchMethod = extras.getString("searchMethod");
        		
        		
        		if(searchMethod.equals("flight number"))
        			flightNumber = extras.getString("flightNo");
        		
        		
        		else{
        			srcAirport = extras.getString("srcAirport");
        			desAirport = extras.getString("desAirport");
        		}
        			
        	}
        
        	gender = "both";
        	
        setAdaptors();
     }
	
	public void setAdaptors(){
		
		toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
        toggleButton3 = (ToggleButton) findViewById(R.id.toggleButton3);
        
        numOfKgs = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        price = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        nationalityView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView3);
        
        String[] numbers = getResources().getStringArray(R.array.numbers_array);
        ArrayAdapter<String> numAdapter = new ArrayAdapter<String>(this, R.layout.list_item, numbers);
    
        numOfKgs.setAdapter(numAdapter);
        price.setAdapter(numAdapter);
        
        String[] nationalities = getResources().getStringArray(R.array.nationalities_array);
        ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<String>(this, R.layout.list_item, nationalities);
        
        nationalityView.setAdapter(nationalityAdapter);
        
		
	}
	
	public void onClick_both_genders(View v) 
	{
		toggleButton2.setChecked(false);
		toggleButton3.setChecked(false);
		gender = "both";
		
	}
	
	public void onClick_male_gender(View v) 
	{
		toggleButton1.setChecked(false);
		toggleButton3.setChecked(false);
		gender = "male";
	}
	
	public void onClick_female_gender(View v) 
	{
		toggleButton1.setChecked(false);
		toggleButton2.setChecked(false);
		gender = "female";
	}
	
	public void onClick_search_offers(View v) 
	{
		availableKgs = numOfKgs.getText().toString();
		pricePerKg = price.getText().toString();
		nationality = nationalityView.getText().toString();
		
		Log.e("mm", availableKgs);
		Log.e("mm", pricePerKg);
		Log.e("mm", gender);
		Log.e("mm", nationality);
		
		
	}
	
}
