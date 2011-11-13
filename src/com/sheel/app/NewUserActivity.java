package com.sheel.app;


import android.app.Activity;
import android.os.Bundle;
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
        String[] codeStrings = getResources().getStringArray(R.array.codes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, codeStrings);   
        countryCodes.setAdapter(adapter);
        
        
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
		if(mobileNumber.length()>0 && firstName.length()>0 && middleName.length()>0 &&
				lastName.length()>0 && email.length()>0 && passportNumber.length()>0 && gender!=null)
			allValid = true;
		else
			allValid = false;
	}
	
	public void OnClick_register(View v)
	{
		String countryCode = countryCodes.getText().toString();
		mobileNumber = countryCode + (mobileNumberField.getText().toString());
		firstName = firstNameField.getText().toString();
		middleName = middleNameField.getText().toString();
		lastName = lastNameField.getText().toString();
		email = emailField.getText().toString();
		passportNumber = passportNumberField.getText().toString();
		
		validate();
		if(allValid == false) Toast.makeText(this, "Please fill all the data", 0).show();
		else{
		String toToast =  mobileNumber + " " + firstName + " " + middleName + " " + lastName + 
				" " + email + " " + passportNumber + " " + gender;
	
		Toast toast = Toast.makeText(this, toToast, 0);
		toast.show();
		}
	}

}
