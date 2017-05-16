package br.usp.ime.aet.SeminarioApp;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import android.app.Activity;

/**
 * Tratamos aqui todos os detalhes das respostas do acessoWeb, que não seguem um padrão muito bem
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

    private AcessoWeb acessoWeb;
    private Callback callback;
    private ComunicacaoThreadUI ui;

    public Servidor(Activity tela, Callback callback) {
        this.callback = callback;
        this.ui = new ComunicacaoThreadUI(tela);
        this.callback.setUi(ui);
        this.acessoWeb = new AcessoWeb();
    }

    /** Aqui permitimos a entrada de um mock do AcessoWeb :) */
    public void setAcessoWeb(AcessoWeb acesso) {
        this.acessoWeb = acesso;
    }

    public void get(final String url) {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... xxxx) {
                ui.mostrarLoading();
                try {
                    acessoWeb.setUrl(Consts.SERVIDOR + url);
                    String json = acessoWeb.get();
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
                    acessoWeb.setParams(params);
                    acessoWeb.setUrl(Consts.SERVIDOR + url);
                    String json = acessoWeb.post();
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
