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
/**
 * Activity used for filtering the offers the user is searching for by combination of filter (weight, price/kg, gender
 * or nationality of offer owner as well as setting if he wants to filter by facebook)
 * 
 * @author 
 *		Magued George (magued.george1990@gmail.com)
 *
 */
public class FilterPreferencesActivity extends UserSessionStateMaintainingActivity {
	

	/**
	 * User search status either less (0) or extra (1) weight
	 */
	int searchStatus;

	/**
	 * Date selected by the user
	 */
	String selectedDate;

	/**
	 * User search method either flight number (0) or source and destination (1) weight
	 */
	int searchMethod;
	
	String flightNumber;
    /**
	 * Entered source airport
	 */
    String srcAirport;
    /**
	 * Entered destination airport
	 */
    String desAirport;
	
    /**
	 * Toggle button for male
	 */
	ToggleButton male;
	/**
	 * Toggle button for female
	 */
	ToggleButton female;

	EditText numOfKgsText;
	EditText priceText;
	AutoCompleteTextView nationalityView;
	
	/**
	 * Entered kgs
	 */
	String availableKgs;
	/**
	 * Entered price
	 */
	String pricePerKg;
	/**
	 * Entered gender
	 */
	String gender;
	/**
	 * Entered nationality
	 */
	String nationality;
	
	int kgs, price;
	boolean allValid;
	
	OwnerFacebookStatus facebook;
	
	/**
	 * Nationalities list 
	 */
	String[] nationalitiesList;
	
	/**
	 * The request string sent to the server
	 */
    String request;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_preferences);
        
        Bundle extras = getIntent().getExtras();
        
        ///////////////// Getting data from the intent //////////////////////
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
	
	/**
     * Setting nationality auto complete text view adaptor and validator
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
	public void setNationalityAdaptor(){
	
        nationalityView = (AutoCompleteTextView) findViewById(R.id.nationality);
  
        final String[] nationalities = getResources().getStringArray(R.array.nationalities_array);
        ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<String>(this, R.layout.list_item, nationalities);
        
        nationalityView.setAdapter(nationalityAdapter);
        Validator NationalityValidator = new Validator() {

//			@Override
			public boolean isValid(CharSequence text) {

				String stringText = text.toString();
				stringText = stringText.trim();
				
				Arrays.sort(nationalities);
				if ((Arrays.binarySearch(nationalities, stringText) > 0)) {
					return true;
				}
				return false;
			}

//			@Override
			public CharSequence fixText(CharSequence invalidText) {

				return invalidText;
			}

		};
		nationalityView.setValidator(NationalityValidator);
        
		
	}
	
	/**
     * Creates a dialog for the user to select the owner facebook status
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
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
//			@Override
			public void onClick(View v) {
				friendsOfFriends.setChecked(false);
				facebook = OwnerFacebookStatus.FRIEND;	
			}
		});
		friendsOfFriends.setText(getResources().getString(R.string.friends_of_friends));
		layout.addView(friendsOfFriends);
		
		friendsOfFriends.setOnClickListener(new OnClickListener() {
//			@Override
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
	
	/**
     * Creates a dialog with the given title and message used for validation message
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
	public void showErrors(String title, String message){
		 
		 AlertDialog alertDialog;
		 alertDialog = new AlertDialog.Builder(this).create();
		 alertDialog.setTitle(title);
		 alertDialog.setMessage(message);
		 alertDialog.show();
	 }
	
	/**
     * Takes a string and removes all spaces at the beginning and the end and replaces all spaces in the middle
     * with %20
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
	public String removeSpaces(String str){
		str = str.trim();
		return str;
	}
	
	/**
     * Listener for male button
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
	public void onClick_male_gender(View v) 
	{
		female.setChecked(false);
		gender = "male";
	}
	
	/**
     * Listener for female button
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
	public void onClick_female_gender(View v) 
	{
		male.setChecked(false);
		gender = "female";
	}

	
	/**
     * Listener for search offers button .... checks if all input is valid
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
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
	
	/**
     * Sets the request string and starts the activity that will do the search and display the results
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
	public void startViewResultsActivity(){
		
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
	
	 /**
     * Gets the index of the selected nationality from the nationalities list
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
	public String getNationalityIndex(String nationality){
		 
		 for(int i = 0 ; i < nationalitiesList.length ; i++){
			 if(nationality.equals(nationalitiesList[i]))
				 return i+"";	 
		 }
		 
		 return -1+"";
	 }
	
	/**
     * Overrides onResume and sets all toggle buttons to unchecked and resets text of all text fields
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
	@Override
	protected void onResume() {
			
		super.onResume();
			
		male.setChecked(false);
    	female.setChecked(false);
    	nationalityView.setText("");
    	numOfKgsText.setText("");
    	priceText.setText("");
	}
	
	/**
     * Overrides onConfigurationChanged to keep the dialogs open when orientation changed
     * 
     * @author 
     *		Magued George (magued.george1990@gmail.com)
     */
	@Override
	public void onConfigurationChanged(Configuration newConfig){
	    super.onConfigurationChanged(newConfig);
	}
}
