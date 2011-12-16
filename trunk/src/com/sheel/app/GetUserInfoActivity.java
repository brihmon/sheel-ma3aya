package com.sheel.app;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GetUserInfoActivity extends UserSessionStateMaintainingActivity {
    /** Called when the activity is first created. */
	
	int searchStatus;
	String selectedDate;
	int searchMethod;
	
	RadioButton lessWeight;
	RadioButton extraWeight;
	ToggleButton srcDes;
	ToggleButton flightNo;
	
	TextView dateDisplay;
    Button pickDate;
    int year;
    int month;
    int day;
   
    static final int DATE_DIALOG_ID = 0;	
    static Calendar datePicked = Calendar.getInstance();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_user_info);
        
        lessWeight = (RadioButton) findViewById(R.id.lessWeight);
        extraWeight = (RadioButton) findViewById(R.id.extraWeight);
        srcDes = (ToggleButton) findViewById(R.id.srcDes);
        flightNo = (ToggleButton) findViewById(R.id.flightNo);
        
        dateDisplay = (TextView) findViewById(R.id.dateDisplay);
        pickDate = (Button) findViewById(R.id.changeDate);

        // add a click listener to the button
        pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        year = datePicked.get(Calendar.YEAR);
        month = datePicked.get(Calendar.MONTH);
        day = datePicked.get(Calendar.DAY_OF_MONTH);

        // display the current date (this method is below)
        updateDisplay();
    }
    
    private void updateDisplay() {
    	
        dateDisplay.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(month + 1).append("-")
                    .append(day).append("-")
                    .append(year));
        
        selectedDate = (String) dateDisplay.getText();

    }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int selectedYear, 
                                  int monthOfYear, int dayOfMonth) {
            	
            	GetUserInfoActivity.datePicked.set(selectedYear, monthOfYear, dayOfMonth);
                year = selectedYear;
                month = monthOfYear;
                day = dayOfMonth;
                updateDisplay();
            }
        };
        
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                            mDateSetListener,
                            year, month, day);
            }
            return null;
   }

	public void onClick_lessWeight(View v) 
	{
		extraWeight.setChecked(false);
	}
      
	public void onClick_extraWeight(View v) 
	{
		lessWeight.setChecked(false);
	}
	
	public void onClick_srcDes(View v) 
	{
		flightNo.setChecked(false);
		if(!isValidInput())
			return;
		
		searchMethod = 1;
		Intent intent = setSessionInformationBetweenActivities(SearchBySrcDesActivity.class);
		intent.putExtra("searchStatus", searchStatus);
		intent.putExtra("selectedDate", selectedDate);
		intent.putExtra("searchMethod", searchMethod);
		
		startActivity(intent);
		
	}
	
	public void onClick_flightNo(View v) 
	{
		srcDes.setChecked(false);
		
		if(!isValidInput())
			return;
		
		searchMethod = 0;
		Intent intent = setSessionInformationBetweenActivities(SearchByFlightNoActivity.class);
		intent.putExtra("searchStatus", searchStatus);
		intent.putExtra("selectedDate", selectedDate);
		intent.putExtra("searchMethod", searchMethod);
		
		startActivity(intent);
			
	}
	
	 public void showErrors(String title, String message){
		 AlertDialog alertDialog;
		 alertDialog = new AlertDialog.Builder(this).create();
		 alertDialog.setTitle(title);
		 alertDialog.setMessage(message);
		 alertDialog.show();
	 }
	 
	 public boolean isValidInput(){
		 
		 if(!lessWeight.isChecked() && !extraWeight.isChecked()){
				showErrors("Missing Input", "Please select the user status you're searching for");
				srcDes.setChecked(false);
		    	flightNo.setChecked(false);
				return false;
		}
		 
		/*if(datePicked.before(Calendar.getInstance())){
	    	showErrors("Invalid Input", "Please enter a valid date");
	    	srcDes.setChecked(false);
	    	flightNo.setChecked(false);
	    	return false;
	    }*/
		 
		 if(lessWeight.isChecked())
				searchStatus = 0;
			
			else if(extraWeight.isChecked())
				searchStatus = 1;
		 
		 return true;
	 }
	 
		@Override
		protected void onResume() {
			
			super.onResume();
			
			srcDes.setChecked(false);
	    	flightNo.setChecked(false);
			
			
		}
}