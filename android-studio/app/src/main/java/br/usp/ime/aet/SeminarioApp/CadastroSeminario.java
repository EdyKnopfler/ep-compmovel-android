package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import java.util.HashMap;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class CadastroSeminario extends BaseActivity {
	private EditText e_name;
	private String id, name;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.cadastro_seminario);
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
			url = "seminar/edit";
			data.put("id", id);
		}
		else
			url = "seminar/add";

		setCallback(new Resposta());
		post(url, data, true);
	}

	private class Resposta extends Callback {

		@Override
		void sucesso() {
            Intent result = new Intent();
            result.putExtra("name", name);
            setResult(Activity.RESULT_OK, result);
			AlertDialog.Builder alert = new AlertDialog.Builder(CadastroSeminario.this);
			alert.setTitle(getResources().getString(R.string.sucesso));
			alert.setMessage(getResources().getString(R.string.seminario_cadastrado));
			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			alert.show();
		}

    }

}
