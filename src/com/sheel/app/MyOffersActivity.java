package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.CONFIRMED;
import static com.sheel.utils.SheelMaayaaConstants.HALF_CONFIRMED_ME_CONFIRMED_USER_NOT_OFFER_OWNER;
import static com.sheel.utils.SheelMaayaaConstants.HALF_CONFIRMED_ME_OFFER_OWNER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;
import static com.sheel.utils.SheelMaayaaConstants.NOT_CONFIRMED;
import static com.sheel.utils.SheelMaayaaConstants.*;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sheel.datastructures.Category;
import com.sheel.datastructures.Confirmation;
import com.sheel.datastructures.Flight;
import com.sheel.datastructures.Offer;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.User;
import com.sheel.listeners.InflateListener;
import com.sheel.utils.GuiUtils;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.InternetManager;

/**
 * This activity is used for displaying and interacting with
 * the offers of the logged-in user.
 * 
 * @author Hossam_Amer
 *
 */

public class MyOffersActivity extends SwypingHorizontalViewsActivity 
{
	private static final String TAG = MyOffersActivity.class.getName();
	
	
	/**
	 * Path for the database query
	 */
	String path;
	
	/**
	 * Filter added for this activity to filter the actions received by the receiver
	 */
	IntentFilter filter;
	
	/**
	 *  The receiver used for detecting the HTTP data arrival 
	 */
	private SheelMaayaaBroadCastRec receiver;
	
	/**
	 * Offers not-confirmed retrieved from the database.
	 */
	ArrayList<OfferDisplay2> searchResults_new = new ArrayList<OfferDisplay2>();
	
	/**
	 * Offers half-confirmed that the logged in user is not the offer owner.
	 */
	ArrayList<OfferDisplay2> searchResults_HalfConfirmedByMeNotDeclaredByMe = new ArrayList<OfferDisplay2>();
	
	
	/**
	 * Offers half-confirmed declared and half-confirmed by the user retrieved from the database.
	 */
	ArrayList<OfferDisplay2> searchResults_half = new ArrayList<OfferDisplay2>();
	
	/**
	 * Offers full-confirmed retrieved from the database.
	 */
	ArrayList<OfferDisplay2> searchResults_full = new ArrayList<OfferDisplay2>();
		
	/**
	 * Dialog for displaying the loading pop-up for the user
	 */
	ProgressDialog dialog;
	
	/**
	 * Airports list used for localization
	 */
	
	String[] airportsList;
	
	/**
	 * Nationalities list used for localization
	 */
	
	String[] nationalitiesList;
		
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
	//======================Checking Available Internet Connection=============	
		
