/**
 * 
 */
package br.rio.puc.inf.app.activecal.screen;

import br.rio.puc.inf.app.activecal.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		SharedPreferences pref = getSharedPreferences("ACTIVECAL", MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString("login", textNome.getText().toString());
		editor.putString("pass", textPass.getText().toString());

		Log.i(TAG,"Status salvo para: " + textNome.getText().toString());

		editor.commit();
		Toast.makeText(getApplicationContext(), "Autenticando...", Toast.LENGTH_SHORT).show();
		finishFromChild(getParent());
	}
}
