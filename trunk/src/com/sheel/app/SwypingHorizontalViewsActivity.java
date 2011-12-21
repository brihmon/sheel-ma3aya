package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_CONFIRM_OFFER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sheel.adapters.HorizontalSwypingPagingAdapter;
import com.sheel.app.MyOffersActivity.SheelMaayaaBroadCastRec;
import com.sheel.datastructures.Category;
import com.sheel.datastructures.Confirmation;
import com.sheel.datastructures.OfferDisplay2;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * Activity used for displaying multiple views in a horizontal-sliding
 * manner. It has built in page indicator  with categories names as its 
 * title.
 * <br><b>For best results, the view should not be used with a lot of 
 * categories (i.e. more than 4)</b>
 * 
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class SwypingHorizontalViewsActivity extends UserSessionStateMaintainingActivity {
	
	private static final String TAG = MyOffersActivity.class.getName();
	
	
	
	/**
	 * Different categories of offers to be displayed
	 */
	 private ArrayList<Category> categories;
	/**
	 * Adapter using for handling pages swiping 
	 */
	private  HorizontalSwypingPagingAdapter swypeAdapter;
	
	
	/**
	 *  The receiver used for detecting the HTTP data arrival 
	 */
	private SheelMaayaaBroadCastRec receiver;
	
    /** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheel_offers_list);
        
        ViewPager swypePager = (ViewPager)findViewById(R.id.swypeView);
        swypeAdapter = new HorizontalSwypingPagingAdapter(new ArrayList<Category>(), getApplicationContext(), SwypingHorizontalViewsActivity.this, getFacebookService().getFacebookUser());
        
//        swypePager.setOnPageChangeListener(new MyPageChangeListener());
        Log.e("Swype Pager Listen Please: ", "Inside Swype Activity");
        
        categories = (ArrayList<Category>) getLastNonConfigurationInstance();
        
        if(categories == null)
        {
//          initializeContent();
        	categories = new ArrayList<Category>();
        	swypeAdapter = new HorizontalSwypingPagingAdapter(categories, getApplicationContext(), SwypingHorizontalViewsActivity.this, getFacebookService().getFacebookUser());
        	
        }
        else
        {        	
        	swypeAdapter.setCatagories(categories);
        	swypeAdapter.notifyDataSetChanged();        
        }
        
    	swypePager.setAdapter(swypeAdapter);

        TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.categoriesIndicator);
        titleIndicator.setViewPager(swypePager);

    }// end onCreate
    
	/**
     * Sets the input categories (data models) used for displaying 
     * the different categories contents
     * 
     * @param categories
     * 		Different categories representing the tabs
     * 
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public void setCategories (ArrayList<Category> categories) {
    	this.categories = categories;
    	// Notify swyper that views had a data change
    	swypeAdapter.notifyDataSetChanged();
    	
    }// end setCategories
    
    public void updateCategoryContent (ArrayList<OfferDisplay2> newOffersWrappers , int categoryIndex , boolean isAppend) {
    	
    	// Update the data of the category
    	categories.get(categoryIndex).setOffersDisplayed(newOffersWrappers, isAppend);
    	// Notify swyper that views had a data change
    	swypeAdapter.notifyDataSetChanged();
    	
    }// end updateCategoryContent
   
    
    /**
     * Get the categories of this class
     * 
     *  @author Hossam_Amer
     */
    public ArrayList<Category> getCategories()
    {
    	return this.categories;
    }// end getCategories
    
    /**
     * Used to add new categories to be displayed at any
     * time in the code and notify the activity with the changes.
     * 
     * @param newCategories
     * 		New categories that should be added to the swiping view
     * @param location
     * 		place to add the new categories (their order in the tabs).
     * 		If out of bounds, it will add to the end of the categories 
     * 		by default. Use -1 to append to the end
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public void addCategory (ArrayList<Category> newCategories, int location) {
    	
    	// check location is within range and add
    	if (location<0 || location>=this.categories.size()) {
    		this.categories.addAll(newCategories);
    	}// end if: range not within acceptable limits -> append to end
    	else {
    		this.categories.addAll(location, newCategories);
    	}// end else: add in the requested location
    	
    	/* If adapter is null -> no need to notify -> it is in the onCreate ->
    	 * Therefore the update will be detected*/
    	if (swypeAdapter != null) {
    		swypeAdapter.notifyDataSetChanged();
    	}//end if : notify if adapter exists
    	
    }// end addCategory
    
    /**
     * Used to add new categories to be displayed at any
     * time in the code and notify the activity with the changes
     * to the end of the swiping view.
     * 
     * @param newCategories
     * 		New categories that should be added to the swiping view
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public void addCategory (ArrayList<Category> newCategories) {
    	addCategory(newCategories,-1);    	
    }// end addCategories
    
    /**
     * Used to add new category to be displayed at any
     * time in the code and notify the activity with the changes
     * to the end of the swiping view.
     * 
     * @param newCategory
     * 		New category that should be added to the end of swiping view
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public void addCategory (Category newCategory) {
    	ArrayList<Category> newCategories = new ArrayList<Category>();
    	newCategories.add(newCategory);
    	addCategory(newCategories,-1);    	
    }// end addCategories
    
    /**
     * Used to add new category to be displayed at any
     * time in the code and notify the activity with the changes
     * to the end of the swiping view.
     * 
     * @param newCategory
     * 		New category that should be added to the end of swiping view
     * @param location
     * 		place to add the new categories (their order in the tabs).
     * 		If out of bounds, it will add to the end of the categories 
     * 		by default. Use -1 to append to the end
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public void addCategory (Category newCategory, int location) {
    	ArrayList<Category> newCategories = new ArrayList<Category>();
    	newCategories.add(newCategory);
    	addCategory(newCategories,location);    	
    }// end addCategories
    
    
    
    /**
     * Tester method for giving dummy data to categories
     * 
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    private void initializeContent() {
   
    	categories.add(new Category("Communicate", R.layout.communicate));
    	categories.add(new Category("My Offers", R.layout.my_offers_main));
    	categories.add(new Category("My Offers2", R.layout.my_offers_main));    
    }// end initializeContent
    
    /**
     * Testing method
     * @param v
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */    
    public void click_tester_updateWithOffers(View v) {
    	System.out.println("The tester button is working");
    	
    	ArrayList<OfferDisplay2> newOffers = new ArrayList<OfferDisplay2>();
    	newOffers.add(new OfferDisplay2("11", 1, "Passant"));
    	newOffers.add(new OfferDisplay2("22", 2, "Mohamed"));
    	updateCategoryContent(newOffers, 1, false);    	
    }
    
    /**
     * Testing method
     * @param v
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public void click_tester_updateWithOffers2(View v) {
    	System.out.println("The tester button is working");
    	
    	ArrayList<OfferDisplay2> newOffers = new ArrayList<OfferDisplay2>();
    	newOffers.add(new OfferDisplay2("11", 1, "Hossam"));
    	newOffers.add(new OfferDisplay2("22", 2, "Mahmoud"));
    	updateCategoryContent(newOffers, 2, true);   	
    	
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
//		dialog = null;
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
		
		/**
		 * Filter added for this activity to filter the actions received by the receiver
		 */
		IntentFilter filter2;
		
		filter2 = new IntentFilter();
		
		// Add the filters of your activity
		filter2.addAction(HTTP_CONFIRM_OFFER);
		
		receiver = new SheelMaayaaBroadCastRec();
		
		Log.e(TAG, "Receiver Registered: " + "SwypeActivity");
		registerReceiver(receiver, filter2);
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
	 * Used for returning the <code>categories</code> just before onDestroy()
	 * 
	 * @author Hossam_Amer
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return this.categories;
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
					responseStr = intent.getExtras().getString(HTTP_RESPONSE);
					if(action.equals(HTTP_CONFIRM_OFFER))
					{
						Log.e(TAG, responseStr);
						
						if(responseStr.equals(Confirmation.alreadyConfirmed))
							Toast.makeText(getApplicationContext(), "This Offer is already confirmed by two users.", Toast.LENGTH_SHORT).show();
						else if(responseStr.equals(Confirmation.confirmedByAnotherPerson))
							Toast.makeText(getApplicationContext(), "Sorry, this offer is already confirmed by another user.", Toast.LENGTH_SHORT).show();
						else
						{
							Log.e("updateConfirmedOffersOnUI(responseStr)", TAG);
							Log.e("hashas: ", responseStr + "");
							updateConfirmedOffersOnUI(responseStr);
							
						}
					}	
						/**
						 * if(half-confirmed by another user)
						 * Sorry!
						t * if(confirmed success)
						 * You confirmed the user + 7abashtakaanaat fel view lw fi!
						 * 
						 * if(offerOwner is confirming the offer)
						 * it should not re-appear, it is fetching again
						 * 
						 */
