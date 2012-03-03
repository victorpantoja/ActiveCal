/**
 * 
 */
package br.rio.puc.inf.app.activecal.screen;

import br.rio.puc.inf.app.activecal.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author victor.pantoja
 *
 */
public class SetupScreen  extends Activity implements OnClickListener
{
	protected static final String TAG = "activecal";
	private EditText textName, textPass;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.login_layout);
        
        Button button = (Button) findViewById(R.id.btnLogin);
		textName = (EditText) findViewById(R.id.campoLogin);
		textPass = (EditText) findViewById(R.id.campoSenha);
		
		SharedPreferences pref = getSharedPreferences("ACTIVECAL", 0);
		
		String name = pref.getString("login", "not_found");
		String pass = pref.getString("pass", "not_found");
		
		if(!name.equals("not_found"))
			textName.setText(name);
		
		if(!pass.equals("not_found"))
			textPass.setText(pass);
        
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		SharedPreferences pref = getSharedPreferences("ACTIVECAL", MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString("login", textName.getText().toString());
		editor.putString("pass", textPass.getText().toString());

		editor.commit();

		finish();
	}
}
