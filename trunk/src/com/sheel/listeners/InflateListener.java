package com.sheel.listeners;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_CONFIRM_OFFER;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewStub.OnInflateListener;
import android.widget.Button;
import android.widget.TextView;

import com.sheel.app.R;
import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.Flight;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.User;
import com.sheel.datastructures.enums.OwnerFacebookStatus;
import com.sheel.utils.GuiUtils;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.InternetManager;
import com.sheel.utils.SheelMaayaaConstants;

/**
 * InflateListener is the listener used when the Stub in the (Search
 * results) view is inflated. It provides the basic rendering features 
 * between (MyOffers) and (SearchResults) View. Customatization between
 * both views is done by extending the class then overriding the different
 * methods
 * 
 * It is used to save the position of the buttons inside their tags
 * 
 * @author 
 * 		Hossam_Amer
 * @author		    
 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)	
 *
 */

public class InflateListener implements OnInflateListener, OnClickListener {
	
	private static final String TAG = InflateListener.class.getName();

	
	/**
	 * Swyping Gui utils instance
	 */
	public GuiUtils swypeCatsGuiUtils;
	
	
	/**
	 *  Position Clicked
	 */
	int mPos;
	/**
	 * Wrapper representing data of an offer and other data that should
	 * be displayed in the view stub
	 */
	OfferDisplay2 offerDisplay;
	
	/**
	 * Current context of the Activity
	 */
	Context mContext;
	
	/**
	 * Number of buttons
	 */
	int numButtons = 4;
	
	/**
	 * Array of Button IDs
	 */
	private int [] buttonIDs = new int [numButtons];
	
	/**
	 * Activity to launch the operations on
	 */
	private Activity mActivity;
	
	/**
	 * Current user logged In
	 */
	private FacebookUser mUser;


	
	/**
	 * 
	 * @param position
	 * 		Clicked position
	 * @param mContext
	 * 
	 * @param offerDisplay
	 * 		Data wrapper representing offer, flight and user used
	 * 		for displaying data in the stub
	 * 
	 * @author
	 * 		Hossam Amer 
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 *  
	 */
	public InflateListener(int position, Context mContext, OfferDisplay2 offerDisplay,
			Activity activity, FacebookUser mUser, GuiUtils swypeCatsGuiUtils)
	{
		mPos = position;

		this.offerDisplay = offerDisplay;

		this.mContext = mContext;
		
		this.mActivity = activity;
		
		this.mUser = mUser;
		
		this.swypeCatsGuiUtils = swypeCatsGuiUtils;
		
		
		// <=== List your buttons here ===>
		buttonIDs[0] = R.id.details_button_call; 
		buttonIDs[1] = R.id.details_button_confirm;
		buttonIDs[2] = R.id.details_button_sendSms;
		buttonIDs[3] = R.id.details_button_options;

	}

	/**
	 * 
	 */
	public void onInflate(ViewStub stub, View inflated) {
			
		/**
		 * Initialize the summary (nationality + gender + facebook extra info)
		 */
		TextView details_summary = (TextView)inflated.findViewById(R.id.sheel_details_textView_summary);
		TextView details_email = (TextView)inflated.findViewById(R.id.sheel_details_textView_email);
		TextView details_telephone = (TextView)inflated.findViewById(R.id.sheel_details_textView_telephone);
		TextView details_flight = (TextView)inflated.findViewById(R.id.sheel_details_textView_flight);
		
		renderFirstTextView(details_summary, offerDisplay, inflated);
		renderSecondTextView(details_email, offerDisplay, inflated);
		renderThirdTextView(details_telephone, offerDisplay, inflated);
		renderFourthTextView(details_flight, offerDisplay, inflated);
		
		
		/**
		 * Initialize the buttons
		 */
		for(int i=0; i<buttonIDs.length;i++)
		{
			Button btn = (Button) inflated.findViewById(buttonIDs[i]);
			btn.setTag(mPos);
			Log.e(TAG, "Button: " + btn.getId());
			
			//btn.setOnClickListener(this);
			btn.setOnClickListener(this);
			Log.e(TAG, "Button Listener done: " + btn.getId());
		}
	
	}// end onInflate

