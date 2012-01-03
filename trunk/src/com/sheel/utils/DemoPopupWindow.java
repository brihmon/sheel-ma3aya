package com.sheel.utils;


 

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sheel.app.R;
import com.sheel.app.R.id;
import com.sheel.datastructures.Flight;
import com.sheel.datastructures.Offer;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.listeners.InflateListener;

public class DemoPopupWindow extends BetterPopupWindow implements OnClickListener {
	OfferDisplay2 offer;
	private Activity mActivity;
	private InflateListener inflateListener;
	
	/********* Edit offer UI components ********/
	private RadioGroup type;
	private EditText noKGsET;
	private EditText pricePerKGET;
	private TextView mDateDisplay;       
	private static int mYear;    
	private static int mMonth;    
	private static int mDay;
	EditText flightNumberET ;
	AutoCompleteTextView sAirportET ;
	AutoCompleteTextView dAirportET  ;
	static Calendar datePicked = Calendar.getInstance();

	private TextView eo_error[] = new TextView[2];
	
	
	/******* Internet service components *******/
	private IntentFilter filter;
	private BroadcastReceiver br;
 
	static final int DATE_DIALOG_ID = 1;
	public static int BUTTON_CALL = 1;
	public static int BUTTON_SMS = 2;
	public static int BUTTON_CONFIRM = 4;
	public static int BUTTON_EDIT = 8;
	public static int BUTTON_DEACTIVATE = 16;
	
	public static int enabledButtons;
	
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                	datePicked.set(year, monthOfYear, dayOfMonth, 23 , 59 , 59);
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
 
 
	
	public DemoPopupWindow(View anchor, OfferDisplay2 offer, Activity activity, InflateListener inflateListener) {
		super(anchor);
		this.mActivity = activity;
		this.offer = offer;
		this.inflateListener = inflateListener;
	}
	
	@Override
	protected void onCreate() {
		LayoutInflater inflater =
				(LayoutInflater) this.anchor.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.popup_grid_layout, null);


		if((enabledButtons&BUTTON_CALL)==0){
		((TableRow)(root.findViewById(R.id.qa_call).getParent())).removeView(root.findViewById(R.id.qa_call));
		}
		else{
			(root.findViewById(R.id.qa_call)).setOnClickListener(this);
		}
		if((enabledButtons&BUTTON_SMS)==0){
		((TableRow)(root.findViewById(R.id.qa_sms).getParent())).removeView(root.findViewById(R.id.qa_sms));
		}else
		{
			(root.findViewById(R.id.qa_sms)).setOnClickListener(this);
		}
		
		if((enabledButtons&BUTTON_EDIT)==0){
		((TableRow)(root.findViewById(R.id.qa_editOffer).getParent())).removeView(root.findViewById(R.id.qa_editOffer));
		((TableRow)(root.findViewById(R.id.qa_editFlight).getParent())).removeView(root.findViewById(R.id.qa_editFlight));
		}else{
			(root.findViewById(R.id.qa_editOffer)).setOnClickListener(this);
			(root.findViewById(R.id.qa_editFlight)).setOnClickListener(this);
		}
		
		if((enabledButtons&BUTTON_CONFIRM)==0){
			((TableRow)(root.findViewById(R.id.qa_confirm).getParent())).removeView(root.findViewById(R.id.qa_confirm));
		}else{
			(root.findViewById(R.id.qa_confirm)).setOnClickListener(this);
		}
		
		if((enabledButtons&BUTTON_DEACTIVATE)==0){
			((TableRow)(root.findViewById(R.id.qa_deactivate).getParent())).removeView(root.findViewById(R.id.qa_deactivate));
		}else{
			(root.findViewById(R.id.qa_deactivate)).setOnClickListener(this);
		}
						
						//b.setOnClickListener(this);
					
				
			
		

