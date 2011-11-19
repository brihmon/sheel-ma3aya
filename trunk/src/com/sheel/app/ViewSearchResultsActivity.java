
package com.sheel.app;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
	
	String request;
	OwnerFacebookStatus facebook;
	String facebookStatus;
		
	ArrayList<OfferDisplay> searchResults=new ArrayList<OfferDisplay>();
	FacebookWebservice fbService = new FacebookWebservice();

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
    		
    		else if (facebookStatus.equals(OwnerFacebookStatus.UNRELATED.name()))
    			facebook = OwnerFacebookStatus.UNRELATED;
    		
    		Log.e("mm", facebookStatus);
    		}
    	
    	filterOffers();
        

        
        // TODO CHECK MIGHT CAUSE BUG
        fbService.getUserInformation(true);
        

        setIconsForDetailsItems();
        initListView();
        
     //  fbService.login(this,true,true);
      
        
    }// end onCreate
    
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    //	fbService.authorizeCallback(requestCode, resultCode, data);
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
    	
    /*	new FacebookUser(-1, "firstName", "middleName", "lastName", true, "passant@gmail.com", true);
    	users = new ArrayList<FacebookUser>();
    	users.add(new FacebookUser(1,"Passant", "Akram", "El.Agroudy", true,"passant@gmail.com", true));
    	users.add(new FacebookUser(2,"Hossam", "Mostafa", "Amer", false, "hossam@hotmail.com", true));
    	users.add(new FacebookUser(3,"Nada", "", "Adly", true,"nada@yahoo.com", true));
    	users.add(new FacebookUser(4,"Passant", "Akram", "El.Agroudy", true,"passant@gmail.com", false));
    	users.add(new FacebookUser(5,"Hossam", "Mostafa", "Amer", false,"7as7as@gmail.com", true));
    	users.add(new FacebookUser(6,"Nada", "", "Adly", true,"nido@msn.com", false));
    	users.add(new FacebookUser(7,"Passant", "Akram", "El.Agroudy", true,"passant@gmail.com", true));
    	//users.add(new FacebookUser(8,"Hossam", "Mostafa", "Amer", false));
    	//users.add(new FacebookUser(9,"Nada", "", "Adly", true));
    	 */
    	
    	searchResults.add(new OfferDisplay("ownr", "OFR", "Passant", "passant@gmail.com", "+20105005872", OfferWeightStatus.LESS, 5, 3, OwnerFacebookStatus.FRIEND, true, "", "", "",""));
    	// Create adapter to control how list is displayed
    	adapter = new SearchResultsListAdapter(this, searchResults);
    	// Set adapter to the list view 	
    	searchResultsList.setAdapter(adapter);
    	    	
    	// To enable filtering certain content of the method
    	searchResultsList.setTextFilterEnabled(true);
    	
    	searchResultsList.setOnItemClickListener(new OnItemClickListener() {
    	    public void onItemClick(AdapterView<?> parent, View view,
    	        int position, long id) {
    	     
    	    	//Toast.makeText(getApplicationContext(), "Hello "+position, Toast.LENGTH_SHORT).show();
    	    	
    	    	// TODO simulate clicking of details button
    	    	//Button detailsBtn = (Button)findViewById(R.id.button_slidingDrawer);
    	    	    	    	
    	    	// TODO : update details pane with appropriate data
    	    	updateDetailsPane(position);
    	    	
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
    	
    	// get price
    	prefix += " "+searchResult.getPrice()+" " + "euro per Kg";
    	
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
    
    private void updateSearchResultsList(Hashtable<String,OfferDisplay> offersFromFriends, 
    		Hashtable<String,OfferDisplay> offersFromFriendsOfFriends, 
    		Hashtable<String,OfferDisplay> offersFromCommonNetworks,
    		Hashtable<String, OfferDisplay> offersFromUnRelatedOwners){
    	
    	// Sort list of friends of friends (ones with highest number of mutual friends on top)
    	
    	// Sort list of common networks (ones with highest number of common networks on top)
    	
    	// Display each list in order( friends, mutual sorted , network sorted , unrelated)
    	updateSearchResultsList(offersFromFriends,false);
    	updateSearchResultsList(offersFromFriendsOfFriends, true);
    	updateSearchResultsList(offersFromCommonNetworks, true);
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
    		
    		Log.e("passant","social network filtering enabled");
    		
    		Hashtable<String,OfferDisplay> offersFromFriends=null;
    		Hashtable<String,OfferDisplay> offersFromFriendsOfFriends=null;
    		Hashtable<String,OfferDisplay> offersFromCommonNetworks=null;
    		
    		// Search for offers with owners friends with the app user 
    		offersFromFriends = fbService.filterOffersFromFriends(offersFromUsers);
    		Log.e("passant","offers from friends are filtered");
    		
    		// Reduce offers searched by removing offers whose owners are friends with the app user
    		@SuppressWarnings("unchecked")
			Hashtable<String,OfferDisplay> remainingOffers = (Hashtable<String, OfferDisplay>)offersFromUsers.clone();
    		FacebookWebservice.removeDuplicates(offersFromFriends, remainingOffers);
    		Log.e("passant","offers from NON friends are filtered");
    		
    		// Search for offers with owners friends of user friends but not the user's friends
    		if (maximumOwnerFacebookStatus == OwnerFacebookStatus.FRIEND_OF_FRIEND || maximumOwnerFacebookStatus == OwnerFacebookStatus.COMMON_NETWORKS ){
    			offersFromFriendsOfFriends = fbService.filterOffersFromOwnersWithMutualFriends(remainingOffers);
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
    
    public void test_searchUsingFacebook(){
    	
    	Log.e("Passant" , "test_searchUsingFacebook: begin");
    	// Create input
    	String usrId1 = "32529";		// naglaa
    	String usrId2 = "1446932354";	// olcay
    	String usrId3 = "592566654";	// ahmad
    	String usrId4 = "1207059"; 		// total stranger 
    	String usrId5 = "1041486620";	// total stranger: ahmed celil 
    	
    	Hashtable<String, OfferDisplay> offersFromUsers = new Hashtable<String, OfferDisplay>();
    	offersFromUsers.put(usrId1, new OfferDisplay(usrId1, "ofr1","naglaa_friend"));
    	offersFromUsers.put(usrId2, new OfferDisplay(usrId1, "ofr2","olcay_friend"));
    	offersFromUsers.put(usrId3, new OfferDisplay(usrId1, "ofr3","ahmad_friendOfFriend"));
    	offersFromUsers.put(usrId4, new OfferDisplay(usrId1, "ofr4","stranger"));
    	offersFromUsers.put(usrId5, new OfferDisplay(usrId1, "ofr5","ahmed celil_stranger"));
    	Log.e("Passant" , "test_searchUsingFacebook: input initialized");
    	
    	// Invoke test method
    	searchUsingFacebook(offersFromUsers, OwnerFacebookStatus.FRIEND_OF_FRIEND);
    	
    	Log.e("Passant" , "test_searchUsingFacebook: search is done");
        
    }// end test_searchUsingFacebook
    
   
    public void filterOffers(){
		
		HTTPClient sc = new HTTPClient() {
	           
			@Override
			public void doSomething() {
				final String str = this.rspStr;
				 
							 runOnUiThread(new Runnable()
                             {
                                 @Override
                                 public void run()
                                 {
                                     Toast.makeText(ViewSearchResultsActivity.this, str, Toast.LENGTH_LONG).show();
                                     
                                     try {
                                    	 
                                    	JSONArray jsonArray = new JSONArray(builder.toString());
                                    	
                                    	Hashtable<String,OfferDisplay> offersFromUsers = new Hashtable<String,OfferDisplay>();
                                    	OfferDisplay offerDisplay;
                                    	
                                    	JSONObject offer;
                                    	
                                    	 for (int i = 0; i < jsonArray.length(); i++) {
                                    		 
                                    		 offer = jsonArray.getJSONObject(i);
                        
                                    		 offerDisplay = mapOffer(offer);
                                    		 
                                    		 String ownerId = offerDisplay.getOwnerFacebookId();
                                    		 offersFromUsers.put(ownerId, offerDisplay);
                                    		                                     	
                                    		 System.out.println("Testing : size=" + searchResults.size()+" hashtable values: " + searchResults);
                   
                                    	 }// end for
                                    	 
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
			
			return new OfferDisplay(
				ownerId, offerId , displayName, email, mobile, weightStatus, kgs , price , ownerFbStatus, 
				isFemale, nationality, flightNumber, source, destination);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  	
    }
   
    
}// end Activity

