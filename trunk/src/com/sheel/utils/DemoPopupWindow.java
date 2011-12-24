package com.sheel.utils;


 

import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.sheel.app.R;
import com.sheel.datastructures.OfferDisplay2;

public class DemoPopupWindow extends BetterPopupWindow implements OnClickListener {
	OfferDisplay2 offer;
	private Activity mActivity;
	
	/********* Edit offer UI components ********/
	private RadioGroup type;
	private EditText noKGsET;
	private EditText pricePerKGET;
	private TextView eo_error[] = new TextView[2];
	
	
	/******* Internet service components *******/
	private IntentFilter filter;
	private BroadcastReceiver br;
 
	
	
	
	
	public DemoPopupWindow(View anchor, OfferDisplay2 offer, Activity activity) {
		super(anchor);
		this.mActivity = activity;
		this.offer = offer;
	}

	@Override
	protected void onCreate() {
		LayoutInflater inflater =
				(LayoutInflater) this.anchor.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.popup_grid_layout, null);

		// setup button events
		for(int i = 0, icount = root.getChildCount() ; i < icount ; i++) {
			View v = root.getChildAt(i);

			if(v instanceof TableRow) {
				TableRow row = (TableRow) v;

				for(int j = 0, jcount = row.getChildCount() ; j < jcount ; j++) {
					View item = row.getChildAt(j);
					if(item instanceof Button) {
						Button b = (Button) item;
						b.setOnClickListener(this);
					}
				}
			}
		}

		// set the inflated view as what we want to display
		this.setContentView(root);
		
		
		
	}
	AlertDialog dialog;
	@Override
	public void onClick(View v) {
		// we'll just display a simple toast on a button click
		Button b = (Button) v;
		
		switch(b.getId()){
		
		/*****************Quick action bar , Edit Offer clicked******************************/
		
		case R.id.two:{
			LayoutInflater inflater =
					(LayoutInflater) this.anchor.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View edit_offer =   inflater.inflate(R.layout.edit_offer, null);
			
		    AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
			alert.setTitle("Offer details");
			alert.setMessage("Please make the desired changes, then click Save");
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
				 eo_error[count++].setText("* Please insert the number of kilograms.");
			 } else if(Integer.parseInt(noKGsET.getText().toString())>30){
				 checkErrors += "Number of kilograms cannot exceed 30.\n";
				 eo_error[count++].setText("* Number of kilograms cannot exceed 30.");
			 }else if(Integer.parseInt(noKGsET.getText().toString())==0){
				 checkErrors += "Number of kilograms cannot be zero.\n";
				 eo_error[count++].setText("* Number of kilograms cannot be zero.");
			 }
			 
			 if(pricePerKGET.getText().toString().equals("")){
				 checkErrors += "Please insert the price.\n";
				 eo_error[count++].setText("* Please insert the price.");
			 }else  if( Integer.parseInt(pricePerKGET.getText().toString())>30){
				 checkErrors += "Price per kilogram cannot exceed 30 euros.\n";
				 eo_error[count++].setText("* Price per kilogram cannot exceed 30 euros.");
			 }
			 
			 break;
			 }
			 
			
			
			
			//dialog.dismiss();
		}
		
		
		
		 
		this.dismiss();
	}
}