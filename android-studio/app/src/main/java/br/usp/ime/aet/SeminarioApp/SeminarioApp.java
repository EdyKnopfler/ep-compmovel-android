// Referência:
// https://github.com/joaobmonteiro/livro-android/blob/master/06-TwitterSearch-1/src/br/com/casadocodigo/twittersearch/TwitterSearchActivity.java

package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import java.util.ArrayList;
import android.os.AsyncTask;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;
import android.app.AlertDialog;

public class SeminarioApp extends Activity {

    private Cache cache;
    private ComunicacaoThreadUI ui;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        cache = new Cache(this);
        ui = new ComunicacaoThreadUI(this);
        new Reenvio().start();
    }

    public void pagLogin(View v) {
        Intent i = new Intent(this, LoginActivity.class);

        if (v == findViewById(R.id.professor)) i.putExtra("tipo", "prof");
        else i.putExtra("tipo", "aluno");

        startActivity(i);
    }

    private class Reenvio extends Thread {
        @Override
        public void run() {
            boolean reenviouAlgo = false;
            ArrayList<Cache.PostPendente> posts = cache.postsPendentes();
            if (posts.size() == 0) return;

            ui.mostrarLoading();
            try {
                for (Cache.PostPendente p : posts) {
                    String json = HttpRequest.post(Consts.SERVIDOR + p.url).form(p.params).body();
                    JSONObject token = new JSONObject(json);
                    reenviouAlgo = token.getString("success").equals("true");
                    // Não tentaremos reenviar coisas que deram erro no servidor
                    // (ex.: NUSP já cadastrado)
                    cache.remover(p.strParams);
                }

                if (reenviouAlgo)
                    ui.mensagemSimples("", ui.pegarString(R.string.cache_enviado));
            }
            catch (Exception e) {
                // Pode ignorar? afinal, bastaria manter no cache para os
                // próximos envios
                e.printStackTrace();
            }
            ui.fecharLoading();
        }
    }

}
