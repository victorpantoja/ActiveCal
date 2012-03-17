/**
 * May 17, 2010
 *
 * @author victor
 */
package br.rio.puc.inf.app.activecal;

import java.util.Map;
import android.content.Context;
import android.util.Log;
import br.rio.puc.inf.lac.mobilis.cms.ContextConsumer;
import br.rio.puc.inf.lac.mobilis.cms.ContextInformationObject;
import br.rio.puc.inf.app.activecal.ActiveCal.ActiveCalendarXML;

/**
 * @author victor
 *
 */
public class ActiveCalContextConsumer extends ContextConsumer {
	
	protected static final String TAG = "activecal-consumer";
	
	public ActiveCalContextConsumer(Context context)
	{
		super(context);
	}
	
	@Override
	protected void newContextInformation(ContextInformationObject info)
	{
		Log.d(TAG, "nova informacao de contexto, class=" + info.getInformationClass() + ", device=" + info.getDevice());

		/*Obtendo as informacoes de localizacao dos outros guests*/
		if (info.getInformationClass().equals("distance") && info.containsContextInformation("time"))
		{
			Log.e(TAG + "time", info.getContextInformation("time"));
			//b.putString("time", info.getContextInformation("time"));
		}

		if (info.getInformationClass().equals("distance") && info.containsContextInformation("meters"))
		{
			Log.e(TAG + "distance", info.getContextInformation("meters"));
			//b.putString("distance", info.getContextInformation("meters"));
		}
		
		if (info.getInformationClass().equals("meeting") && !info.getContextInformation("feed").equals(null)) {
			ActiveCal.cal = new ActiveCalendarXML(info.getContextInformation("feed"));
			Map <String,String> x[] = ActiveCal.cal.deXML();
			for (int i = 0; i < x.length; i++) {
				for (String s : x[i].keySet())
					Log.d(TAG, s + ": " + x[i].get(s));
			}
			return;
		}
	}
}