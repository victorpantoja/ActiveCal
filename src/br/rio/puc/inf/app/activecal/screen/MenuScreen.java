/**
 * 
 */
package br.rio.puc.inf.app.activecal.screen;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * @author victor.pantoja
 *
 */
public class MenuScreen extends ListActivity
{
	private final String TAG = "activecal";
	private static final String[] menu = new String[] {"My Calendar","Coming Events"};
	private static final int MENU_SETUP = 0;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.d(TAG, "onCreate on DetailScreen");
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setBackgroundDrawableResource(R.drawable.fundo_240x320);
        
		this.setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu));
	}


	@Override
	protected void onListItemClick(ListView l, View v, int posicao, long id)
	{
		switch (posicao)
		{
			case 0:
				startActivity(new Intent(this, CalendarScreen.class));
				break;
			case 1:
				startActivity(new Intent(this,ComingEventsScreen.class));
				break;
			default:
				break;
		}
	}
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_SETUP, 0, "Setup");
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU_SETUP:
	        startActivity(new Intent(this, SetupScreen.class));
	        return true;
	    }
	    return false;
	}
}