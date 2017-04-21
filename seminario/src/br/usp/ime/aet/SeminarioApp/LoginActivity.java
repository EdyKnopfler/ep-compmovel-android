package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.os.AsyncTask;
import java.util.HashMap;
import com.github.kevinsawicki.http.HttpRequest;
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
         url = Consts.SERVIDOR + "login/teacher";
      else
         url = Consts.SERVIDOR + "login/student";
      
      Log.d(LOG, nusp + " " + pass);
      new LoginTask().execute();
   }
   
   private class LoginTask extends AsyncTask<Void, Void, Void> {
      @Override
      protected Void doInBackground(Void ... params) {
         try {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("nusp", nusp);
				data.put("pass", pass);
				String json = HttpRequest
						.post(url)
						.form(data)
						.body();

				JSONObject token = new JSONObject(json);
				Log.d(LOG, token.getString("success"));
         }
         catch (Exception e) {
            Log.d(LOG, "\n\nDeu xabláu!", e);
         }
         
         return null;
      }
   }
   
}