	/**
	 * Used to render the data displayed in the first text view.
	 * By default it is nationality, Icon representing gender and 
	 * a controller to reach facebook extra info (like mutual friends
	 * if it exists).
	 * 
	 * <br><br><b>Override the method to change the content
	 * displayed</b>	
	 *  
	 * <br><br><b>To hide, override the method then
	 * write <code>textView.setVisibility(View.INVISIBLE)</code></b>
	 * 
	 * @param textView
	 * 		first text view in the view stub from top
	 * @param offerDisplay
	 * 		Data wrapper containing all info about offer, user 
	 * 		and flight for a certain offer
	 * @param inflated
	 * 		Inflated view representing the layout file displayed
	 * 		inside the view stub. It is used to enable adding of
	 * 		icons using 
	 * 		{@link GuiUtils#setIconForATextField(Context, View, TextView, int, int)}.
	 * 		To neglect such parameter, pass it as null.
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void renderFirstTextView(TextView textView, OfferDisplay2 offerDisplay, View inflated) {
		String detailsSummary = offerDisplay.getUser().getNationality() ;		
		
		if (offerDisplay.getOwnerFacebookRelationWithUser() == OwnerFacebookStatus.FRIEND) {
			detailsSummary += " " +  swypeCatsGuiUtils.getSwpeCats()[5];
//			detailsSummary += " Friend";
		}// end if: offer owner is the user's friend
		else if (offerDisplay.getOwnerFacebookRelationWithUser() == OwnerFacebookStatus.FRIEND_OF_FRIEND) {
			detailsSummary += " " +  swypeCatsGuiUtils.getSwpeCats()[6];
//			detailsSummary += " Friend of friend";
		}// end if: offer owner is the user's friend of friend
		else if (offerDisplay.getOwnerFacebookRelationWithUser() == OwnerFacebookStatus.UNRELATED) {
			detailsSummary += " " +  swypeCatsGuiUtils.getSwpeCats()[4];
//			detailsSummary += " Stranger";
		}// end if: offer owner is a stranger
		textView.setText(detailsSummary);
		
		if (offerDisplay.getUser().gender.equals(SheelMaayaaConstants.GENDER_FEMALE)) {
			this.swypeCatsGuiUtils.setIconForATextField(mContext, inflated, textView, R.drawable.sheel_gender_female, 3);
		}// end if : offer owner is a female
		else {
			this.swypeCatsGuiUtils.setIconForATextField(mContext, inflated, textView, R.drawable.sheel_gender_male, 3);
		}// end if:offer owner is a male or unknown -> show icon
		
	}// end renderFirstTextView
	
	/**
	 * Used to render the data displayed in the second text view.
	 * By default it is the email of the offer owner.
	 * 
	 * <br><br><b>Override the method to change the content
	 * displayed</b>
	 * 
	 * <br><br><b>To hide, override the method then
	 * write <code>textView.setVisibility(View.INVISIBLE)</code></b>
	 * 
	 * @param textView
	 * 		second text view in the  view stub from top
	 * @param offerDisplay
	 * 		Data wrapper containing all info about offer, user 
	 * 		and flight for a certain offer
	 * @param inflated
	 * 		Inflated view representing the layout file displayed
	 * 		inside the view stub. It is used to enable adding of
	 * 		icons using 
	 * 		{@link GuiUtils#setIconForATextField(Context, View, TextView, int, int)}.
	 * 		To neglect such parameter, pass it as null.
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void renderSecondTextView(TextView textView, OfferDisplay2 offerDisplay,  View inflated) {
				
		textView.setText(offerDisplay.getUser().email);
		if (inflated != null) {
			this.swypeCatsGuiUtils.setIconForATextField(mContext, inflated, textView,
					R.drawable.sheel_result_email, 3);
		}// end if
		
	}// end renderSecondTextView
	
	/**
	 * Used to render the data displayed in the third text view.
	 * By default it is the mobile of the offer owner
	 * 
	 * <br><br><b>Override the method to change the content
	 * displayed.</b>
	 * 
	 * <br><br><b>To hide, override the method then
	 * write <code>textView.setVisibility(View.INVISIBLE)</code></b>
	 * 
	 * @param textView
	 * 		third text view in the view stub from top
	 * @param offerDisplay
	 * 		Data wrapper containing all info about offer, user 
	 * 		and flight for a certain offer
	 * @param inflated
	 * 		Inflated view representing the layout file displayed
	 * 		inside the view stub. It is used to enable adding of
	 * 		icons using 
	 * 		{@link GuiUtils#setIconForATextField(Context, View, TextView, int, int)}.
	 * 		To neglect such parameter, pass it as null.
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void renderThirdTextView(TextView textView, OfferDisplay2 offerDisplay, View inflated) {
		
		textView.setText(offerDisplay.getUser().mobileNumber);
		if (inflated != null) {
			this.swypeCatsGuiUtils.setIconForATextField(mContext, inflated, textView, R.drawable.sheel_result_phone, 3);
		}// end if
		
	}// end renderThirdTextView
	
	/**
	 * Used to render the data displayed in the fourth text view.
	 * By default it is the showing the flight details in the format
	 * returned by {@link Flight#displayFlight(swypeCatsGuiUtils)}
	 * 
	 * <br><br><b>Override the method to change the content
	 * displayed</b>
	 *
	 * <br><br><b>To hide, override the method then
	 * write <code>textView.setVisibility(View.INVISIBLE)</code></b>
	 * 
	 * @param textView
	 * 		third text view in the view stub from top
	 * @param offerDisplay
	 * 		Data wrapper containing all info about offer, user 
	 * 		and flight for a certain offer
	 * @param inflated
	 * 		Inflated view representing the layout file displayed
	 * 		inside the view stub. It is used to enable adding of
	 * 		icons using 
	 * 		{@link GuiUtils#setIconForATextField(Context, View, TextView, int, int)}.
	 * 		To neglect such parameter, pass it as null.
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 *@author Hossam_Amer
	 */
	public void renderFourthTextView(TextView textView, OfferDisplay2 offerDisplay, View inflated) {
		
		textView.setText(offerDisplay.getFlight().displayFlight(swypeCatsGuiUtils));
		if (inflated != null) {
			this.swypeCatsGuiUtils.setIconForATextField(mContext, inflated, textView, R.drawable.sheel_result_flight, 0);
		}// end if
		
	}// end renderFourthTextView
	
