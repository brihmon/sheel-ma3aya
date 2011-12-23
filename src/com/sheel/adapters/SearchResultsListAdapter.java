package com.sheel.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
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
	 * GUI Utilities instance
	 */
	 GuiUtils swypeCatsGuiUtils;
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
		
		swypeCatsGuiUtils = new GuiUtils(this.context);
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
		
		View listRow;
		
		if (convertView == null){
			
			OfferDisplay2 offerDisplay = searchResults.get(position);
			
			// Get the needed inflater to read the XML layout file
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Read the Layout description from the XML file and load it to the
			// view
			listRow = inflater.inflate(R.layout.search_result_summary, null);
								
			// get the different available text fields according to their order
			TextView summary_name = (TextView) listRow.findViewById(R.id.summary_name);
			TextView summary_kilos = (TextView) listRow.findViewById(R.id.summary_numberOfKilos);
			TextView summary_price = (TextView) listRow.findViewById(R.id.summary_price);
		
			renderFirstTextView(summary_name, offerDisplay, listRow);
			renderSecondTextView(summary_kilos, offerDisplay, listRow);
			renderThirdTextView(summary_price, offerDisplay,  listRow);
		}// end if : first time to initialize
		else{
			listRow = convertView;
		}// end else: already there
		
		return listRow;
	}// end getView
	
	/**
	 * Used to render the data displayed in the first text view.
	 * By default it is the display name of the offer owner.
	 * 
	 * <br><br><b>Override the method to change the content
	 * displayed</b>
	 * 
	 * <br><br><b>To hide, override the method then
	 * write <code>textView.setVisibility(View.INVISIBLE)</code></b>
	 * 
	 * @param textView
	 * 		first text view in the list row from the left side
	 * @param offerDisplay
	 * 		Data wrapper containing all info about offer, user 
	 * 		and flight for a certain offer
	 * @param visibility
	 * 		Control the visibility of the target text view.
	 * 		Possible values are: {@link View#VISIBLE}, 
	 * 		{@link View#INVISIBLE}. By default it is set to
	 * 		visible
	 * @param listRow
	 * 		Inflated view representing the list row. It is used 
	 * 		to enable adding of icons using 
	 * 		{@link GuiUtils#setIconForATextField(Context, View, TextView, int, int)}.
	 * 		To neglect such parameter, pass it as null.
	 * 		<br><b>IMPORTANT: If you use the default implementation, you must pass if 
	 * 		correctly</b>
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void renderFirstTextView(TextView textView, OfferDisplay2 offerDisplay, View listRow) {
		// Set (Name) of the user 
		textView.setText(offerDisplay.getDisplayName());	
	}// end renderFirstTextView
	
	/**
	 * Used to render the data displayed in the first text view.
	 * By default it is the number of Kgs of the offer and 
	 * an icon showing if it is extra weight or less weight.
	 * 
	 * <br><br><b>Override the method to change the content
	 * displayed</b>
	 * 
	 * <br><br><b>To hide, override the method then
	 * write <code>textView.setVisibility(View.INVISIBLE)</code></b>
	 * 
	 * @param textView
	 * 		second text view in the list row from the left side
	 * @param offerDisplay
	 * 		Data wrapper containing all info about offer, user 
	 * 		and flight for a certain offer
	 * @param listRow
	 * 		Inflated view representing the list row. It is used 
	 * 		to enable adding of icons using 
	 * 		{@link GuiUtils#setIconForATextField(Context, View, TextView, int, int)}.
	 * 		To neglect such parameter, pass it as null.
	 *  	<br><b>IMPORTANT: If you use the default implementation, you must pass if 
	 * 		correctly</b>
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void renderSecondTextView(TextView textView, OfferDisplay2 offerDisplay, View listRow) {
		
		// Set (Number of Kilograms) according to offer			
		int numberOfKilos = offerDisplay.getOffer().noOfKilograms;
		if (numberOfKilos <=9) {
			textView.setText("0"+numberOfKilos+ " Kg");
		}// end if: add extra space for alignment
		else {
			textView.setText(numberOfKilos+ " Kg");
		}// end else: number of kilos is 2 digits -> just show
		
		if (offerDisplay.getOffer().userStatus == SheelMaayaaConstants.OfferWeightStatus_LESS) {
			this.swypeCatsGuiUtils.setIconForATextField(context, listRow,
					R.id.summary_numberOfKilos,
					R.drawable.sheel_result_arrow_down, 0);
		}// end if: put image indicating status -> Offer has less weight ->
			// wants users with extra weight
		else {
			this.swypeCatsGuiUtils.setIconForATextField(context, listRow,
					R.id.summary_numberOfKilos,
					R.drawable.sheel_result_arrow_up, 0);
		}// end else: put image indicating status -> Offer has more weight ->
			// wants users with less weight
	
		
	}// end renderFirstTextView
	
	/**
	 * Used to render the data displayed in the first text view.
	 * By default it is the price per kg for the offer  and 
	 * a static icon for money	 
	 * 
	 * <br><br><b>Override the method to change the content
	 * displayed</b>
	 * 
	 * <br><br><b>To hide, override the method then
	 * write <code>textView.setVisibility(View.INVISIBLE)</code></b>
	 * 
	 * @param textView
	 * 		third text view in the list row from the left side
	 * @param offerDisplay
	 * 		Data wrapper containing all info about offer, user 
	 * 		and flight for a certain offer
	 * @param listRow
	 * 		Inflated view representing the list row. It is used 
	 * 		to enable adding of icons using 
	 * 		{@link GuiUtils#setIconForATextField(Context, View, TextView, int, int)}.
	 * 		To neglect such parameter, override the method and pass it as null. 
	 * 		<br><b>IMPORTANT: If you use the default implementation, you must pass if 
	 * 		correctly</b>
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void renderThirdTextView(TextView textView,
			OfferDisplay2 offerDisplay, View listRow) {

		// Set (Price) according to the offer
		textView.setText(offerDisplay.getOffer().pricePerKilogram + " €/Kg");
		// Add image
		this.swypeCatsGuiUtils.setIconForATextField(context, listRow, R.id.summary_price,
				R.drawable.sheel_result_money, 0);

	}// end renderFirstTextView
	
	/**
	 * Sets the list to be displayed and notifies the UI to update
	 * 
	 * @param newResults
	 * 		List containing new results to be displayed. It replaces
	 * 		the old list and does not append 
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public void setList(ArrayList<OfferDisplay2> newResults){
		this.searchResults = newResults;
		this.notifyDataSetChanged();
	}// end setList
	
	 
	    
}// end SearchResultsListAdapter
