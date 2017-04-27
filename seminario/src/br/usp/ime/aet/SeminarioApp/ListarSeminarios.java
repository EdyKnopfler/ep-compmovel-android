package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import java.util.HashMap;
import android.os.Bundle;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import static android.widget.AdapterView.OnItemClickListener;
import org.json.JSONObject;
import org.json.JSONArray;
import com.github.kevinsawicki.http.HttpRequest;
import android.util.Log;

public class ListarSeminarios extends Activity implements OnItemClickListener{

	private final static int ALTERACAO_SEMINARIO = 222;
	private final static String LOG = "LISTAR";

	private String tipo;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private HashMap<String, String> seminarios;
	private String url = Consts.SERVIDOR + "seminar";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(LOG, "Entrou no ListarSeminarios");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listar_seminarios);
		tipo = getIntent().getStringExtra("tipo");
		seminarios = new HashMap<String, String>();
		listView = (ListView) findViewById(R.id.lista_seminarios);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		listView.setAdapter(adapter);
		listView.setClickable(true);
		listView.setOnItemClickListener(this);
		listar();
	}

	private void listar() {
		//Vai preencher com o nome dos seminarios
		Log.d(LOG, url);
		try{
			String json = HttpRequest.get(url).body();

			JSONObject token = new JSONObject(json);
			Log.d(LOG, token.getString("success"));

			if( token.getString("success").equals("true")){
				adapter.clear();
				JSONArray data = token.getJSONArray("data");
				for(int j=0; j<data.length(); j++){
					String id = data.getJSONObject(j).getString("id");
					String name = data.getJSONObject(j).getString("name");
					seminarios.put(name, id);
					adapter.add(name);
				}
			}
		}
		catch (Exception e){
			Log.d(LOG, "Deu ruim", e);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> lista, View item, int pos, long id) {
		Log.d(LOG,"!!!!SEMINARIO CLICADO!!!!");
		String nomeSem = (String) listView.getItemAtPosition(pos);
		Log.d(LOG, nomeSem);
		Intent i;
		if (tipo.equals("prof")) {
			i = new Intent(this, SeminarioProfMenu.class);
			// TODO: criar a SeminarioAlunoMenu
			i.putExtra("id", seminarios.get(nomeSem));
			i.putExtra("nome", nomeSem);
			startActivityForResult(i, ALTERACAO_SEMINARIO);
		}
	}

	@Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == ALTERACAO_SEMINARIO)
			listar();
	}

}
