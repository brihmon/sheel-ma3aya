package com.sheel.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
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

public class NewUserActivity extends Activity {

	public static final String FIRST_NAME_KEY = "firstName";
	public static final String MIDDLE_NAME_KEY = "middleName";
	public static final String LAST_NAME_KEY = "lastName";
	public static final String EMAIL_KEY = "email";
	public static final String GENDER_KEY = "gender";
	public static final String PASSPORT_IMAGE_KEY = "passportImage";

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	private static final String TAG = "MyActivity";

	String path;

	ToggleButton toggleMale; // male
	ToggleButton toggleFemale; // female

	AutoCompleteTextView countryCodes;
	EditText mobileNumberField;
	EditText firstNameField;
	EditText middleNameField;
	EditText lastNameField;
	EditText emailField;
	EditText passportNumberField;
	EditText nationalityField;

	String gender;
	String firstName;
	String middleName;
	String lastName;
	String passportImage;
	String passportNumber;
	String email;
	String mobileNumber;
	String nationality;

	boolean allValid = false;

	public void NewUser() {
		String gender = "";
		String firstName = "";
		String middleName = "";
		String lastName = "";
		String email = "";

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			firstName = extras.getString(FIRST_NAME_KEY);
			middleName = extras.getString(MIDDLE_NAME_KEY);
			lastName = extras.getString(LAST_NAME_KEY);
			email = extras.getString(EMAIL_KEY);
			gender = extras.getString(GENDER_KEY);

		}// end if: extract info sent by the intent

		firstNameField.setText(firstName);
		middleNameField.setText(middleName);
		lastNameField.setText(lastName);
		emailField.setText(email);
		this.gender = gender;

		if (gender.equalsIgnoreCase("male"))
			toggleMale.setChecked(true);
		else if (gender.equalsIgnoreCase("female"))
			toggleFemale.setChecked(true);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		setVariables();
		NewUser();

	}

	public void setVariables() {

		toggleMale = (ToggleButton) findViewById(R.id.toggleMale);
		toggleFemale = (ToggleButton) findViewById(R.id.toggleFemale);

		countryCodes = (AutoCompleteTextView) findViewById(R.id.auto);
		final String[] codeStrings = getResources().getStringArray(
				R.array.codes);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, codeStrings);
		countryCodes.setAdapter(adapter);
		Validator validator = new Validator() {

			@Override
			public boolean isValid(CharSequence text) {
				Log.v("Test", "Checking if valid: " + text);
				Arrays.sort(codeStrings);
				if (Arrays.binarySearch(codeStrings, text.toString()) > 0) {
					allValid = true;

					// Toast toast = Toast.makeText(NewUserActivity.this,
					// "Valid", 0);
					// toast.show();
					return true;
				}
				allValid = false;
				Toast toast = Toast.makeText(NewUserActivity.this,
						"Please insert a valid country", 0);
				toast.show();
				return false;
			}

			@Override
			public CharSequence fixText(CharSequence invalidText) {
				// Log.v("Test", "Returning fixed text");
				// Toast toast = Toast.makeText(NewUserActivity.this, "Fixing",
				// 0);
				// toast.show();
				return invalidText;
			}
		};

		countryCodes.setValidator(validator);

		mobileNumberField = (EditText) findViewById(R.id.mobileNumber);

		firstNameField = (EditText) findViewById(R.id.FirstName);
		middleNameField = (EditText) findViewById(R.id.MiddleName);
		lastNameField = (EditText) findViewById(R.id.LastName);

		emailField = (EditText) findViewById(R.id.email);

		passportNumberField = (EditText) findViewById(R.id.passNum);

	}

	public void onClick_male(View v) {
		toggleFemale.setChecked(false);
		gender = "male";
	}

	public void onClick_female(View v) {
		toggleMale.setChecked(false);
		gender = "female";
	}

	public void onClick_takePhoto(View v) {
		/*
		 * Intent photoIntent = new Intent(this, TakePhotoActivity.class);
		 * startActivity(photoIntent); passportImage =
		 * photoIntent.getExtras().getString(PASSPORT_IMAGE_KEY);
		 * System.out.println(passportImage);
		 */
		path = Environment.getExternalStorageDirectory().getName()
				+ File.separatorChar + "Android/data/"
				+ NewUserActivity.this.getPackageName() + "/PassportPhoto.jpg";
		File file = new File(path);
		try {
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

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
															// name

		// start the image capture Intent
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			System.out.println(path);
			Toast.makeText(this, "Fine", Toast.LENGTH_LONG).show();
			System.out.println("First IF");
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

	public void validate() {
		String countryCode = countryCodes.getText().toString();
		countryCodes.getValidator().isValid(countryCode);

		if (allValid && mobileNumber.length() > 5 && firstName.length() > 0
				&& middleName.length() > 0 && lastName.length() > 0
				&& email.length() > 0 && passportNumber.length() > 0
				&& gender != null)
			allValid = true;
		else
			allValid = false;
	}

	public void OnClick_register(View v) {
		String countryCode = countryCodes.getText().toString();

		mobileNumber = (mobileNumberField.getText().toString());
		firstName = firstNameField.getText().toString();
		middleName = middleNameField.getText().toString();
		lastName = lastNameField.getText().toString();
		email = emailField.getText().toString();
		passportNumber = passportNumberField.getText().toString();
		nationality = "Egyptian";

		validate();
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
			/*SheelMaaayaClient sc = new SheelMaaayaClient() {

				@Override
				public void doSomething() {
					// final String str = this.rspStr;

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// Toast.makeText(this, "Done in DataBase",
							// Toast.LENGTH_LONG).show();
							System.out.println("Done in DataBase");
							System.out.println(passportImage.length());

							
						}
					});

				}
			};

			// passportImage = "PassPortImage";

			sc.runHttpRequest("/insertuser/" + email + "/" + firstName + "/"
					+ middleName + "/" + lastName + "/" + mobileNumber + "/"
					+ nationality + "/" + passportNumber + "/" + gender + "/"
					+ passportImage);
			Toast.makeText(getApplicationContext(), "Done with Database",
					Toast.LENGTH_SHORT).show();
			*/

			SheelMaayaRegisterClient sc2 = new SheelMaayaRegisterClient() {

				@Override
				public void doSomething() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// Toast.makeText(this, "Done in DataBase",
							// Toast.LENGTH_LONG).show();
							System.out.println("Done in DataBase222");
							//System.out.println(passportImage.length());

							
						}
					});

				}

			};
			

			List<NameValuePair> params = new LinkedList<NameValuePair>();
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("firstName", firstName));
			params.add(new BasicNameValuePair("middleName", middleName));
			params.add(new BasicNameValuePair("lastName", lastName));
			params.add(new BasicNameValuePair("mobileNumber", mobileNumber));
			params.add(new BasicNameValuePair("nationality", nationality));
			params.add(new BasicNameValuePair("passportNumber", passportNumber));
			params.add(new BasicNameValuePair("gender", gender));
			params.add(new BasicNameValuePair("passportPhoto",passportImage));

			sc2.runHttpRequest(params);
			
			startActivity(new Intent(this, ConnectorUserActionsActivity.class));

		}
	}

}
