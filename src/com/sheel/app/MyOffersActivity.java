package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;
import static com.sheel.utils.SheelMaayaaConstants.pathKey;
import static com.sheel.utils.SheelMaayaaConstants.*;

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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sheel.adapters.SearchResultsListAdapter;
import com.sheel.datastructures.Category;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.listeners.InflateListener;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.InternetManager;
import com.sheel.webservices.SheelMaayaaService;

import com.sheel.datastructures.Category;
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
	 *  Last position clicked in the listView, used for saving the  
	 */
	int mPos;
	
	/**
	 *  View stub that has our controls (buttons..etc) inside
	 */
	ViewStub stub;
	
	/**
	 *  Inflated view from the stub
	 */
	View inflated;
	
	
	/**
	 *  Adapter used for populating the offers list
	 */
	SearchResultsListAdapter adapter;

	
	/**
	 * Offers half-confirmed retrieved from the database.
	 */
	ArrayList<OfferDisplay2> searchResults_half = new ArrayList<OfferDisplay2>();
	
	/**
	 * Offers full-confirmed retrieved from the database.
	 */
	ArrayList<OfferDisplay2> searchResults_full = new ArrayList<OfferDisplay2>();
	
	/**
	 * SearchResults listView to be found by Id
	 */
	ListView searchResultsList;
	
	/**
	 * Intent used to pass the path for SheelMaayaService for handling HTTP Requests
	 */
	Intent serviceIntent;	
	
	/**
	 * Dialog for displaying the loading pop-up for the user
	 */
	ProgressDialog dialog;
	
	/**
	 * Flag to indicate whether to load from the database or the activity resources
	 */
	
	InflateListener	infListener;
		
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
			
			super.getCategories().add(new Category("Full-Confirmed", R.layout.my_offers_main));
			super.getCategories().add(new Category("Half-Confirmed", R.layout.my_offers_main));
				
			// Create a new list
			searchResults_half = new ArrayList<OfferDisplay2>();
			
			searchResults_full = new ArrayList<OfferDisplay2>();
			
			//======Start the HTTP Request=========
			path = "/getmyoffers/" + getFacebookService().getFacebookUser().getUserId();
			// XXXXhashas
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
		filter.addAction("test");
		
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
			
			if(action.equals("test"))
				dialog = ProgressDialog.show(MyOffersActivity.this, "", "Please wait..", true, false);
			else
			{
				int httpStatus = intent.getExtras().getInt(HTTP_STATUS);
				Log.e(TAG, "HTTPSTATUS: "+ httpStatus);
				
				if( httpStatus == HttpStatus.SC_OK)
				{
					if (action.equals(HTTP_GET_MY_OFFERS_FILTER))
					{
						String responseStr = intent.getExtras().getString(HTTP_RESPONSE);
						loadSearchResultsOnUI(responseStr);
						 
						// Dialog dismissing
						if(dialog != null) dialog.dismiss();
						Log.e(TAG, responseStr);
							
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
               		
               		else if(offer.getOffer().offerStatus.equals("confirmed"))
               			searchResults_full.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i)));

               		 
               	 }// end for
               	 
               	 updateCategoryContent(searchResults_half, 0, false);
               	 updateCategoryContent(searchResults_full, 1, false);
		            
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
				
	}
		
  

}
