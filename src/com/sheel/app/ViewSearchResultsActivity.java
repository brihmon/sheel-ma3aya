
package com.sheel.app;

import static com.sheel.utils.SheelMaayaaConstants.HTTP_GET_MY_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_RESPONSE;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_SEARCH_FOR_OFFERS_FILTER;
import static com.sheel.utils.SheelMaayaaConstants.HTTP_STATUS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sheel.app.MyOffersActivity.SheelMaayaaBroadCastRec;
import com.sheel.datastructures.Category;
import com.sheel.datastructures.Flight;
import com.sheel.datastructures.Offer;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.User;
import com.sheel.datastructures.Utils;
import com.sheel.datastructures.enums.OwnerFacebookStatus;
import com.sheel.utils.GuiUtils;
import com.sheel.utils.HTTPManager;
import com.sheel.utils.InternetManager;

/**
 * This activity is used for displaying and interacting with
 * different search results
 * 
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 * @author
 * 		Magued
 *
 */
public class ViewSearchResultsActivity extends SwypingHorizontalViewsActivity {
	
	ProgressDialog dialog;
	String request;
	OwnerFacebookStatus facebook=OwnerFacebookStatus.UNRELATED;
	String facebookStatus;
	
	private SheelMaayaaBroadCastRec receiver;
	ArrayList<OfferDisplay2> searchResults;
	IntentFilter filter;
	
	/**
	 * Map between each user and his/her corresponding offersWrappers retrieved
	 * from the database
	*/
	Hashtable<String,ArrayList<OfferDisplay2>> offersFromUsers = new Hashtable<String, ArrayList<OfferDisplay2>>();
	

	/** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
          
        Bundle extras = getIntent().getExtras();
        
    	if(extras !=null){
    		request = extras.getString("request");
    		facebookStatus = extras.getString("facebook");
    		
    		if (facebookStatus.equals(OwnerFacebookStatus.FRIEND.name()))
    			facebook = OwnerFacebookStatus.FRIEND;
    		
    		else if (facebookStatus.equals(OwnerFacebookStatus.FRIEND_OF_FRIEND.name()))
    			facebook = OwnerFacebookStatus.FRIEND_OF_FRIEND;
    		
    		}
    	
    	if((ArrayList<Category>) super.getLastNonConfigurationInstance() == null)
    		startHttpService();
        
       // test_categorizeOffersUsingFacebook();        
    }// end onCreate
  
    
    private String getMutualFriends(OfferDisplay2 current){
    	String mutualFriends="";
    	
    	JSONArray data = current.getFacebookExtraInfo();
    	String mutualFriendName = "";
    	
    	for (int i=0 ; i < data.length() ; i++){
    		try {
				mutualFriendName = " , " + (data.getJSONObject(i)).getString("name");
			} catch (JSONException e) {
				mutualFriendName = "";
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
    		mutualFriends += " , " + mutualFriendName;    		
    	}// end for: get names of the mutual friends for displaying 
    	
    	return mutualFriends;
    }// end getMutualFriends    
    
    /**
     * Used to update the contents of the different available lists from 
     * search results in their respective tabs
     * 
     * @param offersFromFriends
     * 		Offers whose owners are friends of the user
     * @param offersFromFriendsOfFriends
     * 		Offers whose owners are friends of friends of the user
     * @param offersFromStrangers
     * 		Offers from strangers 
     * 
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    private void updateDisplayedSearchResults (ArrayList<OfferDisplay2> offersFromFriends , 
    		ArrayList<OfferDisplay2> offersFromFriendsOfFriends, ArrayList<OfferDisplay2> offersFromStrangers ) {
    	
    	// Layout files used for displaying results
    	int layout_searchResultsFound = R.layout.my_offers_main;
    	
    	/**
    	 * The flags are used to avoid displaying a category for a an empty list
    	 * Example: Assume you want filtering with facebook up to friends of friends
    	 * All offers found were from either friends or strangers -> No need to 
    	 * display friends of friends category and then tell user it is empty !
    	 * Moreover they are used to give the user a prompt message when no results
    	 * were found
    	 */
    	boolean hasResultsFromFriends = offersFromFriends != null && offersFromFriends.size() >0;
    	boolean hasResultsFromFriendsOfFriends = offersFromFriendsOfFriends != null && offersFromFriendsOfFriends.size() >0;
    	boolean hasResultsFromStrangers = offersFromStrangers != null && offersFromStrangers.size() >0;
        
