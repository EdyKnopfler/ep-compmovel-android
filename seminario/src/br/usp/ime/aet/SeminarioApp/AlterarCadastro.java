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

public class AlterarCadastro extends Activity {
	private final static	String LOG = "alterar";
	private EditText e_nusp, e_pass, e_name;
	private String nusp, pass, name, url;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.alterar_cadastro);
		Log.d(LOG, "On Create do Alterar");
		e_nusp = (EditText) findViewById(R.id.nusp);
		e_pass = (EditText) findViewById(R.id.password);
		e_name = (EditText) findViewById(R.id.name);
	}

	public void postAlterar(View view) {
		nusp = e_nusp.getText().toString();
		pass = e_pass.getText().toString();
		name = e_name.getText().toString();

		if (getIntent().getStringExtra("tipo").equals("prof"))
			url = Consts.SERVIDOR + "teacher/edit";
		else
			url = Consts.SERVIDOR + "student/edit";

		Log.d(LOG, nusp + " " + pass);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setPositiveButton("OK", null);
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("nusp", nusp);
		data.put("pass", pass);
		data.put("name", name);
		try {
			String json = HttpRequest
				.post(url)
				.form(data)
				.body();

			JSONObject token = new JSONObject(json);
			Log.d(LOG, token.getString("success"));
			String alertTitle;
			String alertMessage;
			if(token.getString("success").equals("true")){
				alertTitle = getResources().getString(R.string.sucesso);
				alertMessage = getResources().getString(R.string.usuario_alterado);
			}else{
				alertTitle = getResources().getString(R.string.falha);
				alertMessage = getResources().getString(R.string.dados_incorretos);
			}
			alert.setTitle(alertTitle);
			alert.setMessage(alertMessage);
			alert.show();

		}
		catch (Exception e) {
			new Cache(this).salvar(data, url);
			alert.setTitle(getResources().getString(R.string.falha_conexao));
			alert.setMessage(getResources().getString(R.string.salvo_cache));
			alert.show();
		}

	}

}
