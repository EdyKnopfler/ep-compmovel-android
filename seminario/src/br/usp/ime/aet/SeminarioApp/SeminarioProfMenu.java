package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.HashMap;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;
import org.json.JSONArray;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.util.Log;

public class SeminarioProfMenu extends Activity {

	private static final int ALTERACAO_SEMINARIO = 222;
	private static final String LOG = "spm";

	private String id, nome;
	private TextView tvNome;
	private ListView lista;
	private ArrayAdapter adapter;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.seminario_prof_menu);
		id = getIntent().getStringExtra("id");
		nome = getIntent().getStringExtra("nome");
		tvNome = (TextView) findViewById(R.id.nome_sem);
		if (nome != null) tvNome.setText(nome);
		lista = (ListView) findViewById(R.id.lista_alunos);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		lista.setAdapter(adapter);
		listarAlunos();
   }

	private void listarAlunos() {
		try {
			String url = Consts.SERVIDOR + "attendence/listStudents";
			HashMap<String, String> fm = new HashMap<String, String>();
			fm.put("seminar_id", id);
			String json = HttpRequest.post(url).form(fm).body();
			JSONObject token = new JSONObject(json);

			if (token.getString("success").equals("true")){
				// TODO Estou fazendo uma suposição sobre o formato dos dados no servidor :P
				JSONArray data = token.getJSONArray("data");
				for (int j = 0; j < data.length(); j++){
					String nusp = data.getJSONObject(j).getString("nusp");
					String name = data.getJSONObject(j).getString("name");
					adapter.add(nusp + " - " + name);
				}
			}
		}
		catch (Exception e) {
			Log.d(LOG, "Erro: ", e);
		}
	}

   public void profBlueTooth(View v) {
		Intent i = new Intent(this, ProfessorConfirmBluetooth.class);
		i.putExtra("id_seminario", id);
		startActivity(i);
   }

	public void alterar(View v){
		Intent i = new Intent(this, CadastroSeminario.class);
		i.putExtra("id", id);
		i.putExtra("name", nome);
		startActivityForResult(i, ALTERACAO_SEMINARIO);
   }

	public void excluir(View v){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		AcaoExcluir acao = new AcaoExcluir();
		alert.setTitle(getResources().getString(R.string.excluir));
		alert.setMessage(getResources().getString(R.string.conf_excluir));
		alert.setPositiveButton(getResources().getString(R.string.sim), acao);
		alert.setNegativeButton(getResources().getString(R.string.nao), null);
		alert.show();
   }

	@Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == ALTERACAO_SEMINARIO)
			tvNome.setText(data.getStringExtra("name"));
	}

	private class AcaoExcluir implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			try {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("id", id);
				String url = Consts.SERVIDOR + "seminar/delete";
				String json = HttpRequest.post(url).form(data).body();
				JSONObject token = new JSONObject(json);

				if (token.getString("success").equals("true"))
					finish();
			}
			catch (Exception e) {
				Log.d(LOG, "Erro: ", e);
			}
		}
	}

}
