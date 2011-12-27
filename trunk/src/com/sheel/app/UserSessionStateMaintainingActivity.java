package com.sheel.app;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.NavigationItem;
import com.sheel.datastructures.enums.SharedValuesBetweenActivities;
import com.sheel.listeners.LoginDataBaseListener;
import com.sheel.utils.GuiUtils;
import com.sheel.webservices.FacebookWebservice;

/**
 * Base class for all activities inside the app to maintain critical
 * information of user and session that must be passed between all 
 * activities
 * 
 * @author 
 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class UserSessionStateMaintainingActivity extends Activity {

	/**
	 * Constant used for tracing purposes "class name (package name)"
	 */
	private final String TAG_CLASS_PACKAGE = "UserSessionStateMaintainingActivity (com.sheel.app): ";
	/**
	 * Encapuslators for different info needed for navigation items (menus, dash board, etc.)
	 * @see NavigationItem
	 */
	 final NavigationItem[] NAVIGATION_ITEMS = new NavigationItem[] {
		new NavigationItem("Search existing offers", R.drawable.sheel_menu_main_search, R.drawable.sheel_dashboard_search_en, GetUserInfoActivity.class),
		new NavigationItem("Declare New Offer", R.drawable.sheel_menu_main_declare, R.drawable.sheel_dashboard_declare_en, InsertOfferActivity.class),
		new NavigationItem("View My Offers", R.drawable.sheel_menu_main_myoffers, R.drawable.sheel_dashboard_myoffers_en, MyOffersActivity.class),
		new NavigationItem("Logout", R.drawable.sheel_menu_main_logout, R.drawable.sheel_menu_main_logout, SheelMaayaaActivity.class)
	};
	
	/**
	 * It handles all requests to facebook API. Moreover, it has
	 * all related info to logged in user and session
	 */
	private  static FacebookWebservice fbService = null;
	
	/**
	 * Filter added for this activity to filter the actions received by the receiver
	 */
	private IntentFilter filter = new IntentFilter();
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (fbService != null)
			Log.e(TAG_CLASS_PACKAGE,"Info read from static facebook service: userId = " + fbService.getFacebookUser()+" , accessToken= "+  fbService.getUserAccessToken() + " ,accessExpiry= " + fbService.getUserAccessTokenExpiryTime());
		else
			Log.e(TAG_CLASS_PACKAGE,"Facebook service is not initialized yet");
		
		/**
		 * on creating a new activity -> check the facebook session status.
		 * If not connected, tell the user and prompt login 
		 * The if statement to exclude the case of the dashboard,
		 * because already each button checks 
		 */
		if (!this.getClass().equals(SheelMaayaaActivity.class)) {
			validateSession(true);
		}// end if 
	
		// do any logic you want
		
	}// end onCreate
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/**
		 * Used to check on the back button that the facebook
		 * session hasn't expired yet. If it expired it shows
		 * a warning message to the user and diverts him to dash 
		 * board
		 * @author Passant El.Agroudy (passant.elagroudy@gmail.com)
		 */
	//	if (keyCode == KeyEvent.KEYCODE_BACK) {
	 //       validateSession(true);
	        //return true;
	  //  }
	   // return super.onKeyDown(keyCode, event);
