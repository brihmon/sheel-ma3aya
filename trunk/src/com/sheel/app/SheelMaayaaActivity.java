package com.sheel.app;

import com.sheel.webservices.FacebookWebservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SheelMaayaaActivity extends Activity {
	
	// New FB service
	FacebookWebservice fbService = new FacebookWebservice();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        setContentView(R.layout.connector_welcome_page);
    }
    
    public void onClick_goToSheelMa3aya(View v){
    	startActivity(new Intent(this, ConnectorWelcomePageActivity.class));
    }
    
    
    public void onClick_insertoffer(View v){
		 startActivity(new Intent(this, InsertOfferActivity.class));
	 }
    
	 public void onClick_go (View v) 
	 {
		 Toast.makeText(getApplicationContext(), "Displaying contact info", Toast.LENGTH_SHORT).show();
		 startActivity(new Intent(this, PhoneCommunication.class));
	}	 
	 
	 public void onClick_goToFacebook (View v) 
	 {
		 //Toast.makeText(getApplicationContext(), "Displaying contact info", Toast.LENGTH_SHORT).show();
		 //FacebookWebservice fbService = new FacebookWebservice();
		 // will automatically login and get the first set of info
		 Log.e("passant","Login");
		 //fbService.login(this);
	}	 
	 
	 public void onClick_goToFacebookToLogout (View v) 
	 {
		  Log.e("passant","Logout");
		  fbService.logout(this);
	 }	 
	 
	 public void onClick_doSocialSearch(View v){
		 ViewSearchResultsActivity act = new ViewSearchResultsActivity();
		 Log.e("Passant", "start testing search results");
		 act.test_searchUsingFacebook();
	 }
	 
	 public void onClick_goToSearchResults(View v){
		 startActivity(new Intent(this, ViewSearchResultsActivity.class));
	 }
	 
	 public void onClick_goHTTP (View v) 
	 {
	        SheelMaaayaClient sc = new SheelMaaayaClient() {
				
				@Override
				public void doSomething() {
					final String str = this.rspStr;
					 
								 runOnUiThread(new Runnable()
	                             {
	                 //                @Override
	                                 public void run()
	                                 {
	                                     Toast.makeText(SheelMaayaaActivity.this, str, Toast.LENGTH_LONG).show();
	                                     startActivity(new Intent(SheelMaayaaActivity.this, PhoneCommunication.class));
	                                 }
	                             });

				}
			};
	        
	        sc.runHttpRequest("/test");
	        Toast.makeText(getApplicationContext(), "MainApp" , Toast.LENGTH_SHORT).show();
		 	
		 
	}	 
	 
	 public void onClick_view1 (View v) 
	 {
		 startActivity(new Intent(this, GetUserInfoActivity.class));
	 }	
	 
	 public void onClick_register (View v) 
	 {

		   startActivity(new Intent(this, NewUserActivity.class));
		
		 
		  /* Intent mIntent = new Intent(this, NewUserActivity.class);
			// Pass variable to detailed view activity using the intent
			mIntent.putExtra(NewUserActivity.FIRST_NAME_KEY, "NADA");
			mIntent.putExtra(NewUserActivity.MIDDLE_NAME_KEY, "EMAD");
			mIntent.putExtra(NewUserActivity.LAST_NAME_KEY, "ADLY");
			mIntent.putExtra(NewUserActivity.GENDER_KEY, "female");
			// Start the new activity
			startActivity(mIntent);
			*/
	 
	 }
	
}