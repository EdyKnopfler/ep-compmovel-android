package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.os.AsyncTask;
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

	public void postLogin(View view) {
		nusp = e_nusp.getText().toString();
		pass = e_pass.getText().toString();

		if (getIntent().getStringExtra("tipo").equals("prof"))
			url = Consts.SERVIDOR + "login/teacher";
		else
			url = Consts.SERVIDOR + "login/student";

		Log.d(LOG, nusp + " " + pass);
		new LoginTask().execute();
	}

	//Metodo que busca os dados do usuario no banco a partir do numero USP
	public void searchUser(View view){
		Integer isProfessor;
		Log.d(LOG, "Entrou aqui!!!");
		nusp = e_nusp.getText().toString();


		Log.d(LOG, nusp);
		if (getIntent().getStringExtra("tipo").equals("prof")){
			isProfessor = 1;
			url = Consts.SERVIDOR + "teacher/get/" + nusp;
		}else{
			isProfessor = 0;
			url = Consts.SERVIDOR + "student/get/" + nusp;
		}
		Log.d(LOG, url);
		try{
			String json = HttpRequest.get(url).body();

			JSONObject token = new JSONObject(json);
			Log.d(LOG, token.getString("success"));

			if( token.getString("success").equals("true")){
				//cria um objeto usuario para ser passado para as proximas activitys 
				//que precisasem das informacoes do usuario
				JSONObject data = token.getJSONObject("data");
				name = data.getString("name");
				Log.d(LOG, name);
				User user = new User(name, nusp, isProfessor);
			}
		}
		catch (Exception e){
			Log.d(LOG, "Deu ruim", e);
		}

	}

	public void cadastroUsuario(View v){
		Intent i;
		i = new Intent(this, CadastroUsuario.class);
		i.putExtra("tipo", getIntent().getStringExtra("tipo"));
		startActivity(i);
	}

	private class LoginTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void ... params) {
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
				//TODO:Trocar o false para true quando o servidor funcionar!!!
				if(token.getString("success").equals("false")){
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

			return null;
		}
	}

}
