package br.usp.ime.aet.SeminarioApp;

import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import java.util.HashMap;
import android.content.Intent;
import android.app.AlertDialog;

public class LoginActivity extends BaseActivity {
	private EditText e_nusp, e_pass;
	private String nusp, pass, url;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.login);
		e_nusp = (EditText) findViewById(R.id.nusp);
		e_pass = (EditText) findViewById(R.id.password);
	}

    public void confirmarClick(View view) {
		nusp = e_nusp.getText().toString();
		pass = e_pass.getText().toString();

		if (getIntent().getStringExtra("tipo").equals("prof"))
			url = "login/teacher";
		else
			url = "login/student";

		postLogin();
	}

	public void cadastroUsuario(View v) {
		Intent i;
		i = new Intent(this, CadastroUsuario.class);
		i.putExtra("tipo", getIntent().getStringExtra("tipo"));
		startActivity(i);
	}

	private void postLogin() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("nusp", nusp);
		data.put("pass", pass);
		setCallback(new RespostaLogin());
		post(url, data, false);
	}

    private class RespostaLogin extends Callback {

		@Override
		void sucesso() {
			Intent i;
			if(getIntent().getStringExtra("tipo").equals("prof"))
				i = new Intent(LoginActivity.this, MenuProf.class);
			else
				i = new Intent(LoginActivity.this, MenuAluno.class);
			i.putExtra("nusp", nusp);
			startActivity(i);
			LoginActivity.this.finish();
		}

		@Override
		void erroDadoInvalido(String msg) {
			AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
			alert.setTitle(getResources().getString(R.string.falha_login));
			alert.setMessage(getResources().getString(R.string.login_incorreto));
			alert.setPositiveButton("OK", null);
			alert.show();
		}

	}

}
