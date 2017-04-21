package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class SeminarioApp extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
   }
   
   public void pagLogin(View v) {
      Intent i = new Intent(this, LoginActivity.class);
      
      if (v == findViewById(R.id.professor)) i.putExtra("tipo", "prof");
      else i.putExtra("tipo", "aluno");
            
      startActivity(i);
   }
   
}
