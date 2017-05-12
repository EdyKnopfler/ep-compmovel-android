package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

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
        HashMap<String, String> fm = new HashMap<String, String>();
        fm.put("nusp", nusp);
        new Servidor(this, new Seminarios())
                .post("attendence/listSeminars", fm, false);
    }

    private class Seminarios extends Servidor.Callback {
        @Override
        void sucesso(JSONObject resposta) {
            try {
                if (!resposta.has("data")) return;
                JSONArray data = resposta.getJSONArray("data");
                for (int j = 0; j < data.length(); j++){
                    String idSem = data.getJSONObject(j).getString("seminar_id");
                    new Servidor(SeminariosDoAluno.this, new DadosSeminario())
                            .get("seminar/get/" + idSem);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DadosSeminario extends Servidor.Callback {
        @Override
        void sucesso(JSONObject resposta) {
            try {
                JSONObject seminario = resposta.getJSONObject("data");
                adapter.add(seminario.getString("name"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