	/**
	 * Alters the  visibility of the different available buttons:
	 * Confirm, call , send SMS
	 * 
	 * <br><br><b>To use it:
	 * <ol> 
	 * 		<li>Override {@link OnInflateListener#onInflate(ViewStub, View)} 
	 * 		in child class and call super</li>
	 * 		<li>Call the method and pass <code>inflated</code></li>
	 * </ol></b>
	 * @param inflated
	 * 		Inflated view representing the layout file displayed
	 * 		inside the view stub. Possible values are: {@link View#VISIBLE}, 
	 * 		{@link View#INVISIBLE}.	 		
	 * @param confirm
	 * 		visibility of confirm button. Possible values are: {@link View#VISIBLE}, 
	 * 		{@link View#INVISIBLE}.
	 * @param call
	 * 		visibility of call button. Possible values are: {@link View#VISIBLE}, 
	 * 		{@link View#INVISIBLE}.
	 * @param sendSms
	 * 		visibility of send SMS button. Possible values are: {@link View#VISIBLE}, 
	 * 		{@link View#INVISIBLE}.
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 *@author Hossam_Amer
	 */
	public void setVisibilityButtonsVisibility(View inflated, int confirm, int call, int sendSms, int options) {
		((Button) inflated.findViewById(buttonIDs[0])).setVisibility(call);
		((Button) inflated.findViewById(buttonIDs[1])).setVisibility(confirm);
		((Button) inflated.findViewById(buttonIDs[2])).setVisibility(sendSms);	
		((Button) inflated.findViewById(buttonIDs[3])).setVisibility(options);	
	}// end setVisibilityButtonsVisibility
	
	
	
