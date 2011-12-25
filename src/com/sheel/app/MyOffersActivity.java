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
import android.widget.Toast;

import com.sheel.adapters.MyOffersResultsListAdapter;
import com.sheel.datastructures.Category;
import com.sheel.datastructures.Confirmation;
import com.sheel.datastructures.Flight;
import com.sheel.datastructures.Offer;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.User;
import com.sheel.listeners.InflateListener;
import com.sheel.listeners.MyOffersInflateListener;
import com.sheel.utils.DemoPopupWindow;
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
	 * Offers half-confirmed that the logged in user is not the offer owner.
	 */
	ArrayList<OfferDisplay2> searchResults_HalfConfirmedByMeNotDeclaredByMe = new ArrayList<OfferDisplay2>();

	/**
	 * Offers half-confirmed declared and half-confirmed by the user retrieved
	 * from the database.
	 */
	ArrayList<OfferDisplay2> searchResults_half = new ArrayList<OfferDisplay2>();

	/**
	 * Offers full-confirmed retrieved from the database but I am the offer owner.
	 */
	ArrayList<OfferDisplay2> searchResults_full = new ArrayList<OfferDisplay2>();

	/**
	 * Offers full-confirmed retrieved from the database but I am not the offer owner.
	 */
	ArrayList<OfferDisplay2> searchResults_fullConfirmedByMeNotDeclaredByMe = new ArrayList<OfferDisplay2>();

	
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
				searchResults_half = new ArrayList<OfferDisplay2>();
				searchResults_full = new ArrayList<OfferDisplay2>();
				searchResults_HalfConfirmedByMeNotDeclaredByMe = new ArrayList<OfferDisplay2>();
				searchResults_new = new ArrayList<OfferDisplay2>();
				searchResults_fullConfirmedByMeNotDeclaredByMe = new ArrayList<OfferDisplay2>();

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
		this.swypeCatsGuiUtils.showAlertWhenNoResultsAreAvailable(this, swypeCatsGuiUtils.dialogInternetConnection
				, swypeCatsGuiUtils.okay, ConnectorUserActionsActivity.class, "", FilterPreferencesActivity.class);
	}

	/**
	 * Used to specify the class used as a list adapter for the displayed lists
	 * in the swiper. {@link MyOffersResultsListAdapter} is used
	 * 
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
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
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
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
			
			// If I need to update the UI of the confirm offer, go update it without any further check.
			if(action.equals(HTTP_CONFIRM_OFFER_UI))
			{
				String res = intent.getExtras().getString("response");
				Log.e("I am Child Broad Cast receiver taking the UI update", "updateOffersOnUI()");
				updateOffersOnUI(res);
				Log.e("I am Child Broad Cast receiver finishing the UI update", "updateOffersOnUI()");
				
			}
			else
			{
			int httpStatus = intent.getExtras().getInt(HTTP_STATUS);
			Log.e(TAG, "HTTPSTATUS: " + httpStatus);

			String responseStr;
			if (httpStatus == HttpStatus.SC_OK) {

				if (action.equals(HTTP_GET_MY_OFFERS_FILTER)) {
					responseStr = intent.getExtras().getString(HTTP_RESPONSE);
					Log.e(TAG, "Will Start to deal/load with my offers");
					loadSearchResultsOnUI(responseStr);

					// Dialog dismissing
					if (dialog != null)
						dialog.dismiss();
					Log.e(TAG, responseStr);

				}// end if Get my offers filter
				
				if (action.equals(HTTP_EDIT_OFFER)) {
					responseStr = intent.getExtras().getString(HTTP_RESPONSE);
					Log.e(TAG, "Will Start to edit offers");
					//showEditResult(responseStr);
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

	
	private void showEditResult(String rsp){
		 AlertDialog alertDialog;
		 alertDialog = new AlertDialog.Builder(this).create();
		 
		 
		 if(rsp.equals("OK")){
		 alertDialog.setTitle(getResources().getString(R.string.Success));
		 alertDialog.setMessage(getResources().getString(R.string.Changes_saved_successfully));
		 }else{
			 alertDialog.setTitle(getResources().getString(R.string.Sorry));
			 alertDialog.setMessage(getResources().getString(R.string.Cannot_save_changes));
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

				Log.e("loadSearchResultsOnUI: Inisde the loop of my offers: offerDisplay2 ",
						jsonArray.getJSONObject(i) + "");

				OfferDisplay2 offer = OfferDisplay2.mapOfferNew(
						jsonArray.getJSONObject(i), airportsList,
						nationalitiesList);

				Log.e("loadSearchResultsOnUI: Inisde the loop of my offers: offer ",
						offer + "");
					
				try
				{
				// To check if the offer is half confirmed by an offer owner, and I am that offer owner.
				if (offer.getOffer().offerStatus
						.equals(Confirmation.half_confirmed_offerOwner)
						&& getFacebookService().getFacebookUser().getUserId().equals(offer.getUser().getFacebookId()))
					searchResults_half.add(OfferDisplay2.mapOfferNew(
							jsonArray.getJSONObject(i), airportsList,
							nationalitiesList));
				// To check if the offer is half confirmed by an offer owner, and I am that offer owner.
				if (offer.getOffer().offerStatus
						.equals(Confirmation.half_confirmed_offerOwner)
						&& !getFacebookService().getFacebookUser().getUserId().equals(offer.getUser().getFacebookId()))
					searchResults_HalfConfirmedByMeNotDeclaredByMe.add(OfferDisplay2.mapOfferNew(
							jsonArray.getJSONObject(i), airportsList,
							nationalitiesList));
				
				// To check if the offer is half confirmed by other but declared by Offer owner, and I am this other
				else if (offer.getOffer().offerStatus
						.equals(Confirmation.half_confirmed_other)
						&& !getFacebookService().getFacebookUser().getUserId().equals(offer.getUser().getFacebookId()))
					searchResults_HalfConfirmedByMeNotDeclaredByMe
							.add(OfferDisplay2.mapOfferNew(
									jsonArray.getJSONObject(i), airportsList,
									nationalitiesList));
				
				else if (offer.getOffer().offerStatus
						.equals(Confirmation.half_confirmed_other)
						&& getFacebookService().getFacebookUser().getUserId().equals(offer.getUser().getFacebookId()))
					searchResults_half
							.add(OfferDisplay2.mapOfferNew(
									jsonArray.getJSONObject(i), airportsList,
									nationalitiesList));
				
				
				
				// To check if the offer is confirmed and I am the offer owner.
				else if (offer.getOffer().offerStatus
						.equals(Confirmation.confirmed)
						&& getFacebookService().getFacebookUser().getUserId().equals(offer.getUser().getFacebookId()))
					searchResults_full.add(OfferDisplay2.mapOfferNew(
							jsonArray.getJSONObject(i), airportsList,
							nationalitiesList));
				
				// To check if the offer is not confirmed and I am offer owner
				else if (offer.getOffer().offerStatus
						.equals(Confirmation.not_confirmed)
						&& getFacebookService().getFacebookUser().getUserId().equals(offer.getUser().getFacebookId()))
					searchResults_new.add(OfferDisplay2.mapOfferNew(
							jsonArray.getJSONObject(i), airportsList,
							nationalitiesList));
				
				// To check if the offer is full confirmed and I am not offer owner
				else if (offer.getOffer().offerStatus
						.equals(Confirmation.confirmed)
						&& !getFacebookService().getFacebookUser().getUserId().equals(offer.getUser().getFacebookId()))
					searchResults_fullConfirmedByMeNotDeclaredByMe.add(OfferDisplay2.mapOfferNew(
							jsonArray.getJSONObject(i), airportsList,
							nationalitiesList));
				// 
				}
				catch (Exception e) {
					
					Log.e(TAG, "Nast offer");
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
	 */

	private void updateCategoriesInSwypeActivity() {

		if (searchResults_full.isEmpty() && searchResults_half.isEmpty()
				&& searchResults_HalfConfirmedByMeNotDeclaredByMe.isEmpty()
				&& searchResults_new.isEmpty() && searchResults_HalfConfirmedByMeNotDeclaredByMe.isEmpty())
			super.swypeCatsGuiUtils.showAlertWhenNoResultsAreAvailable(this,
					"You do not have any offers yet! ", "Declare new offer",
					InsertOfferActivity.class, "Change filters",
					FilterPreferencesActivity.class);
		else {
			int index = 0;

			// =============================HALF CONFIRMED NOT DECLARED ME========================================
			// 

			// * - Half confirmed offers I confirmed but not declared by me (I
			// am not offer owner)
			if (!searchResults_HalfConfirmedByMeNotDeclaredByMe.isEmpty()) {
				Log.e("Display Name in My offers DCBME: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[0]);
				getCategories()
						.add(new Category(
								"" + swypeCatsGuiUtils.getSwpeCats()[0],
								HALF_CONFIRMED_ME_CONFIRMED_USER_NOT_OFFER_OWNER,
								R.layout.my_offers_main));
				updateCategoryContent(
						searchResults_HalfConfirmedByMeNotDeclaredByMe,
						index++, false);
			}// end
				// if(!searchResults_HalfConfirmedByMeNotDeclaredByMe.isEmpty())

			// ==================================HALF CONFIRMED BY ME======================================
			// 

			// * - Half confirmed offers I confirmed but not declared by me
			if (!searchResults_half.isEmpty()) {
				Log.e("Display Name in My offers DCBME: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[1]);
				getCategories().add(
						new Category("" + swypeCatsGuiUtils.getSwpeCats()[1],
								HALF_CONFIRMED_ME_OFFER_OWNER,
								R.layout.my_offers_main));
				updateCategoryContent(searchResults_half, index++, false);
			}// end if(!searchResults_half.isEmpty())

			// =====================================NOT CONFIRMED===================================
			//
			// * - New offers I declared
			if (!searchResults_new.isEmpty()) {
				Log.e("Display Name in My offers DCBME: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[2]);
				getCategories().add(
						new Category("" + swypeCatsGuiUtils.getSwpeCats()[2],
								NOT_CONFIRMED, R.layout.my_offers_main));
				updateCategoryContent(searchResults_new, index++, false);
			}// end if(!searchResults_new.isEmpty())
				// ========================================FULL CONFIRMED DECLARED BY ME================================
				//

			// * - Confirmed offers, I am the offer owner
			if (!searchResults_full.isEmpty()) {
				Log.e("Display Name in My offers DCBME: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[3]);
				getCategories().add(
						new Category("" + swypeCatsGuiUtils.getSwpeCats()[3],
								CONFIRMED_BY_ME_OFFER_OWNER, R.layout.my_offers_main));
				updateCategoryContent(searchResults_full, index, false);
			}// end if(!searchResults_full.isEmpty())

			// ========================================FULL CONFIRMED DECLARED BY OTHER================================
			//
			
			
			// * - Confirmed offers, I am not the offer owner
			if (!searchResults_fullConfirmedByMeNotDeclaredByMe.isEmpty()) {
				Log.e("Display Name in My offers DCBOTHER: ", ""
						+ swypeCatsGuiUtils.getSwpeCats()[7]);
				getCategories().add(
						new Category("" + swypeCatsGuiUtils.getSwpeCats()[7],
								CONFIRMED_BY_OTHER_OFFER_OWNER, R.layout.my_offers_main));
				updateCategoryContent(searchResults_fullConfirmedByMeNotDeclaredByMe, index, false);
			}// end if(!searchResults_full.isEmpty())

			// ============================================================================================

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

		super.reponseStr = response;
//		super.updateOffersOnUI(super.reponseStr);
		Log.e("Update on Response UI SUPER CALLED IN CHILD : ",
				super.reponseStr);
		try {

			JSONObject confirmationJSON = new JSONObject(super.reponseStr);
			Log.e("hashas confirmationJSON: ", confirmationJSON + "");

			Confirmation confirmation = Confirmation
					.mapConfirmation(confirmationJSON);
			Log.e("hashas confirmation", confirmation + "");

			/**
			 * If offer is full confirmed, we need to add it in its appropriate
			 * group either Confirmed by me as an offer owner
			 * or Confirmed by other as an offer owner.
			 * @see updateCategoriesInSwypeActivity for checks
			 */
			
			if (confirmation.isStatusTransactionUser1()
					&& confirmation.isStatusTransactionUser2()) {
				// ============Delete offer from UI=========

				// Log.e(TAG, "Dismissing Dialog : " + dialogConfirm);
				// if(dialogConfirm != null) dialogConfirm.dismiss();
				Toast.makeText(getApplicationContext(),
						getString(R.string._hossamConfirmedByTwoUsers),
						Toast.LENGTH_SHORT).show();
				Toast.makeText(
						getApplicationContext(),
						getFacebookService().getFacebookUser().getFirstName()
								+ ", "
								+ getString(R.string._hossamConfirmationMail),
						Toast.LENGTH_SHORT).show();

				// If User 1 is the Offer owner
				if (getFacebookService().getFacebookUser().getUserId()
						.equals(confirmation.getUser1().getFacebookId()))
					InflateListener.sendSMS(
							getSMSContentForConfirmation(
									confirmation.getOffer().userStatus,
									confirmation.getUser1(),
									confirmation.getUser2(),
									confirmation.getOffer(),
									confirmation.getFlight()), confirmation
									.getUser2().mobileNumber,
							MyOffersActivity.this);

				else
					InflateListener.sendSMS(
							getSMSContentForConfirmation(
									confirmation.getOffer().userStatus,
									confirmation.getUser2(),
									confirmation.getUser1(),
									confirmation.getOffer(),
									confirmation.getFlight()), confirmation
									.getUser1().mobileNumber,
							MyOffersActivity.this);
				
				// If the offer is half confirmed by Offer owner
				/**
				 * You need to check whether the offer is half confirmed by whom 
				 * and it in its group
				 * @see Check the groups in SheelMaayaaConstants for logic names.
				 */
			} else if (confirmation.isStatusTransactionUser1()) {

				Toast.makeText(getApplicationContext(),
						"Hello offer Owner, you have confirmed this offer",
						Toast.LENGTH_SHORT).show();

				/**
				 * You need to check whether the offer is half confirmed by whom 
				 * and it in its group
				 * @see Check the statuses in SheelMaayaaConstants for logic names.
				 */
				// If the offer is half confirmed by not an Offer owner
			} else if (confirmation.isStatusTransactionUser2()) {
				Toast.makeText(getApplicationContext(),
						"Hello offer other, you have confirmed this offer",
						Toast.LENGTH_SHORT).show();
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

}// end MyOffersActivity
