package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_CONFIRM_OFFER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_CONFIRM_OFFER_UI;

import java.util.ArrayList;

import org.apache.http.HttpStatus;

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
import com.sheel.adapters.SearchResultsListAdapter;
import com.sheel.datastructures.Category;
import com.sheel.datastructures.Confirmation;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.listeners.InflateListener;
import com.sheel.utils.GuiUtils;
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
 *		Hossam Amer
 *
 */
public class SwypingHorizontalViewsActivity extends UserSessionStateMaintainingActivity {
	
	private static final String TAG = SwypingHorizontalViewsActivity.class.getName();
	
	/**
	 * GUI utils used for localization
	 */
	public GuiUtils swypeCatsGuiUtils;
	
	/**
	 * Response String from the confirm offer
	 */
	String reponseStr;
	
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

		this.swypeCatsGuiUtils = new GuiUtils(getApplicationContext());

		ViewPager swypePager = (ViewPager) findViewById(R.id.swypeView);
		swypeAdapter = new HorizontalSwypingPagingAdapter(
				new ArrayList<Category>(), getApplicationContext(),
				SwypingHorizontalViewsActivity.this, getFacebookService()
						.getFacebookUser(), this.swypeCatsGuiUtils,
				getClassOfListAdapter(), getClassOfInflateListener());

		// swypePager.setOnPageChangeListener(new MyPageChangeListener());
		Log.e("Swype Pager Listen Please: ", "Inside Swype Activity");

		categories = (ArrayList<Category>) getLastNonConfigurationInstance();

		if (categories == null) {
			// initializeContent();
			categories = new ArrayList<Category>();
			swypeAdapter = new HorizontalSwypingPagingAdapter(categories,
					getApplicationContext(),
					SwypingHorizontalViewsActivity.this, getFacebookService()
							.getFacebookUser(), this.swypeCatsGuiUtils,
					getClassOfListAdapter(), getClassOfInflateListener());

		} else {
			swypeAdapter.setCatagories(categories);
			// swypeAdapter.notifyDataSetChanged();
		}

		swypePager.setAdapter(swypeAdapter);

		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.categoriesIndicator);
		titleIndicator.setViewPager(swypePager);

	}// end onCreate
    
    /**
     * Used to specify the class used as a list adapter for the displayed
     * lists in the swiper. By default it is set to {@link SearchResultsListAdapter}.
     * <br><br>
     * 
     * <b>If you wish to change the adapter, override the method</b>
     * @return
     * 		any class that extends {@link SearchResultsListAdapter}
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    @SuppressWarnings("unchecked")
	public  Class getClassOfListAdapter() {
    	return SearchResultsListAdapter.class;
    }// end getClassOfListAdapter
    
    /**
     * Used to specify the class used as an inflate listener for the 
     * accordian (found in each row for showing more details).
     * By default it is set to {@link InflateListener}.
     * <br><br>
     * 
     * <b>If you wish to change the adapter, override the method</b>
     * @return
     * 		any class that extends {@link InflateListener}
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public Class getClassOfInflateListener() {    	
    	return InflateListener.class;
    }// end getClassOfInflateListener
    
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
     * Checks if a category already exists in the swiping views or not
     * to ensure uniqueness
     * 
     * @param categoryName
     * 		Name of the category. It is not case sensitive
     * @return
     * 		index of category if existing, -1 otherwise
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     *@author 
     *			Hossam_Amer
     *Change: getLogicName instead (@author Hossam_Amer)
     */
    public int findCategoryBy(String categoryName) {
    	
    	for (int i=0 ; i< this.categories.size() ; i++) {
    		if (this.categories.get(i).getLogicName().equalsIgnoreCase(categoryName)) {
    			return i;
    		}// end if: category is found
    	}// end for: search in categories
    	
    	return -1;
    }// end isExistingCategory
    
    /**
     * Adds an offer to a certain category. If the category is existing, 
     * it appends to its offers. otherwise, it creates the new category
     * and adds the offer
     * 
     * @param categoryName
     * 		Name of the category. It is not case sensitive
     * @param newOffer
     * 		Offer to be added
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public void addOfferInCategory(String categoryName, OfferDisplay2 newOffer) {
    	int categoryIndex = findCategoryBy(categoryName);
    	
    	if (categoryIndex == -1) {
    		addCategory(new Category(categoryName, R.layout.my_offers_main, newOffer));
    	}// end if: category is not found-> create new one and add offer
    	else {
    		this.categories.get(categoryIndex).addOffer(newOffer); 
    		swypeAdapter.notifyDataSetChanged();    		
    	}// end else: category is found -> append offer to it
    }// end addOfferInCategory
    
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
//		dialogConfirm = null;
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
							Toast.makeText(getApplicationContext(), R.string._hossamAlreadyConfirmed, Toast.LENGTH_SHORT).show();
						else if(responseStr.equals(Confirmation.confirmedByAnotherPerson))
							Toast.makeText(getApplicationContext(), R.string._hossamConfirmedByAnotherPerson, Toast.LENGTH_SHORT).show();
						else
						{
							Log.e("updateConfirmedOffersOnUI(responseStr)", TAG);
							Log.e("hashas: ", responseStr + "");
							
//							XXXNeed him feel the change now, before here, you have a confirmationJSON object.. Now it calls the child class 3ady.
//							getCategories().add(new )

							/**
							 * Add the action of the intent
							 * and send a signal to the broadcast receiver of the child
							 * to receive the response string and deal with it as appropriate (updating the UI...etc).
							 */
							Intent data = new Intent(HTTP_CONFIRM_OFFER_UI);
							data.putExtra("response", responseStr);
							sendBroadcast(data);
							
							
							
						}
					}					
				}
			}
		}	

			/**
			 * Finds the Offer and deletes it from the list
			 * @author Ahmed Moshen
			 */
			private void deleteOfferFromCategory() {
				// TODO Mohsens's Task to delete the offer from UI + DB
				
				/**
				 * @Ahmed: It is for you to fill, delete the offer from the category
				 */
			}
	
}// end class