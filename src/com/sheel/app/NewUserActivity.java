package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_CHECK_REGISTERED;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_REGISTER_USER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import com.sheel.datastructures.User;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.InternetManager;
import com.sheel.webservices.FacebookWebservice;

/**
 * This activity is used to display to the new user the registration form and to
 * send his data to the database
 * 
 * @author Nada Emad
 * 
 */
public class NewUserActivity extends UserSessionStateMaintainingActivity {

	public static final String FIRST_NAME_KEY = "firstName";
	public static final String MIDDLE_NAME_KEY = "middleName";
	public static final String LAST_NAME_KEY = "lastName";
	public static final String EMAIL_KEY = "email";
	public static final String GENDER_KEY = "gender";
	public static final String PASSPORT_IMAGE_KEY = "passportImage";
	

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	private static final String TAG = "MyActivity";
	
	/**
	 * Filter added for this activity to filter the actions received by the receiver
	 */
	IntentFilter filter;
	
	/**
	 *  The receiver used for detecting the HTTP data arrival 
	 */
	private SheelMaayaaBroadCastRec receiver;

     
     ProgressDialog dialog;

	/** Path string where the taken passport photo is saved */
	String ImagePath;
	/** Male Toggle Button. Not required to register */
	RadioButton toggleMale;
	/** Female Toggle Button. Not required to register */
	RadioButton toggleFemale; // female
	/** AutoComplete field for mobile country codes. Required to register */
	AutoCompleteTextView countryCodes;
	/** AutoComplete field for nationality. Not required to register */
	AutoCompleteTextView nationalityField;
	/** Mobile number text field. Required to register */
	EditText mobileNumberField;
	/** First name field. Required to register */
	EditText firstNameField;
	/** Middle name field. Not required to register */
	EditText middleNameField;
	/** Last name field. Required to register */
	EditText lastNameField;
	/** Email field. Required to register */
	EditText emailField;
	/** Passport number field. Required to register */
	EditText passportNumberField;
	
	EditText validationCodeField;

	String gender = ""; /* Declaration of gender string */
	String firstName; /* Declaration of first name string */
	String middleName; /* Declaration of middle name string */
	String lastName; /* Declaration of last name string */
	/** String containing the passport image encoded data string */
	String passportImage="image";
	String passportNumber; /* Declaration of passport string */
	String email; /* Declaration of email string */
	String mobileNumber; /* Declaration of mobile number string */
	String nationality; /* Declaration of nationality string */
	/** String containing the user facebook ID */
	String faceBookID;
	
	String LoggedID;

	int code;
	String userValidationCode;
	/**
	 * Boolean variable to check that all the required fields are filled and in
	 * the right format. True if everything is valid. False otherwise
	 */
	boolean allValid = false;
	
	boolean codeValid = false;
	boolean nationalityValid = false;
	boolean gotResponse = false;
	boolean photoTaken = true;
	boolean mobileValid = true;

	/*
	 * public void NewUser() { String gender = ""; String firstName = ""; String
	 * middleName = ""; String lastName = ""; String email = "";
	 * 
	 * Bundle extras = getIntent().getExtras(); if (extras != null) { firstName
	 * = extras.getString(FIRST_NAME_KEY); middleName =
	 * extras.getString(MIDDLE_NAME_KEY); lastName =
	 * extras.getString(LAST_NAME_KEY); email = extras.getString(EMAIL_KEY);
	 * gender = extras.getString(GENDER_KEY);
	 * 
	 * }// end if: extract info sent by the intent
	 * 
	 * 
	 * firstNameField.setText(firstName); middleNameField.setText(middleName);
	 * lastNameField.setText(lastName); emailField.setText(email); this.gender =
	 * gender;
	 * 
	 * if (gender.equalsIgnoreCase("male")) toggleMale.setChecked(true); else if
	 * (gender.equalsIgnoreCase("female")) toggleFemale.setChecked(true);
	 * 
	 * }
	 */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the contentView of this activity to the the register
		setContentView(R.layout.register);

		setFacebookService(new FacebookWebservice());
		/////////////////////////////
		//getFacebookService().login(this, true, false);
		
