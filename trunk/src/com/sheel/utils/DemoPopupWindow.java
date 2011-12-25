package com.sheel.utils;


 

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sheel.app.MyOffersActivity;
import com.sheel.app.R;
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
	private TextView eo_error[] = new TextView[2];
	
	
	/******* Internet service components *******/
	private IntentFilter filter;
	private BroadcastReceiver br;
 
	public static int BUTTON_CALL = 1;
	public static int BUTTON_SMS = 2;
	public static int BUTTON_CONFIRM = 4;
	public static int BUTTON_EDIT = 8;
	public static int BUTTON_DEACTIVATE = 16;
	
	public static int enabledButtons;
	
	
	
	
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
	        	
	        	offer.getOffer().setPricePerKilogram(Integer.parseInt(noKGsET.getText().toString()));
	        	offer.getOffer().setNoOfKilograms((Integer.parseInt(pricePerKGET.getText().toString())));
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
			 
			
			
			
			//dialog.dismiss();
		}
		
		
		this.dismiss();
		 
		 
	}
}