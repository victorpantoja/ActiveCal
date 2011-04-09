/**
 * 
 */
package br.rio.puc.inf.app.activecal.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.rio.puc.inf.app.activecal.ActiveCal;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author victor.pantoja
 *
 */
public class ComingEventsScreen  extends ListActivity
{
	
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        List<String> menu = new ArrayList<String>(0);
        
        Map<String,String> events[] = ActiveCal.cal.deXML();
        for (int i = 0; i < events.length; i++)
        	menu.add(events[i].get("name"));
		
		this.setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu.toArray(new String[0])));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int posicao, long id)
	{
		Intent event = new Intent(this,EventScreen.class);
		event.putExtra("posicao", posicao);
		startActivity(event);
	}
}