		// call the setVariables
		setVariables();

	}
	
	/*@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	*/
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
		filter.addAction(HTTP_CHECK_REGISTERED);
		filter.addAction(HTTP_REGISTER_USER);
		receiver = new SheelMaayaaBroadCastRec();
		
		Log.e(TAG, "Receiver Registered");
		registerReceiver(receiver, filter);
	}

	/**
	 * This method is called to create all the UI components and create and set
	 * the adaptors to the AutoComplete fields
	 */
	public void setVariables() {

		/* Nationality field, its adaptor, its validator */
		nationalityField = (AutoCompleteTextView) findViewById(R.id.autoNationality);
		final String[] nationalityStrings = getResources().getStringArray(
				R.array.nationalities_array);
		ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<String>(
				this, R.layout.list_item, nationalityStrings);
		nationalityField.setAdapter(nationalityAdapter);
		Validator NationalityValidator = new Validator() {

			@Override
			public boolean isValid(CharSequence text) {

				Log.v("Test", "Checking if valid: " + text);
				String stringText = text.toString();
				stringText = stringText.trim();
				

				Arrays.sort(nationalityStrings);
				if ((Arrays.binarySearch(nationalityStrings, stringText) > 0)) {
					nationalityValid = true;
					nationality = stringText.replaceAll(" ", "%20");
					

					return true;
				}
				nationalityValid = false;
				Toast toast = Toast.makeText(NewUserActivity.this,
						"Please insert a valid nationality", 0);
				toast.show();
				return false;
			}

			@Override
			public CharSequence fixText(CharSequence invalidText) {

				return invalidText;
			}

		};
		nationalityField.setValidator(NationalityValidator);

		/* Mobile country code field, its adaptor, its validator */
		countryCodes = (AutoCompleteTextView) findViewById(R.id.auto);
		final String[] codeStrings = getResources().getStringArray(
				R.array.codes);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, codeStrings);
		countryCodes.setAdapter(adapter);
		Validator MobileValidator = new Validator() {

			@Override
			public boolean isValid(CharSequence text) {
				Log.v("Test", "Checking if valid: " + text);
				Arrays.sort(codeStrings);
				if (Arrays.binarySearch(codeStrings, text.toString()) > 0) {
					codeValid = true;
					return true;
				}
				codeValid = false;
				Toast toast = Toast.makeText(NewUserActivity.this,
						"Please insert a valid country", 0);
				toast.show();
				return false;
			}

			@Override
			public CharSequence fixText(CharSequence invalidText) {

				return invalidText;
			}
		};
		countryCodes.setValidator(MobileValidator);

		/* Mobile number field */
		mobileNumberField = (EditText) findViewById(R.id.mobileNumber);

		/* First, middle and last names fields 
		firstNameField = (EditText) findViewById(R.id.FirstName);
		middleNameField = (EditText) findViewById(R.id.MiddleName);
		lastNameField = (EditText) findViewById(R.id.LastName);
		*/
	
		/* Email field */
		emailField = (EditText) findViewById(R.id.email);

		/* Passport number field */
		passportNumberField = (EditText) findViewById(R.id.passNum);
		validationCodeField = (EditText) findViewById(R.id.validationCode);

	} // end setVariables

	/** Method called when male toggle button is clicked */
	public void onClick_male(View v) {
		
		// Uncheck the female radio button
		toggleFemale.setChecked(false);
		// Set the gender to male
		gender = "male";
		
	} // end onClick_male

	/** Method called when female toggle button is clicked */
	public void onClick_female(View v) {
		
		// Uncheck the male radio button
		toggleMale.setChecked(false);
		// Set the gender to male
		gender = "female";
		
	} // end onClick_female

	/** Method called when the take photo button is clicked to open the camera */
	public void onClick_takePhoto(View v) {
		/*
		 * Intent photoIntent = new Intent(this, TakePhotoActivity.class);
		 * startActivity(photoIntent); passportImage =
		 * photoIntent.getExtras().getString(PASSPORT_IMAGE_KEY);
		 * System.out.println(passportImage);
		 */

		// Set the path where the taken photo will be saved
		
		/*path = Environment.getExternalStorageDirectory().getName()
				+ File.separatorChar + "Android/data/"
				+ NewUserActivity.this.getPackageName()+ "/PassportPhoto.jpg";
		*/
		
		ImagePath =  Environment.getExternalStorageDirectory().getPath() 
				+ File.separatorChar+ "Android/data/"
						+ NewUserActivity.this.getPackageName() +File.separatorChar+  "PassportPhoto.jpg";
		System.out.println("Path: " + ImagePath.toString());
		/** The file containing the taken passport photo */
		File file = new File(ImagePath);

		try {
			// Create a file at the location specified in the path
			if (file.exists() == false) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

		} catch (IOException e) {
			Log.e(TAG, "Could not create file.", e);
		}
		Log.i(TAG, ImagePath);

		fileUri = Uri.fromFile(file);

		// create Intent to take a picture and return control to the calling
		// application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// set the image file name
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		// start the image capture Intent
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE); 
		} // end onClick_takePhoto
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			
			// User done with capturing image
			if (resultCode == RESULT_OK) {
				System.out.println("Result OK");
				// Image captured and saved to fileUri specified in the Intent
				onPhotoTaken();
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
				System.out.println("Cancel");
				Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
			} else {
				// Image capture failed, advise user
				Toast.makeText(this, "Image Capture failed", Toast.LENGTH_LONG)
						.show();
			}
		}
		
		
	}

	protected void onPhotoTaken() {

		ImageView i = (ImageView) findViewById(R.id.pictureView);
		Bitmap bitmap = BitmapFactory.decodeFile(ImagePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	//	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the
																// bitmap object
		byte[] image = baos.toByteArray();

		passportImage = Base64.encodeBytes(image);
		System.out.println("Photo: "+ passportImage);
		i.setImageBitmap(bitmap);
		System.out.println("Photo Success");
		
	}

	public void OnClick_mobileValidate(View v){
		mobileNumber = (mobileNumberField.getText().toString());
		String countryCode = countryCodes.getText().toString();
		if(mobileNumber.length() < 1 && countryCode.length()<1){
			 Toast.makeText(getBaseContext(), "Please insert the mobile code and number", 
                    Toast.LENGTH_SHORT).show();
		}else{
		double code1 = Math.random()*12345;
		code = (int)code1;
		
		String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        
        String phoneCode = countryCode.toString().split(" ")[1];
		mobileNumber =  phoneCode + mobileNumber;

		mobileNumber = mobileNumber.trim();
		mobileNumber = mobileNumber.replace("(", "");
		mobileNumber = mobileNumber.replace(")", "");
		mobileNumber = mobileNumber.replace(" ", "");
		System.out.println("MOB: "+mobileNumber+" Code: "+code);
		
		 PendingIntent sent = PendingIntent.getActivity(this, 0, new Intent(SENT), 0);
		 PendingIntent delivered = PendingIntent.getActivity(this, 0, new Intent(DELIVERED), 0);
		        System.out.println("Message sent");
		       
		       //---when the SMS has been sent---
		        registerReceiver(new BroadcastReceiver(){
		            @Override
		            public void onReceive(Context arg0, Intent arg1) {
		                switch (getResultCode())
		                {
		                    case Activity.RESULT_OK:
		                        Toast.makeText(getBaseContext(), "SMS sent", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
		                        Toast.makeText(getBaseContext(), "Generic failure", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                    case SmsManager.RESULT_ERROR_NO_SERVICE:
		                        Toast.makeText(getBaseContext(), "No service", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                    case SmsManager.RESULT_ERROR_NULL_PDU:
		                        Toast.makeText(getBaseContext(), "Null PDU", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                    case SmsManager.RESULT_ERROR_RADIO_OFF:
		                        Toast.makeText(getBaseContext(), "Radio off", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                }
		            }
		        }, new IntentFilter(SENT));
		 
		        //---when the SMS has been delivered---
		        registerReceiver(new BroadcastReceiver(){
		            @Override
		            public void onReceive(Context arg0, Intent arg1) {
		                switch (getResultCode())
		                {
		                    case Activity.RESULT_OK:
		                        Toast.makeText(getBaseContext(), "SMS delivered", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                    case Activity.RESULT_CANCELED:
		                        Toast.makeText(getBaseContext(), "SMS not delivered", 
		                                Toast.LENGTH_SHORT).show();
		                        break;                        
		                }
		            }
		        }, new IntentFilter(DELIVERED));  
		        SmsManager sms = SmsManager.getDefault();
			       sms.sendTextMessage(mobileNumber,mobileNumber, ("This is a message from SheelMaaya /n Validation Code:" + code), null,null);        
			       Toast.makeText(this, "SMS SENT", Toast.LENGTH_LONG)
					.show();
		}
	}
	public void emailValidation(String emailstring) {
		Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		if (emailMatcher.matches()) {
			allValid = true;
		} else {
			allValid = false;
			Toast.makeText(this, "Please enter a valid email address",
					Toast.LENGTH_LONG).show();
		}
	}
	
	public void photoValidation( String passporttemp){
		if(passporttemp.length()>0){
			photoTaken = true;
			
		}
		else{
			photoTaken = false;
			Toast.makeText(this, "Please take the passport photo", Toast.LENGTH_LONG).show();
		}
	}

	public void validate() {
		String countryCode = countryCodes.getText().toString();
		countryCodes.getValidator().isValid(countryCode);
		System.out.println("Went to validator");
		//nationality = nationalityField.getText().toString();
		if(!nationality.equals("%20")){
			nationalityField.getValidator().isValid(nationality);
			
		}
		else{
			nationalityValid = true;
		}
		
		if(userValidationCode.length()>0 && code == Integer.parseInt(userValidationCode)){
			System.out.println(code + " " + userValidationCode);
			mobileValid = true;
		}
		else{
			Toast.makeText(this, "Validation code mismatch", 0).show();
		}
		emailValidation(email);
		
		photoValidation(passportImage);

		if (nationalityValid && codeValid && photoTaken && mobileValid && mobileNumber.length() > 2
				&& passportNumber.length() > 0)
			allValid = true;
		else
			allValid = false;
	}

	public void GetUserFacebookID() {
		String tempID = "";
		//getFacebookService().getUserInformation(true);
		if (getFacebookService() != null) {

			if (!getFacebookService().getFacebookUser()
					.isRequestedBeforeSuccessfully()) {
/////////////////////////
				//getFacebookService().getUserInformation(true);
			} else {
				tempID = getFacebookService().getFacebookUser().getUserId();
				firstName = getFacebookService().getFacebookUser().getFirstName();
				 firstName = firstName.trim();
				 if(firstName.length() == 0) firstName = "%20";
				 middleName = getFacebookService().getFacebookUser().getMiddleName();
				middleName = middleName.trim();
				if(middleName.length() == 0) middleName = "%20";
				lastName =  getFacebookService().getFacebookUser().getLastName();
				lastName = lastName.trim();
				if(lastName.length() == 0) lastName = "%20";
				email =  getFacebookService().getFacebookUser().getEmail();
				email = email.trim();
				if(email.length() == 0) email = "%20";
				 if(getFacebookService().getFacebookUser().isFemale()) gender = "female";
				 else gender = "male";
			}

		}

		System.out.println("FB ID: " + tempID);
		System.out.println(email);
	}
	
	public final boolean isInternetOn() {
		ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		// ARE WE CONNECTED TO THE NET
		if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
		connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
		// MESSAGE TO SCREEN FOR TESTING (IF REQ)
			//System.out.println("CONNECTED");
		//Toast.makeText(this, connectionType + " connected", Toast.LENGTH_SHORT).show();
		return true;
		} else if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED  ) {
		//System.out.println("Not Connected");
		return false;
		}
		return false;
		}

	public AlertDialog showAlert(String title, String message){
		 AlertDialog alertDialog;
		 alertDialog = new AlertDialog.Builder(this).create();
		 //alertDialog.setButton(, listener)
		 alertDialog.setTitle(title);
		 alertDialog.setMessage(message);
		 //alertDialog.show();
		 return alertDialog;
	 }
	
	class SheelMaayaaBroadCastRec extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(dialog != null)	
         		dialog.dismiss();
			Log.e(TAG, intent.getAction());
			String action = intent.getAction();
			int httpStatus = intent.getExtras().getInt(HTTP_STATUS);
			Log.e(TAG, "HTTPSTATUS: "+ httpStatus);
			
			if( httpStatus == HttpStatus.SC_OK)
			{
				if (action.equals(HTTP_CHECK_REGISTERED))
				{
					String responseStr = intent.getExtras().getString(HTTP_RESPONSE);
					
					Toast.makeText(NewUserActivity.this, responseStr, Toast.LENGTH_LONG).show();
					if(responseStr.equals("false")){
						System.out.println("Not found");
						String path = "/registeruser";/*" + faceBookID + "/" + email + "/"
							+ firstName + "/" + middleName + "/" + lastName + "/"
							+ mobileNumber + "/" + nationality + "/" + passportNumber
							+ "/" + gender + "/" + passportImage;*/

						User user = new User("", firstName, middleName, lastName, passportImage, passportNumber, 
								email, mobileNumber, faceBookID, gender, nationality);
						
						Gson gson = new Gson();
						String input = gson.toJson(user);
						System.out.println("GSONSTRING: " + input);
						
						HTTPManager.startHttpService(path, input, HTTP_REGISTER_USER, getApplicationContext());
						Toast.makeText(NewUserActivity.this, "Successful Registration", Toast.LENGTH_LONG).show();
						//LoggedID = faceBookID;
						System.out.println("Done in DataBase");
						System.out.println(passportImage.length());
						//System.out.println("LogID " + LoggedID);
						
					}
					else if(responseStr.equalsIgnoreCase("true")){
						AlertDialog a = showAlert("Registration failed","Sorry you are already registered");
						a.setButton("ok", new android.content.DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent statedIntent = setSessionInformationBetweenActivities(ConnectorUserActionsActivity.class);
								statedIntent.putExtra(ConnectorUserActionsActivity.LOGGED_ID_KEY, faceBookID);
								startActivity(statedIntent);
							}
						});
						a.show();
						System.out.println("Already Registered");
					}
					// Dialog dismissing
					//if(dialog != null) dialog.dismiss();
					Log.e(TAG, responseStr);
						
				}
				else if (action.equals(HTTP_REGISTER_USER))
				{
					Intent statedIntent = setSessionInformationBetweenActivities(ConnectorUserActionsActivity.class);
					statedIntent.putExtra(ConnectorUserActionsActivity.LOGGED_ID_KEY, faceBookID);
					startActivity(statedIntent);
					finish();
				}
			
			}
			
		}
	}
	
	public void OnClick_register(View v) {
		//if(isInternetOn())
		if(InternetManager.isInternetOn(getApplicationContext()))
		{
			// INTERNET IS AVAILABLE, DO STUFF..
			System.out.println("CONNECCTIVITY OK");
			
		boolean registered = false;
		GetUserFacebookID();
		//faceBookID = Session.facebookUserId;
		faceBookID = getFacebookService().getFacebookUser().getUserId();
		

	//	mobileNumber = (mobileNumberField.getText().toString());
		//mobileNumber = mobileNumber.trim();
		
		//firstName = firstNameField.getText().toString();
		//firstName = firstName.trim();
		
		//middleName = middleNameField.getText().toString();
		//middleName = middleName.trim();
		
		if(middleName.length() == 0){ 
			middleName = "%20";
			System.out.println("Middle: " + middleName);}
		
		//lastName = lastNameField.getText().toString();
		//lastName = lastName.trim();
		
		//email = emailField.getText().toString();
		//email = email.trim();
		
		passportNumber = passportNumberField.getText().toString();
		passportNumber = passportNumber.trim();
		
		nationality = nationalityField.getText().toString();
		nationality = nationality.trim();
		if(nationality.length() == 0) {nationality = "%20";}
		if(gender.length() == 0) gender = "%20";
		
		userValidationCode = validationCodeField.getText().toString();

		System.out.println("Before Validate " + allValid);
		validate();
		System.out.println("After Validate " + allValid);
		if (allValid == false)
			Toast.makeText(this, "Please fill all the data", 0).show();
		else {

			dialog = ProgressDialog.show(NewUserActivity.this, "", "Registering, Please wait..", true, false);
			dialog.setCancelable(false);
			
			
			String path = "/checkRegistered/" + faceBookID;
			
			
			//sc.runHttpRequest("/checkRegistered/" + faceBookID);
			HTTPManager.startHttpService(path, HTTP_CHECK_REGISTERED, getApplicationContext());
			//passportImage = "PassPortImage";
			
		}
		}
		else{
			// NO INTERNET AVAILABLE, DO STUFF..
			
				showAlert("Network connection","Rgistration failed because no network connectivity was detected").show();
			}
	}


}
