package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sheel.datastructures.Flight;
import com.sheel.datastructures.Offer;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.SheelMaayaaConstants;

/**
 * @author Mohsen
 */




/******************************************* IF YOU COMMENT THIS CODE, BAD LUCK WILL HAPPEN TO YOU *******************/
/******************************************* IF YOU COMMENT THIS CODE, BAD LUCK WILL HAPPEN TO YOU *******************/
/******************************************* IF YOU COMMENT THIS CODE, BAD LUCK WILL HAPPEN TO YOU *******************/
/******************************************* IF YOU COMMENT THIS CODE, BAD LUCK WILL HAPPEN TO YOU *******************/
/******************************************* IF YOU COMMENT THIS CODE, BAD LUCK WILL HAPPEN TO YOU *******************/


public class InsertOfferActivity extends UserSessionStateMaintainingActivity {
	static Offer offer ;// = new Offer(0,0,-1,"INVALID");
	static Flight flight = new Flight("","","","");
	static final int DATE_DIALOG_ID = 1;
	
	/************** UI Components*******************/
	private EditText noKGsET;
	private EditText pricePerKGET;
	private RadioGroup type;
	private TextView mDateDisplay;       
	private int mYear;    
	private int mMonth;    
	private int mDay;
	EditText flightNumberET ;
	AutoCompleteTextView sAirportET ;
	AutoCompleteTextView dAirportET  ;
	static ProgressDialog dialog;
	static Calendar datePicked = Calendar.getInstance();

	 /************* INTERNET COMPONENTS ********************/
	
	IntentFilter filter;
	BroadcastReceiver br;
	
	 
	 
	private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                	InsertOfferActivity.datePicked.set(year, monthOfYear, dayOfMonth, 23 , 59 , 59);
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
 
            
	 protected Dialog onCreateDialog(int id) {
	                switch (id) {
	                case DATE_DIALOG_ID:
	                    return new DatePickerDialog(this,
	                                mDateSetListener,
	                                mYear, mMonth, mDay);               

	                }
	                return null;
	            }
	
	 
	 	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.insert_offer_2);
	        noKGsET = (EditText)findViewById(R.id.kgs);
	        pricePerKGET = (EditText)findViewById(R.id.price);
	        type = (RadioGroup)findViewById(R.id.typesOffers);
	        
	        if(offer!=null){
	        	noKGsET.setText(offer.getNoOfKilograms()+"");
	        	pricePerKGET.setText(offer.getPricePerKilogram()+"");
	        	type.check(offer.getUserStatus()==0?R.id.avWeight:R.id.exWeight);
	        }
	        else{
	        	type.check(R.id.exWeight);
	        }
	        String[] airports = getResources().getStringArray(R.array.airports_array);
			
	        flightNumberET = (EditText) findViewById(R.id.fNum);
			 sAirportET = (AutoCompleteTextView) findViewById(R.id.sAirport);
			 dAirportET = (AutoCompleteTextView) findViewById(R.id.dAirport);
	         flightNumberET.setText(flight.getFlightNumber());
	         sAirportET.setText((src!=-1?airports[src]:flight.getSource()));
	         dAirportET.setText(des!=-1?airports[des]:flight.getDestination());
	        
	        
	          
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
    
		      mDateDisplay = date;
		       
		       final Calendar c = Calendar.getInstance();
		        mYear = c.get(Calendar.YEAR);
		        mMonth = c.get(Calendar.MONTH);
		        mDay = c.get(Calendar.DAY_OF_MONTH);

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
	            new StringBuilder()//
	                    // Month is 0 based so add 1
	                    .append(mMonth + 1).append("-")
	                    .append(mDay).append("-")
	                    .append(mYear));
	        

	        
	    }
  
	 
  
  
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		filter = new IntentFilter();
		filter.addAction(SheelMaayaaConstants.HTTP_INSERT_OFFER);		
		br = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				String responseStr = intent.getExtras().getString(HTTP_RESPONSE);
				//((TextView) findViewById(R.id.deactivate_response)).setText(responseStr);
			  	 if((!(dialog==null))&&dialog.isShowing()){
         		 dialog.dismiss();
         	 }
         	 if(responseStr.equals("OK")){
         	 ///////////// SHOULD GO HERE TO ANOTHER ACTIVITY
               
               showMessageBox(getResources().getString(R.string.Success),
            		   getResources().getString(R.string.your_offer_has_been_posted_successfully));
               
         	 }
          	 else{
         	 showMessageBox(getResources().getString(R.string.Sorry),
         			getResources().getString(R.string.Unfortunately_we_cannot_process_your_offer));
        	 }
           }
		};
				 
		registerReceiver(br, filter);
	}
  
  
	 static int des = -1;
	 static int src = -1;

	 public boolean isAlpha(char c){
		 return (c<='Z'&&c>='A'||c>='a'&&c<='z');
	 }
	 
  public void onClick_submit(View v){
	    
	  	if(!extractOffer()){
	  		return;
	  	}
	    String errors = "";
	    
	    if(flightNumberET.getText().length()<3){
	    	errors+= getResources().getString(R.string.flight_valid);
	    	showErrors(errors);
	    	return;
	    }
	    if(!(isAlpha(flightNumberET.getText().charAt(0)) && isAlpha(flightNumberET.getText().charAt(1)))){
	    	errors+=getResources().getString(R.string.flight_valid);
	    	showErrors(errors);
	    	return;
	    }
	    if(sAirportET.getText().toString().length()<4||dAirportET.getText().toString().length()<4){
	    	errors+=getResources().getString(R.string.Please_insert_valid_source_and_destination);
	    	showErrors(errors);
	    	return;
	    }
	    if(sAirportET.getText().toString().equals(dAirportET.getText().toString())){
	    	errors+=getResources().getString(R.string.src_des_valid);
	    	showErrors(errors);
	    	return;
	    }
	    if(datePicked.before(Calendar.getInstance())){
	    	errors+=getResources().getString(R.string.date_valid);
	    	showErrors(errors);
	    	return;
	    }
	    
	   
	    
		flight = new Flight(flightNumberET.getText().toString(),
					 sAirportET.getText().toString(),
					 dAirportET.getText().toString(),
					 mDateDisplay.getText().toString());
		

		 
		 
		
		String[] airports = getResources().getStringArray(R.array.airports_array);
		des = -1;
		src = -1;
		for(int i = 0; i < airports.length ; i++)
		{
			if(flight.destination.equals(airports[i]))
			{
				flight.destination = i+"";
				des = i;
			}
			if(flight.source.equals(airports[i]))
			{
				flight.source = i+"";
				src = i;
			}			
		}
		
		if(des == -1){
			showErrors(getResources().getString(R.string.des_valid));
			return;
		}
		if(src == -1){
			showErrors(getResources().getString(R.string.source_valid));
			return;
		}
		
		Gson gson = new Gson();
		  dialog = new ProgressDialog(this);
	      dialog.setMessage(getResources().getString(R.string.please_wait));
	      dialog.show();
		
		String input = gson.toJson(flight);
		input+= "<>"+gson.toJson(offer);

		filter = new IntentFilter();
		filter.addAction(SheelMaayaaConstants.HTTP_INSERT_OFFER);
		registerReceiver(br, filter);
		HTTPManager.startHttpService("/insertnewoffer/"+getFacebookService().getFacebookUser().getUserId(),
												input , SheelMaayaaConstants.HTTP_INSERT_OFFER, getApplicationContext());
	 Log.e("User id ", getFacebookService().getFacebookUser().getUserId());
		
	}

protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dialog = null;
		unregisterReceiver(br);
	}

public void showMessageBox(String title,String message){
	 AlertDialog alertDialog;
	 alertDialog = new AlertDialog.Builder(this).create();
	 alertDialog.setTitle(title);
	 alertDialog.setMessage(message);
	 alertDialog.show();
}

 
public void onClick_change(View v){
	 showDialog(DATE_DIALOG_ID);
}
	 
	 
public boolean extractOffer(){
		 
		 ////////////////////////////////////////////SOME VALIDATION///////////////////////////////////////////////
		 String checkErrors = "";
		 
		 if(noKGsET.getText().toString().equals("")||
		    pricePerKGET.getText().toString().equals("")){
			 checkErrors += getResources().getString(R.string.Please_insert_num_kilos_and_price)+"\n";
		 }
		 else{
			 
		 if(Integer.parseInt(noKGsET.getText().toString())>30){
			 checkErrors += getResources().getString(R.string.kgs_valid)+"\n";
		 }
		 if( Integer.parseInt(pricePerKGET.getText().toString())>30){
			 checkErrors += getResources().getString(R.string.price_valid)+"\n";
		 }
		 }
		 if(!checkErrors.equals("")){
			 showErrors(checkErrors);
			 return false;
		 }
		 ////////////////////////////////////////////SOME VALIDATION///////////////////////////////////////////////
			
		 offer = new Offer(
				 !noKGsET.getText().toString().equals("")?
				 Integer.parseInt(noKGsET.getText().toString()):0,
				 !pricePerKGET.getText().toString().equals("")?
				 Integer.parseInt(pricePerKGET.getText().toString()):0,
				 type.getCheckedRadioButtonId()==R.id.exWeight?1:0,
				 "new");
		 return true;
		 
	 }
	 
	 public void showErrors(String message){
		 AlertDialog alertDialog;
		 alertDialog = new AlertDialog.Builder(this).create();
		 alertDialog.setTitle(getResources().getString(R.string.invalid_input));
		 alertDialog.setMessage(message);
		 alertDialog.show();
	 }
	 
	 

}
