package br.usp.ime.aet.SeminarioApp;

import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;

public class AlterarCadastro extends BaseActivity {
    private EditText e_pass, e_name;
    private String tipo, nusp, pass, name;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.alterar_cadastro);
        tipo = getIntent().getStringExtra("tipo");
        nusp = getIntent().getStringExtra("nusp");
        e_pass = (EditText) findViewById(R.id.password);
        e_name = (EditText) findViewById(R.id.name);
        String url;
        if (tipo.equals("prof"))
            url = "teacher/get/";
        else
            url = "student/get/";
        setCallback(new RespostaGet());
        get(url + nusp);
    }

    private class RespostaGet extends Callback {
        @Override
        void sucesso(JSONObject resposta) {
            try {
                JSONObject dados = resposta.getJSONObject("data");
                e_name.setText(dados.getString("name"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void postAlterar(View view) {
        pass = e_pass.getText().toString();
        name = e_name.getText().toString();
        String url;

        if (tipo.equals("prof"))
            url = "teacher/edit";
        else
            url = "student/edit";

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("nusp", nusp);
        data.put("pass", pass);
        data.put("name", name);

        setCallback(new RespostaPost());
        post(url, data, true);
    }

    private class RespostaPost extends Callback {
        @Override
        void sucesso() {
            AlertDialog.Builder alert = new AlertDialog.Builder(AlterarCadastro.this);
            alert.setPositiveButton("OK", null);
            alert.setTitle(getResources().getString(R.string.sucesso));
            alert.setMessage(getResources().getString(R.string.usuario_alterado));
            alert.show();
            finish();
        }
    }

}
