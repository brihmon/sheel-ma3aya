package com.sheel.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.sheel.adapters.HorizontalSwypingPagingAdapter;
import com.sheel.datastructures.Category;
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
public class SwypingHorizontalViewsActivity extends Activity {
	
	/**
	 * Different categories of offers to be displayed
	 */
	private ArrayList<Category> categories = new ArrayList<Category>();
	/**
	 * Adapter using for handling pages swiping 
	 */
	private  HorizontalSwypingPagingAdapter swypeAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       setContentView(R.layout.sheel_offers_list);
       initializeContent();
       
        swypeAdapter = new HorizontalSwypingPagingAdapter(this.categories,this.getApplicationContext());
        ViewPager swypePager = (ViewPager)findViewById(R.id.swypeView);
        swypePager.setAdapter(swypeAdapter);
        
        //Bind the title indicator to the adapter        
       TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.categoriesIndicator);
       titleIndicator.setViewPager(swypePager);
     
    }// end onCreate
    
    public void updateCategoryContent (ArrayList<OfferDisplay2> newOffersWrappers , int categoryIndex , boolean isAppend) {
    	
    	// Update the data of the category
    	categories.get(categoryIndex).setOffersDisplayed(newOffersWrappers, isAppend);
    	// Notify swyper that views had a data change
    	swypeAdapter.notifyDataSetChanged();
    	
    }// end updateCategoryContent
   
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
    	updateCategoryContent(newOffers, 1, true);    	
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
}// end class