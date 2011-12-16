package com.sheel.datastructures;



public class Offer
{

	public Long id;
	
	/**
	 * Flight ID for the offer (If available)
	 */
	public Long flightId;
	
	/**
	 * User ID for the offer (If available)
	 * Normally, this is the ID of the user using the application,
	 * and it should not be needed since user-database interactions
	 * are recorded on the server side.
	 */
	public Long userId;

	public int pricePerKilogram;

    public int noOfKilograms;

    public int userStatus;  //1 for Extra weight and 0 for Less weight

    public String offerStatus; 
    
    //public String currency; //Will be done next sprint
    
    public Offer(Long id,int noOfKilograms,
    		int pricePerKilogram,
            int userStatus,
            String offerStatus) 
    {
    	this.id = id;
    	this.noOfKilograms = noOfKilograms;
        this.pricePerKilogram = pricePerKilogram;
        this.userStatus = userStatus;
        this.offerStatus = offerStatus;

    }
    
    public Offer(int noOfKilograms,
    		int pricePerKilogram,
            int userStatus,
            String offerStatus) 
    {
    	this.noOfKilograms = noOfKilograms;
        this.pricePerKilogram = pricePerKilogram;
        this.userStatus = userStatus;
        this.offerStatus = offerStatus;

    }
    
    /**
     * Constructor used for testing purposes
     * @param offerId
     * 		ID of the offer
     * @author 
     * 		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public Offer (long offerId) {
    	this.id = offerId;
    }// end constructor
    
    public int getNoOfKilograms() {
        return noOfKilograms;
    }

    public void setNoOfKilograms(int noOfKilograms) {
        this.noOfKilograms = noOfKilograms;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public int getPricePerKilogram() {
        return pricePerKilogram;
    }

    public void setPricePerKilogram(int pricePerKilogram) {
        this.pricePerKilogram = pricePerKilogram;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
    
    /**
     * Offer ID from  the data base
     * @return
     * 		Long representing offer ID
     * @author 
     *		Passant El.Agroudy (passant.elagroudy@gmail.com)
     */
    public long getId() {
    	return id;
    }// end getId

	@Override
	public String toString() {
		return "Offer [id=" + id + ", noOfKilograms=" + noOfKilograms +
				", pricePerKilogram=" + pricePerKilogram +
				", userStatus=" + userStatus + ", offerStatus=" + offerStatus + "]";
	}
	
}