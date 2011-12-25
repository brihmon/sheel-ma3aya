package com.sheel.app;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.NavigationItem;
import com.sheel.datastructures.enums.SharedValuesBetweenActivities;
import com.sheel.listeners.LoginDataBaseListener;
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
		
	
		// do any logic you want
		
	}// end onCreate
	
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
		
//		if (item.getItemId() == R.id.menu_main_logout) {
//			if (getFacebookService() != null)
//				getFacebookService().logout(this);
//				/* because the session is static, if not set to null, the app
//				 * will never login again until it is closed then re-opened*/
//			setFacebookService(null);
//		}// end if : user wants to logout
//		
		
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
	
	/**
	 * IMPORTANT: Before leaving any activity, you must call this  method to pass
	 * the important details about session and user 
	 *	 
	 * @param typeOfNextActivity
	 * 		Class type of new activity you intend to navigate to. Write activity name
	 * 		then call .getClass() method
	 * 
	 * @return
	 * 		Intent containing data needed for the next activity. Use 
	 * 		{@link Activity#startActivity(Intent)} to start the new activity
	 */
	public Intent setSessionInformationBetweenActivities (Class<?> typeOfNextActivity){

		//return new Intent(this, typeOfNextActivity);
		   Intent mIntent = new Intent(this, typeOfNextActivity);
		   
		   if (fbService != null){
			// Pass variable to detailed view activity using the intent
			   if (fbService.getFacebookUser() != null)
			mIntent.putExtra(SharedValuesBetweenActivities.userFacebookId.name(), fbService.getFacebookUser().getUserId());
			mIntent.putExtra(SharedValuesBetweenActivities.userAccessToken.name(), fbService.getUserAccessToken());
			mIntent.putExtra(SharedValuesBetweenActivities.accessTokenExpiry.name(), fbService.getUserAccessTokenExpiryTime());
		   }
		   
			return mIntent;
	}// end SetSessionInformationBetweenActivities
	
	
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

		if (!getFacebookService().getFacebookUser()
				.isRequestedBeforeSuccessfully()) {
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
			Toast.makeText(getApplicationContext(), "The user is logging in", Toast.LENGTH_LONG).show();
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
