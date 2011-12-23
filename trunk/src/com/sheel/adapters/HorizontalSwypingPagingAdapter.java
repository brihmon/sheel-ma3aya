/**
 * 
 */
package com.sheel.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sheel.app.R;
import com.sheel.datastructures.Category;
import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.listeners.InflateListener;
import com.sheel.listeners.MyOffersInflateListener;
import com.sheel.utils.GuiUtils;
import com.viewpagerindicator.TitleProvider;

/**
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *		Hossam_Amer
 *
 */
public class HorizontalSwypingPagingAdapter extends PagerAdapter  implements TitleProvider{
	
	private static final String TAG = HorizontalSwypingPagingAdapter.class.getName();
	
	private Class listAdapterClass = SearchResultsListAdapter.class;
	private Class inflateListenerClass = InflateListener.class;
	
	/**
	 * Different categories of offers to be displayed
	 */
	private ArrayList<Category> categories = new ArrayList<Category>();
	
	
	
	private Context appContext;
	
	/**
	 * Current running Activity using the adapter
	 */
	Activity mActivity;
	
	/**
	 * Current user logged in User
	 */
	FacebookUser mUser;
	
	/**
	 * Swyping Gui utils instance
	 */
	public GuiUtils swypeCatsGuiUtils;
	
	
	
	/**
	 * Constructor for creating adapter to swap between set of 
	 * views and update accordingly its indicator with the name 
	 * of the category.
	 * 	 
	 * @param
	 * 		Different categories to be displayed in the swiping 
	 * 		views with their respective data
	 * @param listAdapterClass
	 * 		Class used as a list adapter to render the different rows
	 * @param inflateListenerClass
	 * 		Class used as an inflate listener for the stub actions
	 * 		and its rendering
	 *  	
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	

	public HorizontalSwypingPagingAdapter(ArrayList<Category> categories,
			Context appContext, Activity mActivity, FacebookUser mUser,
			GuiUtils swypeCatsGuiUtils, Class listAdapterClass , Class inflateListenerClass) {

		this.categories = categories;
		this.appContext = appContext;
		this.mActivity = mActivity;
		this.mUser = mUser;
		this.inflateListenerClass = inflateListenerClass;
		this.listAdapterClass = listAdapterClass;
		this.swypeCatsGuiUtils = swypeCatsGuiUtils;
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
		
		/**
		 * Listener for one row inside the list
		 * 		  		 
		 * @author 
		 * 		Hossam Amer
		 * @author 
		 *		Passant El.Agroudy (passant.elagroudy@gmail.com)		 * 
		 *
		 */
		 class ListItemClickListener implements OnItemClickListener{

			 /**
			  * Represents data displayed inside a row of the list
			  */
			 private ArrayList<OfferDisplay2> offersInList;
			 
			 private Context appContext;
			 
			 /**
			  * Constructor of listener
			  * 
			  * @param data
			  * 	Data representing the row adn attached to it
			  */
			 public ListItemClickListener(Context appContext, ArrayList<OfferDisplay2> data) {
				 this.offersInList = data;
				 this.appContext = appContext;
			 }// end constructor
			 
			/* (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 */
	//		@Override
			public void onItemClick(AdapterView<?> parent, View v,
			        int position, long id)  {
		    	//==============Showing and Hiding Effect===============
				System.out.println("Offer currently expanded: " + this.offersInList.get(position) );
				
					if (v != null) 
					{
						ViewStub stub = (ViewStub) v.findViewById(R.id.infoStub);
						View inflated = (View) v.findViewById(R.id.infoStubInflated);
						
						toggleVisibilityOfStub(stub, position);
						toggleVisibilityOfStubInfo(inflated);
					}//end if
				
				
			}// end onItemClick
			
