package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.util.Log;

public class SeminarioProfMenu extends Activity {
	private final static	String LOG = "SeminarioProfMenu";
   @Override
   public void onCreate(Bundle savedInstanceState) {
      Log.d(LOG, "Entrou no SeminarioProfMenu");
      super.onCreate(savedInstanceState);
      setContentView(R.layout.seminario_prof_menu);
   }
   
   public void profBlueTooth(View v) {
	   /*Intent i;
	   i = new Intent(this, AlterarCadastro.class);
	   i.putExtra("tipo", "prof");
	   startActivity(i);*/
   }
 


   public void listarAlunos(View v){
	   /*Intent i;
	   i = new Intent(this, ListarSeminarios.class);
	   startActivity(i);*/
   }
   
}
