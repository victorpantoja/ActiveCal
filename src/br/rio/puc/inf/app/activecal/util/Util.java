/**
 * 
 */
package br.rio.puc.inf.app.activecal.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * @author victor.pantoja
 *
 */
public class Util {

	private static final String TAG = "Util";

	public static Date getDate(String date_str, String format)
	{
		String data = date_str.replace("T"," ");

		Date date = null;
		DateFormat formatter = new SimpleDateFormat(format);
		
		try {
			date = (Date)formatter.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,e.getMessage());
		}
		
		return date;
	}
	
}