	/**
	 * Click Action listener for the buttons inside the view stub
	 * @author 
	 * 		Hossam_Amer
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void onClick(View v) {
		
		/**
		 * The helper methods called in the switch are used
		 * to handle giving customized actions for the views
		 * to the buttons by overriding the methods
		 * 
		 * Example: (Call) button is search results view
		 * is the (Edit) button in My Offers view 
		 */
		switch (v.getId()) {
		case R.id.details_button_call:
			onClick_button1(v);
			break;
			
		case R.id.details_button_sendSms:
			onClick_button2(v);
			break;
			
		case R.id.details_button_confirm:
			onClick_confirm(v);
			break;
		case R.id.details_button_options:
			onClick_options(v);
			break;
	
		default:
			break;
		}// end switch
		
	}// end onClick
	
	/**
	 * Called on clicking the fourth button form the left.
	 * By default this button performs call functions for
	 * offer owner. To change such behavior, override the
	 * method in child class
	 * 
	 * @param v
	 * 		Clicked button
	 * @author 
	 *		Mohsen
	 */
	public void onClick_options(View v) {
		Log.e("Options button", "For the quick action menu");
		//call(this.offerDisplay.getUser().mobileNumber);
	}// end onClick_button1
	
	
	/**
	 * Called on clicking the first button form the left.
	 * By default this button performs call functions for
	 * offer owner. To change such behavior, override the
	 * method in child class
	 * 
	 * @param v
	 * 		Clicked button
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 * @author 
	 * 		Hossam_Amer
	 */
	public void onClick_button1(View v) {
		Log.e("Call button", this.offerDisplay.getUser().mobileNumber);
		call(this.offerDisplay.getUser().mobileNumber);
	}// end onClick_button1
	
	
	
	/**
	 * Called on clicking the second button form the left.
	 * By default this button performs send SMS functions for
	 * offer owner. To change such behavior, override the
	 * method in child class
	 * 
	 * @param v
	 * 		Clicked button
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 * @author 
	 * 		Hossam_Amer
	 */
	public void onClick_button2(View v) {
		Log.e("SMS button", this.offerDisplay.getUser().mobileNumber);
		sendSMS(getSMSMessage(), this.offerDisplay.getUser().mobileNumber, mActivity);
	}// end onClick_button2
	
	
	/**
	 * Called on clicking the third button form the left.
	 * By default this button performs confirm functions for
	 * offer owner with possibility to update the UI accordingly.
	 * The default implementation does not update the database.
	 * To change such behavior, override the method in child class
	 * 
	 * @param v
	 * 		Clicked button
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 * @author 
	 * 		Hossam_Amer
	 */
	private void onClick_confirm(View v) {
		Log.e("Confirm button", "Hello");
		if(InternetManager.isInternetOn(mContext))
		{	
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setMessage(R.string._hossamConfirmYesNo)
			       .setCancelable(false)
			       .setPositiveButton(R.string._hossamYes, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog2, int id) { 
			        	   
			        		
							confirmOffer();
			           }
			       });
			builder.setNegativeButton(R.string._hossamNo, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog2, int whichButton) {
		        	   dialog2.cancel();
                }
            	});
			
			 builder.create();
			 builder.show();
			
		
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setMessage("" + R.string._hossamInternetConn)
			       .setCancelable(false)
			       .setPositiveButton( "" + R.string._hossamOk, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   dialog.cancel();
			           }
			       
			       });
			 builder.create();
			 builder.show();

		}
	}// end onClick_confirm
	
	/**
	 * Launches the activity of sending SMS
	 * @param sms_content The template form of the SMS
	 * @param number The destination number the SMS will be sent to
	 * @author Hossam_Amer
	 * @see http://stackoverflow.com/questions/8429003/open-sms-send-app-with-parameters-in-android-2-2
	 */
	public static void sendSMS(String sms_content, String number, Activity mActivity)
	{
//		Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
//		sendIntent.putExtra("sms_body", sms_content);
//		mActivity.startActivity(sendIntent);
		Log.e("SMS button", number);
		Uri uri = Uri.parse("smsto:"+ number);  
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);  
		intent.putExtra("sms_body", sms_content); 
		mActivity.startActivity(intent);
	}
	
	/**
	 * Starts the activity for calling
	 * @param number The phone number the user will contact
	 * @author Hossam_Amer
	 */
	private void call(String number) {
		
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel: " + number));
		mActivity.startActivity(callIntent);
	}
	
	private void confirmOffer() {
		
//XXX		
		String path = "/insertconfirmation/" + mUser.getUserId()+"/"+this.offerDisplay.getOffer().id;
//		path = "/insertconfirmation/" + 673780564+"/"+this.offerDisplay.getOffer().id;
//		path = "/insertconfirmation/" + "100003145017782" +"/"+this.offerDisplay.getOffer().id;
		Log.e("Confirm offer path: ", path);
		HTTPManager.startHttpService(path, HTTP_CONFIRM_OFFER, mContext);
		
	}
	
	/**
	 * Gets the SMS template form depending on the offer status either less weight or extra weight
	 * @return the SMS template form
	 * @author Hossam_Amer
	 */

	/**XXXXXXNeed to check how to get the name
	 *XXXXXhashas
	 *
	 *<br><br>From Passant: {@link OfferDisplay2#getUser()} then {@link User#firstName} 
	 *OR {@link OfferDisplay2#getDisplayName()}
	 */
	private String getSMSMessage() {
		
		String sms_content = "";
		String sms_content1 = "";
		String sms_content2 = "";
		String sms_content3 = "";
		String sms_content4 = "";
		String sms_content5 = "";
		String sms_content6 = "";
		
		/**XXXXXXNeed to check how to get the name
		 *XXXXXhashas
		 */
		
	if(this.offerDisplay.getOffer().userStatus == 0)
	{
		sms_content1 = "Hello " + this.offerDisplay.getDisplayName() +",";
		sms_content2 = "I have seen your offer (Flight: " 
			+  this.offerDisplay.getFlight().flightNumber + ", date: " + this.offerDisplay.getFlight().departureDate
				+ ") on Sheel M3aaya app that you " +
				"have an extra space ("+ this.offerDisplay.getOffer().noOfKilograms +  "Kilograms) in your bags." +
				" So, I would like to inform you that " +
				"I am interested in putting some of my stuff in your bags.";
		sms_content3 = "Please contact me at this number if your space is still available.";
		sms_content6 = "Best Regards,\n" + mUser.getFirstName() ;
		
		
	}	
	
	else
	{
		
		sms_content1 = "Hello "+ this.offerDisplay.getDisplayName() +",";
		sms_content2 = "I have seen your request" + "(Flight: " 
			+  this.offerDisplay.getFlight().flightNumber + ", date: " + this.offerDisplay.getFlight().departureDate
			+	") on Sheel M3aaya app that you " +
				"need an extra space ("+ this.offerDisplay.getOffer().noOfKilograms + " Kilograms) in your bags." +
				" So, I would like to inform you that " +
				"I am interested in offering you some of my space in my bags.";
		sms_content3 = "Please contact me at this number if you are still intrested.";
		sms_content6 = "Best Regards,\n" + mUser.getFirstName();
	}
	
	sms_content4 = "Thanks in advance :)";
	sms_content5 = "Wish you a nice flight.";
	sms_content = sms_content1 
				+ "\n" + "\n" + sms_content2
				+ "\n" + "\n" + sms_content3
				+ "\n" + "\n" + sms_content4
				+ "\n" + "\n" + sms_content5
				+ "\n" + "\n" + sms_content6;
	
		return sms_content;
	}

}
