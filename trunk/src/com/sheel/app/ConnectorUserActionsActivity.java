package com.sheel.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * This activity displays the home page that will be displayed to the user after
 * registering
 * 
 * @author Nada Emad
 * 
 */
public class ConnectorUserActionsActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connector_user_action);

	}

	/**
	 * This is called when the declare button is clicked
	 * 
	 * @param v
	 */

	public void onClick_DeclareOffer(View v) {
		Toast.makeText(getApplicationContext(), "Ahmed: Declare Offers",
				Toast.LENGTH_SHORT).show();

	}

	/**
	 * This is called when the search button is clicked
	 * 
	 * @param v
	 */

	public void onClick_SearchOffers(View v) {
		Toast.makeText(getApplicationContext(), "Magued: Search for Offers",
				Toast.LENGTH_SHORT).show();

	}

}
