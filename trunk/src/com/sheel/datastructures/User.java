package com.sheel.datastructures;

/**
 * 
 * @author Nada Emad
 * @author 
 * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
 */


public class User
{
		
		public Long id;
		public String username;
	    public String firstName;
	    public String middleName;
	    public String lastName;
		public String passportPhoto; 
	    public String passportNumber;
	    public String email;
	    public String mobileNumber;
	    private String facebookAccount;
		public String gender;
		public String nationality;
	    
	    public User(
	    		String facebookAccount,
	            String firstName,
	            String middleName,
	            String lastName,
	            String passportPhoto,
	            String passportNumber,
	            String email,
	            String mobileNumber,
	            String gender,
	            String nationality) {
	     
	        this.firstName = firstName;
	        this.middleName = middleName;
	        this.lastName = lastName;
	        this.passportPhoto = passportPhoto;
	        this.passportNumber = passportNumber;
	        this.email = email;
	        this.mobileNumber = mobileNumber;
	        this.facebookAccount = facebookAccount;
	        this.gender = gender;
	        this.nationality = nationality;

	    }
	    
	    //nada
	    public User(
	    		String username, 
	            String firstName,
	            String middleName,
	            String lastName,
	            String passportPhoto,
	            String passportNumber,
	            String email,
	            String mobileNumber,
	            String facebookAccount,
	            String gender,
	            String nationality) {
	        this.username = username;
	        this.firstName = firstName;
	        this.middleName = middleName;
	        this.lastName = lastName;
	        this.passportPhoto = passportPhoto;
	        this.passportNumber = passportNumber;
	        this.email = email;
	        this.mobileNumber = mobileNumber;
	        this.facebookAccount = facebookAccount;
	        this.gender = gender;
	        this.nationality = nationality;

	    }
	    
	    /**
	     * Constructor created for testing purposes.
	     * The constructor auto-sets the middle name to "middle" 
	     * and last name to "last" for testing
	     * 
	     * @param facebookId
	     * 		User Facebook ID
	     * @param firstName
	     * 		User First Name
	     */
	    public User (String facebookId, String firstName) {
	    	this.facebookAccount = facebookId;
	    	this.firstName = firstName;
	    	this.middleName = "middle";
	    	this.lastName = "last";
	    		
	    }// end constructor
	    
	    /**
	     * Facebook ID of the user
	     * @return
	     * 		String representing the Facebook ID of the user
	     * @author 
	     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	     */
	    public String getFacebookId () {
	    	return facebookAccount;
	    }
	    public String toString()
	    {
	    	return
	    	"User [id: " + id + 
	    	", First Name: " + firstName +
	    	", Middle Name: " + middleName +
	    	", Last Name: " + lastName +
	    	", Passport number: " + passportNumber +
	    	", Email: " + email +
	    	", Mobile number: " + mobileNumber +
	    	", Facebook account: " + facebookAccount +
	    	", Gender: " + gender +
	    	", Nationality: " + nationality + "]";
	    }
}