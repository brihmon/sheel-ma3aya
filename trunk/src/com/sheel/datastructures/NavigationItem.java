/**
 * 
 */
package com.sheel.datastructures;

/**
 * Wrapper for all information representing an item in 
 * a menu or a dash board
 * 
 * @author 
 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
 *
 */
public class NavigationItem {

	/**
	 * Displayed name for the item
	 */
	private String name;
	/**
	 * Resource ID of compound drawable used in the menu
	 */
	private int resourceId_menu;
	/**
	 * Resource ID of the compound drawable used in the dash board
	 */
	private int resourceId_dashBoard;
	/**
	 * Type of activity that the item should navigate to
	 */
	Class<?> activityType;
	
	/**
	 * Constructor 
	 * 
	 * @param name
	 * 		Displayed name for the item. Get it from </code>strings.xml</code>
	 * 		for localization
	 * @param rscIdForMenu
	 * 		Resource ID of compound drawable used in the menu
	 * @param rscIdForDashBoard
	 * 		Resource ID of the compound drawable used in the dash board
	 * @param nextActivityType
	 * 		Type of activity that the item should navigate to
	 * 
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public NavigationItem(String name, int rscIdForMenu , int rscIdForDashBoard, Class<?> nextActivityType) {
		this.name = name;
		this.resourceId_menu = rscIdForMenu;
		this.resourceId_dashBoard = rscIdForDashBoard;
		this.activityType =  nextActivityType;
	}// end constructor

	/**
	 * Returns  the displayed name for the item. 
	 * 
	 * @return the name
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public String getName() {
		return this.name;
	}// end getName

	/**
	 * Returns resource ID of compound drawable used in the menu
	 * 
	 * @return the resourceId_menu
	 */
	public int getResourceIdOfmenu() {
		return this.resourceId_menu;
	}// end getResourceIdOfmenu

	/**
	 * Returns resource ID of the compound drawable used in the dash board
	 * 
	 * @return the resourceId_dashBoard
	 * 
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public int getResourceIdOfDashBoard() {
		return this.resourceId_dashBoard;
	}// end getResourceIdOfDashBoard

	/**
	 * Returns type of activity that the item should navigate to.
	 * It will be used in the intent
	 * 
	 * @return the activityType
	 * @author 
	 *		Passant El.Agroudy (passant.elagroudy@gmail.com)
	 */
	public Class<?> getActivityType() {
		return this.activityType;
	}// end getActivityType
	
	
	
}// end class
