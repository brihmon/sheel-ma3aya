package com.sheel.app;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GetUserInfoActivity extends UserSessionStateMaintainingActivity {
    /** Called when the activity is first created. */
	
	int searchStatus;
	String selectedDate;
	int searchMethod;
	
	ToggleButton lessWeight;
	ToggleButton extraWeight;
	ToggleButton srcDes;
	ToggleButton flightNo;
	
	TextView dateDisplay;
    Button pickDate;
    int year;
    int month;
    int day;
   
    static final int DATE_DIALOG_ID = 0;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_user_info);
        
        lessWeight = (ToggleButton) findViewById(R.id.lessWeight);
        extraWeight = (ToggleButton) findViewById(R.id.extraWeight);
        srcDes = (ToggleButton) findViewById(R.id.srcDes);
        flightNo = (ToggleButton) findViewById(R.id.flightNo);
        
        dateDisplay = (TextView) findViewById(R.id.dateDisplay);
        pickDate = (Button) findViewById(R.id.pickDate);

        // add a click listener to the button
        pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

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
	}
	
	public void onClick_flightNo(View v) 
	{
		srcDes.setChecked(false);
	}
	
	public void onClick_checkSelectedButtons(View v){
		
		if(!lessWeight.isChecked() && !extraWeight.isChecked())
			return;
		
		if(lessWeight.isChecked())
			searchStatus = 0;
		
		else if(extraWeight.isChecked())
			searchStatus = 1;
			
		if(flightNo.isChecked()){
			
			searchMethod = 0;
			
			//Intent intent = new Intent(getBaseContext(), SearchByFlightNoActivity.class);
			Intent intent = setSessionInformationBetweenActivities(SearchByFlightNoActivity.class);
			intent.putExtra("searchStatus", searchStatus);
			intent.putExtra("selectedDate", selectedDate);
			intent.putExtra("searchMethod", searchMethod);
			
			Bundle extras = getIntent().getExtras();
			long userId = extras.getLong("userId");
			intent.putExtra("userId", userId);
//			Toast.makeText(getApplicationContext(),"" + userId, Toast.LENGTH_SHORT).show();
			
			startActivity(intent);
		}
				
		else if(srcDes.isChecked()){
			
			searchMethod = 1;
			
			//Intent intent = new Intent(getBaseContext(), SearchBySrcDesActivity.class);
			Intent intent = setSessionInformationBetweenActivities(SearchBySrcDesActivity.class);
			intent.putExtra("searchStatus", searchStatus);
			intent.putExtra("selectedDate", selectedDate);
			intent.putExtra("searchMethod", searchMethod);
			
//			Bundle extras = getIntent().getExtras();
//			long userId = extras.getLong("userId");
//			intent.putExtra("userId", userId);
//			Toast.makeText(getApplicationContext(),"" + userId, Toast.LENGTH_SHORT).show();
		
			
			startActivity(intent);
			
		}
		
	}
   
}