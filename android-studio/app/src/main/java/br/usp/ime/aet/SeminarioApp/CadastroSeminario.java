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
import android.content.DialogInterface;

public class CadastroSeminario extends Activity {
	private final static	String LOG = "cadSeminario";
	private EditText e_name;
	private String id, name;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.cadastro_seminario);
		Log.d(LOG, "On Create do Cadastro Seminario");
		e_name = (EditText) findViewById(R.id.name);
		id = getIntent().getStringExtra("id");  // em caso de alteração
		name = getIntent().getStringExtra("name");
		if (name != null) e_name.setText(name);
	}

	public void postCadastroSeminario(View view) {
		name = e_name.getText().toString();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("name", name);
		String url;

		if (id != null) {
			url = Consts.SERVIDOR + "seminar/edit";
			data.put("id", id);
		}
		else
			url = Consts.SERVIDOR + "seminar/add";

		try {
			Log.d(LOG, "entrou no postCadastroSeminario");
			String json = HttpRequest
				.post(url)
				.form(data)
				.body();

			Log.d(LOG, "fez o post");
			JSONObject token = new JSONObject(json);
			Log.d(LOG, token.getString("success"));
			String alertTitle;
			String alertMessage;
			if(token.getString("success").equals("true")){
				alertTitle = getResources().getString(R.string.sucesso);
				alertMessage = getResources().getString(R.string.sucesso_operacao);
			}else{
				alertTitle = getResources().getString(R.string.falha);
				alertMessage = getResources().getString(R.string.falha_operacao);

			}
			Log.d(LOG, "cria o resultado da intent");
			Intent result = new Intent();
			result.putExtra("name", name);
			setResult(Activity.RESULT_OK, result);
			Log.d(LOG, "cria o alert");
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(alertTitle);
			alert.setMessage(alertMessage);
			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			alert.show();

		}
		catch (Exception e) {
			new Cache(this).salvar(data, url);
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setPositiveButton("OK", null);
			alert.setTitle(getResources().getString(R.string.falha_conexao));
			alert.setMessage(getResources().getString(R.string.salvo_cache));
			alert.show();
		}

	}

}