/*                        if(str.contains("12") || str.contains("13"))
                     {
                     sendMail
                    	 Toast.makeText(PhoneCommunication.this, "Offer has been confirmed!", Toast.LENGTH_LONG).show(); 
                     }
                     else if(str.contains("Success"))
                    	 Toast.makeText(PhoneCommunication.this, "You have confirmed the offer!", Toast.LENGTH_LONG).show();
                     else if(str.contains("Failure"))
                         Toast.makeText(PhoneCommunication.this, "You could not confirm this offer anymore!", Toast.LENGTH_LONG).show();
*/						
				
				}
			}

			private void updateConfirmedOffersOnUI(String responseStr) {
				// TODO Auto-generated method stub
				
				try {
					
					JSONObject confirmationJSON = new JSONObject(responseStr);
					Log.e("hashas confirmationJSON: ", confirmationJSON + "");
					
					Confirmation confirmation = Confirmation.mapConfirmation(confirmationJSON);
					Log.e("hashas confirmation", confirmation + "");
					
					if(confirmation.isStatusTransactionUser1() && confirmation.isStatusTransactionUser2())
					{
						Toast.makeText(getApplicationContext(), "Offer confirmed by two users", Toast.LENGTH_SHORT).show();
						
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
				
			}
		}

	
}// end class