package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.HashMap;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;
import org.json.JSONArray;

import android.util.Log;

public class SeminariosDoAluno extends Activity {

   private ListView lista;
	private ArrayAdapter adapter;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.seminarios_do_aluno);
      lista = (ListView) findViewById(R.id.lista_seminarios);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		lista.setAdapter(adapter);
      listar(getIntent().getStringExtra("nusp"));
   }

   private void listar(String nusp) {
      try {
			String url = Consts.SERVIDOR + "attendence/listSeminars";
			HashMap<String, String> fm = new HashMap<String, String>();
			fm.put("nusp", nusp);
			String json = HttpRequest.post(url).form(fm).body();
			JSONObject token = new JSONObject(json);

			if (token.getString("success").equals("true")){
				JSONArray data = token.getJSONArray("data");
				for (int j = 0; j < data.length(); j++){
					String idSem = data.getJSONObject(j).getString("seminar_id");
					url = Consts.SERVIDOR + "seminar/get/" + idSem;
					json = HttpRequest.get(url).body();
					JSONObject res = new JSONObject(json);
					JSONObject seminario = res.getJSONObject("data");
					adapter.add(nusp + " - " + seminario.getString("name"));
				}
			}
		}
		catch (Exception e) {
			Log.d("X", "Erro: ", e);
		}
   }

}
