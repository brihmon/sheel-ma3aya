package com.sheel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sheel.app.R;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.utils.GuiUtils;
import com.sheel.utils.SheelMaayaaConstants;

/**
 * This class is used as an adapter to control the behavior
 * and display of list view displaying different search results
 * of offers
 * 
 * @author passant
 *
 */
public class SearchResultsListAdapter extends BaseAdapter {
	
	/**
	 * Context of the application having the list view. 
	 * It is used to have access to (inflation) properties
	 */
	private Context context;
	/**
	 * List of hybrid object used to display data about the 
	 * offer and its owner
	 */
	private ArrayList<OfferDisplay2> searchResults;
	
	/**
	 * Constructor to control the behaviour of a certain ListView
	 * @param c
	 * 		Context of the application having the list view. 
	 * 		It is used to have access to (inflation) properties
	 * @param offersWrappers
	 * 		List of hybrid object used to display data about the 
	 * 		offer and its owner
	 */
	public SearchResultsListAdapter(Context c, ArrayList<OfferDisplay2> offersWrappers ){
		this.context = c;
		this.searchResults = offersWrappers;
	}// end constructor

	
	public int getCount() {
		return searchResults.size();
	}// end getCount

	public Object getItem(int position) {
		return searchResults.get(position);
	}// end getItem

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}// end getItemId


	public View getView(int position, View convertView, ViewGroup parent) {
		
		View listItem;
		
		if (convertView == null){
			
			OfferDisplay2 offerDisplay = searchResults.get(position);
			
			// Get the needed inflater to read the XML layout file
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Read the Layout description from the XML file and load it to the
			// view
			listItem = inflater.inflate(R.layout.search_result_summary, null);
			
			// Set (Name) of the user 
			TextView summary_name = (TextView) listItem.findViewById(R.id.summary_name);
			summary_name.setText(offerDisplay.getDisplayName());
			
	
			// Set (Number of Kilograms) according to offer			
			TextView summary_kilos = (TextView) listItem.findViewById(R.id.summary_numberOfKilos);
			int numberOfKilos = offerDisplay.getOffer().noOfKilograms;
			if (numberOfKilos <=9) {
				summary_kilos.setText("0"+numberOfKilos+ " Kg");
			}// end if: add extra space for alignment
			else {
				summary_kilos.setText(numberOfKilos+ " Kg");
			}// end else: number of kilos is 2 digits -> just show
			
			if (offerDisplay.getOffer().userStatus == SheelMaayaaConstants.OfferWeightStatus_LESS) {
				GuiUtils.setIconForATextField(context,listItem,R.id.summary_numberOfKilos,R.drawable.sheel_result_arrow_down,0);
			}// end if: put image indicating status -> Offer has less weight -> wants users with extra weight
			else {
				GuiUtils.setIconForATextField(context,listItem,R.id.summary_numberOfKilos,R.drawable.sheel_result_arrow_up,0);
			}// end else:  put image indicating status -> Offer has more weight -> wants users with less weight
			
			// Set (Price) according to the offer
			TextView summary_price = (TextView) listItem.findViewById(R.id.summary_price);
			summary_price.setText(offerDisplay.getOffer().pricePerKilogram + " €/Kg");
			// Add image
			GuiUtils.setIconForATextField(context,listItem,R.id.summary_price,R.drawable.sheel_result_money,0);
		}// end if : first time to initialize
		else{
			listItem = convertView;
		}// end else: already there
		
		return listItem;
	}// end getView
	
	public void setList(ArrayList<OfferDisplay2> newResults){
		this.searchResults = newResults;
		this.notifyDataSetChanged();
	}
	
	  /** Used as a helper method to add an image to the left side of a text view.
	     * @param textViewId 
	     * 		ID of the text view from (R.id) collection
	     * @param imgId
	     * 		ID of the resource image used from (R.drawable) collection
	     * @param mode 
	     * 		indicates size of icon.
	     * 		<ul>
	     * 			<li>0: small    (36X36px)</li>
	     * 			<li>1: medium   (50X50px)</li>
	     * 			<li>2: large    (80X80px)</li>
	     * 		</ul>
	     */
	    private void setIconForATextField(TextView txtView , int imgId , int mode){
	    	
	    	Drawable img = context.getResources().getDrawable(imgId );
	    	switch(mode){
	    	case 0:img.setBounds( 0, 0, 36, 36 ); break;
	    	case 1:img.setBounds( 0, 0, 50, 50 ); break;
	    	case 2:img.setBounds( 0, 0, 80, 80 ); break;
	    	default:img.setBounds( 0, 0, 50, 50 ); break;
	    	}// end switch : specify size according to mode
	    	
	    	txtView.setCompoundDrawables( img, null, null, null );
	    }// end SetIconForATextField
	   
	    
}// end SearchResultsListAdapter
