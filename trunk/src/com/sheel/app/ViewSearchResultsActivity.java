
package com.sheel.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sheel.adapters.SearchResultsListAdapter;
import com.sheel.datastructures.Flight;
import com.sheel.datastructures.Offer;
import com.sheel.datastructures.OfferDisplay;
import com.sheel.datastructures.OfferDisplay2;
import com.sheel.datastructures.User;
import com.sheel.datastructures.enums.OfferWeightStatus;
import com.sheel.datastructures.enums.OwnerFacebookStatus;
import com.sheel.webservices.FacebookWebservice;

/**
 * This activity is used for displaying and interacting with
 * different search results
 * 
 * @author passant
 *
 */
public class ViewSearchResultsActivity extends UserSessionStateMaintainingActivity {
	
	SearchResultsListAdapter adapter;
	ProgressDialog dialog;
	String request;
	OwnerFacebookStatus facebook=OwnerFacebookStatus.UNRELATED;
	String facebookStatus;
	
	
	// Hossam Amer add-on
	static int selectedIndex;
		
	ArrayList<OfferDisplay> searchResults=new ArrayList<OfferDisplay>();

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
       setContentView(R.layout.search_result_details);

        
        Bundle extras = getIntent().getExtras();
        
    	if(extras !=null){
    		request = extras.getString("request");
    		facebookStatus = extras.getString("facebook");
    		
    		if (facebookStatus.equals(OwnerFacebookStatus.FRIEND.name()))
    			facebook = OwnerFacebookStatus.FRIEND;
    		
    		else if (facebookStatus.equals(OwnerFacebookStatus.FRIEND_OF_FRIEND.name()))
    			facebook = OwnerFacebookStatus.FRIEND_OF_FRIEND;
    		
    		Log.e("mm", facebookStatus);
    		}
    	
    	filterOffers();
        
    	if (getFacebookService() != null)
        getFacebookService().getUserInformation(true);        

