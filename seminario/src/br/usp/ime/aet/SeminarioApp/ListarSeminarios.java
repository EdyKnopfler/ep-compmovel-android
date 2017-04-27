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

	ListView listView;
	private final static String LOG = "LISTAR"; 
	private String url = Consts.SERVIDOR + "seminar";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(LOG, "Entrou no ListarSeminarios");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listar_seminarios);
		HashMap<String, String> seminarios = new HashMap<String, String>();
		// Get ListView object from xml
		listView = (ListView) findViewById(R.id.lista_seminarios);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		listView.setAdapter(adapter);
	       	listView.setClickable(true);
		listView.setOnItemClickListener(this);	

		//Vai preencher com o nome dos seminarios
		Log.d(LOG, url);
		try{
			String json = HttpRequest.get(url).body();

			JSONObject token = new JSONObject(json);
			Log.d(LOG, token.getString("success"));

			if( token.getString("success").equals("true")){
				JSONArray data = token.getJSONArray("data");
				for(int j=0; j<data.length(); j++){
					String id = data.getJSONObject(j).getString("id");
					String name = data.getJSONObject(j).getString("name");
					seminarios.put(id, name);
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
		//TODO: consulta se eh prof ou aluno pra chamar a view certa
		Intent i = new Intent(ListarSeminarios.this, SeminarioProfMenu.class);
		//TODO: passa aqui o nome e id do seminario
		startActivity(i);
	
	}
}	

