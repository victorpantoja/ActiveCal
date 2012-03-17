/**
 * 
 */
package br.rio.puc.inf.app.activecal.screen;

import com.mobilesocialshare.mss.MSSApi;

import br.rio.puc.inf.app.activecal.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author victor.pantoja
 *
 */
public class LoginScreen  extends Activity implements OnClickListener
{
	private MSSApi mss;
	protected static final String TAG = "activecal";
	private EditText textNome, textPass;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		SharedPreferences pref = getSharedPreferences("ACTIVECAL", MODE_PRIVATE);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.login_layout);
        
        Button button = (Button) findViewById(R.id.btnLogin);
		textNome = (EditText) findViewById(R.id.campoLogin);
		textPass = (EditText) findViewById(R.id.campoSenha);
		
		String login = pref.getString("login", "not_found");
		if (!login.equals("not_found")) {
			textNome.setText(login);
			textPass.requestFocus();
		}
		
		String pass = pref.getString("pass", "not_found");
		if (!pass.equals("not_found")) {
			textPass.setText(pass);
		}

		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
    	
    	mss = new MSSApi("http://192.168.0.191:9080");
    	String auth = mss.Login(textNome.getText().toString(), textPass.getText().toString());
    	
		SharedPreferences pref = getSharedPreferences("ACTIVECAL", MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();
		editor.putString("login", textNome.getText().toString());
		editor.putString("pass", textPass.getText().toString());
		editor.commit();
		
		if(auth.equals("")){
			Toast.makeText(getApplicationContext(), "Wrong User or Password", Toast.LENGTH_SHORT).show();
			editor.putString("pass", "");
			editor.commit();
			textPass.setText("");
		}
		else{
			Intent mainScreen = new Intent(getApplicationContext(),MenuScreen.class);
			mainScreen.putExtra("auth", auth.split(";")[0]);
			mainScreen.putExtra("invites", Integer.parseInt(auth.split(";")[1]));
			startActivity(mainScreen);
		}
	}
}
