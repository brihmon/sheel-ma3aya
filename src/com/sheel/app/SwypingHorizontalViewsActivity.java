package com.sheel.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.sheel.adapters.HorizontalSwypingPagingAdapter;
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
	 * category titles that will be displayed in the page 
	 * indicator. It should not be too long.
	 */
	private ArrayList<String> categoriesNames= new ArrayList<String>();
	/**
	 * Respective categories resources IDs (Layout IDs to be inflated)
	 */
	private ArrayList<Integer> categoriesResourcesIds = new ArrayList<Integer>();
	
	/**
	 * Constructor for creating a new activity that can handle
	 * swapping horizontally multiple views with a built-in 
	 * page indicator with categories names as its title
	 * 
	  * @param catNames
	 * 		category titles that will be displayed in the page 
	 * 		indicator. It should not be too long.
	 * @param catResources
	 * 		Respective categories resources IDs (Layout IDs to be inflated)
	 * 
	 *  	
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	/*public SwypingHorizontalViewsActivity (ArrayList<String> catNames, ArrayList<Integer> catResources) { 	
		this.categoriesNames = catNames;
		this.categoriesResourcesIds = catResources;		
	}// end constructor*/
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       setContentView(R.layout.sheel_offers_list);
       initializeContent();
        
        HorizontalSwypingPagingAdapter swypeAdapter = new HorizontalSwypingPagingAdapter(this.categoriesNames,this.categoriesResourcesIds);
        ViewPager swypePager = (ViewPager)findViewById(R.id.swypeView);
        swypePager.setAdapter(swypeAdapter);
        
        //Bind the title indicator to the adapter        
       TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.categoriesIndicator);
       titleIndicator.setViewPager(swypePager);
     
    }// end onCreate
    
    private void initializeContent() {
    	
		categoriesNames.add("communicate"); categoriesNames.add("search");
		
		categoriesResourcesIds.add(R.layout.communicate); categoriesResourcesIds.add(R.layout.get_user_info);
		
		
    
    }
}