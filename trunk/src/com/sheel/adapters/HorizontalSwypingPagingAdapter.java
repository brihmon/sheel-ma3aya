/**
 * 
 */
package com.sheel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.sheel.app.R;
import com.sheel.datastructures.Category;
import com.viewpagerindicator.TitleProvider;

/**
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class HorizontalSwypingPagingAdapter extends PagerAdapter  implements TitleProvider{
	
	/**
	 * Different categories of offers to be displayed
	 */
	private ArrayList<Category> categories = new ArrayList<Category>();
	
	/**
	 * Constructor for creating adapter to swap between set of 
	 * views and update accordingly its indicator with the name 
	 * of the category.
	 * <b>IMPORTANT: both inputs must be equal in size</b>	
	 * 	 
	 * @param
	 * 		Different categories to be displayed in the swyping 
	 * 		views with their respective data
	 *  	
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public HorizontalSwypingPagingAdapter(ArrayList<Category> categories) {
		this.categories = categories;
	}// end constructor
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return categories.size();
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
		
		View view = inflater.inflate(categories.get(position).getResourceId(), null);
		
		//ListView displayList = (ListView)view.findViewById(R.id.list);
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
		return categories.get(position).getName();
	}

	
}// end class
