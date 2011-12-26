package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.CONFIRMED_BY_ME_OFFER_OWNER;
import static com.sheel.utils.SheelMaayaaConstants.CONFIRMED_BY_OTHER_OFFER_OWNER;
import static com.sheel.utils.SheelMaayaaConstants.HALF_CONFIRMED_ME_CONFIRMED_USER_NOT_OFFER_OWNER;
import static com.sheel.utils.SheelMaayaaConstants.HALF_CONFIRMED_ME_OFFER_OWNER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_CONFIRM_OFFER_UI;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_EDIT_OFFER;
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
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.sheel.adapters.MyOffersResultsListAdapter;
import com.sheel.datastructures.Category;
import com.sheel.datastructures.Confirmation;
import com.sheel.datastructures.Flight;
import com.sheel.datastructures.Offer;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.User;
import com.sheel.listeners.InflateListener;
import com.sheel.listeners.MyOffersInflateListener;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.InternetManager;

/**
 * This activity is used for displaying and interacting with the offers of the
 * logged-in user.
 * 
 * @author Hossam_Amer
 * 
 */

public class MyOffersActivity extends SwypingHorizontalViewsActivity {
	private static final String TAG = MyOffersActivity.class.getName();

	/**
	 * Path for the database query
	 */
	String path;

	/**
	 * Filter added for this activity to filter the actions received by the
	 * receiver
	 */
	IntentFilter filter;

	/**
	 * The receiver used for detecting the HTTP data arrival
	 */
	private SheelMaayaaBroadCastRec receiver;

	/**
	 * Offers not-confirmed retrieved from the database.
	 */
	ArrayList<OfferDisplay2> searchResults_new = new ArrayList<OfferDisplay2>();

	/**
	 * Offers full-confirmed retrieved from the database but I am the offer
	 * owner.
	 */
	ArrayList<OfferDisplay2> searchResults_fullIDeclared = new ArrayList<OfferDisplay2>();

	/**
	 * Offers full-confirmed retrieved from the database but I am not the offer
	 * owner.
	 */
	ArrayList<OfferDisplay2> searchResults_fullConfirmedByMeNotDeclaredByMe = new ArrayList<OfferDisplay2>();

	/**
	 * Offers HALF CONFIRMED + I HALF CONFIRMED + OTHERS DECLARED
	 */
	ArrayList<OfferDisplay2> searchResults_HALF_DECLARED_ME_CONFIRMED_OTHER= new ArrayList<OfferDisplay2>();

	/**
	 * Offers HALF CONFIRMED + I HALF CONFIRMED + ME DECLARED
	 */
	ArrayList<OfferDisplay2> searchResults_HALF_DECLARED_OTHER_CONFIRMED_ME = new ArrayList<OfferDisplay2>();

	/**
	 * Offers HALF CONFIMED + I DID NOT HALF CONFIRMED + ME DECLARED + OTHERS +
	 * CONFIRMED
	 */
	ArrayList<OfferDisplay2> searchResults_nothalfConfirmedMeMeDeclaredOthersConfirmed = new ArrayList<OfferDisplay2>();

	/**
	 * Dialog for displaying the loading pop-up for the user
	 */
	public ProgressDialog dialog;

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

		// ======================Checking Available Internet
		// Connection=============

