package com.sheel.app;

import com.sheel.webservices.FacebookWebservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SheelMaayaaActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    
	 public void onClick_go (View v) 
	 {
		 Toast.makeText(getApplicationContext(), "Displaying contact info", Toast.LENGTH_SHORT).show();
		 startActivity(new Intent(this, PhoneCommunication.class));
	}	 
	 
	 public void onClick_goToFacebook (View v) 
	 {
		 //Toast.makeText(getApplicationContext(), "Displaying contact info", Toast.LENGTH_SHORT).show();
		 FacebookWebservice fbService = new FacebookWebservice();
		 // will automatically login and get the first set of info
		 fbService.login(this);
	}	 
	 
	 public void onClick_goHTTP (View v) 
	 {
	        SheelMaaayaClient sc = new SheelMaaayaClient() {
				
				@Override
				public void doSomething() {
					final String str = this.rspStr;
					 
								 runOnUiThread(new Runnable()
	                             {
	                                 @Override
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
	 }	
	
}