package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.pathKey;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sheel.adapters.HorizontalSwypingPagingAdapter;
import com.sheel.datastructures.Category;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.webservices.SheelMaayaaService;
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
	
	/**
	 * Different categories of offers to be displayed
	 */
	 private ArrayList<Category> categories;
	/**
	 * Adapter using for handling pages swiping 
	 */
	private  HorizontalSwypingPagingAdapter swypeAdapter;
	
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
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
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
	
}// end class