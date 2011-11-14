package com.sheel.app;


import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class NewUserActivity extends Activity{
	
	ToggleButton toggleMale; //male
	ToggleButton toggleFemale; //female
	
	AutoCompleteTextView countryCodes;
	EditText mobileNumberField;
	EditText firstNameField;
	EditText middleNameField;
	EditText lastNameField;
	EditText emailField;
	EditText passportNumberField;
	
	String gender; 
    String firstName;
    String middleName;
    String lastName;
    String passportPhoto;
    String passportNumber;
    String email;
    String mobileNumber;
    
    boolean allValid = false;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        setVariables();
        
    }
    
    public void setVariables(){
    	
    	toggleMale = (ToggleButton) findViewById(R.id.toggleMale);
        toggleFemale = (ToggleButton) findViewById(R.id.toggleFemale);
    	
        countryCodes = (AutoCompleteTextView)findViewById(R.id.auto);
        final String[] codeStrings = getResources().getStringArray(R.array.codes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, codeStrings);   
        countryCodes.setAdapter(adapter);
        Validator validator = new Validator() {
			
			@Override
			public boolean isValid(CharSequence text) {
				 Log.v("Test", "Checking if valid: "+ text);
		           Arrays.sort(codeStrings);
		            if (Arrays.binarySearch(codeStrings, text.toString()) > 0) {
		            	allValid = true;
		            	
		            	//Toast toast = Toast.makeText(NewUserActivity.this, "Valid", 0);
		        		//toast.show();
		                return true;
		            }
		            allValid = false;
		            Toast toast = Toast.makeText(NewUserActivity.this, "Please insert a valid country", 0);
		    		toast.show();
				return false;
			}
			
			@Override
			public CharSequence fixText(CharSequence invalidText) {
				//Log.v("Test", "Returning fixed text");
				//Toast toast = Toast.makeText(NewUserActivity.this, "Fixing", 0);
				//toast.show();
				return invalidText;
			}
		};
		
        countryCodes.setValidator(validator);
        
        mobileNumberField = (EditText) findViewById(R.id.mobileNumber);
        
        firstNameField = (EditText) findViewById(R.id.FirstName);
        middleNameField = (EditText) findViewById(R.id.MiddleName);
        lastNameField = (EditText) findViewById(R.id.LastName);
        
        emailField = (EditText) findViewById(R.id.email);
        
        passportNumberField = (EditText) findViewById(R.id.passNum);
        
    }
    
    public void onClick_male(View v) 
	{
		toggleFemale.setChecked(false);
		gender = "male";
	}
	
	public void onClick_female(View v) 
	{
		toggleMale.setChecked(false);
		gender = "female";
	}
	
	public void onClick_takePhoto(View v)
	{
		
	}
	
	public void validate()
	{
		String countryCode = countryCodes.getText().toString();
		countryCodes.getValidator().isValid(countryCode);
		
		
		if(allValid  && mobileNumber.length()>5 && firstName.length()>0 && middleName.length()>0 &&
				lastName.length()>0 && email.length()>0 && passportNumber.length()>0 && gender!=null)
			allValid = true;
		else
			allValid = false;
	}
	
	public void OnClick_register(View v)
	{
		String countryCode = countryCodes.getText().toString();
		
		mobileNumber = (mobileNumberField.getText().toString());
		firstName = firstNameField.getText().toString();
		middleName = middleNameField.getText().toString();
		lastName = lastNameField.getText().toString();
		email = emailField.getText().toString();
		passportNumber = passportNumberField.getText().toString();
		
		validate();
		if(allValid == false) Toast.makeText(this, "Please fill all the data", 0).show();
		else{
			
			String phoneCode = countryCode.toString().split(" ")[1];
			mobileNumber = phoneCode + mobileNumber;
			String toToast =  mobileNumber + " " + firstName + " " + middleName + " " + lastName + 
				" " + email + " " + passportNumber + " " + gender;
	
		Toast toast = Toast.makeText(this, toToast, 0);
		toast.show();
		}
	}

}