		// set the inflated view as what we want to display
		this.setContentView(root);
		
		
		
	}
	AlertDialog dialog;
	static int src = -1;
	static int des = -1;
	@Override
	public void onClick(View v) {
		// we'll just display a simple toast on a button click
		Button b = (Button) v;
		
		switch(b.getId()){
		
		
		case R.id.qa_confirm:{
			inflateListener.onClick_confirm(v);
			break;
		}
		
		case R.id.qa_sms:{
			 inflateListener.onClick_button2(v);
			 break;
		}
		
		case R.id.qa_call:{
			inflateListener.onClick_button1(v);
			break;
		}
		
		
		
		case R.id.edit_flight_cancel:{
			dialog.dismiss();
			break;
		}
		
		/***************************  DEACTIVATING AN OFFER ************************/
		case R.id.yes_deactivate:{
		  
			Log.e("Deactivating offer with id = ", offer.getFlight().id+"");
        	HTTPManager.startHttpService("/deactivateoffer/ab",
        			offer.getOffer().id+"" , SheelMaayaaConstants.HTTP_DEACTIVATE_OFFER, mActivity.getApplicationContext());   
        	dialog.dismiss();
		
			break;
		}
		
		case R.id.no_deactivate:{
			dialog.dismiss();
			break;
		}
		
		case R.id.qa_deactivate:{
			LayoutInflater inflater =
					(LayoutInflater) this.anchor.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View deactivate_offer =   inflater.inflate(R.layout.deactivate_offer, null);
			
		    AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
			alert.setView(deactivate_offer);
		    dialog = alert.create();
			dialog.show();
			
			Button b1 = (Button)dialog.findViewById(R.id.yes_deactivate);
			b1.setOnClickListener(this);
			Button b2 = (Button)dialog.findViewById(R.id.no_deactivate);
			b2.setOnClickListener(this);
	
			break;
		}
		
		
		/****************QUICK ACTION BAR, EDIT FLIGHT SAVE ***********************************/
		case R.id.edit_flight_save:{
			  int count = 0;
			  eo_error[0].setText("");
			  eo_error[1].setText("");
			  
			    if(flightNumberET.getText().length()<3){
			     eo_error[0].setText(mActivity.getResources().getString(R.string.flight_valid));
			     count++;
			     return;
			     
			    }
			    if(!(isAlpha(flightNumberET.getText().charAt(0)) && isAlpha(flightNumberET.getText().charAt(1)))){
			    	eo_error[0].setText(mActivity.getResources().getString(R.string.flight_valid));
				     count++;
				     return;
			    }
			    if(sAirportET.getText().toString().length()<4||dAirportET.getText().toString().length()<4){
			     	eo_error[0].setText(eo_error[0].getText().toString()+
			     			mActivity.getResources().getString(R.string.Please_insert_valid_source_and_destination));
				     count++;
				     return;
			    }
			    if(sAirportET.getText().toString().equals(dAirportET.getText().toString())){
			    	eo_error[count==0?0:1].setText(eo_error[count==0?0:1].getText().toString()+
			    			mActivity.getResources().getString(R.string.src_des_valid));
				     count++;
				     return;
			    }
			    if(datePicked.before(Calendar.getInstance())){
			    	eo_error[count==0?0:1].setText(
			    			eo_error[count==0?0:1].getText().toString()+
			    			mActivity.getResources().getString(R.string.date_valid));
				     count++;
				     return;
			    }
			    
			   
			   
			    
				 
				
				offer.getFlight().setFlightNumber(flightNumberET.getText().toString());
				offer.getFlight().setSource(sAirportET.getText().toString());
				offer.getFlight().setDestination(dAirportET.getText().toString());
				offer.getFlight().setDepartureDateTime(mDateDisplay.getText().toString()) ;
				
					

				
				 
				
				String[] airports = mActivity.getResources().getStringArray(R.array.airports_array);
				des = -1;
				src = -1;
				for(int i = 0; i < airports.length ; i++)
				{
					if(offer.getFlight().destination.equals(airports[i]))
					{
						
						des = i;
					}
					if(offer.getFlight().source.equals(airports[i]))
					{
						
						src = i;
					}			
				}
				
				if(des == -1){
					eo_error[count==0?0:1].setText(mActivity.getResources().getString(R.string.des_valid));
					 count++;
				}else{
					offer.getFlight().destination = des+"";
				}
				if(src == -1){
					eo_error[count==0?0:1].setText(mActivity.getResources().getString(R.string.source_valid));
					 count++;
				}else{
					offer.getFlight().source = src+"";
				}
				if(count>0)
					return;
				
				
				Gson gson = new Gson();
	         
		  	    String input = gson.toJson(offer.getFlight());
		  	    Log.e("JSON SENT = ", offer.getFlight().id+"");
	        	HTTPManager.startHttpService("/editflight/"+offer.getOffer().id,
				input , SheelMaayaaConstants.HTTP_EDIT_FLIGHT, mActivity.getApplicationContext());   
	        	offer.getFlight().setSource(airports[src]);
				offer.getFlight().setDestination(airports[des]);
			
	        	dialog.dismiss();
			
			
			break;
		}
		
		/*****************Quick action bar , Edit Flight clicked******************************/
		case R.id.qa_editFlight:{
			LayoutInflater inflater =
					(LayoutInflater) this.anchor.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View edit_flight =   inflater.inflate(R.layout.edit_flight, null);
			
		    AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
			alert.setTitle(mActivity.getResources().getString(R.string.flight_details));
			alert.setMessage(mActivity.getResources().getString(R.string.Please_make_changes));
			alert.setView(edit_flight);
		    dialog = alert.create();
			dialog.show();
			
			Button b1 = (Button)dialog.findViewById(R.id.edit_flight_cancel);
			b1.setOnClickListener(this);
			Button b2 = (Button)dialog.findViewById(R.id.edit_flight_save);
			b2.setOnClickListener(this);
			
			 String[] airports = mActivity.getResources().getStringArray(R.array.airports_array);
			 
			  AutoCompleteTextView textView = 
					  (AutoCompleteTextView)  dialog.findViewById(R.id.sAirport);
			  ArrayAdapter<String> adapter = 
					  new ArrayAdapter<String>(mActivity, R.layout.list_item, airports);
			  textView.setAdapter(adapter);
			 
			  AutoCompleteTextView textView2 = 
					  (AutoCompleteTextView)  dialog.findViewById(R.id.dAirport);
			  ArrayAdapter<String> adapter2 = 
					  new ArrayAdapter<String>(mActivity, R.layout.list_item, airports);
			  textView2.setAdapter(adapter2);
			 
		    mDateDisplay = (TextView ) dialog.findViewById(R.id.editText1);   	
			
			Button bb = (Button)dialog.findViewById(R.id.the_change_button); 
			bb.setOnClickListener(this);
			
			flightNumberET = (EditText) dialog.findViewById(R.id.fNum);
			sAirportET = (AutoCompleteTextView) dialog.findViewById(R.id.sAirport);
			dAirportET = (AutoCompleteTextView) dialog.findViewById(R.id.dAirport);
			//src = Integer.parseInt(offer.getFlight().getSource());
			//des = Integer.parseInt(offer.getFlight().getDestination());
			
			flightNumberET.setText(offer.getFlight().getFlightNumber());
	        sAirportET.setText(offer.getFlight().getSource());
	        dAirportET.setText(offer.getFlight().getDestination());
			mDateDisplay.setText(offer.getFlight().departureDate);
			String[] date = offer.getFlight().departureDate.split("-");
			mMonth = Integer.parseInt(date[0])-1;
			mDay =  Integer.parseInt(date[1]);
			mYear =  Integer.parseInt(date[2]);
			
			datePicked.set(mYear, mMonth, mDay, 23 , 59 , 59);
			eo_error[0] = (TextView)dialog.findViewById(R.id.edit_error1);
		    eo_error[1] = (TextView)dialog.findViewById(R.id.edit_error2);
			
			
			break;
		}
		
		
		/***************** On change date clicked *************************************/
		
		case R.id.the_change_button:{
			 
			DatePickerDialog dp = new DatePickerDialog(mActivity,
                    mDateSetListener,
                    mYear, mMonth, mDay);   
			dp.show();

			break;
		}
		
		/*****************Quick action bar , Edit Offer clicked******************************/
		
		case R.id.qa_editOffer:{
			LayoutInflater inflater =
					(LayoutInflater) this.anchor.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View edit_offer =   inflater.inflate(R.layout.edit_offer, null);
			
		    AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
			alert.setTitle(mActivity.getResources().getString(R.string.offer_details));
			alert.setMessage(mActivity.getResources().getString(R.string.Please_make_changes));
			alert.setView(edit_offer);
		    dialog = alert.create();
			dialog.show();
			
			Button b1 = (Button)dialog.findViewById(R.id.edit_offer_cancel);
			b1.setOnClickListener(this);
			Button b2 = (Button)dialog.findViewById(R.id.edit_offer_save);
			b2.setOnClickListener(this);
			
			noKGsET = (EditText)dialog.findViewById(R.id.edit_offer_kgs);
	        pricePerKGET = (EditText)dialog.findViewById(R.id.edit_offer_price);
	        type = (RadioGroup)dialog.findViewById(R.id.edit_offer_type);
	        
	       
	        eo_error[0] = (TextView)dialog.findViewById(R.id.edit_error1);
	        eo_error[1] = (TextView)dialog.findViewById(R.id.edit_error2);
	        
	        noKGsET.setText(offer.getOffer().getNoOfKilograms()+"");
        	pricePerKGET.setText(offer.getOffer().getPricePerKilogram()+"");
        	type.check(offer.getOffer().getUserStatus()==0?R.id.edit_avWeight:R.id.edit_exWeight);
        	String[]date = offer.getFlight().getDepartureDate().split("-");
        	mMonth = Integer.parseInt(date[0]) -1;
        	mDay = Integer.parseInt(date[1]);
        	mYear = Integer.parseInt(date[2]);
        	datePicked.set(mYear, mMonth, mDay, 23 , 59 , 59);

			break;
		}
		
		/*****************Edit offer , cancel clicked******************************/
		
		case R.id.edit_offer_cancel:{
			dialog.dismiss();
			break;
		}
		
		/*****************Edit offer , save clicked******************************/
		
		case R.id.edit_offer_save:{
			
			 String checkErrors = "";
			 for(int i = 0 ; i < 2 ; i++)
			 eo_error[i].setText("");
			 int count = 0;
			 
			 if(noKGsET.getText().toString().equals("")){
				 checkErrors += "Please insert the number of kilograms.\n";
				 eo_error[count++].setText(mActivity.getResources().getString(R.string.kgs_valid));
			 } else if(Integer.parseInt(noKGsET.getText().toString())>30){
				 checkErrors += "Number of kilograms cannot exceed 30.\n";
				 eo_error[count++].setText(mActivity.getResources().getString(R.string.kgs_valid));
			 }else if(Integer.parseInt(noKGsET.getText().toString())==0){
				 checkErrors += "Number of kilograms cannot be zero.\n";
				 eo_error[count++].setText(mActivity.getResources().getString(R.string.kgs_valid));
			 }
			 
			 if(pricePerKGET.getText().toString().equals("")){
				 checkErrors += "Please insert the price.\n";
				 eo_error[count++].setText(mActivity.getResources().getString(R.string.price_valid));
			 }else  if( Integer.parseInt(pricePerKGET.getText().toString())>30){
				 checkErrors += "Price per kilogram cannot exceed 30 euros.\n";
				 eo_error[count++].setText(mActivity.getResources().getString(R.string.price_valid));
			 }
			 
			 if(count==0){
			 
		    	Gson gson = new Gson();
	        	
	        	offer.getOffer().setPricePerKilogram(Integer.parseInt(pricePerKGET.getText().toString()));
	        	offer.getOffer().setNoOfKilograms((Integer.parseInt(noKGsET.getText().toString())));
	        	offer.getOffer().setUserStatus(type.getCheckedRadioButtonId()==R.id.edit_exWeight?1:0);
	        	
	        	
	        	Offer offerIn = offer.getOffer();
		  	    String input = gson.toJson(offerIn);
		  	    Log.e("JSON SENT = ", input);
	        	HTTPManager.startHttpService("/editoffer",
				input , SheelMaayaaConstants.HTTP_EDIT_OFFER, mActivity.getApplicationContext());   
	        	dialog.dismiss();
	        
	        	
	        	
			 }
			 
			 
			 break;
			 }
			 
			
			
			
	 
		}
		
		
		this.dismiss();
		 
		 
	}
	
	
	  private void updateDisplay() {
	    	
	        mDateDisplay.setText(
	            new StringBuilder()//
	                    // Month is 0 based so add 1
	                    .append(mMonth + 1).append("-")
	                    .append(mDay).append("-")
	                    .append(mYear));
	        

	        
	    }
	  
	  public boolean isAlpha(char c){
			 return (c<='Z'&&c>='A'||c>='a'&&c<='z');
		 }

	
	
}