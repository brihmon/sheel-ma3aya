package com.sheel.app;

import com.sheel.adapters.SearchResultsListAdapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewStub;

public class MyOffersActivity extends UserSessionStateMaintainingActivity 
{
	private static final String TAG = MyOffersActivity.class.getName();
	private static final String HTTP_GET_MY_OFFERS_FILTER = "HTTP_GET_MY_OFFERS";
		
	private BroadcastReceiver receiver;
	
	int mPos;
	ViewStub stub;
	View inflated;
	
	SearchResultsListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_offers_main);
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
