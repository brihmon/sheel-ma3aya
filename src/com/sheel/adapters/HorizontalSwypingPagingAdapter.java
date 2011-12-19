/**
 * 
 */
package com.sheel.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.sheel.app.R;
import com.sheel.datastructures.Category;
import com.sheel.listeners.InflateListener;
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
	
	
	private Context appContext;
	
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
	public HorizontalSwypingPagingAdapter(ArrayList<Category> categories, Context appContext) {
		this.categories = categories;
		this.appContext = appContext;
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
		
		System.out.println("HorizontalSwypingPager: instantiateItem: HorizontalAdapterAddress: " + this);
		// Prepare an inflater service to load the current view to be created
		LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		View view = inflater.inflate(categories.get(position).getResourceId(), null);
		
		System.out.println("HorizontalSwypingPager: instantiateItem: offers: " + categories.get(position).getOffersDisplayed() );
		
		ListView displayList = (ListView)view.findViewById(R.id.list);
		
		
		if (displayList != null) {
			System.out.println("HorizontalSwypingPager: instantiateItem: List was retrieved successfully");
			displayList.setAdapter(new SearchResultsListAdapter(appContext, categories.get(position).getOffersDisplayed()));
			((SearchResultsListAdapter)displayList.getAdapter()).notifyDataSetChanged();
			
			displayList.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View v,
			        int position, long id) {
			    	
			    	
			    	// It gets the clicked position
			int mPos = position;
//			Log.e(TAG, "Pos: " + position);
			
				//==============Showing and Hiding Effect===============
				
				if (v != null) 
				{
					ViewStub stub = (ViewStub) v.findViewById(R.id.infoStub);
					View inflated = (View) v.findViewById(R.id.infoStubInflated);
						 
					if(stub != null)
					{
						if(stub.getVisibility() == View.GONE)
							{
//								Log.e(TAG, "Error Inflating");
								InflateListener infListener = new InflateListener(mPos);
								stub.setOnInflateListener(infListener);
								stub.setVisibility(View.VISIBLE);
								
							}
						else
							stub.setVisibility(View.GONE);
					}
					
					if(inflated != null)
					{
						if(inflated.getVisibility() == View.GONE)
							{
							
								inflated.setVisibility(View.VISIBLE);
							}
						else
							inflated.setVisibility(View.GONE);
					}
					
					////===========================================
						
					}//end if
			
				
				    	    	
			}});// end onItemClick
			    	
	    	
	    	//=====================================

		}
		else {
			System.out.println("HorizontalSwypingPager: instantiateItem: List could not be retrieved ");
		}
		
		System.out.println("HorizontalSwypingPager: instantiateItem: offers2: " + categories.get(position).getOffersDisplayed() );
		
		((ViewPager) container).addView(view, 0);
		
		
        return view;
 
		/*
		System.out.println("Inedx of category to be changed: " + position);
		ListView v = new ListView( appContext );
	    String[] from = new String[] { "str" };
	    int[] to = new int[] { android.R.id.text1 };
	    List<Map<String, String>> items =
	        new ArrayList<Map<String, String>>();
	    for ( int i = 0; i < categories.get(position).getOffersDisplayed().size(); i++ )//20
	    {
	        Map<String, String> map =
	            new HashMap<String, String>();
	        //map.put( "str", String.format( "Item %d", i + 1 ) );
	        map.put( "str", categories.get(position).getOffersDisplayed().get(i).getDisplayName() );
	        items.add( map );
	    }
	    
	    System.out.println("Items : " + items);
	    SimpleAdapter adapter = new SimpleAdapter( appContext, items,
	        android.R.layout.simple_list_item_1, from, to ); 
	    
	    // SearchResultsListAdapter adapter = new SearchResultsListAdapter(appContext, offersWrappers)
	    
	    v.setAdapter( adapter );
	    ( (ViewPager) container ).addView( v, 0 );
	    return v;
		
		*/
	
		
	}// end instantiateItem
	
	//@Override 
	//public void startUpdate(android.view.ViewGroup container) {
	//	System.out.println("startUpdate");
	//}
	
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
	//@Override
	public String getTitle(int position) {
		return categories.get(position).getName();
	}

	@Override
	public int getItemPosition(Object object) {
		/*COPIED ONLINE: essential for the notifyDataSetChanged to update correctly*/
	    System.out.println("getItemPosition: "+object.getClass());	    
	    
		return POSITION_NONE;
	}// end getItemPosition
}// end class
