package com.sheel.datastructures;

/**
 * 
 * @author Nada Emad
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
	    public String facebookAccount;
		public String gender;
		public String nationality;
	    
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
	    
	    
	    public String toString()
	    {
	    	return
	    	"User [id: " + id + ", Username: " + username + 
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