//	}// end onKeyDown
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onRestart() {
		/**
		 * Used to check on restarting an activity if the facebook
		 * session is valid or not
		 * @author Passant El.Agroudy (passant.elagroudy@gmail.com)
		 */
		super.onRestart();
		validateSession(true);
	}// end onStart
	
	@Override 
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    	/*This method must be called for facebook login to function properly*/
		getFacebookService().authorizeCallback(requestCode, resultCode, data);
    }// end onActivityResult
      
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/**
		 * This method is used to create the main menu of the app
		 * 
		 * @author Passant El.Agroudy (passant.elagroudy@gmail.com)
		 */
		if (this.getClass().equals(SheelMaayaaActivity.class)) {
			return false;
		}// end if: in the dashboard view hide menu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu_main, menu);
		return true;

	}// end onCreateOptionsMenu
	
	public NavigationItem[] getNavigationItems() {
		return NAVIGATION_ITEMS;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/**
		 * This method is used to handle clicks of the app main menu
		 * @author 
		 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
		 */
		
		Class<?> typeOfNextActivity = null;
		
		switch (item.getItemId()) {
			case R.id.menu_main_search: 
				typeOfNextActivity = GetUserInfoActivity.class;
				break;				
			case R.id.menu_main_declare:
				typeOfNextActivity = InsertOfferActivity.class;
				break;					
			case R.id.menu_main_myoffers:
				typeOfNextActivity = MyOffersActivity.class;
				break;					
			case R.id.menu_main_logout: 
				if (getFacebookService() != null)
					getFacebookService().logout(this);
					/* because the session is static, if not set to null, the app
					 * will never login again until it is closed then re-opened*/
				setFacebookService(null);
				startActivity(new Intent(this, SheelMaayaaActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
				
		}// end switch: according to selected action -> decide next activity
		
		Intent intent = new Intent(this, typeOfNextActivity);
		startActivity(intent);
		return true;
				
	}// end onOptionsItemSelected
	
	/**
	 * Returns class responsible for handling all requests to facebook API. 
	 * Moreover, it has all related info to logged in user and session.
	 * <br><br>
	 * <b>Note:</b> 
	 * <ul>
	 * 		<li>In case you want details of logged in, use 
	 * 		{@link FacebookWebservice#getFacebookUser()} method.</li>
	 * 		<li>In case you want logged-in user facebook ID, use 
	 * 		{@link FacebookUser#getUserId()} method.</li>
	 *      <li>In case you want details about session, use 
	 * 		{@link FacebookWebservice#isSessionValid()} method.</li>
	 * </ul>
	 * 
	 * <b>Example:</b><br>
	 * <code>
	 * 		if (getFacebookService() != null) <br>
	 * 		String userFacebookId = getFacebookService().getFacebookUser().getUserId();
	 * </code> <br>
	 * 
	 * @return
	 * 		Object used for interacting with facebook
	 * 
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public FacebookWebservice getFacebookService(){
		return fbService;
	}// end getFacebookService
	
		
	public void setFacebookService(FacebookWebservice fbServ){
		fbService = fbServ;
	}
	
	/**
	 * Used to move to next activity 
	 * 
	 * @param typeOfNextActivity
	 * 		Class of the new activity. You can get it using 
	 * 		<code>ActivityName.class</code>
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void goToActivity(Class<?> typeOfNextActivity) {
		Intent intent = new Intent(this.getApplicationContext(), typeOfNextActivity);
		startActivity(intent);
	}// end goToActivity

	public void goToActivity(int position, Class<?> typeOfNextActivity) {
		Intent intent = new Intent(this.getApplicationContext(), typeOfNextActivity);
		intent.putExtra(NewUserActivity.POSITION_KEY, position);
		startActivity(intent);
	}// end goToActivity

	/**
	 * Used to lock current state of orientation.
	 * i.e: If mobile in landscape mode, lock it to landscape.
	 * 
	 * <br><br><b>IMPORTANT: to release orientation back, 
	 * use <code>setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);</code></b>
	 * 
	 * @author 
	 * 		Pilot_51 ({@link http://stackoverflow.com/questions/3611457/android-temporarily-disable-orientation-changes-in-an-activity})
	 *		Used by: Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void lockCurrentStateForOrientation () {
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}// end lockCurrentStateForOrientation
	
	public void onClick_dashBoardItem(int position) {
		System.out.println("onClick_dashBoardItem");
		System.out.println("onClick_dashBoardItem: " + getFacebookService());

		if (fbService == null)
			fbService = new FacebookWebservice();

		System.out.println("onClick_dashBoardItem: IsvalidSession? "+ getFacebookService().isSessionValid());
	
		validateSession(false);
		
		if (!getFacebookService().getFacebookUser()
				.isRequestedBeforeSuccessfully() ) {
			CheckUserLoginStatusFromDbListener dbListener = new CheckUserLoginStatusFromDbListener(
					filter, position);
			registerReceiver(dbListener, filter);
			lockCurrentStateForOrientation();
			getFacebookService().login(this, true, true, dbListener,
					getApplicationContext());
			System.out.println("First time to register receiver");
		} else {
			System.out.println("already registered receiver -> move to activity");
			goToActivity(NAVIGATION_ITEMS[position].getActivityType());
		}

	}// end onClick_dashBoardItem

	/**
	 * Used to validate the facebook session.
	 * If the session has expired, it shows a warning message for the
	 * user and diverts him to the dash board optionally
	 * 
	 * @param goToDashboard
	 * 		<ul>
	 * 			<li><code>true</code>: go to dash baord after log out</li>
	 * 			<li><code>false</code>: just logout and initialize service</li>
	 * 		<ul>
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void validateSession(boolean goToDashboard) {
		
		Log.e(TAG_CLASS_PACKAGE, "validate session");
		if (getFacebookService() != null) {
			if (!getFacebookService().isSessionValid()) {			
				
				getFacebookService().logout(this);
				
							
				setFacebookService(new FacebookWebservice());				

				if (goToDashboard) {
					GuiUtils localizedGuiUtils = new GuiUtils(
							getApplicationContext());
						
					localizedGuiUtils.showAlertWhenNoResultsAreAvailable(this,
							"Your sessionhas expired",
							localizedGuiUtils.getOkay(),
							SheelMaayaaActivity.class, null, null);
										
					goToActivity(SheelMaayaaActivity.class);
				}
			}// end if: session has expired -> force logout
		}// end if: avoid null poionter exceptions when app is opened
	}// end validateSession
	
	/**
	 * Class used to listen to the responses of the Database for 
	 * checking whether the logging user exists -or needs to register
	 * 
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 * @author 
	 *		Nada Emad
	 *
	 */
	class CheckUserLoginStatusFromDbListener extends LoginDataBaseListener{
		/**
		 * Element position in the list to know which activity should it divert to
		 */
		int position;

		public CheckUserLoginStatusFromDbListener(IntentFilter filterToAddActionTo, int position){
			super(filterToAddActionTo);		
			this.position = position;
		}// end constructor
		
		public void doActionUserIsRegistered() {
			System.out.println("doActionUserIsRegistered from UserSessionMaintainingActivity ");
//			Toast.makeText(getApplicationContext(), "The user is logging in", Toast.LENGTH_LONG).show();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			goToActivity(NAVIGATION_ITEMS[position].getActivityType());
		}// end doActionUserIsRegistered

		public void doActionUserIsNotRegistered() {
			System.out.println("doActionUserIsNotRegistered from UserSessionMaintainingActivity");	
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_toast), Toast.LENGTH_LONG).show();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			goToActivity(position,NewUserActivity.class);			
		}// end doActionUserIsNotRegistered
		
	}// end CheckUserLoginStatusFromDbListener
	
}// end class
