package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class SeminarioProfMenu extends Activity {

    private static final int ALTERACAO_SEMINARIO = 222;

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
        HashMap<String, String> fm = new HashMap<String, String>();
        fm.put("seminar_id", id);
        new Servidor(this, new ListaEstudantes()).post("attendence/listStudents", fm, false);
    }

    private class ListaEstudantes extends Servidor.Callback {
        @Override
        void sucesso(JSONObject resposta) {
            try {
                if (!resposta.has("data")) return;
                JSONArray data = resposta.getJSONArray("data");
                for (int j = 0; j < data.length(); j++) {
                    String nusp = data.getJSONObject(j).getString("student_nusp");
                    new Servidor(SeminarioProfMenu.this, new DadosEstudante())
                            .get("student/get/" + nusp);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DadosEstudante extends Servidor.Callback {
        @Override
        void sucesso(JSONObject resposta) {
            try {
                JSONObject aluno = resposta.getJSONObject("data");
                adapter.add(aluno.getString("nusp") + " - " + aluno.getString("name"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void profBlueTooth(View v) {
        Intent i = new Intent(this, ProfessorConfirmBluetooth.class);
        i.putExtra("id_seminario", id);
        startActivity(i);
    }

    public void gerarQrCode(View v) {
        Intent i = new Intent(this, GerarQRCode.class);
        i.putExtra("id_seminario", id);
        startActivity(i);
    }

    public void alterar(View v) {
        Intent i = new Intent(this, CadastroSeminario.class);
        i.putExtra("id", id);
        i.putExtra("name", nome);
        startActivityForResult(i, ALTERACAO_SEMINARIO);
    }

    public void excluir(View v) {
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
        if (requestCode == ALTERACAO_SEMINARIO && resultCode == RESULT_OK)
            tvNome.setText(data.getStringExtra("name"));
    }

    private class AcaoExcluir implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            new Servidor(SeminarioProfMenu.this, new Servidor.Callback() {
                @Override
                void sucesso() {
                    finish();
                }
            }).post("seminar/delete", data, true);

            /*
            try {
                String url = Consts.SERVIDOR + "seminar/delete";
                String json = HttpRequest.post(url).form(data).body();
                JSONObject token = new JSONObject(json);

                if (token.getString("success").equals("true"))
                    finish();
            } catch (Exception e) {
                Log.d(LOG, "Erro: ", e);
            }
            */
        }
    }

}
