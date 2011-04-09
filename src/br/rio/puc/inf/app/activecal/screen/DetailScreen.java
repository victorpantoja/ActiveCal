/**
 * 
 */
package br.rio.puc.inf.app.activecal.screen;

import java.util.Date;
import java.util.Map;

import br.rio.puc.inf.app.activecal.ActiveCal;
import br.rio.puc.inf.app.activecal.R;
import br.rio.puc.inf.app.activecal.util.Util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

/**
 * @author victor.pantoja
 *
 */
public class DetailScreen extends Activity
{
	private final String TAG = "DetailScreen";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Log.d(TAG, "onCreate on DetailScreen");

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.detail_layout); //seta o layout como a exibicao na tela
		
		int posicao = getIntent().getIntExtra("posicao", -1);
		Map <String,String> events[] = ActiveCal.cal.deXML();
		
		TextView nameView = (TextView) findViewById(R.id.name);
		nameView.setText(events[posicao].get("name"));
		
		TextView authorView = (TextView) findViewById(R.id.author);
		authorView.setText("Author: " + events[posicao].get("author"));
		
		TextView whereView = (TextView) findViewById(R.id.where);
		whereView.setText("Where: " + events[posicao].get("place"));
		
		TextView whenView = (TextView) findViewById(R.id.when);
		Date date = Util.getDate(events[posicao].get("when"),"yyyy-MM-dd HH:mm:ss.SSSZ");
		whenView.setText("When: " + date.getDate() + "/" + (date.getMonth()+1) + "/" + (date.getYear()+1900) + " at " + date.getHours() + ":" + date.getMinutes() + "h");
		
		TextView guestsView = (TextView) findViewById(R.id.guests);
		guestsView.setText("Guests: " + events[posicao].get("guests"));
	}
}
