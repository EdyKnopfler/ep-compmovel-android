// Referência:
// https://github.com/joaobmonteiro/livro-android/blob/master/06-TwitterSearch-1/src/br/com/casadocodigo/twittersearch/TwitterSearchActivity.java

package br.usp.ime.aet.SeminarioApp;

import android.os.AsyncTask;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import android.app.Activity;

/**
 * Tratamos aqui todos os detalhes das respostas do servidor, que não seguem um padrão muito bem
 * definido...
 */
public class Servidor {

    public static class Callback {

        private ComunicacaoThreadUI ui;

        /** sobrescrito quando os dados interessam */
        void sucesso(JSONObject resposta) {
            sucesso();
        }

        /** sobrescrito quando os dados não interessam */
        void sucesso() {}

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

    public void get(final String url) {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... xxxx) {
                ui.mostrarLoading();
                try {
                    String json = HttpRequest.get(Consts.SERVIDOR + url).body();
                    chamarCallback(new JSONObject(json));
                }
                catch (Exception ex) {
                    ui.mensagemSimples("", ui.pegarString(R.string.falha_conexao));
                }
                ui.fecharLoading();
                return null;
            }
        }).execute();
    }

    public void post(final String url, final HashMap<String, String> params,
                     final boolean fazerCache) {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... xxxx) {
                ui.mostrarLoading();
                try {
                    String json = HttpRequest.post(Consts.SERVIDOR + url).form(params).body();
                    chamarCallback(new JSONObject(json));
                }
                catch (Exception ex) {
                    if (fazerCache) {
                        new Cache(ui.getTela()).salvar(params, url);
                        ui.mensagemSimples(ui.pegarString(R.string.falha_conexao),
                                ui.pegarString(R.string.salvo_cache));
                    }
                    else
                        ui.mensagemSimples("", ui.pegarString(R.string.falha_conexao));
                }
                ui.fecharLoading();
                return null;
            }
        }).execute();
    }




    private void chamarCallback(final JSONObject resposta) {
        ui.rodarNaUI(new Runnable() {
            @Override
            public void run() {
                try {
                    if (resposta.getString("success").equals("true"))
                        callback.sucesso(resposta);
                    else if (resposta.has("message"))
                        callback.erroDadoInvalido(resposta.getString("message"));
                    else
                        callback.erroDadoInvalido(ui.pegarString(R.string.falha_operacao));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
