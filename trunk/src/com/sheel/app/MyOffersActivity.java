package com.sheel.app;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
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
import com.sheel.datastructures.OfferDisplay;
import com.sheel.listeners.InflateListener;

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
	 *  The receiver used for detecting the HTTP data arrival 
	 */
	private BroadcastReceiver receiver;
	
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
	ArrayList<OfferDisplay> searchResults = new ArrayList<OfferDisplay>();
	
	
	/**
	 * SearchResults listView to be found by Id
	 */
	ListView searchResultsList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_offers_main);
		
		//======Start the HTTP Request=========
		
		searchResults.add(new OfferDisplay("8", "13", "Hossam_Amer"));
		searchResults.add(new OfferDisplay("8", "13", "Hossam_Amer"));
		searchResults.add(new OfferDisplay("8", "13", "Hossam_Amer"));
		searchResults.add(new OfferDisplay("8", "13", "Hossam_Amer"));
		searchResults.add(new OfferDisplay("8", "13", "Hossam_Amer"));
		
		//=====================================
		
		
		//========Initialize the adapter======
			initAdapter();
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

	@Override
	public Object onRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		return super.onRetainNonConfigurationInstance();
	}
	
	 private Handler handler = new Handler()
	  {

	        @Override
	        public void handleMessage(Message msg)
	        {
	            adapter.notifyDataSetChanged();
	            super.handleMessage(msg);
	        }

	   };

}
