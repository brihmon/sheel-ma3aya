package com.sheel.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.sheel.datastructures.Offer;

/**
 * @author Mohsen
 */
public class InsertOfferActivity extends UserSessionStateMaintainingActivity {
	static Offer offer ;// = new Offer(0,0,-1,"INVALID");
    private EditText noKGsET;
	private EditText pricePerKGET;
	private RadioGroup type;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.insert_offer_1);
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
	        
	    }
	 
	 
	 public void onClick_next(View v){
		 
		 ////////////////////////////////////////////SOME VALIDATION///////////////////////////////////////////////
		 String checkErrors = "";
		 
		 if(noKGsET.getText().toString().equals("")||
		    pricePerKGET.getText().toString().equals("")){
			 checkErrors += "Please insert the number of kilograms and the corresponding price.\n";
		 }
		 else{
			 
		 if(Integer.parseInt(noKGsET.getText().toString())>30){
			 checkErrors += "Number of kilograms cannot exceed 30.\n";
		 }
		 if( Integer.parseInt(pricePerKGET.getText().toString())>30){
			 checkErrors += "Price per kilogram cannot exceed 30 euros.\n";
		 }
		 }
		 if(!checkErrors.equals("")){
			 showErrors(checkErrors);
			 return;
		 }
		 ////////////////////////////////////////////SOME VALIDATION///////////////////////////////////////////////
			
		 offer = new Offer(
				 !noKGsET.getText().toString().equals("")?
				 Integer.parseInt(noKGsET.getText().toString()):0,
				 !pricePerKGET.getText().toString().equals("")?
				 Integer.parseInt(pricePerKGET.getText().toString()):0,
				 type.getCheckedRadioButtonId()==R.id.exWeight?1:0,
				 "new");
		 
		 //startActivity(new Intent(this, InsertFlightActivity.class));
		 Intent intent =setSessionInformationBetweenActivities(InsertFlightActivity.class);
			try{
				Bundle extras = getIntent().getExtras();
				long userId = extras.getLong("userId");
				intent.putExtra("userId", userId);
				}
				catch(Exception e){
					
				}
		 startActivity(intent);
	 }
	 
	 public void showErrors(String message){
		 AlertDialog alertDialog;
		 alertDialog = new AlertDialog.Builder(this).create();
		 alertDialog.setTitle("Invalid input");
		 alertDialog.setMessage(message);
		 alertDialog.show();
	 }
	 
	 

}