		if (InternetManager.isInternetOn(getApplicationContext())) {

			// ========Get the last state for my list========

			if ((ArrayList<Category>) super.getLastNonConfigurationInstance() == null) {

				// Create a new list
				searchResults_fullIDeclared = new ArrayList<OfferDisplay2>();
				searchResults_new = new ArrayList<OfferDisplay2>();
				searchResults_fullConfirmedByMeNotDeclaredByMe = new ArrayList<OfferDisplay2>();
				searchResults_HALF_DECLARED_ME_CONFIRMED_OTHER = new ArrayList<OfferDisplay2>();
				searchResults_HALF_DECLARED_OTHER_CONFIRMED_ME = new ArrayList<OfferDisplay2>();
				searchResults_nothalfConfirmedMeMeDeclaredOthersConfirmed = new ArrayList<OfferDisplay2>();

				// ======Start the HTTP Request=========
				path = "/getmyoffers/"
						+ getFacebookService().getFacebookUser().getUserId();

				// <<<<<Some Test Paths>>>>>>>>
				// path = "/getmyoffers/626740322";
				// path = "/getmyoffers/673780564";

				dialog = ProgressDialog.show(MyOffersActivity.this, "",
						swypeCatsGuiUtils.dialogFetchMyOffers, true, false);
				HTTPManager.startHttpService(path, HTTP_GET_MY_OFFERS_FILTER,
						getApplicationContext());
			}

		}// end if (isInternet)
		else
			noInternetConnectionHandler();

	}// end OnCreate

	/**
	 * Internet connection handler handles when there is not Internet
	 * connection.
	 */

	private void noInternetConnectionHandler() {
		this.swypeCatsGuiUtils.showAlertWhenNoResultsAreAvailable(this,
				swypeCatsGuiUtils.dialogInternetConnection,
				swypeCatsGuiUtils.okay, SheelMaayaaActivity.class, "",
				FilterPreferencesActivity.class);
	}

	/**
	 * Used to specify the class used as a list adapter for the displayed lists
	 * in the swiper. {@link MyOffersResultsListAdapter} is used
	 * 
	 * @author Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class getClassOfListAdapter() {
		return MyOffersResultsListAdapter.class;
	}// end getClassOfListAdapter

	/**
	 * Used to specify the class used as an inflate listener for the accordian
	 * (found in each row for showing more details).
	 * {@link MyOffersInflateListener} is used
	 * 
	 * @author Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class getClassOfInflateListener() {
		return MyOffersInflateListener.class;
	}// end getClassOfInflateListener

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
		filter.addAction(HTTP_CONFIRM_OFFER_UI);
		filter.addAction(HTTP_EDIT_OFFER);

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
	 * {@link SheelMaayaaBroadCastRec} Class for Broadcast receiver i.e to
	 * receive the result from the HTTP request
	 * 
	 * @author Hossam_Amer
	 * 
	 */

	class SheelMaayaaBroadCastRec extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			Log.e(TAG, intent.getAction());
			String action = intent.getAction();

			// If I need to update the UI of the confirm offer, go update it
			// without any further check.
			if (action.equals(HTTP_CONFIRM_OFFER_UI)) {
				String res = intent.getExtras().getString("response");
				Log.e("I am Child Broad Cast receiver taking the UI update",
						"updateOffersOnUI()");
				updateOffersOnUI(res);
				Log.e("I am Child Broad Cast receiver finishing the UI update",
						"updateOffersOnUI()");

			} else {
				int httpStatus = intent.getExtras().getInt(HTTP_STATUS);
				Log.e(TAG, "HTTPSTATUS: " + httpStatus);

				String responseStr;
				if (httpStatus == HttpStatus.SC_OK) {

					if (action.equals(HTTP_GET_MY_OFFERS_FILTER)) {
						responseStr = intent.getExtras().getString(
								HTTP_RESPONSE);
						Log.e(TAG, "Will Start to deal/load with my offers");
						loadSearchResultsOnUI(responseStr);

						// Dialog dismissing
						if (dialog != null)
							dialog.dismiss();
						Log.e(TAG, responseStr);

					}// end if Get my offers filter

					if (action.equals(HTTP_EDIT_OFFER)) {
						responseStr = intent.getExtras().getString(
								HTTP_RESPONSE);
						Log.e(TAG, "Will Start to edit offers");
						// showEditResult(responseStr);
						// Dialog dismissing
						if (dialog != null)
							dialog.dismiss();

						showEditResult(responseStr);
						Log.e(TAG, responseStr);

					}// end if Get my offers filter

				}
			}// end if (httpStatus == HttpStatus.SC_OK) {

		}// end onReceive

	}// end class SheelMaayaaBroadCastRec

	private void showEditResult(String rsp) {
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).create();

		if (rsp.equals("OK")) {
			alertDialog.setTitle(getResources().getString(R.string.Success));
			alertDialog.setMessage(getResources().getString(
					R.string.Changes_saved_successfully));
		} else {
			alertDialog.setTitle(getResources().getString(R.string.Sorry));
			alertDialog.setMessage(getResources().getString(
					R.string.Cannot_save_changes));
		}
		alertDialog.show();
	}

	/**
	 * Updates the UI with the offers confirmed by the database. It fetches the
	 * offers that are either one from 4 categories: - New offers I declared -
	 * Half confirmed offers I declared (I am Offer owner) - Half confirmed
	 * offers I confirmed but not declared by me (I am not offer owner)
	 * 
	 * @param responseStr
	 *            The response string retrieved from the server
	 * @author Hossam_Amer
	 */
	private void loadSearchResultsOnUI(String responseStr) {
		try {

			JSONArray jsonArray = new JSONArray(responseStr);
			airportsList = getResources()
					.getStringArray(R.array.airports_array);
			nationalitiesList = getResources().getStringArray(
					R.array.nationalities_array);

			// Log.e("JSON ARRAY MISLEADINg: ",
			// jsonArray.getJSONObject(jsonArray.length()-1) + "");

			for (int i = 0; i < jsonArray.length(); i++) {


				OfferDisplay2 offer = OfferDisplay2.mapOfferNew(jsonArray
						.getJSONObject(i), airportsList, nationalitiesList);

				try {

					/**
					 * searchResults_new > Provider for new offers
					 * searchResults_fullIDeclared >Provider for offers I confirmed and I am the offer owner
					 * searchResults_fullConfirmedByMeNotDeclaredByMe > Provider for offers I confirmed and I NOT am the offer owner
					 * searchResults_halfConfirmedMeDeclaredOthers > Provider for offers I half confirmed and I am NOT the offer owner 
					 * searchResults_DeclaredMeHalfConfirmedMe > Provider for offers I am the offer owner, and I confirmed.
					 * 
					 */

					// To check if the offer is not confirmed and I am offer
					// owner
					if (offer.getOffer().offerStatus
							.equals(Confirmation.not_confirmed)) 
					{
						Log.e("Offers new ID: ", ""+ offer.getOffer().id);
						
						searchResults_new.add(OfferDisplay2.mapOfferNew(
								jsonArray.getJSONObject(i), airportsList,
								nationalitiesList));
					}

					else if (offer.getOffer().offerStatus
							.equals(Confirmation.confirmed)) 
					{
						Log.e("Offers confirmed ID: ", ""+offer.getOffer().id);
						if (getFacebookService().getFacebookUser().getUserId()
								.equals(offer.getUser().getFacebookId()))
							searchResults_fullIDeclared.add(OfferDisplay2
									.mapOfferNew(jsonArray.getJSONObject(i),
											airportsList, nationalitiesList));
						else
							searchResults_fullConfirmedByMeNotDeclaredByMe
									.add(OfferDisplay2.mapOfferNew(jsonArray
											.getJSONObject(i), airportsList,
											nationalitiesList));
					}
					
				else
				{
					Log.e("Offers half confirmed ID: ", ""+ offer.getOffer().id);
						
						// Me Declared others confirmed						
						if((offer.getOffer().offerStatus.equals(Confirmation.half_confirmed_other)))
						{
								
						if(getFacebookService().getFacebookUser().getUserId()
								.equals(offer.getUser().getFacebookId()))
						{
							
							Log.e("searchResults_HALF_DECLARED_ME_CONFIRMED_OTHER: ", ""+ offer.getOffer().id);

							
							searchResults_HALF_DECLARED_ME_CONFIRMED_OTHER
							.add(OfferDisplay2.mapOfferNew(jsonArray
									.getJSONObject(i), airportsList,
									nationalitiesList));
						}
							
					}
						else
						{
							Log.e("searchResults_HALF_DECLARED_OTHER_CONFIRMED_ME: ", ""+ offer.getOffer().id);
							searchResults_HALF_DECLARED_OTHER_CONFIRMED_ME
							.add(OfferDisplay2.mapOfferNew(jsonArray
									.getJSONObject(i), airportsList,
									nationalitiesList));
					
						}
						
						
							
				}
					

				} catch (Exception e) {

					Log.e(TAG, "Nasty offer");
				}
			}// end for

			// Update the categories in Swype Activity
			updateCategoriesInSwypeActivity();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// end catch

	}// end loadSearchResultsOnUI
	
	
	/**
	 * After fetching the data from the server, it creates the categories
	 * depending on the input data. Gives alert if there are no data, and lets
	 * you go into InsertOffer activity
	 * 
	 * @author Hossam_Amer
	 */
	private void updateCategoriesInSwypeActivity() {

		/**
		 * searchResults_new > Provider for new offers searchResults_full >
		 * Provider for offers I confirmed and I am the offer owner
		 * searchResults_fullConfirmedByMeNotDeclaredByMe > Provider for offers
		 * I confirmed and I NOT am the offer owner searchResults_half >
		 * Provider for offers I half confirmed and I am the offer owner
		 * searchResults_HalfConfirmedByMeNotDeclaredByMe > Provider for offers
		 * I half confirmed and I am NOT the offer owner
		 * searchResults_halfConfirmedMeDeclaredOthersConfirmed > Provider for
		 * offers I am the offer owner, and others confirmed.
		 */
		if (searchResults_fullConfirmedByMeNotDeclaredByMe.isEmpty()
				&&
				searchResults_fullIDeclared.isEmpty()
				&&
				searchResults_HALF_DECLARED_ME_CONFIRMED_OTHER.isEmpty()
				&& searchResults_new.isEmpty()
				&&
				searchResults_HALF_DECLARED_OTHER_CONFIRMED_ME
						.isEmpty())

			super.swypeCatsGuiUtils.showAlertWhenNoResultsAreAvailable(this,
					"You do not have any offers yet! ", "Declare new offer",
					InsertOfferActivity.class, "Change filters",
					FilterPreferencesActivity.class);
		else {
			int index = 0;

			// =====================================NOT
			// CONFIRMED===================================

			// * - New offers I declared
			if (!searchResults_new.isEmpty()) {
				Log.e("Display Name in My offers DCBME: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[2]);
				getCategories().add(
						new Category("" + swypeCatsGuiUtils.getSwpeCats()[2],
								NOT_CONFIRMED, R.layout.my_offers_main));
				updateCategoryContent(searchResults_new, index++, false);
			}// end if(!searchResults_new.isEmpty())

			// =============================Others declared ME
			// confirmed========================================
			//

			/**
			 * Offers HALF CONFIRMED + I HALF CONFIRMED + OTHERS DECLARED
			 */

			// * - Half confirmed offers I confirmed but not declared by me (I
			// am not offer owner)
			if (!searchResults_HALF_DECLARED_ME_CONFIRMED_OTHER.isEmpty()) {
				Log.e("Display Name in My offers DCBME: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[8]);

				getCategories()
						.add(new Category(
										"" + swypeCatsGuiUtils.getSwpeCats()[8],
										HALF_DECLARE_ME_CONFIRM_ME,
										com.sheel.app.R.layout.my_offers_main));
				updateCategoryContent(
						searchResults_HALF_DECLARED_ME_CONFIRMED_OTHER, index++,
						false);
			}// end
			// if(!searchResults_HalfConfirmedByMeNotDeclaredByMe.isEmpty())

			/**
			 * Offers HALF CONFIMED + I DID NOT HALF CONFIRMED + ME DECLARED +
			 * OTHERS + CONFIRMED
			 */


			// * - Half confirmed offers I confirmed but not declared by me
			if (!searchResults_HALF_DECLARED_OTHER_CONFIRMED_ME
					.isEmpty()) {
				Log.e("Display Name in My offers DCBME: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[0]);
				getCategories().add(
						new Category("" + swypeCatsGuiUtils.getSwpeCats()[8],
								HALF_DECLARE_OTHER_CONFIRM_ME,
								R.layout.my_offers_main));
				updateCategoryContent(
						searchResults_HALF_DECLARED_OTHER_CONFIRMED_ME,
						index++, false);
			}// end if(!searchResults_half.isEmpty())
			// ========================================FULL CONFIRMED DECLARED
			// BY ME================================
			//

			// * - Confirmed offers, I am the offer owner
			if (!searchResults_fullIDeclared.isEmpty()) {
				Log.e("Display Name in My offers DCBME: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[3]);
				getCategories().add(
						new Category("" + swypeCatsGuiUtils.getSwpeCats()[3],
								FULL_DECLARE_ME,
								R.layout.my_offers_main));
				updateCategoryContent(searchResults_fullIDeclared, index, false);
			}// end if(!searchResults_full.isEmpty())

			// ========================================FULL CONFIRMED DECLARED
			// BY OTHER================================
			//

			// * - Confirmed offers, I am not the offer owner
			if (!searchResults_fullConfirmedByMeNotDeclaredByMe.isEmpty()) {
				Log.e("Display Name in My offers DCBOTHER: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[7]);
				getCategories().add(
						new Category("" + swypeCatsGuiUtils.getSwpeCats()[7],
								FULL_DECLARE_OTHER,
								R.layout.my_offers_main));
				updateCategoryContent(
						searchResults_fullConfirmedByMeNotDeclaredByMe, index,
						false);
			}// end if(!searchResults_full.isEmpty())

			// =====================================================
			// =======================================

		}// end else

	}// end updateCategoriesInSwypeActivity()

	/**
	 * Updates the UI with the offers confirmed by the user. It changes the
	 * status of the offer and reflect the change on the UI. We have four
	 * categories: - New offers I declared - Half confirmed offers I declared (I
	 * am Offer owner) - Half confirmed offers I confirmed but not declared by
	 * me (I am not offer owner)
	 * 
	 * @param responseStr
	 *            The response string retrieved from the server
	 * @author Hossam_Amer
	 * @author Passant El.Agroudy
	 */
	private void updateOffersOnUI(String response) {
		
		Log.e("Hello from updateOffers upon confirmation", "Conf");
		super.reponseStr = response;
		// super.updateOffersOnUI(super.reponseStr);
		Log.e("Update on Response UI SUPER CALLED IN CHILD : ",
				super.reponseStr);
		try {
			
			JSONObject confirmationJSON = new JSONObject(super.reponseStr);

			Confirmation confirmation = Confirmation
					.mapConfirmation(confirmationJSON);
				OfferDisplay2 offer = new OfferDisplay2(confirmation.offerOwner, confirmation.getFlight(), confirmation.getOffer());
				
				Log.e("hashas offer display", offer + "");
				
				String currentCategoryLogicName = getCategory(offer, getFacebookService().getFacebookUser().getUserId());
				
				if(currentCategoryLogicName.equals(FULL_DECLARE_OTHER)
						|| currentCategoryLogicName.equals(HALF_DECLARE_ME_CONFIRM_ME))
				{
					offer.userOther = confirmation.getUser2();
				}

				String currentCategoryDisplayName = getCategoryDisplayName(currentCategoryLogicName);
				addOfferInCategory(currentCategoryLogicName, offer, currentCategoryDisplayName);
					
				if(offer.getOffer().offerStatus.equals(Confirmation.confirmed))
				{
                    // If User 1 is the Offer owner
                    if (getFacebookService().getFacebookUser().getUserId().equals(
                                    confirmation.getUser1().getFacebookId()))
                            InflateListener.sendSMS(getSMSContentForConfirmation(
                                            confirmation.getOffer().userStatus, confirmation
                                                            .getUser1(), confirmation.getUser2(),
                                            confirmation.getOffer(), confirmation.getFlight()),
                                            confirmation.getUser2().mobileNumber,
                                            MyOffersActivity.this);

                    else
                            InflateListener.sendSMS(getSMSContentForConfirmation(
                                            confirmation.getOffer().userStatus, confirmation
                                                            .getUser2(), confirmation.getUser1(),
                                            confirmation.getOffer(), confirmation.getFlight()),
                                            confirmation.getUser1().mobileNumber,
                                            MyOffersActivity.this);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// updateConfirmedOffersOnUI(String responseStr)

	private String getSMSContentForConfirmation(int offerStatus, User userSrc,
			User userDes, Offer offer, Flight flight) {

		String msgBodyOffer = "";
		double price = offer.noOfKilograms * offer.pricePerKilogram;

		// More weight
		if (offerStatus == 1)
			msgBodyOffer = "Hello "
					+ userDes.firstName
					+ ", \n\n"
					+ "I am sending you an auto confirmation from Sheel M3aya app describing details of my transaction.\n\n"
					+ "I have requested " + offer.noOfKilograms
					+ " kilograms with " + price + " euros on flight "
					+ flight.getFlightNumber() + ", date: "
					+ flight.getDepartureDate() + ".\n\n"
					+ "Have a nice flight :-),\n " + userSrc.firstName;

		else
			msgBodyOffer = "Hello "
					+ userDes.firstName
					+ ", \n\n"
					+ "This is an auto confirmation from Sheel M3aya app describing details of your transaction.\n\n"
					+ "I have offered " + offer.noOfKilograms
					+ " kilograms with " + price + " euros on flight "
					+ flight.getFlightNumber() + ", date: "
					+ flight.getDepartureDate() + ".\n\n"
					+ "Have a nice flight :-),\n " + userSrc.firstName;

		return msgBodyOffer;
	}
	
	
	/**
	 * Gets the category logic name depending on the check
	 * @param offer offer fetched from the DB
	 * @param facebookID facebook id of the logged in uers
	 * @return
	 * 			category name
	 * @author Hossam_Amer
	 * 
	 */
	public String getCategory(OfferDisplay2 offer, String facebookID)
	{
		
		if (offer.getOffer().offerStatus
				.equals(Confirmation.not_confirmed)) 
		{
			
			return NOT_CONFIRMED;
		}
		
		else if (offer.getOffer().offerStatus
				.equals(Confirmation.confirmed)) 
		{
			if (facebookID.equals(offer.getUser().getFacebookId()))
				return FULL_DECLARE_ME;
			else
				return FULL_DECLARE_ME;
				

		}
		else
		{
			
			if((offer.getOffer().offerStatus.equals(Confirmation.half_confirmed_other)))
			{
					
				if(getFacebookService().getFacebookUser().getUserId()
						.equals(offer.getUser().getFacebookId()))
				{	
					
					return HALF_DECLARE_ME_CONFIRM_ME; 
				}
				
				else 
				{
					if(!getFacebookService().getFacebookUser().getUserId()
							.equals(offer.getUser().getFacebookId()))
					{	
												
						return HALF_DECLARE_OTHER_CONFIRM_ME;
					}
				}
			
			}
		}	
		
		return "";
		
		
	}// end getCategory method
	
	
	/**
	 * Gets the category display name
	 * @param currentCategoryLogicName Current category name in logic
	 * @return	
	 * 		display name of the category
	 */
	private String getCategoryDisplayName(String currentCategoryLogicName)
	{
		String currentCategoryDisplayName = "";
		if(currentCategoryLogicName.equals(CONFIRMED_BY_ME_OFFER_OWNER))
		{
			currentCategoryDisplayName = swypeCatsGuiUtils.swpeCats[3] ;
		}
		else if(currentCategoryLogicName.equals(CONFIRMED_BY_OTHER_OFFER_OWNER))
		{
			currentCategoryDisplayName = swypeCatsGuiUtils.swpeCats[7] ;
		}
		else if (currentCategoryLogicName.equals(NOT_CONFIRMED))
		{
			currentCategoryDisplayName = swypeCatsGuiUtils.swpeCats[2] ;
		}
		else if(currentCategoryLogicName.equals(HALF_DECLARE_OTHER_CONFIRM_ME))
		{
			currentCategoryDisplayName = swypeCatsGuiUtils.getSwpeCats()[8];
			
		}
		else 
		{
			currentCategoryDisplayName = swypeCatsGuiUtils.swpeCats[0];
		}
		
		return currentCategoryDisplayName;
	}
	

}// end MyOffersActivity
