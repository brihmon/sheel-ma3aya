
package com.sheel.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.FbDialog;
import com.sheel.adapters.SearchResultsListAdapter;
import com.sheel.datastructures.FacebookUser;
import com.sheel.datastructures.Offer;
import com.sheel.datastructures.OfferDisplay;
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
        
        test_searchUsingFacebook();
        
//      / fbService.login(this,true,true);
      
        
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
        	
    	// Get list using its ID
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
    	
    	if (offersFromUsers != null){
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
   
    }// end updateSearchResultsList
    
    private void updateSearchResultsList(ArrayList<OfferDisplay> offersFromUsers,
    		boolean isAppend){
    	
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
   
    }// end updateSearchResultsList
    
    /**
     * Used to filter results using information from facebook account of user
     *  
     * @param offersFromUsers
     * 		returned search results from querying app database,  where:
	 * 			<ul>
	 * 				<li>the <code>key</code> is Facebook ID of offer owner</li>
	 * 				<li>the <code>value</code> is object representing offer 
	 * 				and user details needed to display a search result</li>
	 * 			</ul> 
     * @param maximumOwnerFacebookStatus
     * 		represents depth of search needed. Each level includes also its previous 
     * 		more filtered levels. Available levels are ordered from most filtered level
     * 		on top to least filtered: 
     * 			<ol>
	 * 				<li> {@link OwnerFacebookStatus#FRIEND} : filter offers from facebook
	 * 				friends only</li>
	 * 				<li> {@link OwnerFacebookStatus#FRIEND_OF_FRIEND} : filter offers from facebook
	 * 				friends or friends of friends</li>
	 * 			</ol> 
     */
    public void searchUsingFacebook(Hashtable<String,OfferDisplay> offersFromUsers , OwnerFacebookStatus maximumOwnerFacebookStatus ){
    	
    	if (maximumOwnerFacebookStatus != OwnerFacebookStatus.UNRELATED){
    		
    		Log.e("passant","searchUsingFacebook: social network filtering enabled");
    		
    		Hashtable<String,OfferDisplay> offersFromFriends=null;
    		Hashtable<String,OfferDisplay> offersFromFriendsOfFriends=null;
    		Hashtable<String,OfferDisplay> offersFromCommonNetworks=null;
    		
    		// Search for offers with owners friends with the app user 
    		offersFromFriends = getFacebookService().filterOffersFromFriends(offersFromUsers);
    		Log.e("passant","searchUsingFacebook : offers from friends are filtered");
    		
    		// Reduce offers searched by removing offers whose owners are friends with the app user
    		@SuppressWarnings("unchecked")
			Hashtable<String,OfferDisplay> remainingOffers = (Hashtable<String, OfferDisplay>)offersFromUsers.clone();
    		FacebookWebservice.removeDuplicates(offersFromFriends, remainingOffers);
    		Log.e("passant","searchUsingFacebook: offers from NON friends are filtered");
    		
    		// Search for offers with owners friends of user friends but not the user's friends
    		if (maximumOwnerFacebookStatus == OwnerFacebookStatus.FRIEND_OF_FRIEND || maximumOwnerFacebookStatus == OwnerFacebookStatus.COMMON_NETWORKS ){
    			offersFromFriendsOfFriends = getFacebookService().filterOffersFromOwnersWithMutualFriends(remainingOffers);
    			Log.e("passant","offers from  friends of friends are filtered");    	    	
    			FacebookWebservice.removeDuplicates(offersFromFriendsOfFriends, remainingOffers);
    			Log.e("passant","unrelated offers are filtered");    	    	
    		}// end if : user wants to see offers from indirect acquaintances
    		
    		// TODO next sprint add networks
    		Log.e("passant","search results will be displayed");
        	
    		// Display results
    		updateSearchResultsList(offersFromFriends, offersFromFriendsOfFriends, offersFromCommonNetworks, remainingOffers);
    	    	
    	}// end if : if user does not care -> why waste internet data on searching
    	else{
    		updateSearchResultsList(offersFromUsers, false);
    	}// end else: display results as is
    }// end searchUsingSocialNetworks
    
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

			@Override
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
                                    	
                                    	ArrayList<OfferDisplay> list = new ArrayList<OfferDisplay>();
                                    	Hashtable<String,OfferDisplay> offersFromUsers = new Hashtable<String,OfferDisplay>();
                                    	OfferDisplay offerDisplay;
                                    	
                                    	JSONObject offer;
                                    	
                                    	 for (int i = 0; i < jsonArray.length(); i++) {
                                    		 
                                    		 offer = jsonArray.getJSONObject(i);
                        
                                    		 offerDisplay = mapOffer(offer);
                                    		
                                    		 if(facebookStatus.equals(OwnerFacebookStatus.UNRELATED.name()))
                                    			 list.add(offerDisplay);
                                    		 
                                    		 else{
                                    			 String ownerId = offerDisplay.getOwnerFacebookId();
                                    			 offersFromUsers.put(ownerId, offerDisplay);
                                    		 }
                                    		 
                                    		 System.out.println("Testing : size=" + searchResults.size()+" hashtable values: " + searchResults);
                   
                                    	 }// end for
                                    
                                    if(facebookStatus.equals(OwnerFacebookStatus.UNRELATED.name()))
                                    	updateSearchResultsList(list, false);
                                    	
                                    else	
                                    	searchUsingFacebook(offersFromUsers, facebook);
										
                                    	 
									} catch (JSONException e) {
										
										e.printStackTrace();
									}
                                 }
                             });
				}
        
			};
	        
			sc.runHttpRequest(request); 	
	}
    
    public OfferDisplay mapOffer(JSONObject offer){
    	
    	try {
			JSONObject user = offer.getJSONObject("user");
			JSONObject flight = offer.getJSONObject("flight");
			
			String ownerId = user.getString("facebookAccount");
			String offerId = offer.getLong("id") + "";
			String displayName = user.getString("firstName") + " " + user.getString("middleName") 
														+ " " + user.getString("lastName");
			String email = user.getString("email");
			String mobile = user.getString("mobileNumber");
			
			int status = offer.getInt("userStatus");
			
			OfferWeightStatus weightStatus;
			
			if(status == 0)
				weightStatus = OfferWeightStatus.LESS;
			else
				weightStatus = OfferWeightStatus.MORE;
			
			int kgs = offer.getInt("noOfKilograms");
			int price = offer.getInt("pricePerKilogram");
			
			OwnerFacebookStatus ownerFbStatus = OwnerFacebookStatus.UNRELATED;
			
			String gender = user.getString("gender");
			boolean isFemale = true;
			
			if(!gender.equals("female"))
				isFemale = false;
			
			String nationality = user.getString("nationality");
			
			String flightNumber = flight.getString("flightNumber");
			String source = flight.getString("source");
			String destination = flight.getString("destination");
			
			int userStatus = offer.getInt("userStatus");
			
			return new OfferDisplay(
				ownerId, offerId , displayName, email, mobile, weightStatus, kgs , price , ownerFbStatus, 
				isFemale, nationality, flightNumber, source, destination, userStatus);
			
			
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
    	long userId = 13;
    	
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
    
public void test_searchUsingFacebook(){
    	
    	Log.e("Passant" , "test_searchUsingFacebook: begin");
    	// Create input
    	String usrId1 = "32529";		// naglaa -> friend
    	String usrId2 = "1446932354";	// olcay -> friend
    	String usrId3 = "592566654";	// ahmad -> friendOfFriend
    	String usrId4 = "1207059"; 		// total stranger 
    	String usrId5 = "1041486620";	// total stranger: ahmed celil 
    	String usrId6 = "100000391115192"; // athar -> friend of friend
    	String usrId7 = "100002079601994"; // akram -> friend of friend
    	String usrId8 = "502129122"; // marina -> friend
    	
    	Hashtable<String, OfferDisplay> offersFromUsers = new Hashtable<String, OfferDisplay>();
    	offersFromUsers.put(usrId1, new OfferDisplay(usrId1, "ofr1","naglaa_friend"));
    	offersFromUsers.put("t2", new OfferDisplay("t2", "ofr7","ahmed celil_stranger2"));
    	offersFromUsers.put("t3", new OfferDisplay("t3", "ofr8","ahmed celil_stranger3"));
       	offersFromUsers.put(usrId3, new OfferDisplay(usrId3, "ofr3","ahmad_friendOfFriend"));
    	offersFromUsers.put(usrId4, new OfferDisplay(usrId4, "ofr4","stranger"));
    	offersFromUsers.put(usrId5, new OfferDisplay(usrId5, "ofr5","ahmed celil_stranger"));
    	offersFromUsers.put("t1", new OfferDisplay("t1", "ofr6","ahmed celil_stranger1"));
    	offersFromUsers.put(usrId2, new OfferDisplay(usrId2, "ofr2","olcay_friend"));
    	offersFromUsers.put("t4", new OfferDisplay("t4", "ofr9","ahmed celil_stranger4"));
    	offersFromUsers.put("t5", new OfferDisplay("t5", "ofr10","ahmed celil_stranger5"));
    	offersFromUsers.put("t6", new OfferDisplay("t6", "ofr11","ahmed celil_stranger6"));
    	offersFromUsers.put(usrId6, new OfferDisplay("t4", "ofr9","athar_friendOfFriend"));
    	offersFromUsers.put(usrId8, new OfferDisplay("t5", "ofr10","marina_friend"));
    	offersFromUsers.put(usrId7, new OfferDisplay("t6", "ofr11","akram_friendOfFriendfriendOfFriendfriendOfFriend"));
    	
    	Log.e("Passant" , "test_searchUsingFacebook: input initialized");
    	
    	// Invoke test method
    	searchUsingFacebook(offersFromUsers, OwnerFacebookStatus.FRIEND_OF_FRIEND);
    	
    	Log.e("Passant" , "test_searchUsingFacebook: search is done");
        
    }// end test_searchUsingFacebook
    
   
    
}// end Activity

