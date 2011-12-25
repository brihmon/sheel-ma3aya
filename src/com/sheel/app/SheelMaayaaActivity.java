package com.sheel.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sheel.datastructures.NavigationItem;
import com.sheel.utils.GuiUtils;

/**
 * Connector activity used as a welcome page for the application
 * 
 * @author passant
 *
 */
public class SheelMaayaaActivity extends UserSessionStateMaintainingActivity {

	/**
	 * It handles all requests to facebook API. Moreover, it has
	 * all related info to logged in user and session
	 */
	//private FacebookWebservice fbService = new FacebookWebservice();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.connector_welcome_page);	
		setContentView(R.layout.sheel_dashboard);
		initDashBoardItems();
	}// end onCreate
	
	private void initDashBoardItems() {
		NavigationItem[] dashBoardItems = getNavigationItems();
		// In the same order of the navigation items
		TextView[] textViewsInDashBoard = new TextView[] {				
			(TextView)findViewById(R.id.sheel_dashboard_search)	,
			(TextView)findViewById(R.id.sheel_dashboard_declare),
			(TextView)findViewById(R.id.sheel_dashboard_myOffers)				
		};
		
		GuiUtils localizingGuiUtils = new GuiUtils(getApplicationContext());
		
		for (int i =0 ; i< textViewsInDashBoard.length; i++) {
			TextView currentTextView = textViewsInDashBoard[i];
			currentTextView.setText(dashBoardItems[i].getName());
			localizingGuiUtils.setIconForATextField(dashBoardItems[i].getResourceIdOfDashBoard(), currentTextView, 4);
			currentTextView.setTag(i);
			//localizingGuiUtils.setBackgroundOfTextView(currentTextView, leftToRightRsc, rightToLeftRsc)
			currentTextView.setOnClickListener(new View.OnClickListener() {				
				public void onClick(View v) {					
					int position = ((Integer)((TextView)v).getTag()).intValue();
					onClick_dashBoardItem(position);					
				}
			});
		}// end for: initialize each text view
	}// end setIconsToDashBoardItems
	
	@Override 
	protected void onSaveInstanceState(Bundle outState) {
		
		//out
	}// end onSaveInstanceState
	
	/**
	 * Called when (register) button is clicked
	 * @param v
	 * 		Clicked button
	 */
	public void onClick_button_register(View v){
		/*Toast.makeText(this, "Hello Register", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this, NewUserActivity.class));*/
		
		onClick_dashBoardItem(0);
	}// end onClick_button_register
	
	/**
	 * Called when (login) button is clicked
	 * @param v
	 * 		Clicked button
	 */
	public void onClick_button_login(View v){
		startActivity(new Intent(this,ConnectorUserActionsActivity.class));
	}// end onClick_button_login
	
	/**
	 * Returns class responsible for handling all requests to facebook API. 
	 * Moreover, it has all related info to logged in user and session.
	 * <br><br>
	 * Note: 
	 * <ul>
	 * 		<li>In case you want details of logged in, use 
	 * 		{@link FacebookWebservice#getFacebookUser()} method.</li>
	 *      <li>In case you want details about session, use 
	 * 		{@link FacebookWebservice#isSessionValid()} method.</li>
	 * </ul>
	 * 
	 * @return
	 * 		Object used for interacting with facebook
	 */
	/*public FacebookWebservice getFacebookService(){
		return this.fbService;
	}// end getFacebookService
	*/
}// end class
