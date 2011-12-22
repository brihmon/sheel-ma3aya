package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;

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

import com.sheel.datastructures.Category;
import com.sheel.datastructures.Confirmation;
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
	
	/**
	 * Airports list used for localization
	 */
	
	String[] airportsList;
	
	/**
	 * Nationalities list used for localization
	 */
	
	String[] nationalitiesList;
		
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
							
			// Create a new list
			searchResults_half = new ArrayList<OfferDisplay2>();
			
			searchResults_full = new ArrayList<OfferDisplay2>();
			
			//======Start the HTTP Request=========
			path = "/getmyoffers/" + getFacebookService().getFacebookUser().getUserId();
			
			//	<<<<<Some Test Paths>>>>>>>>			
//			path = "/getmyoffers/626740322";
//			path = "/getmyoffers/673780564";

			dialog = ProgressDialog.show(MyOffersActivity.this, "", "Getting your Offers, Please wait..", true, false);
			HTTPManager.startHttpService(path, HTTP_GET_MY_OFFERS_FILTER, getApplicationContext());
		}
		
	}// end if (isInternet)
	else		
		noInternetConnectionHandler();
	
		
	}//end OnCreate
	
	/**
	 * Internet connection handler handles when there is not Internet connection. 
	 */

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
							
					}// end if Get my offers filter
				}//end onReceive(Context context, Intent intent)
			}//end onReceive
			
		}// end class SheelMaayaaBroadCastRec			

			/**
			 * Updates the UI with the offers confirmed by the database.
			 * It fetches the offers that are either one from 4 categories:
			 * - New offers I declared
			 * - Half confirmed offers I declared (I am Offer owner)
			 * - Half confirmed offers I confirmed but not declared by me
			 * 	(I am not offer owner)
			 * 
			 * @param responseStr 
			 * 				The response string retrieved from the server
			 * @author Hossam_Amer
			 */
			private void loadSearchResultsOnUI(String responseStr) 
			{
				try {
					
					JSONArray jsonArray = new JSONArray(responseStr);
					airportsList = getResources().getStringArray(R.array.airports_array);
					nationalitiesList = getResources().getStringArray(R.array.nationalities_array);

					
               	 for (int i = 0; i < jsonArray.length(); i++) {               		 
               		 
               		 OfferDisplay2 offer = OfferDisplay2.mapOffer(jsonArray.getJSONObject(i), 
               				 airportsList,  nationalitiesList);
               		 
               		 Log.e(TAG, offer + "");
               		 
                      if(offer.getOffer().offerStatus.equals(Confirmation.half_confirmed_offerOwner))
               			searchResults_half.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i), airportsList, 
               					nationalitiesList));
               		
               		else if(offer.getOffer().offerStatus.equals(Confirmation.not_confirmed))
               			searchResults_full.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i), airportsList,
               					nationalitiesList));
               		 
               	 }// end for
              
               	 // Update the categories in Swype Activity
               	 updateCategoriesInSwypeActivity();
               	 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//end catch
				
			}//end loadSearchResultsOnUI
			
			/**
			 * After fetching the data from the server, it creates the categories depending on the input data.
			 * Gives alert if there are no data, and lets you go into InsertOffer activity
			 */

			private void updateCategoriesInSwypeActivity() {
               
				if(searchResults_full.isEmpty() && searchResults_half.isEmpty())
        			GuiUtils.showAlertWhenNoResultsAreAvailable(
             				this, 
             				"You do not have any offers yet! ", 
             				"Declare new offer", InsertOfferActivity.class, 
             				"Change filters", FilterPreferencesActivity.class);
               	else 
               		{
               			int index = 0;
               			String [] swpeCats = getResources().getStringArray(R.array._swyperCats);
               			
               			if(!searchResults_half.isEmpty())
               			{
                     	   getCategories().add(new Category("" + swpeCats[0], R.layout.my_offers_main));
                    	   updateCategoryContent(searchResults_half, index++, false);	
               			}// end if(!searchResults_half.isEmpty())
               			
               			if(!searchResults_full.isEmpty())
               			{
                			getCategories().add(new Category(swpeCats[1], R.layout.my_offers_main));
                			updateCategoryContent(searchResults_full, index, false);	
               			}//end if(!searchResults_full.isEmpty())
               		}//end else
               	 

			}//end updateCategoriesInSwypeActivity()   

}//end MyOffersActivity
