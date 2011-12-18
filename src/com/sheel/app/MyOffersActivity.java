package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;
import static com.sheel.utils.SheelMaayaaConstants.pathKey;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sheel.adapters.SearchResultsListAdapter;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.listeners.InflateListener;
import com.sheel.webservices.SheelMaayaaService;

/**
 * This activity is used for displaying and interacting with
 * the offers of the logged-in user.
 * 
 * @author Hossam_Amer
 *
 */

public class MyOffersActivity extends UserSessionStateMaintainingActivity 
{
	private static final String TAG = MyOffersActivity.class.getName();
	private static final String HTTP_GET_MY_OFFERS_FILTER = "HTTP_GET_MY_OFFERS";
	
	
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
	 * Offers retrieved from the database.
	 */
	ArrayList<OfferDisplay2> searchResults = new ArrayList<OfferDisplay2>();
	
	
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
		
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_offers_main);
		
		//========Get the last state for my list========
		searchResults = (ArrayList<OfferDisplay2>) getLastNonConfigurationInstance();		
		//=====================================

		
		//======Start the HTTP Request=========
			
	if(searchResults == null)
	{
		// Create a new list
		searchResults = new ArrayList<OfferDisplay2>();
	
		path = "/getmyoffers/" + getFacebookService().getFacebookUser().getUserId();
		// hashas
		path = "/getmyoffers/673780564";
		
		startHttpService(path);
	}	
	
	//========Initialize the adapter======
	initAdapter();
	//=====================================
    		
		//=====================================
		
		
    	
    	//==========Item Click Listener========
    	
		/**
		 * Item Click listener that the gets the selected Index
		 * Saves the indices of the buttons inside their tags
		 */
		
		searchResultsList.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View v,
		        int position, long id) {
		    	
		    	
		    	// It gets the clicked position
		mPos = position;
		Log.e(TAG, "Pos: " + position);
		
		try
		{
			if (v != null) 
			{
				stub = (ViewStub) v.findViewById(R.id.infoStub);
				inflated = (View) v.findViewById(R.id.infoStubInflated);
					 
				if(stub != null)
				{
					if(stub.getVisibility() == View.GONE)
						{
							InflateListener	infListener = new InflateListener(mPos);
							stub.setOnInflateListener(infListener);
							stub.setVisibility(View.VISIBLE);
						}
					else
						stub.setVisibility(View.GONE);
				}
				
				if(inflated != null)
				{
					if(inflated.getVisibility() == View.GONE)
						{
						
							inflated.setVisibility(View.VISIBLE);
						}
					else
						inflated.setVisibility(View.GONE);
				}
					
				}//end if
		}
		catch (Exception e) {
			// TODO: handle exception
				Log.e(TAG, e.getStackTrace().toString());
			}
			
			    	    	
		}});// end onItemClick
		    	
    	
    	//=====================================

	}

	/**
	 * Initiates the HTTP call to the server
	 * @param path
	 * 				Path to the server controller
	 */
	private void startHttpService(String path) {
		
		dialog = ProgressDialog.show(MyOffersActivity.this, "", "Getting your Offers, Please wait..", true, false);
		serviceIntent = new Intent(this, SheelMaayaaService.class);
    	serviceIntent.setAction(HTTP_GET_MY_OFFERS_FILTER);
    	serviceIntent.putExtra(pathKey, path);
    	
    	Log.e(TAG, "Before Get My Offers HTTP Request: " + serviceIntent);
	    startService(serviceIntent);
    	
	}

	/**
	 * Initializes the adapter of the list view.
	 */
	private void initAdapter() {
		
		adapter = new SearchResultsListAdapter(this, searchResults);		
		searchResultsList = (ListView)findViewById(R.id.list);
		
    	// Set adapter to the list view 	
    	searchResultsList.setAdapter(adapter);
    	    	
    	// To enable filtering certain content of the method
    	searchResultsList.setTextFilterEnabled(true);
		
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
	 * Used for returning the <code>searchResults</code> just before onDestroy()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {

		return searchResults;
	}
	
	/**
	 * Handler for notifying the change of data to the adapter
	 */
	
	 private Handler handler = new Handler()
	  {

	        @Override
	        public void handleMessage(Message msg)
	        {
	        	Log.e(TAG, "Data Changed");
	        	Log.e(TAG, adapter.toString());
	            adapter.notifyDataSetChanged();
	            super.handleMessage(msg);
	        }

	   };
	   
	   
	   public void onClick(View view) {
	    	Toast.makeText(getApplicationContext(), "Button Position: " +
	    			view.getTag(), Toast.LENGTH_SHORT).show();
	    		    	
	    	String path = "/getmyoffers/673780564";
	    	
	    	    	
	    	Intent serviceIntent = new Intent(this, SheelMaayaaService.class);
	    	serviceIntent.setAction(HTTP_GET_MY_OFFERS_FILTER);
	    	serviceIntent.putExtra(pathKey, path);
	    	
	    	Toast.makeText(getApplicationContext(), "Before Intent Service",
	    			Toast.LENGTH_SHORT).show();
	    	
	    	startService(serviceIntent);
	    	
	    	
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

			/**
			 * Used to fill the adapter with data (Offers)
			 * @param responseStr
			 * 					Response String recieved from the server
			 */
			private void loadSearchResultsOnUI(String responseStr) 
			{
				try {
					
					JSONArray jsonArray = new JSONArray(responseStr);
                	
               	 for (int i = 0; i < jsonArray.length(); i++) {               		 
               		 searchResults.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i)));
               		 
               	 }// end for

					// Notify the adapter
					Log.e(TAG, handler.toString());
		            handler.sendEmptyMessage(1);
		            
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
				
	}
		
  

}
