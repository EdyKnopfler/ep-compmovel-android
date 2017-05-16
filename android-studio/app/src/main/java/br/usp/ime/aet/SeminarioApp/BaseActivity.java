package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe base para as activities - permite injetar mocks dos objetos de acesso à web.
 * Aqui evitamos que os testes acessem o servidor e realizem cache das falhas simuladas.
 */
public class BaseActivity extends Activity {

    private AcessoWeb acessoWeb;
    private Cache cache;
    private ComunicacaoThreadUI ui;
    private Callback callback;

    /** Respota da tela aos acessos ao servidor */
    protected class Callback {
        void sucesso(JSONObject resposta) {
            sucesso();
        }

        void sucesso() {}

        void erroDadoInvalido(String msg) {
            ui.mensagemSimples(ui.pegarString(R.string.falha_operacao),
                    ui.pegarString(R.string.falhou_mensagem) + "\n" + msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acessoWeb = new AcessoWeb();
        cache = new Cache(this);
        ui = new ComunicacaoThreadUI(this);
        callback = null;
    }

    /** Aqui permitimos injeção de mocks */
    public void setAcessoWeb(AcessoWeb acessoWeb) {
        this.acessoWeb = acessoWeb;
    }

    /** Aqui permitimos injeção de mocks */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /** Definição da resposta */
    protected void setCallback(Callback callback) {
        this.callback = callback;
    }

    /** Fazer get assíncrono com ícone de Loading */
    protected void get(final String url) {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... xxxx) {
                ui.mostrarLoading();
                try {
                    acessoWeb.setUrl(Consts.SERVIDOR + url);
                    String json = acessoWeb.get();
                    if (callback != null) chamarCallback(new JSONObject(json));
                }
                catch (Exception ex) {
                    ui.mensagemSimples("", ui.pegarString(R.string.falha_conexao));
                }
                ui.fecharLoading();
                return null;
            }
        }).execute();
    }

    /** Fazer post assíncrono com ícone de Loading e cache dos dados em caso de falha na conexão */
    protected void post(final String url, final HashMap<String, String> params,
                     final boolean fazerCache) {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... xxxx) {
                ui.mostrarLoading();
                try {
                    acessoWeb.setParams(params);
                    acessoWeb.setUrl(Consts.SERVIDOR + url);
                    String json = acessoWeb.post();
                    if (callback != null) chamarCallback(new JSONObject(json));
                }
                catch (Exception ex) {
                    if (fazerCache) {
                        cache.setParams(params);
                        cache.setUrl(url);
                        cache.salvar();
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
