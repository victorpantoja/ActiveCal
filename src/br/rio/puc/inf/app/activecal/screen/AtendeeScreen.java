/**
 * 
 */
package br.rio.puc.inf.app.activecal.screen;

import java.util.Map;

import br.rio.puc.inf.app.activecal.ActiveCal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @author victor.pantoja
 *
 */
public class AtendeeScreen extends Activity
{
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		int posicao = getIntent().getIntExtra("posicao", -1);
		Map <String,String> events[] = ActiveCal.cal.deXML();
		
		String[] guests = events[posicao].get("guests").split(";");
		
		ScrollView scroll = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);

		scroll.addView(layout);
		
		TextView guestsView;
		
		for(int i=0;i<guests.length;i++)
		{
			guestsView = new TextView(this);
			guestsView.setText(guests[i]+"\n");
			layout.addView(guestsView);
		}
		
		setContentView(scroll);
	}
}