			private void toggleVisibilityOfStub(ViewStub stub, int position) {
				if (stub != null) {
					if (stub.getVisibility() == View.GONE) {

						
						stub.setOnInflateListener(createInflateListener(position, this.offersInList.get(position)));

						stub.setVisibility(View.VISIBLE);

					} else
						stub.setVisibility(View.GONE);
				}
			}// end toggleVisibilityOfStub
			
			private void toggleVisibilityOfStubInfo(View inflated) {
				if (inflated != null) {
					if (inflated.getVisibility() == View.GONE) {

						inflated.setVisibility(View.VISIBLE);
					} else
						inflated.setVisibility(View.GONE);
				}
			}// end toggleVisibilityOfStub
				
		 }// end class
		
		System.out.println("HorizontalSwypingPager: instantiateItem: HorizontalAdapterAddress: " + this);
		// Prepare an inflater service to load the current view to be created
		LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		View view = inflater.inflate(categories.get(position).getResourceId(), null);
		
		System.out.println("HorizontalSwypingPager: instantiateItem: offers: " + categories.get(position).getOffersDisplayed() );
		
		ListView displayList = (ListView)view.findViewById(R.id.list);
		
		
		if (displayList != null) {
			System.out.println("HorizontalSwypingPager: instantiateItem: List was retrieved successfully");
			displayList.setAdapter(createListAdapter(position));
			((SearchResultsListAdapter)displayList.getAdapter()).notifyDataSetChanged();

			displayList.setOnItemClickListener(new ListItemClickListener(appContext,this.categories.get(position).getOffersDisplayed()));

		}
		else {
			System.out.println("HorizontalSwypingPager: instantiateItem: List could not be retrieved ");
		}		
			
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
	
	
	/**
	 * Set the list of categories with the new set of categories.
	 * and notifies the swiper with data update
	 * 
	 * @param categories
	 * 				New categories to be set
	 * @author Hossam_Amer
	 * @author Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void setCatagories(ArrayList<Category> categories)
	{
		this.categories = categories;
		this.notifyDataSetChanged();
	}
	
	/**
	 * Used to instantiate a list adapter according type
	 * of view specified
	 * 
	 * @param position
	 * 		category order
	 * @return
	 * 		A list adapter instance depending on the type that extends 
	 * 		{@link SearchResultsListAdapter}
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	private <T extends SearchResultsListAdapter> T createListAdapter(int position){
		
		if (listAdapterClass.equals(SearchResultsListAdapter.class)) {
			return (T)new SearchResultsListAdapter(appContext, categories.get(position).getOffersDisplayed());
		}// end if: activate row view of search results
		else if (listAdapterClass.equals(MyOffersResultsListAdapter.class)) {
			return (T)new MyOffersResultsListAdapter(appContext, categories.get(position).getOffersDisplayed());
		}// end if: activate row view of My Offers
		else {
			return (T)new SearchResultsListAdapter(appContext, categories.get(position).getOffersDisplayed());
		}// end else: by default activate that of search results
	}// end createListAdapter
	
	/**
	 * Used to instantiate a inflate listener for each row in the
	 * list according type of view specified
	 * 
	 * @param position
	 * 		offer index in the list
	 * @return
	 * 		An inflate listener instance depending on the type that extends 
	 * 		{@link InflateListener}
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	private <T extends InflateListener> T createInflateListener(int position, OfferDisplay2 offerDisplay) {
		
		if (inflateListenerClass.equals(InflateListener.class)) {
			return (T)new InflateListener(position, appContext, offerDisplay, mActivity, mUser, swypeCatsGuiUtils);
		}// end if: activate accordian with data in search results view
		else if (inflateListenerClass.equals(MyOffersInflateListener.class)) {
			return (T)new MyOffersInflateListener(position, appContext, offerDisplay, mActivity, mUser, swypeCatsGuiUtils);
		}// end if: activate accordian with data as in my offers view
		else {
			return (T)new InflateListener(position, appContext, offerDisplay, mActivity, mUser, swypeCatsGuiUtils);
		}// end else: default: search view
	}// end createInflateListener
	
}// end class
