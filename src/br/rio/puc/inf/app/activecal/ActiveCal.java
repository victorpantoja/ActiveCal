/**
 * 
 */
package br.rio.puc.inf.app.activecal;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.mobilesocialshare.mss.MSSApi;

import br.rio.puc.inf.app.activecal.screen.LoginScreen;
import br.rio.puc.inf.app.activecal.screen.MenuScreen;
import br.rio.puc.inf.lac.mobilis.cms.ContextConsumer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

/**
 * @author victor.pantoja
 *
 */
public class ActiveCal extends Activity implements Observer
{
	public static class ActiveCalendarXML {
		private String xml;

		ActiveCalendarXML(String s) { xml = s; }

		public Map<String,String>[] deXML()
		{
			class myMap extends HashMap<String, String> {
				private static final long serialVersionUID = 8220430134800207887L;
				myMap (int i){super(i);}
			};
			List<myMap> list = new ArrayList<myMap>(0);

			try
			{
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				StringReader sr = new StringReader(xml);
				is.setCharacterStream(sr);
				Document doc = db.parse(is);	    	
				Node n = doc.getChildNodes().item(0);
				NodeList nl = n.getChildNodes();
				for (int i = 0; i < nl.getLength(); i++)
				{
					NamedNodeMap map = nl.item(i).getAttributes();
					myMap row = new myMap(0);

					for (int j = 0; j < map.getLength(); j++)
						row.put(map.item(j).getNodeName(), map.item(j).getNodeValue());

					list.add(row);
				}
			}

			catch (Exception e)
			{
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}

			return list.toArray(new myMap[0]);
		}
	}


	public static ActiveCalendarXML cal;
	public String login, pass;
	public static String authdata;
	private ContextConsumer consumer;
	private ActiveCalContextConsumer myConsumer;

	private final static String TAG = "activecal";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_splash);

		Toast.makeText(this, "Carregando...", Toast.LENGTH_SHORT).show();

		MSSApi mss;
		SharedPreferences pref;

		pref = getSharedPreferences("MOBILESOCIALSHARE", MODE_PRIVATE);
		SharedPreferences.Editor editor;
		
		String login = pref.getString("login", "not_found");
		String pass = pref.getString("pass", "not_found");
		editor = pref.edit();
		
		if(login.equals("not_found") || pass.equals("not_found")){
			startActivity(new Intent(this,LoginScreen.class));
		}
		else{
			mss = new MSSApi("http://192.168.0.191:9080");
			Log.d(TAG,"mss instanciado");
			
	        try{
	        	 mss.Initiate();
	        	 Log.d(TAG,"mss is up!");
	        }catch (Exception e) {
				// TODO: handle exception
			}
			
            String auth = mss.Login(login,pass);
            Log.d(TAG,"user authenticated: "+auth);
            
			if(auth.equals("")){
				Toast.makeText(this, "Wrong User or Password", Toast.LENGTH_SHORT).show();
				editor.putString("pass", "");
				editor.commit();
				startActivity(new Intent(this,LoginScreen.class));
			}
			else{
				startTests();
				Intent mainScreen = new Intent(this,MenuScreen.class);
				mainScreen.putExtra("auth", auth.split(";")[0]);
				mainScreen.putExtra("invites", Integer.parseInt(auth.split(";")[1]));
				startActivity(mainScreen);
			}
		}
		
	}


	private boolean authenticateWithGoogle (String login, String pass)
	{
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("https://www.google.com/accounts/ClientLogin");

			/* Sets all post parameters */
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Email", login));
			nameValuePairs.add(new BasicNameValuePair("Passwd", pass));
			nameValuePairs.add(new BasicNameValuePair("source", "br.rio.puc.inf.lac.activecal"));
			nameValuePairs.add(new BasicNameValuePair("service", "cl"));
			nameValuePairs.add(new BasicNameValuePair("accountType", "GOOGLE"));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			String responseBody = EntityUtils.toString(response.getEntity());
			Log.d(TAG, "authenticateUser - Status: " + response.getStatusLine().getStatusCode());

			String[] lines = responseBody.split("\n");
			for (String line : lines) {
				if (!line.startsWith("Auth="))
					continue;

				authdata = line.replace("Auth=", "");
				return true;
			}
		}

		catch (Exception e)
		{
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}

		return false;
	}

	@Override
	public void update(Observable observable, Object data) {
		boolean active = (Boolean) data;
		if (active) {
			try {
				Log.d(TAG, "Active!");

				/*Publicando o path no CMS. Necessario apenas uma vez*/
				String domain = "http://victorpantoja.com/cms";

				String apkUrl = domain + "/MeetingContextProvider.apk";
				String descriptionUrl = apkUrl.concat(".desc");
				ContextConsumer.getCmsInterface().addContextProviderRepository(apkUrl, descriptionUrl);

				String apkUrl2 = domain + "/DistanceContextProvider.apk";
				String descriptionUrl2 = apkUrl2.concat(".desc");
				ContextConsumer.getCmsInterface().addContextProviderRepository(apkUrl2, descriptionUrl2);


				ContextConsumer.getCmsInterface().setProviderConfiguration("this.meeting.feed", "setAuth", authdata);				
				ContextConsumer.getCmsInterface().setProviderConfiguration("this.meeting.feed", "refreshInterval", "30");
				consumer.addContextInformationInterest("this.meeting.feed");

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
	
	protected void startTests() {
		ContextConsumer.startUp(getApplicationContext());
		try {
			myConsumer = new ActiveCalContextConsumer(getApplicationContext());
			myConsumer.addObserver(this);
			Log.d(TAG,"observer added");
			update(null, new Boolean(ContextConsumer.isActive()));
		}
		catch (Exception e) {
			Log.d(TAG,"erro ao tentar criar consumer");
			Log.e(TAG, "error while trying to create consumer - " + e.getMessage(), e);
		}
	}
}