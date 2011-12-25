package com.sheel.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sheel.datastructures.NavigationItem;
import com.sheel.utils.GuiUtils;

/**
 * Connector activity used as a welcome page for the application
 * 
 * @author passant
 *
 */
public class SheelMaayaaActivity extends UserSessionStateMaintainingActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.sheel_dashboard);
		initDashBoardItems();
	}// end onCreate
	
	private void initDashBoardItems() {
		NavigationItem[] dashBoardItems = getNavigationItems();
		// In the same order of the navigation items
		ImageButton[] buttonsInDashBoard = new ImageButton[]{
			(ImageButton)findViewById(R.id.searchOffer_dashboard),
			(ImageButton)findViewById(R.id.newOffer_dashboard),
			(ImageButton)findViewById(R.id.viewOffer_dashboard)
		};
		GuiUtils localizingGuiUtils = new GuiUtils(getApplicationContext());
		for (int i =0 ; i< buttonsInDashBoard.length; i++) {
			ImageButton currentButton = buttonsInDashBoard[i];
			currentButton.setTag(i);
			
			currentButton.setOnClickListener(new View.OnClickListener() {				
				public void onClick(View v) {					
					int position = ((Integer)((ImageButton)v).getTag()).intValue();
					onClick_dashBoardItem(position);					
				}
			});
		}// end for: initialize each text view

		}// end setIconsToDashBoardItems
	
	@Override 
	protected void onSaveInstanceState(Bundle outState) {
		
	}// end onSaveInstanceState
	
	
	
}// end class
