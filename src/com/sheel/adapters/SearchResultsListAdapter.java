package com.sheel.adapters;

import java.util.ArrayList;

import com.sheel.app.R;
import com.sheel.datastructures.FacebookUser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
	 * List of facebook users used to display data about 
	 * owners of offers
	 */
	private ArrayList<FacebookUser> users;
	
	/**
	 * Constructor to control the behaviour of a certain ListView
	 * @param c
	 * 		Context of the application having the list view. 
	 * 		It is used to have access to (inflation) properties
	 * @param users
	 * 		List of facebook users used to display data about 
	 * 		owners of offers
	 */
	public SearchResultsListAdapter(Context c, ArrayList<FacebookUser> users ){
		this.context = c;
		this.users = users;
	}// end constructor

	@Override
	public int getCount() {
		return users.size();
	}// end getCount

	@Override
	public Object getItem(int position) {
		return users.get(position);
	}// end getItem

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}// end getItemId

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View listItem;
		
		if (convertView == null){
			
			FacebookUser user = users.get(position);
			
			// Get the needed inflater to read the XML layout file
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Read the Layout description from the XML file and load it to the
			// view
			listItem = inflater.inflate(R.layout.search_result_summary, null);
			
			// Set (Name) of the user 
			TextView summary_name = (TextView) listItem.findViewById(R.id.summary_name);
			summary_name.setText(user.getFullName());
			
			// Set (Gender) image according to user gender			
			if (user.isFemale()){
				setIconForATextField(summary_name, R.drawable.gender_female, 0);
			}// end if : user is female -> show female image
			else{
				setIconForATextField(summary_name, R.drawable.gender_male, 0);
			}// end if : user is male or N/A -> show male image			
			
			// Set (Number of Kilograms) according to offer			
			// TODO where is offer object ?? complete later currently static
			TextView summary_kilos = (TextView) listItem.findViewById(R.id.summary_numberOfKilos);
			summary_kilos.setText("gives 5 kilos");
			

		}// end if : first time to initialize
		else{
			listItem = convertView;
		}// end else: already there
		
		return listItem;
	}// end getView
	
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
