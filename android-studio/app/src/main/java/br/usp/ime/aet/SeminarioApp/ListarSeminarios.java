package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.HashMap;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import static android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class ListarSeminarios extends Activity implements OnItemClickListener {

    private final static int ALTERACAO_SEMINARIO = 222;

    private String tipo;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private HashMap<String, String> seminarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        adapter.clear();
        new Servidor(this, new Resposta()).get("seminar");
    }

    private class Resposta extends Servidor.Callback {
        @Override
        void sucesso(JSONObject resposta) {
            try {
                JSONArray dados = resposta.getJSONArray("data");
                for (int j = 0; j < dados.length(); j++) {
                    String id = dados.getJSONObject(j).getString("id");
                    String name = dados.getJSONObject(j).getString("name");
                    seminarios.put(name, id);
                    adapter.add(name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> lista, View item, int pos, long id) {
        String nomeSem = (String) listView.getItemAtPosition(pos);
        Intent i;
        if (tipo.equals("prof")) {
            i = new Intent(this, SeminarioProfMenu.class);
        }
        else {
            i = new Intent(this, SeminarioAlunoMenu.class);
            i.putExtra("nusp", getIntent().getStringExtra("nusp"));
        }
        i.putExtra("id", seminarios.get(nomeSem));
        i.putExtra("nome", nomeSem);
        startActivityForResult(i, ALTERACAO_SEMINARIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ALTERACAO_SEMINARIO)
            listar();
    }

}
