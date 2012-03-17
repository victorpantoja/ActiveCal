/**
 * 
 */
package br.rio.puc.inf.app.activecal.screen;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import br.rio.puc.inf.app.activecal.ActiveCal;
import br.rio.puc.inf.app.activecal.ActiveCalContextConsumer;
import br.rio.puc.inf.app.activecal.R;
import br.rio.puc.inf.app.activecal.util.Util;
import br.rio.puc.inf.lac.mobilis.cms.ContextConsumer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author victor.pantoja
 *
 */
public class MyPositionScreen extends Activity implements Observer
{
	class myHandler extends Handler {
		public MyPositionScreen parent;
		myHandler(MyPositionScreen s ) {super(); parent = s;}
	}
	
	private final String TAG = "activecal";
	public int posicao;
	public Map <String,String> events[];

	/** a CMS context consumer, used to receive CMS/SDM data */
	private ContextConsumer consumer;

	/** a handler to msgs */
	private Handler msgHandler;
	
	private int timeParser (String time) {
		String r, t;
		
		t = time;
    	r = t.replaceFirst("^(\\d+) minuto(|s)$", "$1");
    	if (!r.equals(t))
    		return Integer.parseInt(r);
    	
    	r = t.replaceFirst("^(\\d+) hora(|s) (\\d+) minuto(|s)$", "$1 $3");
    	if (!r.equals(t)) {
    		String str[] = r.split(" ");
    		return Integer.parseInt(str[0]) * 60 + Integer.parseInt(str[1]);
    	}
    	
		return 0;
	}
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myposition_layout); //seta o layout como a exibicao na tela
		
		posicao = getIntent().getIntExtra("posicao", -1);
		events = ActiveCal.cal.deXML();

		msgHandler = new myHandler(this) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				if(msg.getData().getString("distance")!=null)
				{
					TextView distanceView = (TextView) findViewById(R.id.distance);
					distanceView.setText("Distance: " + msg.getData().getString("distance"));
				}

				if(msg.getData().getString("time")!=null)
				{
					TextView timeView = (TextView) findViewById(R.id.time);
					timeView.setText("Time: " + msg.getData().getString("time"));
					
					Date meetingTime = Util.getDate(parent.events[parent.posicao].get("when"),"yyyy-MM-dd HH:mm:ss.SSSZ");
					
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MINUTE, timeParser(msg.getData().getString("time")));
					Date date = cal.getTime();
					TextView arrivalView = (TextView) findViewById(R.id.arrival);
					arrivalView.setText("Estimated time of arrival: " + date.getDate() + "/" + (date.getMonth()+1) + "/" + (date.getYear()+1900) + " at " + date.getHours() + ":" + date.getMinutes() + "h");
					
					if(date.after(meetingTime))
						Toast.makeText(getApplicationContext(), "You are late!!!", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getApplicationContext(), "You are not late", Toast.LENGTH_SHORT).show();

				}
			}
		};

		run();
	}
	
	protected void run(){
		ContextConsumer.startUp(getApplicationContext());
		try {
			consumer = new ActiveCalContextConsumer(getApplicationContext());
			consumer.addObserver(this);
			update(null, new Boolean(ContextConsumer.isActive()));
		}
		catch (Exception e) {
			Log.e(TAG, "error while trying to create consumer - " + e.getMessage(), e);
		}
	}
	
	@Override
	public void update(Observable observable, Object data) {
		boolean active = (Boolean) data;
		if (active) {
			try {
				consumer.addContextInformationInterest("this.distance.time");
				consumer.addContextInformationInterest("this.distance.meters");;
				
		        ContextConsumer.getCmsInterface().setProviderConfiguration("this.distance.time", "destination",
		          events[posicao].get("place"));

				//TODO - Usar informacao de bateria para saber o tempo de atualizacao do CP de distancia
			}
			catch (Exception e) {
				Log.e(TAG, "error while trying to add consumer - " + e.getMessage(), e);
			}
		}
		else {
			Log.e(TAG, "Not active");
		}
	}
}
