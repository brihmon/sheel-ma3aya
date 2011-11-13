package com.sheel.app;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;


public class NewUserActivity extends Activity{
	
	ToggleButton toggleMale; //male
	ToggleButton toggleFemale; //female
	
	AutoCompleteTextView countryCodes;
	
	String gender;
	
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

}
