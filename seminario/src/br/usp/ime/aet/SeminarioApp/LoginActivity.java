package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.os.AsyncTask;
import java.util.HashMap;
import org.json.JSONObject;
import android.util.Log;

public class LoginActivity extends Activity {
   private final static	String LOG = "login";
   private EditText e_nusp, e_pass;
   private String nusp, pass, url;
   
   @Override
   public void onCreate(Bundle state) {
      super.onCreate(state);
      setContentView(R.layout.login);
      
      e_nusp = (EditText) findViewById(R.id.nusp);
      e_pass = (EditText) findViewById(R.id.password);
   }
   
   public void postLogin(View view) {
      nusp = e_nusp.getText().toString();
      pass = e_pass.getText().toString();
      
      if (getIntent().getStringExtra("tipo").equals("prof"))
         url = "http://207.38.82.139:8001/login/teacher";
      else
         url = "http://207.38.82.139:8001/login/student";
      
      Log.d(LOG, nusp + " " + pass);
      new LoginTask().execute();
   }
   
   private class LoginTask extends AsyncTask<Void, Void, Void> {
      @Override
      protected Void doInBackground(Void ... params) {
         try {
         }
         catch (Exception e) {
            Log.d(LOG, "\n\nDeu xabláu!", e);
         }
         
         return null;
      }
   }
   
}
