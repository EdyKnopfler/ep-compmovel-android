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

import android.util.Log;

public class SeminarioApp extends Activity {

   private Cache cache;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      cache = new Cache(this);
      new Reenvio().execute();
   }

   public void pagLogin(View v) {
      Intent i = new Intent(this, LoginActivity.class);

      if (v == findViewById(R.id.professor)) i.putExtra("tipo", "prof");
      else i.putExtra("tipo", "aluno");

      startActivity(i);
   }

   private class Reenvio extends AsyncTask<String, Void, String> {
      @Override
      protected String doInBackground(String ... params) {
         boolean reenviouAlgo = false;
         ArrayList<Cache.PostPendente> posts = cache.postsPendentes();
         if (posts.size() == 0) return null;

         try {
            for (Cache.PostPendente p : posts) {

               Log.d("X", p.url + "\n" + p.strParams + "\n");

               String json = HttpRequest.post(p.url).form(p.params).body();
               JSONObject token = new JSONObject(json);
               reenviouAlgo = token.getString("success").equals("true");
               // Não tentaremos reenviar coisas que deram erro no servidor
               // (ex.: NUSP já cadastrado)
               cache.remover(p.strParams);
            }

            if (reenviouAlgo) {
               runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                     AlertDialog.Builder alert = new AlertDialog.Builder(SeminarioApp.this);
         				alert.setTitle("Cache Enviado");
         				alert.setMessage(getResources().getString(R.string.cache_enviado));
         				alert.setPositiveButton("OK", null);
         				alert.show();
                  }
               });
            }
         }
         catch (Exception e) {
            // Pode ignorar? afinal, bastaria manter no cache para os
            // próximos envios
         }

         return null;
      }
   }

}
