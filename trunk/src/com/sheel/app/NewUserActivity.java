package com.sheel.app;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

	/** Path string where the taken passport photo is saved */
	String path;
	/** Male Toggle Button. Not required to register */
	ToggleButton toggleMale;
	/** Female Toggle Button. Not required to register */
	ToggleButton toggleFemale; // female
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

	String gender = ""; /* Declaration of gender string */
	String firstName; /* Declaration of first name string */
	String middleName; /* Declaration of middle name string */
	String lastName; /* Declaration of last name string */
	/** String containing the passport image encoded data string */
	String passportImage;
	String passportNumber; /* Declaration of passport string */
	String email; /* Declaration of email string */
	String mobileNumber; /* Declaration of mobile number string */
	String nationality; /* Declaration of nationality string */
	/** String containing the user facebook ID */
	String faceBookID;
	
	String LoggedID;

	/**
	 * Boolean variable to check that all the required fields are filled and in
	 * the right format. True if everything is valid. False otherwise
	 */
	boolean allValid = false;
	
	boolean codeValid = false;
	boolean nationalityValid = false;
	boolean gotResponse = false;

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
		getFacebookService().login(this, false, false);
		// call the setVariables
		setVariables();

	}

	/**
	 * This method is called to create all the UI components and create and set
	 * the adaptors to the AutoComplete fields
	 */
	public void setVariables() {
		/* Gender Toggle Buttons */
		toggleMale = (ToggleButton) findViewById(R.id.toggleMale);
		toggleFemale = (ToggleButton) findViewById(R.id.toggleFemale);

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

					// Toast toast = Toast.makeText(NewUserActivity.this,
					// "Valid", 0);
					// toast.show();
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

		/* First, middle and last names fields */
		firstNameField = (EditText) findViewById(R.id.FirstName);
		middleNameField = (EditText) findViewById(R.id.MiddleName);
		lastNameField = (EditText) findViewById(R.id.LastName);

		/* Email field */
		emailField = (EditText) findViewById(R.id.email);

		/* Passport number field */
		passportNumberField = (EditText) findViewById(R.id.passNum);

	} // end setVariables

	/** Method called when male toggle button is clicked */
	public void onClick_male(View v) {
		if(toggleMale.isChecked()){
			gender = "";
			System.out.println("1");
		}
		else{
		// Uncheck the female toggle button
		toggleFemale.setChecked(false);
		// Set the gender to male
		gender = "male";
		}
	} // end onClick_male

	/** Method called when female toggle button is clicked */
	public void onClick_female(View v) {
		if(!toggleFemale.isChecked()){
			System.out.println("2");
			gender = "";
		}
		else{
		// Uncheck the male toggle button
		toggleMale.setChecked(false);
		// Set the gender to male
		gender = "female";
		}
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
		path = Environment.getExternalStorageDirectory().getName()
				+ File.separatorChar + "Android/data/"
				+ NewUserActivity.this.getPackageName() + "/PassportPhoto.jpg";

		/** The file containing the taken passport photo */
		File file = new File(path);

		try {
			// Create a file at the location specified in the path
			if (file.exists() == false) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

		} catch (IOException e) {
			Log.e(TAG, "Could not create file.", e);
		}
		Log.i(TAG, path);

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
			System.out.println(path);
			Toast.makeText(this, "Fine", Toast.LENGTH_LONG).show();
			System.out.println("First IF");
			// User done with capturing image
			if (resultCode == -1) {
				// Image captured and saved to fileUri specified in the Intent
				onPhotoTaken();
				System.out.println("Greaaaaaaaat1111");
				Toast.makeText(this, "Great111", Toast.LENGTH_LONG).show();
				// System.out.println(data.getData().toString());

				// Toast.makeText(this, "Image saved to:\n" +
				// data.getData(), Toast.LENGTH_LONG).show();
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
				System.out.println("Cancell");
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
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the
																// bitmap object
		byte[] image = baos.toByteArray();

		passportImage = Base64.encodeBytes(image);
		System.out.println(passportImage);
		i.setImageBitmap(bitmap);

		// Intent mIntent = new Intent(this, NewUserActivity.class);
		// Pass variable to detailed view activity using the intent
		// putExtra(NewUserActivity.PASSPORT_IMAGE_KEY, encodedImage);
		// getIntent().putExtra(NewUserActivity.PASSPORT_IMAGE_KEY, );
		System.out.println("Greaaaaaaaat222");
		Toast.makeText(this, "Great222", Toast.LENGTH_LONG).show();
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

	public void validate() {
		String countryCode = countryCodes.getText().toString();
		countryCodes.getValidator().isValid(countryCode);

		//nationality = nationalityField.getText().toString();
		if(!nationality.equals("%20")){
			nationalityField.getValidator().isValid(nationality);
			System.out.println("Went to validator");
		}
		else{
			nationalityValid = true;
		}
		emailValidation(email);

		if (nationalityValid && codeValid && mobileNumber.length() > 5 && firstName.length() > 0
				&& lastName.length() > 0 && email.length() > 0
				&& passportNumber.length() > 0)
			allValid = true;
		else
			allValid = false;
	}

	public String GetUserFacebookID() {
		String tempID = "";
		getFacebookService().getUserInformation(true);
		if (getFacebookService() != null) {

			if (!getFacebookService().getFacebookUser()
					.isRequestedBeforeSuccessfully()) {
				getFacebookService().getUserInformation(true);
			} else {
				tempID = getFacebookService().getFacebookUser().getUserId();
			}

		}

		System.out.println("FB ID: " + tempID);
		return tempID;
	}

	public void OnClick_register(View v) {
		faceBookID = GetUserFacebookID();

		String countryCode = countryCodes.getText().toString();

		mobileNumber = (mobileNumberField.getText().toString());
		firstName = firstNameField.getText().toString();
		middleName = middleNameField.getText().toString();
		
		if(middleName.length() == 0){ 
			middleName = "%20";
			System.out.println("Middle: " + middleName);}
		lastName = lastNameField.getText().toString();
		email = emailField.getText().toString();
		passportNumber = passportNumberField.getText().toString();
		nationality = nationalityField.getText().toString();
		nationality = nationality.trim();
		if(nationality.length() == 0) {nationality = "%20";}
		System.out.println("Before check gender length");
		if(gender.length() == 0) gender = "%20";
		
		System.out.println("Before Validate");

		validate();
		System.out.println("After Validate");
		if (allValid == false)
			Toast.makeText(this, "Please fill all the data", 0).show();
		else {

			String phoneCode = countryCode.toString().split(" ")[1];
			mobileNumber = phoneCode + mobileNumber;
			String toToast = mobileNumber + " " + firstName + " " + middleName
					+ " " + lastName + " " + email + " " + passportNumber + " "
					+ gender;

			Toast toast = Toast.makeText(this, toToast, 0);
			toast.show();

			// /////////////////
			
			SheelMaaayaClient sc = new SheelMaaayaClient() {

				@Override
				public void doSomething() {
					final String str = this.rspStr;

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// Toast.makeText(this, "Done in DataBase", //
							// Toast.LENGTH_LONG).show();
							LoggedID = str;
							System.out.println("Done in DataBase");
							System.out.println(passportImage.length());
							System.out.println("LogID " + LoggedID);
							Intent statedIntent = setSessionInformationBetweenActivities(ConnectorUserActionsActivity.class);
							statedIntent.putExtra(ConnectorUserActionsActivity.LOGGED_ID_KEY, LoggedID);
							startActivity(statedIntent);

						}
					});

				}
			};
			
			

			passportImage = "PassPortImage";
			

			System.out.println("/insertuser/" + faceBookID + "/" + email + "/"
					+ firstName + "/" + middleName + "/" + lastName + "/"
					+ mobileNumber + "/" + nationality + "/" + passportNumber
					+ "/" + gender + "/" + passportImage);
			sc.runHttpRequest("/insertuser/" + faceBookID + "/" + email + "/"
					+ firstName + "/" + middleName + "/" + lastName + "/"
					+ mobileNumber + "/" + nationality + "/" + passportNumber
					+ "/" + gender + "/" + passportImage);
			Toast.makeText(getApplicationContext(), "Done with Database",
					Toast.LENGTH_SHORT).show();

			/*
			 * SheelMaayaRegisterClient sc2 = new SheelMaayaRegisterClient() {
			 * 
			 * @Override public void doSomething() { runOnUiThread(new
			 * Runnable() {
			 * 
			 * @Override public void run() { // Toast.makeText(this,
			 * "Done in DataBase", // Toast.LENGTH_LONG).show();
			 * System.out.println("Done in DataBase222"); //
			 * System.out.println(passportImage.length());
			 * 
			 * } });
			 * 
			 * }
			 * 
			 * };
			 * 
			 * List<NameValuePair> params = new LinkedList<NameValuePair>();
			 * params.add(new BasicNameValuePair("email", email));
			 * params.add(new BasicNameValuePair("firstName", firstName));
			 * params.add(new BasicNameValuePair("middleName", middleName));
			 * params.add(new BasicNameValuePair("lastName", lastName));
			 * params.add(new BasicNameValuePair("mobileNumber", mobileNumber));
			 * params.add(new BasicNameValuePair("nationality", nationality));
			 * params.add(new BasicNameValuePair("passportNumber",
			 * passportNumber)); params.add(new BasicNameValuePair("gender",
			 * gender)); params.add(new BasicNameValuePair("passportPhoto",
			 * passportImage));
			 * 
			 * sc2.runHttpRequest(params);
			 */
			

		}
	}

}
