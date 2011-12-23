package com.sheel.app;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sheel.adapters.MyOffersResultsListAdapter;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.webservices.FacebookWebservice;

/**
 * This activity displays the home page that will be displayed to the user after
 * registering
 * 
 * @author 
 * 		Nada Emad
 * @author
 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
 * 
 */
public class ConnectorUserActionsActivity extends UserSessionStateMaintainingActivity {

	/**
	 * Constant used for tracing purposes "class name (package name)"
	 */
	private final String TAG_CLASS_PACKAGE = "ConnectorUserActionsActivity (com.sheel.app): ";
	public static final String LOGGED_ID_KEY = "LoggedID";
	public static String LoggedID;
	
	TextView LoggedUserField;
	
	/**
	 *  Called when the activity is first created. 
	 *  
	 *  @author 
	 *  	Passant El.Agroudy (passant.elagroudy@gmail.com)
	 *  @author
	 *  	Hossam Amer
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connector_user_action);		
		
		if (getFacebookService() == null){
			Log.e(TAG_CLASS_PACKAGE,"onCreate: facebookservice is not initialized");			
			setFacebookService(new FacebookWebservice());
			//////////////////////
			//getFacebookService().login(this, true, false);
		}// end if : previous activity was welcome(login)

		Bundle extras = getIntent().getExtras(); 
		if (extras != null) LoggedID = extras.getString(LOGGED_ID_KEY);
		
		LoggedUserField = (TextView) findViewById(R.id.userID);
		LoggedUserField.setText("User ID: " + getFacebookService().getFacebookUser().getUserId());
	}// end onCreate

	/**
	 * This is called when the declare button is clicked
	 * 
	 * @param v
	 */

	public void onClick_DeclareOffer(View v) {
	
		Intent intent =setSessionInformationBetweenActivities(InsertOfferActivity.class);
		 
		try{
			long userId = Long.parseLong(LoggedID);
			intent.putExtra("userId", userId);
		}catch(Exception e){
			
		}
		startActivity(intent);
		 
	}// end onClick_DeclareOffer

	/**
	 * This is called when the search button is clicked
	 * 
	 * @param v
	 * 		
	 */

	public void onClick_SearchOffers(View v) {		
		Intent intent =setSessionInformationBetweenActivities(GetUserInfoActivity.class);
		
		try {
		long userId = Long.parseLong(LoggedID);
		intent.putExtra("userId", userId);
		}catch(Exception e) {
			
		}
		startActivity(intent);
	}// end onClick_SearchOffers
	
	/**
	 * This is called when Logout button is clicked
	 * 
	 * @param v
	 * 		clicked button
	 */
	public void onClick_logOut(View v){
		if (getFacebookService() != null)
		getFacebookService().logout(this);
		/* because the session is static, if not set to null, the app
		 * will never login again until it is closed then re-opened*/
		setFacebookService(null);
		startActivity(new Intent(this, SheelMaayaaActivity.class));
	}// end onClick_logOut
	
	public void onClick_checkFacebookSearch(View v){
		
		//getFacebookService().tester_filterOffersFromFriends();
		//getFacebookService().tester_filterOffersFromOwnersWithMutualFriends();
		
		//ArrayList<String> catNames = new ArrayList<String>();
		//catNames.add("declare"); catNames.add("search");
		
		//ArrayList<Integer> catRsc = new ArrayList<Integer>();
		//catRsc.add(R.id.menu_main_declare); catRsc.add(R.id.menu_main_search);
		
//		System.out.println("I am in the correct version");
//		Intent intent = new Intent(getApplicationContext(), SwypingHorizontalViewsActivity.class);
//		startActivity(intent);
		
	//	System.out.println("I am in the correct version");
	//	Intent intent = new Intent(getApplicationContext(), MyOffersActivity.class);
	//	startActivity(intent);
		
		
		Class baseClass = MyOffersResultsListAdapter.class;
		Class[] parametersTypes = new Class[] {Context.class,ArrayList.class};
		try {
			Constructor baseClassConstructor = baseClass.getConstructor(parametersTypes);
			ArrayList<OfferDisplay2> param2 = new ArrayList<OfferDisplay2>();
			param2.add(new OfferDisplay2("owner", 1, "PAssant gdn"));
			Object[] parameters = new Object[] {this,param2};
			try {
				Object o = baseClassConstructor.newInstance(parameters);
				baseClass.cast(o);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			
			Class<?> implementorClass = Class.forName("String");
			String instance = String.class.cast(implementorClass.newInstance());
			
			//Class<?> implementorClass = String.class;
			//String instance = String.class.cast(implementorClass.newInstance());
			
		}catch(ClassNotFoundException e) {
			
		}catch (IllegalAccessException e) {
			
		}catch (InstantiationException e) {
			
		}
		
		
		try {
			
			//Class<?> implementorClass = String.class;
			//String instance = String.class.cast(implementorClass.newInstance());
			
			Class<?> implementorClass = MyOffersResultsListAdapter.class;
			Class[] paramTypes = new Class[] {Context.class,ArrayList.class};
			Constructor baseClassConstructor = implementorClass.getConstructor(paramTypes);
			ArrayList<OfferDisplay2> param2 = new ArrayList<OfferDisplay2>();
			param2.add(new OfferDisplay2("owner", 1, "PAssant gdn"));
			Object[] parameters = new Object[] {this,param2};
			
			//MyOffersResultsListAdapter instance = MyOffersResultsListAdapter.class.cast(implementorClass.newInstance());
			MyOffersResultsListAdapter instance = MyOffersResultsListAdapter.class.cast(baseClassConstructor.newInstance(parameters));
		}catch (IllegalAccessException e) {
			
		}catch (InstantiationException e) {
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}// end onClick_checkFacebookSearch
	
	private void  x (MyOffersResultsListAdapter m) {
		
	}

}
