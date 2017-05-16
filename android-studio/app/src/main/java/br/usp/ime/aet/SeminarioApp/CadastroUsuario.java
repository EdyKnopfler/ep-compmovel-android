package br.usp.ime.aet.SeminarioApp;

import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import java.util.HashMap;
import android.app.AlertDialog;

public class CadastroUsuario extends BaseActivity {
	private EditText e_nusp, e_pass, e_name;
	private String nusp, pass, name, url;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.cadastro_usuario);
		e_nusp = (EditText) findViewById(R.id.nusp);
		e_pass = (EditText) findViewById(R.id.password);
		e_name = (EditText) findViewById(R.id.name);
	}

	public void postCadastro(View view) {
		nusp = e_nusp.getText().toString();
		pass = e_pass.getText().toString();
		name = e_name.getText().toString();

		if (getIntent().getStringExtra("tipo").equals("prof"))
			url = "teacher/add";
		else
			url = "student/add";

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("nusp", nusp);
		data.put("pass", pass);
		data.put("name", name);

		setCallback(new Resposta());
		post(url, data, true);
	}

	private class Resposta extends Callback {
		@Override
		void sucesso() {
			AlertDialog.Builder alert = new AlertDialog.Builder(CadastroUsuario.this);
			alert.setPositiveButton("OK", null);
			alert.setTitle(getResources().getString(R.string.sucesso));
			alert.setMessage(getResources().getString(R.string.usuario_cadastrado));
			alert.show();
			finish();
		}
	}

}
