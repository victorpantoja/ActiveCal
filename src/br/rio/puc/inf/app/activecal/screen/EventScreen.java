/**
 * 
 */
package br.rio.puc.inf.app.activecal.screen;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

/**
 * @author victor.pantoja
 *
 */
public class EventScreen extends TabActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent event = new Intent(this,DetailScreen.class);
		event.putExtra("posicao", getIntent().getIntExtra("posicao", -1));
		
		Intent atendee = new Intent(this,AtendeeScreen.class);
		atendee.putExtra("posicao", getIntent().getIntExtra("posicao", -1));
		
		Intent position = new Intent(this, MyPositionScreen.class);
		position.putExtra("posicao", getIntent().getIntExtra("posicao", -1));

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		final TabHost tabHost = getTabHost();
		
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator("Detail")
				.setContent(event));

		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("Atendees")
				.setContent(atendee));

		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator("My Position")
				.setContent(position));
	}
}
