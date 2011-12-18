/**
 * 
 */
package com.sheel.adapters;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.viewpagerindicator.TitleProvider;

/**
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class HorizontalSwypingPagingAdapter extends PagerAdapter  implements TitleProvider{
	
	/**
	 * category titles that will be displayed in the page 
	 * indicator. It should not be too long.
	 */
	private ArrayList<String> categoriesNames;
	/**
	 * Respective categories resources IDs (Layout IDs to be inflated)
	 */
	private ArrayList<Integer> categoriesResourcesIds;
	
	/**
	 * Constructor for creating adapter to swap between set of 
	 * views and update accordingly its indicator with the name 
	 * of the category.
	 * <b>IMPORTANT: both inputs must be equal in size</b>	
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
	public HorizontalSwypingPagingAdapter(ArrayList<String> catNames, ArrayList<Integer> catResources) {
		this.categoriesNames = catNames;
		this.categoriesResourcesIds = catResources;
	}// end constructor
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return categoriesNames.size();
	}// end getCount
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.View, int)
	 */
	@Override 
	public Object instantiateItem(View container, int position) {
		// Prepare an inflater service to load the current view to be created
		LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		View view = inflater.inflate(categoriesResourcesIds.get(position).intValue(), null);
		((ViewPager) container).addView(view, 0);
		
		
        return view;		
	}// end instantiateItem
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.View, int, java.lang.Object)
	 */
	 @Override
     public void destroyItem(View arg0, int arg1, Object arg2) {
         ((ViewPager) arg0).removeView((View) arg2);

     }// end destroyItem

	
	 /*
	  * (non-Javadoc)
	  * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
	  */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}// end isViewFromObject

	/* (non-Javadoc)
	 * @see com.viewpagerindicator.TitleProvider#getTitle(int)
	 */
	@Override
	public String getTitle(int position) {
		return categoriesNames.get(position);
	}

	
}// end class
