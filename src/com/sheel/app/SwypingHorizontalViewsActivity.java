package com.sheel.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

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
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       setContentView(R.layout.sheel_offers_list);
       initializeContent();
        
        HorizontalSwypingPagingAdapter swypeAdapter = new HorizontalSwypingPagingAdapter(this.categories);
        ViewPager swypePager = (ViewPager)findViewById(R.id.swypeView);
        swypePager.setAdapter(swypeAdapter);
        
        //Bind the title indicator to the adapter        
       TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.categoriesIndicator);
       titleIndicator.setViewPager(swypePager);
     
    }// end onCreate
    
    public void updateCategoryContent (ArrayList<OfferDisplay2> newOffersWrappers , int categoryIndex , boolean isAppend) {
    	
    	// Update the data of the category
    	categories.get(categoryIndex).setOffersDisplayed(newOffersWrappers, isAppend);
    	// Update the view of the list
    	
    	// notify list with the update
    	
    }// end updateCategoryContent
   
    private void initializeContent() {
    	
		//categoriesNames.add("Communicate"); categoriesNames.add("My Offers");
		
		//categoriesResourcesIds.add(R.layout.communicate); categoriesResourcesIds.add(R.layout.my_offers_main);
		
    	categories.add(new Category("Communicate", R.layout.communicate));
    	categories.add(new Category("My Offers", R.layout.my_offers_main));
		
    
    }
}