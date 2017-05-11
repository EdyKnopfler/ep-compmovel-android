package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import java.util.HashMap;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;
import android.util.Log;
import android.content.Intent;
import android.app.AlertDialog;

public class LoginActivity extends Activity {
	private final static	String LOG = "login";
	private EditText e_nusp, e_pass;
	private String nusp, pass, name, url;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.login);
		Log.d(LOG, "On Create do Login");
		e_nusp = (EditText) findViewById(R.id.nusp);
		e_pass = (EditText) findViewById(R.id.password);
	}

	public void confirmarClick(View view) {
		nusp = e_nusp.getText().toString();
		pass = e_pass.getText().toString();

		if (getIntent().getStringExtra("tipo").equals("prof"))
			url = Consts.SERVIDOR + "login/teacher";
		else
			url = Consts.SERVIDOR + "login/student";

		Log.d(LOG, nusp + " " + pass);
		postLogin();
	}

	public void cadastroUsuario(View v){
		Intent i;
		i = new Intent(this, CadastroUsuario.class);
		i.putExtra("tipo", getIntent().getStringExtra("tipo"));
		startActivity(i);
	}

	private void postLogin() {
		try {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("nusp", nusp);
			data.put("pass", pass);
			String json = HttpRequest
				.post(url)
				.form(data)
				.body();

			JSONObject token = new JSONObject(json);
			Log.d(LOG, token.getString("success"));
			if(token.getString("success").equals("true")){
				Intent i;
				if(getIntent().getStringExtra("tipo").equals("prof")){
					i = new Intent(LoginActivity.this, MenuProf.class);
				}
				else{
					i = new Intent(LoginActivity.this, MenuAluno.class);
				}
				i.putExtra("nusp", nusp);
				startActivity(i);
				LoginActivity.this.finish();
			}
			else{
				AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
				alert.setTitle(getResources().getString(R.string.falha_login));
				alert.setMessage(getResources().getString(R.string.login_incorreto));
				alert.setPositiveButton("OK", null);
				alert.show();
			}
		}
		catch (Exception e) {
			Log.d(LOG, "\n\nDeu xabláu!", e);
		}
	}

}
