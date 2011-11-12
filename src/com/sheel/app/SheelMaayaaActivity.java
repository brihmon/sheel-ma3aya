package com.sheel.app;

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
		 Log.e("Passant","Going to facebook");
		 startActivity(new Intent(this, SearchFacebookActivity.class));
	}	 
	
}