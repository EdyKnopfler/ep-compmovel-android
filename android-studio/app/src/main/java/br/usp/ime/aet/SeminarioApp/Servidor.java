package br.usp.ime.aet.SeminarioApp;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import android.app.Activity;
import android.util.Log;

public class Servidor {

    public static abstract class Callback {

        private ComunicacaoThreadUI ui;

        abstract void sucesso();

        void erroDadoInvalido(String msg) {
            ui.mensagemSimples(ui.pegarString(R.string.falha_operacao),
                    ui.pegarString(R.string.falhou_mensagem) + "\n" + msg);
        }

        private void setUi(ComunicacaoThreadUI ui) {
            this.ui = ui;
        }

    }

    private Callback callback;
    private ComunicacaoThreadUI ui;

    public Servidor(Activity tela, Callback callback) {
        this.callback = callback;
        this.ui = new ComunicacaoThreadUI(tela);
        this.callback.setUi(ui);
    }

    public void get(String url) {
        try {
            Log.d("X", "fazendo get");
            String json = HttpRequest.get(Consts.SERVIDOR + url).body();
            chamarCallback(new JSONObject(json));
        }
        catch (Exception ex) {
            Log.d("X", "erro no get");
            ui.mensagemSimples("", ui.pegarString(R.string.falha_conexao));
        }
    }

    public void post(String url, HashMap<String, String> params, boolean fazerCache) {
        try {
            Log.d("X", "fazendo post");
            String json = HttpRequest.post(Consts.SERVIDOR + url).form(params).body();
            chamarCallback(new JSONObject(json));
        }
        catch (Exception ex) {
            Log.d("X", "erro no post");
            if (fazerCache) {
                new Cache(ui.getTela()).salvar(params, url);
                ui.mensagemSimples(ui.pegarString(R.string.falha_conexao),
                        ui.pegarString(R.string.salvo_cache));
                Log.d("X", "fiz cache");
            }
            else
                ui.mensagemSimples("", ui.pegarString(R.string.falha_conexao));
        }
    }

    private void chamarCallback(final JSONObject resposta) {
        ui.rodarNaUI(new Runnable() {
            @Override
            public void run() {
                try {
                    if (resposta.getString("success").equals("true"))
                        callback.sucesso();
                    else if (resposta.has("message"))
                        callback.erroDadoInvalido(resposta.getString("message"));
                    else
                        callback.erroDadoInvalido(ui.pegarString(R.string.falha_operacao));
                } catch (JSONException e) {}
            }
        });
    }

}
