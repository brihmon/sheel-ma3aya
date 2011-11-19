package com.sheel.app;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
//import android.widget.Toast;

import com.google.gson.Gson;
import com.sheel.datastructures.Flight;

/**
 * @author Mohsen
 */
public class InsertFlightActivity extends UserSessionStateMaintainingActivity {
	static Flight flight = new Flight("","","","");
	
	private TextView mDateDisplay;       
	private int mYear;    
	private int mMonth;    
	private int mDay;
//	private int mHour;
//    private int mMinute;
//	private TextView mTimeDisplay;
	//private Button mPickTime;
	
//	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	
	 EditText flightNumberET ;
	 AutoCompleteTextView sAirportET ;
	 AutoCompleteTextView dAirportET  ;
	 static ProgressDialog dialog;
	 static Calendar datePicked = Calendar.getInstance();
			 
	private DatePickerDialog.OnDateSetListener mDateSetListener =
	            new DatePickerDialog.OnDateSetListener() {

	                public void onDateSet(DatePicker view, int year, 
	                                      int monthOfYear, int dayOfMonth) {
	                	InsertFlightActivity.datePicked.set(year, monthOfYear, dayOfMonth, 23 , 59 , 59);
	                    mYear = year;
	                    mMonth = monthOfYear;
	                    mDay = dayOfMonth;
	                    updateDisplay();
	                }
	            };
/*   
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        	    new TimePickerDialog.OnTimeSetListener() {
        	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        	            mHour = hourOfDay;
        	            mMinute = minute;
        	            updateDisplay();
        	        }
        	    };
*/	            
	 protected Dialog onCreateDialog(int id) {
	                switch (id) {
	                case DATE_DIALOG_ID:
	                    return new DatePickerDialog(this,
	                                mDateSetListener,
	                                mYear, mMonth, mDay);  
/*	                case TIME_DIALOG_ID:
	                    return new TimePickerDialog(this,
	                            mTimeSetListener, mHour, mMinute, false);
*/	                 

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
	        
	        
	          String[] airports = getResources().getStringArray(R.array.airports_array);
			
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
//		      TextView  time = (TextView )findViewById(R.id.editText2);  
		        
		      mDateDisplay = date;
//		      mTimeDisplay = time;
		        
		        
/*		      time.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                showDialog(TIME_DIALOG_ID);
		            }
		        });
*/		      
/*		       date.setOnClickListener(
		        		(new OnClickListener() {
							public void onClick(View arg0) {
								showDialog(DATE_DIALOG_ID);
							}
						}));*/
		       
		       final Calendar c = Calendar.getInstance();
		        mYear = c.get(Calendar.YEAR);
		        mMonth = c.get(Calendar.MONTH);
		        mDay = c.get(Calendar.DAY_OF_MONTH);

//		        final Calendar c2 = Calendar.getInstance();
//		        mHour = c2.get(Calendar.HOUR_OF_DAY);
//		        mMinute = c2.get(Calendar.MINUTE);

		        // display the current date
		        
		        if(flight.getDepartureDate().equals("")){
		        updateDisplay();
		        }
		        else{
		        	mDateDisplay.setText(flight.departureDate);
		        }
		        
		        datePicked.set(mYear, mMonth, mDay, 23 , 59 , 59);
				
		        

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
	        
	/*        mTimeDisplay.setText(
	                new StringBuilder()
	                        .append(pad(mHour)).append(":")
	                        .append(pad(mMinute)));*/
	        
	    }
	 
	 
	 
	 
	 
	 public void onClick_submit(View v){
		 
		    String errors = "";
		    
		    if(flightNumberET.getText().length()<3){
		    	errors+="Please insert a valid flight number";
		    	showErrors(errors);
		    	return;
		    }
		    if(sAirportET.getText().toString().length()<4||dAirportET.getText().toString().length()<4){
		    	errors+="Please insert the complete flight info";
		    	showErrors(errors);
		    	return;
		    }
		    if(datePicked.before(Calendar.getInstance())){
		    	errors+="Please insert a valid date";
		    	showErrors(errors);
		    	return;
		    }
		    
		    dialog = new ProgressDialog(this);
	        dialog.setMessage("Please wait");
	        dialog.show();
		    
			flight = new Flight(flightNumberET.getText().toString(),
						 sAirportET.getText().toString(),
						 dAirportET.getText().toString(),
						 mDateDisplay.getText().toString());
			 
			SheelMaaayaClient sc = new SheelMaaayaClient() {
				
				@Override
				public void doSomething() {
					final String str = this.rspStr;
					 
								 runOnUiThread(new Runnable()
	                             {
	                                 @Override
	                                 public void run()
	                                 {
	                                	 if((!(dialog==null))&&dialog.isShowing()){
	                                		 dialog.dismiss();
	                                	 }
	                                	 if(str.equals("OK")){
	                                	 ///////////// SHOULD GO HERE TO ANOTHER ACTIVITY
	                                     showMessageBox("Message","Your offer has been posted successfully!");
	                                	 }
	                                	 else{
	                                	 showMessageBox("Server Error!","Unfortunatly, we currently cannot process your offer, please try again later.");
	                                	 }
	                                 }
	                             });

				}
			};
			
			Gson gson = new Gson();
			String input = gson.toJson(flight);
			input+= "<>"+gson.toJson(InsertOfferActivity.offer);
			sc.runHttpPost("/insertnewoffer", input);
			
		}
	 
	 protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			dialog = null;
		}
	 
	 public void showMessageBox(String title,String message){
		 AlertDialog alertDialog;
		 alertDialog = new AlertDialog.Builder(this).create();
		 alertDialog.setTitle(title);
		 alertDialog.setMessage(message);
		 alertDialog.show();
	 }
	 
	 public void showErrors(String message){
		 showMessageBox("Invalid input",message);
	 }

	 public void onClick_change(View v){
		 showDialog(DATE_DIALOG_ID);
	 }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			flight = new Flight(flightNumberET.getText().toString(),
					 sAirportET.getText().toString(),
					 dAirportET.getText().toString(),
					 mDateDisplay.getText().toString());
		 
			 
			 //startActivity(new Intent(this, InsertOfferActivity.class));	
			Intent intent =setSessionInformationBetweenActivities(InsertOfferActivity.class);
			startActivity(intent);
			break;
		}
		return true;
		
	}
	
	
	public void onClick_goMainPage(View v){
		Intent intent =setSessionInformationBetweenActivities(ConnectorUserActionsActivity.class);
		startActivity(intent);
	}
	 
	 
 
	 

}
