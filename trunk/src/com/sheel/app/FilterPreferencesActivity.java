package com.sheel.app;

import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ToggleButton;
import android.widget.AutoCompleteTextView.Validator;

import com.sheel.datastructures.enums.OwnerFacebookStatus;

public class FilterPreferencesActivity extends UserSessionStateMaintainingActivity {
	
	int searchStatus;
	String selectedDate;
	int searchMethod;
	String flightNumber;
	String srcAirport;
	String desAirport;
	
	ToggleButton male; 
	ToggleButton female;

	EditText numOfKgsText;
	EditText priceText;
	AutoCompleteTextView nationalityView;
	
	String availableKgs;
	String pricePerKg;
	String gender;
	String nationality;
	
	int kgs, price;
	boolean allValid;
	
	OwnerFacebookStatus facebook;
	
	String[] nationalitiesList;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_preferences);
        
        Bundle extras = getIntent().getExtras();
        
        	if(extras !=null){
        		searchStatus = extras.getInt("searchStatus");
        		selectedDate = extras.getString("selectedDate");
        		searchMethod = extras.getInt("searchMethod");
        		
        		
        		if(searchMethod == 0)
        			flightNumber = extras.getString("flightNo");
        		
        		
        		else{
        			srcAirport = extras.getString("srcAirport");
        			desAirport = extras.getString("desAirport");
        		}
        			
        	}
        
        gender = "both";
       	nationality  = "none";
       	facebook = OwnerFacebookStatus.UNRELATED;
       	
    	kgs = 0;
    	price = 0;
		allValid = true;
        	
        numOfKgsText = (EditText) findViewById(R.id.kgs);
       	priceText = (EditText) findViewById(R.id.price);
        male = (ToggleButton) findViewById(R.id.male);
        female = (ToggleButton) findViewById(R.id.female);
        	
        nationalitiesList = getResources().getStringArray(R.array.nationalities_array);
        
        setNationalityAdaptor();
     }
	
	public void setNationalityAdaptor(){
	
        nationalityView = (AutoCompleteTextView) findViewById(R.id.nationality);
  
        final String[] nationalities = getResources().getStringArray(R.array.nationalities_array);
        ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<String>(this, R.layout.list_item, nationalities);
        
        nationalityView.setAdapter(nationalityAdapter);
        Validator NationalityValidator = new Validator() {

			@Override
			public boolean isValid(CharSequence text) {

				String stringText = text.toString();
				stringText = stringText.trim();
				
				Arrays.sort(nationalities);
				if ((Arrays.binarySearch(nationalities, stringText) > 0)) {
					return true;
				}
				return false;
			}

			@Override
			public CharSequence fixText(CharSequence invalidText) {

				return invalidText;
			}

		};
		nationalityView.setValidator(NationalityValidator);
        
		
	}
	
	public void onClick_facebook(View v){
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
		alert.setTitle(getResources().getString(R.string.owner_status));                 

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1);
		final RadioButton friends = new RadioButton(this);
		final RadioButton friendsOfFriends = new RadioButton(this);
		
		friends.setText(getResources().getString(R.string.friends));	
		layout.addView(friends); 
		
		friends.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				friendsOfFriends.setChecked(false);
				facebook = OwnerFacebookStatus.FRIEND;	
			}
		});
		friendsOfFriends.setText(getResources().getString(R.string.friends_of_friends));
		layout.addView(friendsOfFriends);
		
		friendsOfFriends.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				friends.setChecked(false);
				facebook = OwnerFacebookStatus.FRIEND_OF_FRIEND;
			}
		});
		
		if(facebook == OwnerFacebookStatus.FRIEND)
			friends.setChecked(true);
		else if(facebook == OwnerFacebookStatus.FRIEND_OF_FRIEND)
			friendsOfFriends.setChecked(true);
		
		alert.setView(layout);
		
		alert.setPositiveButton(getResources().getString(R.string.confirm_selection), 
															new DialogInterface.OnClickListener() {  
		    public void onClick(DialogInterface dialog, int whichButton) {  
		       	 return;	          
		      }  
	  }); 
		
		alert.setNegativeButton(getResources().getString(R.string.cancel_selection), 
													new DialogInterface.OnClickListener() {  
		    public void onClick(DialogInterface dialog, int whichButton) { 
		    	 friends.setChecked(false);
		    	 friendsOfFriends.setChecked(false);
		    	 facebook = OwnerFacebookStatus.UNRELATED;
		       	 return;	          
		      }  
	  }); 
		alert.show();
	}
	
	public void showErrors(String title, String message){
		 
		 AlertDialog alertDialog;
		 alertDialog = new AlertDialog.Builder(this).create();
		 alertDialog.setTitle(title);
		 alertDialog.setMessage(message);
		 alertDialog.show();
	 }
	
	public String removeSpaces(String str){
		str = str.trim();
		str = str.replaceAll(" ", "%20");
		return str;
	}
	

	public void onClick_male_gender(View v) 
	{
		female.setChecked(false);
		gender = "male";
	}
	
	public void onClick_female_gender(View v) 
	{
		male.setChecked(false);
		gender = "female";
	}

	
	public void onClick_search_offers(View v) 
	{
		allValid = true;
		
		if(!male.isChecked() && !female.isChecked())
			gender = "both";

		availableKgs = numOfKgsText.getText().toString();
		pricePerKg = priceText.getText().toString();
		
		nationality = nationalityView.getText().toString();
		nationality = removeSpaces(nationality);
		
		if(!availableKgs.equals("")){
			kgs = Integer.parseInt(availableKgs);
			
			if(kgs > 30){
				 showErrors(getResources().getString(R.string.invalid_input), 
						 getResources().getString(R.string.kgs_valid));
				 
				 allValid = false;
			}	
		}
		
		if(!pricePerKg.equals("")){
			price = Integer.parseInt(pricePerKg);
			
			if(price > 30 && allValid){
				showErrors(getResources().getString(R.string.invalid_input), 
						getResources().getString(R.string.price_valid));
				
				allValid = false;
			}
		}
		
		if(nationality.equals(""))
			nationality = "none";
		
		else if(!nationalityView.getValidator().isValid(nationality) && allValid){
			showErrors(getResources().getString(R.string.invalid_input), 
					getResources().getString(R.string.nationality_valid));
			allValid = false;
		}
		
		if(allValid)
			startViewResultsActivity();
	}
	
	public void startViewResultsActivity(){
		
		String request = "";
		
		if(!nationality.equals("none"))
			nationality = getNationalityIndex(nationality);
		
		if(searchMethod == 0)
			request = "/filterflightnumberoffers/" + flightNumber + "/" + selectedDate + "/" + searchStatus +
									"/" + kgs + "/" + price + "/" + gender + "/" + nationality; 
		
		else
			request = "/filterairportsoffers/" + srcAirport + "/" + desAirport + "/" + selectedDate 
					+ "/" + searchStatus + "/" + kgs + "/" + price + "/" + gender + "/" + nationality;
		
		Intent intent = new Intent(getBaseContext(), ViewSearchResultsActivity.class);
		intent.putExtra("request", request);
		intent.putExtra("facebook", facebook.name());
	
		startActivity(intent);
	}
	
	public String getNationalityIndex(String nationality){
		 
		 for(int i = 0 ; i < nationalitiesList.length ; i++){
			 if(nationality.equals(nationalitiesList[i]))
				 return i+"";	 
		 }
		 
		 return -1+"";
	 }
	
	@Override
	protected void onResume() {
			
		super.onResume();
			
		male.setChecked(false);
    	female.setChecked(false);
    	nationalityView.setText("");
    	numOfKgsText.setText("");
    	priceText.setText("");
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
	    super.onConfigurationChanged(newConfig);
	}
}
