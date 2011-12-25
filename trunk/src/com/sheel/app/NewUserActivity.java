package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_CHECK_REGISTERED;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_REGISTER_USER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.spec.PSSParameterSpec;
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
import com.sheel.utils.GuiUtils;
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

	/**
	 * Request code for camera image activity
	 */
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	/**
	 * Uri for the passport imafe file
	 */
	private Uri fileUri;

	/**
	 * Tag for MyActivity
	 */
	private static final String TAG = "MyActivity";

	/**
	 * Key for position used to get the activity the user needs to be diverted
	 * to, after registration
	 */
	public static final String POSITION_KEY = "position";

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
	 * Dialog used to show the progress bar at registration time
	 */
	ProgressDialog dialog;

	/** Path string where the taken passport photo is saved */
	String ImagePath = "";
	/** AutoComplete field for mobile country codes. Required to register */
	AutoCompleteTextView countryCodes;
	/** AutoComplete field for nationality. Not required to register */
	AutoCompleteTextView nationalityField;
	/** Mobile number text field. Required to register */
	EditText mobileNumberField;
	/** Passport number field. Required to register */
	EditText passportNumberField;
	/**
	 * Mobile Number validation field, used to enter the validation code sent in message. Required to register
	 */
	EditText validationCodeField;

	/**
	 * Image view to preview the passport image thumbnail taken by the user
	 */
	ImageView i;

	String gender = ""; /* Declaration of gender string */
	String firstName; /* Declaration of first name string */
	String middleName; /* Declaration of middle name string */
	String lastName; /* Declaration of last name string */
	/** String containing the passport image encoded data string */
	String passportImage = "image";
	String passportNumber; /* Declaration of passport string */
	String email; /* Declaration of email string */
	String mobileNumber; /* Declaration of mobile number string */
	static String nationality; /* Declaration of nationality string */
	/** String containing the user facebook ID */
	String faceBookID;

	String LoggedID;

	/**
	 * Validation code sent by message to the user. Default 853589
	 */
	int code = 853589;
	/**
	 * The validation code the user has entered
	 */
	String userValidationCode;
	/**
	 * Boolean variable to check that all the required fields are filled and in
	 * the right format. True if everything is valid. False otherwise
	 */
	boolean allValid = false;
	
	/**
	 * Boolean variable to check if validation code is valid
	 */
	boolean codeValid = false;
	/**
	 * Boolean variable to check if the nationality entered by the user is valid
	 */
	boolean nationalityValid = false;
	/**
	 * Boolean variable to check if the server response was received
	 */
	boolean gotResponse = false;
	/**
	 * Boolean variable to check that the user has taken the photo of his passport
	 */
	boolean photoTaken = false;
	/**
	 * Boolean variable to check if the mobile number entered by the user is valid
	 */
	boolean mobileValid = false;

	/**
	 * Array of all the nationalities
	 */
	String[] nationalityStrings;
	
	/**
	 * Bitmap of the passport photo
	 */
	Bitmap bitmap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("OnCreate");
		// Set the contentView of this activity to the the register
		setContentView(R.layout.register);

		// call the setVariables
		setVariables();

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		savedInstanceState.putString("MobileCode", countryCodes.getText()
				.toString());
		savedInstanceState.putString("MobileNumber", mobileNumberField
				.getText().toString());
		savedInstanceState.putString("ValidationCode", validationCodeField
				.getText().toString());
		savedInstanceState.putString("Nationality", nationalityField.getText()
				.toString());
		savedInstanceState.putString("PassportNumber", passportNumberField
				.getText().toString());
		savedInstanceState.putInt("ValCode", code);
		savedInstanceState.putString("ImageView", ImagePath);
		System.out.println("In SaveInstanceState ");

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		countryCodes.setText(savedInstanceState.getString("MobileCode"));
		mobileNumberField.setText(savedInstanceState.getString("MobileNumber"));
		validationCodeField.setText(savedInstanceState
				.getString("ValidationCode"));
		nationalityField.setText(savedInstanceState.getString("Nationality"));
		passportNumberField.setText(savedInstanceState
				.getString("PassportNumber"));
		ImagePath = savedInstanceState.getString("ImageView");
		code = savedInstanceState.getInt("ValCode");
		if (ImagePath.length() > 2)
			onPhotoTaken();
		System.out.println("onRestoreInstanceState");
	}

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			System.out.println("CATCHED");
		}
		System.out.println("onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {

		System.out.println("onPause");
		super.onPause();

		// Cancel out the dialog
		dialog = null;
	}

	@Override
	protected void onRestart() {
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			System.out.println("CATCHED In On Restart");
		}
		System.out.println("onRestart");
		super.onRestart();

	}

	@Override
	protected void onResume() {
		System.out.println("onResume");
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
		nationalityStrings = getResources().getStringArray(
				R.array.nationalities_array);
		ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<String>(
				this, R.layout.list_item, nationalityStrings);
		nationalityField.setAdapter(nationalityAdapter);
		Validator NationalityValidator = new Validator() {

			// @Override
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
				Toast toast = Toast
						.makeText(NewUserActivity.this, getResources()
								.getString(R.string.nationality_toast), 0);
				toast.show();
				return false;
			}

			// @Override
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

			// @Override
			public boolean isValid(CharSequence text) {
				Log.v("Test", "Checking if valid: " + text);
				Arrays.sort(codeStrings);
				if (Arrays.binarySearch(codeStrings, text.toString()) > 0) {
					codeValid = true;
					return true;
				}
				codeValid = false;
				Toast toast = Toast.makeText(NewUserActivity.this,
						getResources().getString(R.string.country_code_toast),
						0);
				toast.show();
				return false;
			}

			// @Override
			public CharSequence fixText(CharSequence invalidText) {

				return invalidText;
			}
		};
		countryCodes.setValidator(MobileValidator);

		/* Mobile number field */
		mobileNumberField = (EditText) findViewById(R.id.mobileNumber);

		/* Passport number field */
		passportNumberField = (EditText) findViewById(R.id.passNum);
		validationCodeField = (EditText) findViewById(R.id.validationCode);
		i = (ImageView) findViewById(R.id.pictureView);
	} // end setVariables

	/** Method called when the take photo button is clicked to open the camera */
	public void onClick_takePhoto(View v) {

		// if(bitmap!=null) bitmap.recycle();
		// Set the path where the taken photo will be saved
		ImagePath = Environment.getExternalStorageDirectory().getPath()
				+ File.separatorChar + "Android/data/"
				+ NewUserActivity.this.getPackageName() + File.separatorChar
				+ "PassportPhoto.jpg";
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
				Toast.makeText(
						this,
						getResources().getString(
								R.string.camera_cancelled_toast),
						Toast.LENGTH_LONG).show();
			} else {
				// Image capture failed, advise user
				Toast.makeText(this,
						getResources().getString(R.string.camera_failed_toast),
						Toast.LENGTH_LONG).show();
			}
		}

	}

	protected void onPhotoTaken() {

		bitmap = BitmapFactory.decodeFile(ImagePath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos); // bm is the
																// bitmap object
		byte[] image = baos.toByteArray();

		passportImage = Base64.encodeBytes(image);
		System.out.println("Photo: " + passportImage);
		i.setImageBitmap(bitmap);
		System.out.println("Photo Success");

	}

	public void OnClick_mobileValidate(View v) {

		mobileNumber = (mobileNumberField.getText().toString());
		String countryCode = countryCodes.getText().toString();
		System.out.println("MOB: " + mobileNumber.length() + " Code: "
				+ countryCode.length());
		if (mobileNumber.length() < 1 || countryCode.length() < 1) {
			Toast.makeText(getBaseContext(),
					getResources().getString(R.string.mobile_toast),
					Toast.LENGTH_SHORT).show();
		} else if (codeValid) {
			validationCodeField.setEnabled(true);

			double code1 = Math.random() * 12345;
			code = (int) code1;

			String SENT = "SMS_SENT";
			String DELIVERED = "SMS_DELIVERED";

			String phoneCode = countryCode.toString().split(" ")[countryCode
					.toString().split(" ").length - 1];
			mobileNumber = phoneCode + mobileNumber;

			mobileNumber = mobileNumber.trim();
			mobileNumber = mobileNumber.replace("(", "");
			mobileNumber = mobileNumber.replace(")", "");
			mobileNumber = mobileNumber.replace(" ", "");
			System.out.println("MOB: " + mobileNumber + " Code: " + code);

			// ---when the SMS has been sent---
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
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

			// ---when the SMS has been delivered---
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
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
			sms.sendTextMessage(
					mobileNumber,
					mobileNumber,
					("This is a message from SheelMaaya /n Validation Code:" + code),
					null, null);
			Toast.makeText(this, getResources().getString(R.string.sms_toast),
					Toast.LENGTH_LONG).show();
		} else {
			Toast toast = Toast.makeText(NewUserActivity.this, getResources()
					.getString(R.string.country_code_toast), 0);
			toast.show();

		}
	}

	public void photoValidation(String passporttemp) {
		if (passporttemp.length() > 0) {
			photoTaken = true;

		} else {
			photoTaken = false;
			Toast.makeText(this,
					getResources().getString(R.string.passport_photo_toast),
					Toast.LENGTH_LONG).show();
		}
	}

	public void validate() {
		String countryCode = countryCodes.getText().toString();

		countryCodes.getValidator().isValid(countryCode);

		if (codeValid) {
			if (!nationality.equals("-1")) {
				nationalityField.getValidator().isValid(nationality);
			} else {
				nationalityValid = true;
			}
			if (nationalityValid) {

				if (userValidationCode.length() > 0
						&& code == Integer.parseInt(userValidationCode)) {
					System.out.println(code + " " + userValidationCode);
					mobileValid = true;
				} else {
					Toast.makeText(
							this,
							getResources().getString(
									R.string.validation_mismatch_toast), 0)
							.show();
				}
				if (mobileValid) {
					if (!(passportNumber.length() > 0)) {
						Toast.makeText(
								this,
								getResources().getString(
										R.string.passport_number_toast), 0)
								.show();
					} else {
						photoValidation(passportImage);
					}
				}
			}
		}

		if (nationalityValid && codeValid && photoTaken && mobileValid
				&& mobileNumber.length() > 2 && passportNumber.length() > 0)
			allValid = true;
		else
			allValid = false;
	}

	public void GetUserFacebookID() {
		String tempID = "";
		if (getFacebookService() != null) {

			if (!getFacebookService().getFacebookUser()
					.isRequestedBeforeSuccessfully()) {

			} else {
				tempID = getFacebookService().getFacebookUser().getUserId();
				firstName = getFacebookService().getFacebookUser()
						.getFirstName();
				firstName = firstName.trim();
				if (firstName.length() == 0)
					firstName = "%20";
				middleName = getFacebookService().getFacebookUser()
						.getMiddleName();
				middleName = middleName.trim();
				if (middleName.length() == 0)
					middleName = "%20";
				lastName = getFacebookService().getFacebookUser().getLastName();
				lastName = lastName.trim();
				if (lastName.length() == 0)
					lastName = "%20";
				email = getFacebookService().getFacebookUser().getEmail();
				email = email.trim();
				if (email.length() == 0)
					email = "%20";
				if (getFacebookService().getFacebookUser().isFemale())
					gender = "female";
				else
					gender = "male";
			}

		}

		System.out.println("FB ID: " + tempID + " email:" + email);
	}

	public String getNationalityIndex(String nationality) {
		System.out.println("In nationality index");
		for (int i = 0; i < nationalityStrings.length; i++) {
			if (nationality.equals(nationalityStrings[i]))
				return i + "";
		}

		return -1 + "";
	}

	public AlertDialog showAlert(String title, String message) {
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		return alertDialog;
	}

	class SheelMaayaaBroadCastRec extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (dialog != null)
				dialog.dismiss();
			Log.e(TAG, intent.getAction());
			String action = intent.getAction();
			int httpStatus = intent.getExtras().getInt(HTTP_STATUS);
			Log.e(TAG, "HTTPSTATUS: " + httpStatus);

			if (httpStatus == HttpStatus.SC_OK) {
				if (action.equals(HTTP_CHECK_REGISTERED)) {
					String responseStr = intent.getExtras().getString(
							HTTP_RESPONSE);

					if (responseStr.equals("false")) {
						System.out.println("Not found");
						String path = "/registeruser";

						User user = new User("", firstName, middleName,
								lastName, passportImage, passportNumber, email,
								mobileNumber, faceBookID, gender,
								getNationalityIndex(nationality));

						Gson gson = new Gson();
						String input = gson.toJson(user);
						System.out.println("GSONSTRING: " + input);

						dialog = ProgressDialog.show(
								NewUserActivity.this,
								"",
								getResources().getString(
										R.string.registering_dialog), true,
								false);
						dialog.setCancelable(false);

						HTTPManager.startHttpService(path, input,
								HTTP_REGISTER_USER, getApplicationContext());
						Toast.makeText(
								NewUserActivity.this,
								getResources().getString(
										R.string.success_registration_toast),
								Toast.LENGTH_LONG).show();

						System.out.println("Done in DataBase");

					} else if (responseStr.equalsIgnoreCase("true")) {

						Bundle extras = getIntent().getExtras();
						int pos = 0;
						if (extras != null)
							pos = extras.getInt(POSITION_KEY);
						System.out.println("Navigating too: "
								+ NAVIGATION_ITEMS[pos].getActivityType()
										.getName());

						Intent navigateToIntent = new Intent(getBaseContext(),
								NAVIGATION_ITEMS[pos].getActivityType());
						startActivity(navigateToIntent);
						finish();

						System.out.println("Already Registered");
					}
					// Dialog dismissing
					// if(dialog != null) dialog.dismiss();
					Log.e(TAG, responseStr);

				} else if (action.equals(HTTP_REGISTER_USER)) {

					Bundle extras = getIntent().getExtras();
					int pos = 0;
					if (extras != null)
						pos = extras.getInt(POSITION_KEY);
					System.out
							.println("Navigating too: "
									+ NAVIGATION_ITEMS[pos].getActivityType()
											.getName());

					Intent navigateToIntent = new Intent(getBaseContext(),
							NAVIGATION_ITEMS[pos].getActivityType());
					startActivity(navigateToIntent);
					finish();
				}

			}

		}
	}

	public void OnClick_register(View v) {
		if (InternetManager.isInternetOn(getApplicationContext())) {
			// INTERNET IS AVAILABLE, DO STUFF..
			System.out.println("CONNECCTIVITY OK");

			GetUserFacebookID();
			faceBookID = getFacebookService().getFacebookUser().getUserId();

			mobileNumber = (mobileNumberField.getText().toString());
			String countryCode = countryCodes.getText().toString();
			String phoneCode = countryCode.toString().split(" ")[countryCode
					.toString().split(" ").length - 1];
			mobileNumber = phoneCode + mobileNumber;

			mobileNumber = mobileNumber.trim();
			mobileNumber = mobileNumber.replace("(", "");
			mobileNumber = mobileNumber.replace(")", "");
			mobileNumber = mobileNumber.replace(" ", "");

			if (middleName.length() == 0) {
				middleName = "%20";
				System.out.println("Middle: " + middleName);
			}

			passportNumber = passportNumberField.getText().toString();
			passportNumber = passportNumber.trim();

			nationality = nationalityField.getText().toString();
			nationality = nationality.trim();
			if (nationality.length() == 0) {
				nationality = "-1";
			}
			if (gender.length() == 0)
				gender = "%20";

			userValidationCode = validationCodeField.getText().toString();

			validate();
			if (allValid) {

				String path = "/checkRegistered/" + faceBookID;

				HTTPManager.startHttpService(path, HTTP_CHECK_REGISTERED,
						getApplicationContext());
			}
		} else {
			// NO INTERNET AVAILABLE, DO STUFF..

			showAlert(
					getResources()
							.getString(R.string.network_fail_header_alert),
					getResources().getString(
							R.string.network_fail_message_alert)).show();
		}
	}

}
