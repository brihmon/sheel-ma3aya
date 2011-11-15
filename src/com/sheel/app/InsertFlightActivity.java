package com.sheel.app;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sheel.datastructures.Flight;

/**
 * @author Mohsen
 */
public class InsertFlightActivity extends Activity {
	static Flight flight = new Flight("","","","");
	
	private TextView mDateDisplay;       
	private int mYear;    
	private int mMonth;    
	private int mDay;
	private int mHour;
    private int mMinute;
	private TextView mTimeDisplay;
	private Button mPickTime;
	
	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	
	 EditText flightNumberET ;
	 AutoCompleteTextView sAirportET ;
	 AutoCompleteTextView dAirportET  ;
	
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
	            new DatePickerDialog.OnDateSetListener() {

	                public void onDateSet(DatePicker view, int year, 
	                                      int monthOfYear, int dayOfMonth) {
	                    mYear = year;
	                    mMonth = monthOfYear;
	                    mDay = dayOfMonth;
	                    updateDisplay();
	                }
	            };
	            
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        	    new TimePickerDialog.OnTimeSetListener() {
        	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        	            mHour = hourOfDay;
        	            mMinute = minute;
        	            updateDisplay();
        	        }
        	    };
	            
	 protected Dialog onCreateDialog(int id) {
	                switch (id) {
	                case DATE_DIALOG_ID:
	                    return new DatePickerDialog(this,
	                                mDateSetListener,
	                                mYear, mMonth, mDay);  
	                case TIME_DIALOG_ID:
	                    return new TimePickerDialog(this,
	                            mTimeSetListener, mHour, mMinute, false);
	                 

	                }
	                return null;
	            }
	
	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.insert_offer_2);
	        
	         flightNumberET = (EditText) findViewById(R.id.fNum);
			 sAirportET = (AutoCompleteTextView) findViewById(R.id.sAirport);
			 dAirportET = (AutoCompleteTextView) findViewById(R.id.dAirport);
	         flightNumberET.setText(flight.getFlightNumber());
	         sAirportET.setText(flight.getSource());
	         dAirportET.setText(flight.getDestination());
	        
	        
	        String[] airports = getResources().getStringArray(R.array.intl_airports_array);
			
			  AutoCompleteTextView textView = 
					  (AutoCompleteTextView) findViewById(R.id.sAirport);
			  ArrayAdapter<String> adapter = 
					  new ArrayAdapter<String>(this, R.layout.list_item, airports);
			  textView.setAdapter(adapter);
			 
			  AutoCompleteTextView textView2 = 
					  (AutoCompleteTextView) findViewById(R.id.dAirport);
			  ArrayAdapter<String> adapter2 = 
					  new ArrayAdapter<String>(this, R.layout.list_item, airports);
			  textView2.setAdapter(adapter2);
			 
			  TextView  date = (TextView )findViewById(R.id.editText1);      
		      TextView  time = (TextView )findViewById(R.id.editText2);  
		        
		      mDateDisplay = date;
		      mTimeDisplay = time;
		        
		        
		      time.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                showDialog(TIME_DIALOG_ID);
		            }
		        });
		      
		       date.setOnClickListener(
		        		(new OnClickListener() {
							public void onClick(View arg0) {
								showDialog(DATE_DIALOG_ID);
							}
						}));
		       
		       final Calendar c = Calendar.getInstance();
		        mYear = c.get(Calendar.YEAR);
		        mMonth = c.get(Calendar.MONTH);
		        mDay = c.get(Calendar.DAY_OF_MONTH);

		        final Calendar c2 = Calendar.getInstance();
		        mHour = c2.get(Calendar.HOUR_OF_DAY);
		        mMinute = c2.get(Calendar.MINUTE);

		        // display the current date
		        
		        if(flight.getDepartureDateTime().equals("")){
		        updateDisplay();
		        }
		        else{
		        	mDateDisplay.setText(flight.departureDateTime);
		        }
		        
				
		        

	    }
	 
	 

	    private static String pad(int c) {
	        if (c >= 10)
	            return String.valueOf(c);
	        else
	            return "0" + String.valueOf(c);
	    }
	    
	    
	    private void updateDisplay() {
	    	
	        mDateDisplay.setText(
	            new StringBuilder()
	                    // Month is 0 based so add 1
	                    .append(mMonth + 1).append("-")
	                    .append(mDay).append("-")
	                    .append(mYear).append(" "));
	        
	        mTimeDisplay.setText(
	                new StringBuilder()
	                        .append(pad(mHour)).append(":")
	                        .append(pad(mMinute)));
	        
	    }
	 
	 
	 public void onClick_back(View v){
		 

	flight = new Flight(flightNumberET.getText().toString(),
				 sAirportET.getText().toString(),
				 dAirportET.getText().toString(),
				 mDateDisplay.getText().toString());
	 
		 
		 startActivity(new Intent(this, InsertOfferActivity.class));
		  
	 }
	 
	 
 
	 

}
