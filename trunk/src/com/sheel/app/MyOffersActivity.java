package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_CONFIRM_OFFER;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sheel.datastructures.Category;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.utils.GuiUtils;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.InternetManager;
/**
 * This activity is used for displaying and interacting with
 * the offers of the logged-in user.
 * 
 * @author Hossam_Amer
 *
 */

public class MyOffersActivity extends SwypingHorizontalViewsActivity 
{
	private static final String TAG = MyOffersActivity.class.getName();
	
	
	/**
	 * Path for the database query
	 */
	String path;
	
	/**
	 * Filter added for this activity to filter the actions received by the receiver
	 */
	IntentFilter filter;
	
	/**
	 *  The receiver used for detecting the HTTP data arrival 
	 */
	private SheelMaayaaBroadCastRec receiver;
	
	
	/**
	 * Offers half-confirmed retrieved from the database.
	 */
	ArrayList<OfferDisplay2> searchResults_half = new ArrayList<OfferDisplay2>();
	
	/**
	 * Offers full-confirmed retrieved from the database.
	 */
	ArrayList<OfferDisplay2> searchResults_full = new ArrayList<OfferDisplay2>();
		
	/**
	 * Dialog for displaying the loading pop-up for the user
	 */
	ProgressDialog dialog;
	
		
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
	//======================Checking Available Internet Connection=============	
		
	if(InternetManager.isInternetOn(getApplicationContext()))
	{

		
		//========Get the last state for my list========
		
		if((ArrayList<Category>) super.getLastNonConfigurationInstance() == null)
		{
			//=================Add Categories====================
			
//			super.getCategories().add(new Category("Half-Confirmed", R.layout.my_offers_main));
//			super.getCategories().add(new Category("Full-Confirmed", R.layout.my_offers_main));
			
				
			// Create a new list
			searchResults_half = new ArrayList<OfferDisplay2>();
			
			searchResults_full = new ArrayList<OfferDisplay2>();
			
			//======Start the HTTP Request=========
			path = "/getmyoffers/" + getFacebookService().getFacebookUser().getUserId();
			// XXXXhashas
//			path = "/getmyoffers/626740322";
			path = "/getmyoffers/673780564";

			dialog = ProgressDialog.show(MyOffersActivity.this, "", "Getting your Offers, Please wait..", true, false);
			HTTPManager.startHttpService(path, HTTP_GET_MY_OFFERS_FILTER, getApplicationContext());
		}
		
	}// end if (isInternet)
	else		
		noInternetConnectionHandler();
	
		
	}//end OnCreate

	private void noInternetConnectionHandler() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Internet Connection is not available")
		       .setCancelable(false)
		       .setPositiveButton("ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       
		       });
		 builder.create();
		 builder.show();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		// Cancel out the dialog
		dialog = null;
		// Unregister the receiver onPause
		unregisterReceiver(receiver);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		filter = new IntentFilter();
		
		// Add the filters of your activity
		filter.addAction(HTTP_GET_MY_OFFERS_FILTER);
		filter.addAction(HTTP_CONFIRM_OFFER);
		
		receiver = new SheelMaayaaBroadCastRec();
		
		Log.e(TAG, "Receiver Registered");
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
		   
	   /**
	    * {@link SheelMaayaaBroadCastRec} Class for Broadcast receiver i.e to receive the result from the HTTP request
	    * @author Hossam_Amer
	    *
	    */
	   
		class SheelMaayaaBroadCastRec extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
			
				
				Log.e(TAG, intent.getAction());
				String action = intent.getAction();
			
				int httpStatus = intent.getExtras().getInt(HTTP_STATUS);
				Log.e(TAG, "HTTPSTATUS: "+ httpStatus);
				
				String responseStr;
				if( httpStatus == HttpStatus.SC_OK)
				{
					responseStr = intent.getExtras().getString(HTTP_RESPONSE);
					if (action.equals(HTTP_GET_MY_OFFERS_FILTER))
					{
						loadSearchResultsOnUI(responseStr);
						 
						// Dialog dismissing
						if(dialog != null) dialog.dismiss();
						Log.e(TAG, responseStr);
							
					}
					else if(action.equals(HTTP_CONFIRM_OFFER))
					{
						Log.e(TAG, responseStr);
						
						/**
						 * if(half-confirmed by another user)
						 * Sorry!
						 * if(confirmed success)
						 * You confirmed the user + 7abashtakaanaat fel view lw fi!
						 * 
						 * if(offerOwner is confirming the offer)
						 * it should not re-appear, it is fetching again
						 * 
						 */
/*                        if(str.contains("12") || str.contains("13"))
                        {
                        sendMail
                       	 Toast.makeText(PhoneCommunication.this, "Offer has been confirmed!", Toast.LENGTH_LONG).show(); 
                        }
                        else if(str.contains("Success"))
                       	 Toast.makeText(PhoneCommunication.this, "You have confirmed the offer!", Toast.LENGTH_LONG).show();
                        else if(str.contains("Failure"))
                            Toast.makeText(PhoneCommunication.this, "You could not confirm this offer anymore!", Toast.LENGTH_LONG).show();
*/
					}
						
				
				}
			}
		}

			/**
			 * Used to fill the adapter with data (Offers)
			 * @param responseStr
			 * 					Response String received from the server
			 */
			private void loadSearchResultsOnUI(String responseStr) 
			{
				try {
					
					JSONArray jsonArray = new JSONArray(responseStr);
                	
               	 for (int i = 0; i < jsonArray.length(); i++) {               		 
               		 
               		 OfferDisplay2 offer = OfferDisplay2.mapOffer(jsonArray.getJSONObject(i));
               		 
               		 //XXXXbad for localization
               		if(offer.getOffer().offerStatus.equals("half-confirmed"))
               			searchResults_half.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i)));
               		
               		else if(offer.getOffer().offerStatus.equals("new"))
               			searchResults_full.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i)));

               		 
               	 }// end for
              
               	if(searchResults_full.isEmpty() && searchResults_half.isEmpty())
               		showAlert();
               	else 
               		{
               			int index = 0;
               			if(!searchResults_half.isEmpty())
               			{
                     	   getCategories().add(new Category("Half-Confirmed", R.layout.my_offers_main));
                    	   updateCategoryContent(searchResults_half, index++, false);	
               			}
               			
               			if(!searchResults_full.isEmpty())
               			{
                			getCategories().add(new Category("Not-Confirmed", R.layout.my_offers_main));
                			updateCategoryContent(searchResults_full, index, false);	
               			}
               		}
               	 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
				
	
		
		public void showAlert()
		{
			GuiUtils.showAlertWhenNoResultsAreAvailable(
     				this, 
     				"You do not have any offers yet! ", 
     				"Declare new offer", InsertOfferActivity.class, 
     				"Change filters", FilterPreferencesActivity.class);
			
//			MyOffersActivity.this.finish();
		}
  

}