    	if ((!hasResultsFromFriends)&&(!hasResultsFromFriendsOfFriends)&&(!hasResultsFromStrangers)) {
     		GuiUtils.showAlertWhenNoResultsAreAvailable(
     				this, 
     				"No offers matching your criteria were found! ", 
     				"Search again", GetUserInfoActivity.class, 
     				"Change filters",FilterPreferencesActivity.class);
    	}// end if: no results were found -> show message to divert to another view
    	else {
    		if (hasResultsFromFriends) {
        		addCategory(new Category("Friends", layout_searchResultsFound, offersFromFriends));    		
        	}// end if : there exists offers from facebook friends -> Add category
        	
        	if (hasResultsFromFriendsOfFriends) {
        		// Sort the results descendingly by number of mutual friends
        		sortSearchResultsFromFacebook(offersFromFriendsOfFriends, null);
        		// Add category
        		addCategory(new Category("Friends of friends", layout_searchResultsFound, offersFromFriendsOfFriends));
        	}// end if: there exists offers from facebook friends of friends -> add category
        	
        	if (hasResultsFromStrangers) {
        		addCategory(new Category("Strangers", layout_searchResultsFound, offersFromStrangers));
          	}// end if: there exists offers from strangers -> add category        	
    		
    	}// end else: one or more of the lists contain results -> display    			
    }// end updateDisplayedSearchResults
    
    /**
     * Used to sort a list of offers (represented as {@link OfferDisplay2}) descendingly
     * with respect to their extra facebook information. 
     * @param offersFromFriendsOfFriends
     * 		Offers whose owners are the user's friends of friends. The offers are sorted 
     * 		according to the number of mutual friends between the offer owners and the user. 
     * @param offersFromCommonNetworks
     * 		Offers whose owners have common networks with the user. The offers are sorted 
     * 		according to the number of mutual networks between the offer owners and the user.  
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    private void sortSearchResultsFromFacebook( 
    		ArrayList<OfferDisplay2> offersFromFriendsOfFriends,
    		ArrayList<OfferDisplay2> offersFromCommonNetworks){
    	
    	/**
    	 * Inner class used for sorting the contents of an array list
    	 * of (OfferDisplay) object DESCENDINGLY 
    	 * @author passant
    	 *
    	 */
    	class FacebookPriorityComparator implements Comparator<OfferDisplay2>{

			//@Override
			public int compare(OfferDisplay2 object1, OfferDisplay2 object2) {
				JSONArray facebookExtraInfo1 = object1.getFacebookExtraInfo();
				JSONArray facebookExtraInfo2 = object2.getFacebookExtraInfo();
				
				/**
				 * NOTE: normally, the returned results should be swapped.
				 * However it leads to ascending sorting. Thus, such swap is
				 * for descending order
				 */
				if (facebookExtraInfo1 != null && facebookExtraInfo2 != null){
					if (facebookExtraInfo1.length() < facebookExtraInfo2.length())
						return 1;
					else if (facebookExtraInfo1.length() > facebookExtraInfo2.length())
						return -1;
				}// end if : double check to avoid exceptions				
				return 0;
			}// end compare    		
    	}// end class
    	
    	
    	// Create comparator
    	FacebookPriorityComparator comparator = new FacebookPriorityComparator();
    	if (offersFromFriendsOfFriends != null){
    		Collections.sort(offersFromFriendsOfFriends,comparator );
    	}
    	
    	if (offersFromCommonNetworks != null){
    		Collections.sort(offersFromCommonNetworks,comparator);
    	}
    }// end sortSearchResultsFromFacebook
    
    /**
     * Used to filter results using information from facebook account of user
     *  
     * @param offersFromUsers
     * 		returned search results from querying app database,  where:
	 * 		<ul>
	 * 			<li>the <code>key</code> is Facebook ID of requested offer owner</li>
	 * 			<li>the <code>value</code> different offersWrappers from this offer owner</li>
	 * 		</ul> 
	 * 		<b>IMPORTANT: the minimum number of allowed elements in the list is 1,
	 * 		i.e. the list must be checked that it is not empty before calling the 
	 * 		method</b>
     * @param maximumOwnerFacebookStatus
     * 		represents depth of search needed. Each level includes also its previous 
     * 		more filtered levels. Available levels are ordered from most filtered level
     * 		on top to least filtered: 
     * 			<ol>
	 * 				<li> {@link OwnerFacebookStatus#FRIEND} : filter offersWrappers from facebook
	 * 				friends only</li>
	 * 				<li> {@link OwnerFacebookStatus#FRIEND_OF_FRIEND} : filter offersWrappers from facebook
	 * 				friends or friends of friends</li>
	 * 			</ol> 
	 * @author 
	 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public void categorizeOffersUsingFacebook (Hashtable<String, ArrayList<OfferDisplay2>> offersFromUsers , OwnerFacebookStatus maximumOwnerFacebookStatus) {
    	
    	    	
    	final String METHOD_NAME = "searchUsingFb";
    	
    	// Create a clone of the input to keep it in tact
    	Hashtable<String, ArrayList<OfferDisplay2>> remainingOffers = (Hashtable<String, ArrayList<OfferDisplay2>>)offersFromUsers.clone();
    	
    	if (maximumOwnerFacebookStatus != OwnerFacebookStatus.UNRELATED){
    		Log.e("passant",METHOD_NAME + ": social network filtering enabled");
    		
    		ArrayList<ArrayList<?>> resultsFromFriends=null;
    		ArrayList<ArrayList<?>> resultsFromFriendsOfFriends=null;
    		
    		// Search for offersWrappers with owners friends with the app user 
    		resultsFromFriends= getFacebookService().filterOffersFromFriends(remainingOffers);
    		Log.e("passant",METHOD_NAME+": offersWrappers from friends are filtered");
    		
    		// Filter remaining offersWrappers by removing offersWrappers from friends
    		remainingOffers.keySet().removeAll((ArrayList<String>)resultsFromFriends.get(1));
    		Log.e("passant",METHOD_NAME+": offersWrappers from NON friends are filtered");
    		
    		// Search for offersWrappers with owners friends of user friends but not the user's friends
    		if (maximumOwnerFacebookStatus == OwnerFacebookStatus.FRIEND_OF_FRIEND || maximumOwnerFacebookStatus == OwnerFacebookStatus.COMMON_NETWORKS ){
    			resultsFromFriendsOfFriends = getFacebookService().filterOffersFromOwnersWithMutualFriends(remainingOffers);
    			Log.e("passant","offersWrappers from  friends of friends are filtered");    	    	
    			// Filter remaining offersWrappers by removing offersWrappers from friends of friends
        		remainingOffers.keySet().removeAll((ArrayList<String>)resultsFromFriendsOfFriends.get(1));
    			Log.e("passant","unrelated offersWrappers are filtered");    	    	
    		}// end if : user wants to see offersWrappers from indirect acquaintances
    		
    		// TODO next sprint add networks
    		Log.e("passant","search results will be displayed"); 
    		
    		System.out.println("Facebook search results: friends: " + resultsFromFriends);
    		System.out.println("Facebook search results: friends of friends: " + resultsFromFriendsOfFriends);
    		System.out.println("Facebook search results: strangers: " + remainingOffers);    		
    		
    		ArrayList<OfferDisplay2> resultsFromStrangers = Utils.getValuesOfHashTable(remainingOffers);
    		System.out.println("Facebook search results: strangers: " + resultsFromStrangers);    		
        	
    		System.out.println("`results from friends: " + resultsFromFriends);
    		System.out.println("`results from friends of friends: " + resultsFromFriendsOfFriends);
    		
    		ArrayList<OfferDisplay2> results_friends = null;
    		ArrayList<OfferDisplay2> results_friendsOfFriends = null;
    		
    		if (resultsFromFriends != null)
    			results_friends = (ArrayList<OfferDisplay2>)resultsFromFriends.get(0);
    		
    		if (resultsFromFriendsOfFriends != null)
    			results_friendsOfFriends = (ArrayList<OfferDisplay2>)resultsFromFriendsOfFriends.get(0);
    		
    		
    		
    		// Display results
    		updateDisplayedSearchResults( results_friends,results_friendsOfFriends,resultsFromStrangers);
    	}// end if : if user does not care -> why waste internet data on searching
    	else {
    		ArrayList<OfferDisplay2> resultsFromStrangers = Utils.getValuesOfHashTable(remainingOffers);
    		// Display results
    		updateDisplayedSearchResults(null, null,resultsFromStrangers);
      	}// end else: facebook search should not be enabled -> display results    	
    	
    }// end searchUsingFacebook
    
   
    public void startHttpService(){
    	

    	if(InternetManager.isInternetOn(getApplicationContext()))
    	{	
    		//======Start the HTTP Request=========
    				
    		if(searchResults == null)
    		{
    			searchResults = new ArrayList<OfferDisplay2>();
    			
    			dialog = ProgressDialog.show(ViewSearchResultsActivity.this, "", "Seaching for Offers..", true, false);
    			HTTPManager.startHttpService(request, HTTP_SEARCH_FOR_OFFERS_FILTER, getApplicationContext());
    		}
    	
    	}// end if (isInternet)
    	else		
    		noInternetConnectionHandler();
   
    }

	private void noInternetConnectionHandler() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Internet Connection is not available")
		       .setCancelable(false)
		       .setPositiveButton("ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       
		       });
		 builder.create();
		 builder.show();
	
	}
	
	private void loadSearchResultsOnUI(String responseStr) 
	{
		try {
			
			JSONArray jsonArray = new JSONArray(responseStr);
        	OfferDisplay2 offerDisplay;
        	
       	 	for (int i = 0; i < jsonArray.length(); i++) {    
	       	 	
       	 		offerDisplay = OfferDisplay2.mapOffer(jsonArray.getJSONObject(i));
	       	 	
       	 		if(facebookStatus.equals(OwnerFacebookStatus.UNRELATED.name())) 
	       	 		searchResults.add(offerDisplay);
	      		                                    		 
       	 		else		 
       	 			addOfferToMap(offersFromUsers, offerDisplay.getUser().getFacebookId(), offerDisplay);
	   	     	 
       	 	}// end for 
       	
       	 	
       	 	if (offersFromUsers.size() > 0 && !facebookStatus.equals(OwnerFacebookStatus.UNRELATED.name())) {
    			categorizeOffersUsingFacebook(offersFromUsers, facebook);
    		}// end if: offersWrappers list is not empty & facebook search required -> do
    		else {
    			updateDisplayedSearchResults(null, null, searchResults);
    		}// end else: no facebook search is required or list is empty
    
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	
	class SheelMaayaaBroadCastRec extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
		
			String action = intent.getAction();
		
		if(action.equals("test"))
			dialog = ProgressDialog.show(ViewSearchResultsActivity.this, "", "Please wait..", true, false);
		else
		{
			int httpStatus = intent.getExtras().getInt(HTTP_STATUS);
			
			if( httpStatus == HttpStatus.SC_OK)
			{
				if (action.equals(HTTP_SEARCH_FOR_OFFERS_FILTER))
				{
					String responseStr = intent.getExtras().getString(HTTP_RESPONSE);
					loadSearchResultsOnUI(responseStr);
					 
					// Dialog dismissing
					if(dialog != null) dialog.dismiss();
						
				}
			
			}
		}	
	}
}
		
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dialog = null;
		
		unregisterReceiver(receiver);
	}
    

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		filter = new IntentFilter();
		
		// Add the filters of your activity
		filter.addAction(HTTP_SEARCH_FOR_OFFERS_FILTER);
		filter.addAction("test");
		
		receiver = new SheelMaayaaBroadCastRec();

		registerReceiver(receiver, filter);
	}
   
   
    /**
     * Helper method used to categorize the adding of a new
     * offer display object in a hashtable according to its owner
     * 
     * @param offersFromUsers
     * 		Hashtable to insert the new <code>OfferDisplay</code> object
     * 		in
     * @param ownerId
     * 		Facebook ID of the offer owner
     * @param offer
     * 		{@link OfferDisplay2} object used to wrap offer details
     * 		with user details and flight. This is the object to be 
     * 		added to the map
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    private void addOfferToMap(Hashtable<String, ArrayList<OfferDisplay2>> offersFromUsers , String ownerId , OfferDisplay2 offer) {
    	
    	if (offersFromUsers.containsKey(ownerId)) {
    		offersFromUsers.get(ownerId).add(offer);
    	}// end if : owner already has offersWrappers -> add this one to them
    	else {
    		ArrayList<OfferDisplay2> offersFromThisUser = new ArrayList<OfferDisplay2>();
    		offersFromThisUser.add(offer);
    		offersFromUsers.put(ownerId, offersFromThisUser);
    	}// end else: new owner and offer -> add to map
    }// end addOfferToMap
public void test_categorizeOffersUsingFacebook(){
	
	Log.e("Passant" , "test_categorizeOffersUsingFacebook: begin");
	// Create input
	String usrId1 = "32529";			String name1 = "naglaa_friend";	// naglaa -> friend
	String usrId2 = "1446932354";		String name2 = "olcay_friend"; // olcay -> friend
	String usrId3 = "592566654";		String name3 = "ahmad_friend"; // ahmad -> friend
	String usrId4 = "1207059"; 			String name4 = "stranger"; // total stranger 
	String usrId5 = "1041486620";		String name5 = "ahmedCelil_stranger"; // total stranger: ahmed celil 
	String usrId6 = "100000391115192"; 	String name6 = "athar_friend of friend"; // athar -> friend of friend
	String usrId7 = "100002079601994"; 	String name7 = "akram_friend of friend"; // akram -> friend of friend
	//String usrId8 = "502129122"; 		String name1 = "marina_friend"; // marina -> friend
	
	ArrayList<OfferDisplay2> offers_usr1 = new ArrayList<OfferDisplay2>();
	ArrayList<OfferDisplay2> offers_usr2 = new ArrayList<OfferDisplay2>();
	ArrayList<OfferDisplay2> offers_usr3 = new ArrayList<OfferDisplay2>();
	ArrayList<OfferDisplay2> offers_usr4 = new ArrayList<OfferDisplay2>();
	ArrayList<OfferDisplay2> offers_usr5 = new ArrayList<OfferDisplay2>();
	ArrayList<OfferDisplay2> offers_usr6 = new ArrayList<OfferDisplay2>();
	ArrayList<OfferDisplay2> offers_usr7 = new ArrayList<OfferDisplay2>();

	// Friends offersWrappers: 1 (U1) , 2 (U1), 3 (U2), 4 (U3)
	// Friends of friends offersWrappers: 5 (U6) , 6 (U6) , 7 (U6) , 8 (U7)
	// Strangers : 9 (U4) , 10 (U4) , 11 (U5)
	
	offers_usr4.add(new OfferDisplay2(usrId4, 9, name4));// stranger
	offers_usr1.add(new OfferDisplay2(usrId1, 1,name1)); // friend
	offers_usr6.add(new OfferDisplay2(usrId6, 5, name6));// friend of friend	
	offers_usr6.add(new OfferDisplay2(usrId6, 7, name6));// duplicate friend of friend	
	offers_usr4.add(new OfferDisplay2(usrId4, 10, name4));// duplicate stranger
	offers_usr1.add(new OfferDisplay2(usrId1, 2,name1)); // duplicate friend
	offers_usr7.add(new OfferDisplay2(usrId7, 8, name7));// friend of friend	
	offers_usr5.add(new OfferDisplay2(usrId5, 11, name5));// stranger
	offers_usr2.add(new OfferDisplay2(usrId2, 3, name2)); // friend	
	offers_usr3.add(new OfferDisplay2(usrId3, 4, name3)); // friend
	offers_usr6.add(new OfferDisplay2(usrId6, 6, name6)); // duplicate friend of friend	
	
	Hashtable<String, ArrayList<OfferDisplay2>> offersFromUsers = new Hashtable<String, ArrayList<OfferDisplay2>>();
	offersFromUsers.put(usrId1, offers_usr1);
	offersFromUsers.put(usrId3, offers_usr3);
	offersFromUsers.put(usrId2, offers_usr2);
	offersFromUsers.put(usrId4, offers_usr4);
	offersFromUsers.put(usrId5, offers_usr5);
	offersFromUsers.put(usrId7, offers_usr7);
	offersFromUsers.put(usrId6, offers_usr6);

	Log.e("Passant" , "test_searchUsingFacebook: input initialized");
	
	// Invoke test method
	categorizeOffersUsingFacebook(offersFromUsers, OwnerFacebookStatus.FRIEND_OF_FRIEND);
	Log.e("Passant" , "categorizeOffersUsingFacebook: search is done");
    
}// end test_searchUsingFacebook

    
}// end Activity