        setIconsForDetailsItems();
        initListView();
    
        
       // test_categorizeOffersUsingFacebook();
       
        
    }// end onCreate
    
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    	getFacebookService().authorizeCallback(requestCode, resultCode, data);
    }// end onActivityResult
        
    /**
     * This method is used to set the default icons for the 
     * different details in the details pane
     */
    private void setIconsForDetailsItems(){
    	    	
       	setIconForATextField(R.id.details_textView_price, R.drawable.details_money,1);
    	setIconForATextField(R.id.details_textView_facebookStatus, R.drawable.details_facebook,1);
    	setIconForATextField(R.id.details_textView_email, R.drawable.details_email,0);
    	setIconForATextField(R.id.details_textView_mobileNumber, R.drawable.details_phone,0);
    	
    	
    }// end setIconsForDetailsItem
    
    /**
     * Used as a helper method to add an image to the left side of a text view.
     * @param textViewId 
     * 		ID of the text view from (R.id) collection
     * @param imgId
     * 		ID of the resource image used from (R.drawable) collection
     * @param mode 
     * 		indicates size of icon.
     * 		<ul>
     * 			<li>0: small    (40X40px)</li>
     * 			<li>1: medium   (50X50px)</li>
     * 			<li>2: large    (80X80px)</li>
     * 		</ul>
     */
    private void setIconForATextField(int textViewId , int imgId , int mode){
    	
    	Drawable img = getApplicationContext().getResources().getDrawable(imgId );
    	switch(mode){
    	case 0:img.setBounds( 0, 0, 40, 40 ); break;
    	case 1:img.setBounds( 0, 0, 50, 50 ); break;
    	case 2:img.setBounds( 0, 0, 80, 80 ); break;
    	default:img.setBounds( 0, 0, 50, 50 ); break;
    	}// end switch : specify size according to mode
    	
    	TextView txtView = (TextView)findViewById(textViewId);
    	txtView.setCompoundDrawables( img, null, null, null );
    }// end SetIconForATextField
    
    /**
     * used to initialize the values of the list view
     */
    private void initListView(){    	
        	
    /*	// Get list using its ID
    	ListView searchResultsList = (ListView)findViewById(R.id.listView_searchResults);
       	// Create adapter to control how list is displayed
    	adapter = new SearchResultsListAdapter(this, searchResults);
    	// Set adapter to the list view 	
    	searchResultsList.setAdapter(adapter);
    	    	
    	// To enable filtering certain content of the method
    	searchResultsList.setTextFilterEnabled(true);
    	
    	searchResultsList.setOnItemClickListener(new OnItemClickListener() {
    	    public void onItemClick(AdapterView<?> parent, View view,
    	        int position, long id) {
    	     
    	    	// update details pane with appropriate data
    	    	updateDetailsPane(position);
    	    	
    	    	
    	    	ViewSearchResultsActivity.selectedIndex = position;
    	    	
    	    	//test_searchUsingFacebook();
    	    	
    	    }// end onItemClick
    	  });
    */
    }// end initListView
    
    /**
     * Method used to update content of sliding drawer displaying 
     * different details about a certain offer
     * 
     * @param position 
     * 		selected element in the list view. It is used
     * 		to retrieve corresponding logical objects of this 
     * 		element
     * 
     */
    private void updateDetailsPane(int position){
    	
    	// Hybrid Datastructure having owner/offer necessary info
    	OfferDisplay searchResult = searchResults.get(position);
    	
    	// Get different GUI components to be updated
    	TextView price = (TextView)findViewById(R.id.details_textView_price);
    	TextView fbStatus = (TextView)findViewById(R.id.details_textView_facebookStatus);
    	TextView mobile = (TextView)findViewById(R.id.details_textView_mobileNumber);
    	TextView email = (TextView)findViewById(R.id.details_textView_email);
    	
    	// Set price value from offer
    		
       	String prefix="";
    	
    	// according to offer status -> specify prefix
    	if (searchResult.getWeightStatus() == OfferWeightStatus.MORE){
    		prefix = " Offers";
    	}// end if : offer for giving luggage -> prefix: pay
    	else{
    		prefix = " Requests";
    	}// end if : offer for carrying luggage -> prefix: wants
    	
    	int priceValue = searchResult.getPrice();
    	if (priceValue < 0){
    		prefix += " N/A per Kg";
    	}else if (priceValue == 0){
    		prefix += " no money per Kg";
    	}else{
	    	// get price
	    	prefix += " "+priceValue+" " + "euro per Kg";
    	}
    	
    	// Set price
    	price.setText(prefix);
    	
    	// Set facebook status of user    		
    	String facebookTxt ="";
    	OwnerFacebookStatus relation = searchResult.getOwnerFacebookRelationWithUser();
    	
    	if (relation == OwnerFacebookStatus.FRIEND){
    		facebookTxt = "Facebook friend";
    	}// end if : user and owner are friends
    	else if (relation == OwnerFacebookStatus.FRIEND_OF_FRIEND){
    		facebookTxt = "Friend of Friend ("+searchResult.getFacebookExtraInfo().length()+ " mutual friends)";
    	}// end else: owner is a friend of user friend
    	else if (relation == OwnerFacebookStatus.COMMON_NETWORKS){
    		facebookTxt = "Common networks ("+searchResult.getFacebookExtraInfo().length()+ " networks)";
    	}// end else: user and owner have common networks only
    	else{
    		facebookTxt = "Stranger";
    	}// end else: user and owner are not related
    	
    		// Set displayed text
    		fbStatus.setText(facebookTxt);
    		// TODO Set image for expansion
    	
    	// Set mobile from user in data base    	
    	mobile.setText("   "+searchResult.getMobile());
    	    	
    	// Set email from facebook user
    	email.setText("   "+searchResult.getEmail());
    }// end updateDetailsPane
    
    private void getMutualFriends(OfferDisplay current){
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
    		
    	}
    	
    }// end getMutualFriends
    
    private void updateSearchResultsList(Hashtable<String,OfferDisplay> offersFromFriends, 
    		Hashtable<String,OfferDisplay> offersFromFriendsOfFriends, 
    		Hashtable<String,OfferDisplay> offersFromCommonNetworks,
    		Hashtable<String, OfferDisplay> offersFromUnRelatedOwners){
    	
    	// Get list of Friends Of Friends and common networks
    	ArrayList<OfferDisplay> list_offersFromFriendsOfFriends = null;
    	ArrayList<OfferDisplay> list_offersFromCommonNetworks=null;
    	
    	if (offersFromFriendsOfFriends != null){
    		list_offersFromFriendsOfFriends=new ArrayList<OfferDisplay>();
        	list_offersFromFriendsOfFriends.addAll(offersFromFriendsOfFriends.values());	
    	}// end if : check if not null -> transform to an array list

    	if (offersFromCommonNetworks != null){
    		list_offersFromCommonNetworks=new ArrayList<OfferDisplay>();
    		list_offersFromCommonNetworks.addAll(offersFromCommonNetworks.values());
    	}// end if : check if not null -> transform to an array list
    	 	
    	// Sort list of friends of friends (ones with highest number of mutual friends on top)
      	// Sort list of common networks (ones with highest number of common networks on top)
    	sortSearchResultsFromFacebook(list_offersFromFriendsOfFriends, list_offersFromCommonNetworks);
    	
    	// Display each list in order( friends, mutual sorted , network sorted , unrelated)
    	updateSearchResultsList(offersFromFriends,false);
    	updateSearchResultsList(list_offersFromFriendsOfFriends, true);
    	updateSearchResultsList(list_offersFromCommonNetworks, true);
    	updateSearchResultsList(offersFromUnRelatedOwners, true);
    }// end updateSearchResultsList
    
    private void updateSearchResultsList(Hashtable<String,OfferDisplay> offersFromUsers,
    		boolean isAppend){
    	
    /*	if (offersFromUsers != null){
    		if (!isAppend){
        		searchResults.clear();
        	}// end if : list not initialized or want to display new search result list
        	
    		System.out.println("updateSearchResultsList: content searchResults before adding: " + searchResults);
        	searchResults.addAll(offersFromUsers.values());  
        	Log.e("passant", "list size: " + offersFromUsers.size() + " list content: " + offersFromUsers);
        	Log.e("Final list: " , searchResults.toString() + "  list size: " + searchResults.size());
        	
        	// Get list using its ID
        	//adapter.setList(searchResults);
        	ListView searchResultsList = (ListView)findViewById(R.id.listView_searchResults);
        	//searchResultsList.invalidateViews();
        	searchResultsList.setAdapter(new SearchResultsListAdapter(this, searchResults));
    	}// end if : list has something
    	else{
    		Log.e("passant","list is empty");
    	}    	
   */
    }// end updateSearchResultsList
    
    private void updateSearchResultsList(ArrayList<OfferDisplay> offersFromUsers,
    		boolean isAppend){
    	/*
    	if (offersFromUsers != null){
    		if (!isAppend){
        		searchResults.clear();
        	}// end if : list not initialized or want to display new search result list
        	
    		System.out.println("updateSearchResultsList: content searchResults before adding: " + searchResults);
        	searchResults.addAll(offersFromUsers);  
        	Log.e("passant", "list size: " + offersFromUsers.size() + " list content: " + offersFromUsers);
        	Log.e("Final list: " , searchResults.toString() + "  list size: " + searchResults.size());
        	
        	// Get list using its ID
        	ListView searchResultsList = (ListView)findViewById(R.id.listView_searchResults);
        	searchResultsList.setAdapter(new SearchResultsListAdapter(this, searchResults));
    	}// end if : list has something
    	else{
    		Log.e("passant","list is empty");
    	}  
    	*/  	
   
    }// end updateSearchResultsList
    
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
    		
    		// TODO remove commenting on displaying of results after finishing
    		// Display results
    		//updateSearchResultsList(offersFromFriends, offersFromFriendsOfFriends, offersFromCommonNetworks, remainingOffers);
    	   
    	}// end if : if user does not care -> why waste internet data on searching
    	else {
    		// Display results
    		//updateSearchResultsList(offersFromFriends, offersFromFriendsOfFriends, offersFromCommonNetworks, remainingOffers);
    	   
    	}// end else: facebook search should not be enabled -> display results
    	
    	
    }// end searchUsingFacebook
    
    private void sortSearchResultsFromFacebook( 
    		ArrayList<OfferDisplay> offersFromFriendsOfFriends,
    		ArrayList<OfferDisplay> offersFromCommonNetworks){
    	
    	/**
    	 * Inner class used for sorting the contents of an array list
    	 * of (OfferDisplay) object DESCENDINGLY 
    	 * @author passant
    	 *
    	 */
    	class FacebookPriorityComparator implements Comparator<OfferDisplay>{

			//@Override
			public int compare(OfferDisplay object1, OfferDisplay object2) {
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
      
    public void filterOffers(){
    	
    	dialog = ProgressDialog.show(ViewSearchResultsActivity.this, "", "Seaching for Offers..", true, false);
		HTTPClient sc = new HTTPClient() {
	           
			@Override
			public void doSomething() {
				final String str = this.rspStr;
				 
							 runOnUiThread(new Runnable()
                             {
//                                 @Override
                                 public void run()
                                 {
                                    // Toast.makeText(ViewSearchResultsActivity.this, str, Toast.LENGTH_LONG).show();
                                     
                                     try {
                                    	 
                                    	 if(dialog != null)
                                     		dialog.dismiss();
                                    	 
                                    	JSONArray jsonArray = new JSONArray(builder.toString());
                                    	
                                    	
                                    	ArrayList<OfferDisplay2> list = new ArrayList<OfferDisplay2>();
                                    	/**
                                    	 * Map between each user and his/her corresponding offersWrappers retrieved
                                    	 * from the database
                                    	 */
                                    	Hashtable<String,ArrayList<OfferDisplay2>> offersFromUsers = new Hashtable<String, ArrayList<OfferDisplay2>>();
                                    	OfferDisplay2 offerDisplay;
                                    
                                    	
                                    	JSONObject offer;
                                    	
                                    	 for (int i = 0; i < jsonArray.length(); i++) {
                                    		 
                                    		 offer = jsonArray.getJSONObject(i);
                        
                                    		 offerDisplay = mapOffer(offer);
                                    		
                                    		 if(facebookStatus.equals(OwnerFacebookStatus.UNRELATED.name()))
                                    			 list.add(offerDisplay);
                                    		 
                                    		 else{
                                    			 
                                    			 addOfferToMap(offersFromUsers, offerDisplay.getUser().getFacebookId(), offerDisplay);
                                    	     }
                                    		 
                                    		 System.out.println("Testing : size=" + searchResults.size()+" hashtable values: " + searchResults);
                   
                                    	 }// end for
                                    
                                    	 //	TO DO: REMOVE COMMENTS AFTER UPDATING -------- PASSANT
                               /*    if(facebookStatus.equals(OwnerFacebookStatus.UNRELATED.name())) {
                                    	updateSearchResultsList(list, false);

                                    }// end if: no facebook search enabled -> display offersWrappers
                                    else if (offersFromUsers.size() > 0) {	
                                    	searchUsingFacebook(offersFromUsers, facebook);
	
                                    }// end else: offersWrappers list is not empty & facebook search required -> do	 
                               */
									} catch (JSONException e) {
										
										e.printStackTrace();
									}
                                 }
                             });
				}
        
			};
	        
			sc.runHttpRequest(request); 	
	}
    
    public OfferDisplay2 mapOffer(JSONObject offerJSON){
    	
    	try {
			JSONObject userJSON = offerJSON.getJSONObject("user");
			JSONObject flightJSON = offerJSON.getJSONObject("flight");
						
			String ownerId = userJSON.getString("facebookAccount");
			String firstName = userJSON.getString("firstName");
			String middleName = userJSON.getString("middleName");
			String lastName = userJSON.getString("lastName");
			String email = userJSON.getString("email");
			String mobile = userJSON.getString("mobileNumber");
			String gender = userJSON.getString("gender");
			String nationality = userJSON.getString("nationality");
			
			User user = new User(ownerId, firstName, middleName, lastName, "", "", email, mobile, gender, nationality);
			
			Long offerId = offerJSON.getLong("id");
			String offerstatus = offerJSON.getString("offerStatus");
			int userstatus = offerJSON.getInt("userStatus");
			int kgs = offerJSON.getInt("noOfKilograms");
			int price = offerJSON.getInt("pricePerKilogram");
			
			Offer offer = new Offer(offerId, kgs, price, userstatus, offerstatus);
			    
			String flightNumber = flightJSON.getString("flightNumber");
			String source = flightJSON.getString("source");
			String destination = flightJSON.getString("destination");
			String departureDate = flightJSON.getString("departureDate");
			
			Flight flight = new Flight(flightNumber, source, destination, departureDate);
	
			
			return new OfferDisplay2(user, flight, offer);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  	
    }
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dialog = null;
	}
   
    public void onClick_communicate(View v){
    	// TODO Hossam link ur view .. phone comm is where u want to go
    	
    	/* Whatever you need either get it from user or preferable query the db
    	 * using the facebook ID
    	 * 
    	 * Before navigating back to my activity again you must call
    	 * Intent intent = setSessionInformationBetweenActivities(PhoneCommunication.class);
    	 * 
    	 * make your class extend UserSessionStateMaintainingActivity not just activity
    	 */
    	// Parameters sent in intent    	
    	int status = searchResults.get(selectedIndex).getUserStatus();
    	status = (status==0)? 1:0;
    	
    	String mobile = searchResults.get(selectedIndex).getMobile();
    	int kgs = searchResults.get(selectedIndex).getNumberOfKgs();
    	String offerId1 = searchResults.get(selectedIndex).getOfferId();    	
    	long offerId = Long.parseLong(offerId1);
    	String email = searchResults.get(selectedIndex).getEmail();
    	String fullName = searchResults.get(selectedIndex).getDisplayName();
    	
    	
    	Bundle extras = getIntent().getExtras();
		long userId = extras.getLong("userId"); 
		Toast.makeText(getApplicationContext(),"" + userId, Toast.LENGTH_SHORT).show();
    	    	

		
    	Intent intent = setSessionInformationBetweenActivities(PhoneCommunication.class);
    	intent.putExtra("fb_account", getFacebookService().getFacebookUser().getEmail());
    	intent.putExtra("fbId", getFacebookService().getFacebookUser().getUserId());
    	
    	
    	
    	// Query mobile from app DB
    	intent.putExtra("mobile", mobile);
    	intent.putExtra("kgs", kgs);
    	intent.putExtra("offerId", offerId);
    	intent.putExtra("email", email);
    	intent.putExtra("fullName", fullName);
    	intent.putExtra("userId", userId);
    	intent.putExtra("user_status", status);
    	
    	// navigate to new activity
    	startActivity(intent);
    	
    }// end onClick_communicate

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