	if(InternetManager.isInternetOn(getApplicationContext()))
	{
		
		//========Get the last state for my list========
		
		if((ArrayList<Category>) super.getLastNonConfigurationInstance() == null)
		{
							
			// Create a new list
			searchResults_half = new ArrayList<OfferDisplay2>();
			searchResults_full = new ArrayList<OfferDisplay2>();
			searchResults_HalfConfirmedByMeNotDeclaredByMe = new ArrayList<OfferDisplay2>();
			searchResults_new = new ArrayList<OfferDisplay2>();
			
			//======Start the HTTP Request=========
			path = "/getmyoffers/" + getFacebookService().getFacebookUser().getUserId();
			
			//	<<<<<Some Test Paths>>>>>>>>			
//			path = "/getmyoffers/626740322";
//			path = "/getmyoffers/673780564";

			dialog = ProgressDialog.show(MyOffersActivity.this, "", "Getting your Offers, Please wait..", true, false);
			HTTPManager.startHttpService(path, HTTP_GET_MY_OFFERS_FILTER, getApplicationContext());
		}
		
	}// end if (isInternet)
	else		
		noInternetConnectionHandler();
	
		
	}//end OnCreate
	
	/**
	 * Internet connection handler handles when there is not Internet connection. 
	 */

	private void noInternetConnectionHandler() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Internet Connection is not available")
		       .setCancelable(false)
		       .setPositiveButton("ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       
		       });
		 builder.create();
		 builder.show();
		
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
		
		// Cancel out the dialog
		dialog = null;
		// Unregister the receiver onPause
		unregisterReceiver(receiver);
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
		
		filter = new IntentFilter();
		
		// Add the filters of your activity
		filter.addAction(HTTP_GET_MY_OFFERS_FILTER);
		filter.addAction(HTTP_CONFIRM_OFFER);
		
		receiver = new SheelMaayaaBroadCastRec();
		
		Log.e(TAG, "Receiver Registered");
		registerReceiver(receiver, filter);
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
		   
	   /**
	    * {@link SheelMaayaaBroadCastRec} Class for Broadcast receiver i.e to receive the result from the HTTP request
	    * @author Hossam_Amer
	    *
	    */
	   
		class SheelMaayaaBroadCastRec extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
			
				
				Log.e(TAG, intent.getAction());
				String action = intent.getAction();
			
				int httpStatus = intent.getExtras().getInt(HTTP_STATUS);
				Log.e(TAG, "HTTPSTATUS: "+ httpStatus);
				
				String responseStr;
				if( httpStatus == HttpStatus.SC_OK)
				{
					
					if (action.equals(HTTP_GET_MY_OFFERS_FILTER))
					{
						responseStr = intent.getExtras().getString(HTTP_RESPONSE);
						Log.e(TAG, "Will Start to deal/load with my offers");
						loadSearchResultsOnUI(responseStr);
						 
						// Dialog dismissing
						if(dialog != null) dialog.dismiss();
						Log.e(TAG, responseStr);
							
					}// end if Get my offers filter
					else if(action.equals(HTTP_CONFIRM_OFFER))
					{
						updateOffersOnUI();
					}
				}//end onReceive(Context context, Intent intent)
			}//end onReceive
			
		}// end class SheelMaayaaBroadCastRec			

			/**
			 * Updates the UI with the offers confirmed by the database.
			 * It fetches the offers that are either one from 4 categories:
			 * - New offers I declared
			 * - Half confirmed offers I declared (I am Offer owner)
			 * - Half confirmed offers I confirmed but not declared by me
			 * 	(I am not offer owner)
			 * 
			 * @param responseStr 
			 * 				The response string retrieved from the server
			 * @author Hossam_Amer
			 */
			private void loadSearchResultsOnUI(String responseStr) 
			{
				try {
					
					JSONArray jsonArray = new JSONArray(responseStr);
					airportsList = getResources().getStringArray(R.array.airports_array);
					nationalitiesList = getResources().getStringArray(R.array.nationalities_array);

//					Log.e("JSON ARRAY MISLEADINg: ", jsonArray.getJSONObject(jsonArray.length()-1) + "");
					
               	 for (int i = 0; i < jsonArray.length(); i++) {               		 
               		 
               		Log.e("loadSearchResultsOnUI: Inisde the loop of my offers: offerDisplay2 ", jsonArray.getJSONObject(i) + "");
               		
               		 OfferDisplay2 offer = OfferDisplay2.mapOffer(jsonArray.getJSONObject(i), 
               				 airportsList,  nationalitiesList);
               		 
               		 Log.e("loadSearchResultsOnUI: Inisde the loop of my offers: offer ", offer + "");
               		 
                      if(offer.getOffer().offerStatus.equals(Confirmation.half_confirmed_offerOwner))
               			searchResults_half.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i), airportsList, 
               					nationalitiesList));
                 	else if(offer.getOffer().offerStatus.equals(Confirmation.half_confirmed_other))
                 		searchResults_HalfConfirmedByMeNotDeclaredByMe.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i), airportsList,
                   				nationalitiesList)); 
               		else if(offer.getOffer().offerStatus.equals(Confirmation.confirmed))
               			searchResults_full.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i), airportsList,
               					nationalitiesList));
               		else if(offer.getOffer().offerStatus.equals(Confirmation.not_confirmed))
               			searchResults_new.add(OfferDisplay2.mapOffer(jsonArray.getJSONObject(i), airportsList,
               					nationalitiesList));
               		 
               	 }// end for
              
               	 // Update the categories in Swype Activity
               	 updateCategoriesInSwypeActivity();
               	 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//end catch
				
			}//end loadSearchResultsOnUI
			
			/**
			 * After fetching the data from the server, it creates the categories depending on the input data.
			 * Gives alert if there are no data, and lets you go into InsertOffer activity
			 */

			private void updateCategoriesInSwypeActivity() {
               
				if(searchResults_full.isEmpty() && searchResults_half.isEmpty() && 
						searchResults_HalfConfirmedByMeNotDeclaredByMe.isEmpty() && searchResults_new.isEmpty())
        			GuiUtils.showAlertWhenNoResultsAreAvailable(
             				this, 
             				"You do not have any offers yet! ", 
             				"Declare new offer", InsertOfferActivity.class, 
             				"Change filters", FilterPreferencesActivity.class);
               	else 
               		{
               			int index = 0;
               			String [] swpeCats = getResources().getStringArray(R.array._swyperCats);
               		
               			//=============================HALF CONFIRMED NOT DECLARED ME========================================	
               			
               			//   * - Half confirmed offers I confirmed but not declared by me (I am not offer owner)
               			if(!searchResults_HalfConfirmedByMeNotDeclaredByMe.isEmpty())
               			{
               				Log.e("Display Name in My offers DCBME: ", "" + swpeCats[0]);
               				getCategories().add(new Category("" + swpeCats[0], HALF_CONFIRMED_ME_CONFIRMED_USER_NOT_OFFER_OWNER, R.layout.my_offers_main));
               				updateCategoryContent(searchResults_HalfConfirmedByMeNotDeclaredByMe, index++, false);		
               			}// end if(!searchResults_HalfConfirmedByMeNotDeclaredByMe.isEmpty())

               		
               			//==================================HALF CONFIRMED BY ME======================================
	               			
	           			 // * - Half confirmed offers I confirmed but not declared by me
	           			 if(!searchResults_half.isEmpty())
	                  	{
	           				Log.e("Display Name in My offers DCBME: ", "" + swpeCats[1]);
	           				getCategories().add(new Category("" + swpeCats[1], HALF_CONFIRMED_ME_OFFER_OWNER, R.layout.my_offers_main)); 
	                       	   updateCategoryContent(searchResults_half, index++, false);	
	                  	}// end if(!searchResults_half.isEmpty())
	           		
	           			 //=====================================NOT CONFIRMED===================================
        			 	// * - New offers I declared
            			if(!searchResults_new.isEmpty())
            			{
            				Log.e("Display Name in My offers DCBME: ", "" + swpeCats[2]);
            				getCategories().add(new Category("" + swpeCats[2], NOT_CONFIRMED, R.layout.my_offers_main));
            				updateCategoryContent(searchResults_new, index++, false);		
            			}// end if(!searchResults_new.isEmpty())
            			//========================================FULL CONFIRMED================================
           			 
           			 //  * - Confirmed offers
               			if(!searchResults_full.isEmpty())
               			{
               				Log.e("Display Name in My offers DCBME: ", "" + swpeCats[3]);
               				getCategories().add(new Category("" + swpeCats[3], CONFIRMED, R.layout.my_offers_main));
                			updateCategoryContent(searchResults_full, index, false);	
               			}//end if(!searchResults_full.isEmpty())		
               			
               		//============================================================================================
               		
               		}//end else
               	 

			}//end updateCategoriesInSwypeActivity()
			
			
			/**
			 * Updates the UI with the offers confirmed by the user.
			 * It changes the status of the offer and reflect the change on the UI.
			 * We have four categories:
			 * - New offers I declared
			 * - Half confirmed offers I declared (I am Offer owner)
			 * - Half confirmed offers I confirmed but not declared by me
			 * 	(I am not offer owner)
			 * @param responseStr The response string retrieved from the server
			 * @author Hossam_Amer
			 */
			private void updateOffersOnUI() {				
				
				super.updateOffersOnUI(super.reponseStr);
				Log.e("Update on Response UI SUPER CALLED IN CHILD : ", super.reponseStr);
				try {
					
					
					JSONObject confirmationJSON = new JSONObject(super.reponseStr);
					Log.e("hashas confirmationJSON: ", confirmationJSON + "");
					
					Confirmation confirmation = Confirmation.mapConfirmation(confirmationJSON);
					Log.e("hashas confirmation", confirmation + "");
					
					
					
					if(confirmation.isStatusTransactionUser1() && confirmation.isStatusTransactionUser2())
					{
						//============Delete offer from UI=========
						
						
//						Log.e(TAG, "Dismissing Dialog : " + dialogConfirm);
//						if(dialogConfirm != null) dialogConfirm.dismiss();
						Toast.makeText(getApplicationContext(), getString(R.string._hossamConfirmedByTwoUsers), Toast.LENGTH_SHORT).show();
						Toast.makeText(getApplicationContext(), getFacebookService().getFacebookUser().getFirstName() + ", " + 
								getString(R.string._hossamConfirmationMail), Toast.LENGTH_SHORT).show();
						
					if(getFacebookService().getFacebookUser().getUserId().equals(confirmation.getUser1().getFacebookId()))	
							InflateListener.sendSMS( 
										getSMSContentForConfirmation
											(confirmation.getOffer().userStatus, 
											confirmation.getUser1(), 
											confirmation.getUser2(), 
											confirmation.getOffer(),
											confirmation.getFlight()),
									confirmation.getUser2().mobileNumber,
									MyOffersActivity.this);
						
					else
						InflateListener.sendSMS( 
								getSMSContentForConfirmation
									(confirmation.getOffer().userStatus, 
									confirmation.getUser2(), 
									confirmation.getUser1(), 
									confirmation.getOffer(),
									confirmation.getFlight()),
							confirmation.getUser1().mobileNumber,
							MyOffersActivity.this);

						
					}
					else if(confirmation.isStatusTransactionUser1())
					{
						
						Toast.makeText(getApplicationContext(), "Hello offer Owner, you have confirmed this offer", Toast.LENGTH_SHORT).show();
						
					}
					else if(confirmation.isStatusTransactionUser2())
					{
						Toast.makeText(getApplicationContext(), "Hello offer other, you have confirmed this offer", Toast.LENGTH_SHORT).show();
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}// updateConfirmedOffersOnUI(String responseStr)


			 private String  getSMSContentForConfirmation(int offerStatus, User userSrc, User userDes, Offer offer, Flight flight) {
					
			        String msgBodyOffer  = "";
			        double price = offer.noOfKilograms*offer.pricePerKilogram;
			        
			        // More weight
			       if(offerStatus == 1)	 
			    	   msgBodyOffer = "Hello " + userDes.firstName + ", \n\n"
			    	   			+ "I am sending you an auto confirmation from Sheel M3aya app describing details of my transaction.\n\n"
			    	   			+ "I have requested " +  offer.noOfKilograms + " kilograms with " +  
			    	   			  price +  " euros on flight " + flight.getFlightNumber() + ", date: " + flight.getDepartureDate() +  ".\n\n" 
			    	   			+ "Have a nice flight :-),\n "+ userSrc.firstName;
			    	   
			       else
			    	   msgBodyOffer = "Hello " + userDes.firstName + ", \n\n"
						+ "This is an auto confirmation from Sheel M3aya app describing details of your transaction.\n\n"
						+ "I have offered " +  offer.noOfKilograms + " kilograms with " + 
						 price +  " euros on flight " + flight.getFlightNumber() + ", date: " + flight.getDepartureDate() +  ".\n\n"  
						+ "Have a nice flight :-),\n "+ userSrc.firstName;
			    		  
			    	
			              
			       return msgBodyOffer;
				}
		

}//end MyOffersActivity
