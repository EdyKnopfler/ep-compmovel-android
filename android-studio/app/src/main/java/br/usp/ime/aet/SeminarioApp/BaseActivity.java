package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Classe base para as activities - permite injetar mocks dos objetos de acesso à web.
 * Aqui evitamos que os testes acessem o servidor e realizem cache das falhas simuladas.
 */
public class BaseActivity extends Activity {

    protected Servidor servidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        servidor = new Servidor(this);
    }

    public void setAcessoWeb(AcessoWeb acessoWeb) {
        servidor.setAcessoWeb(acessoWeb);
    }

    public void setCache(Cache cache) {
        servidor.setCache(cache);
    }

}
