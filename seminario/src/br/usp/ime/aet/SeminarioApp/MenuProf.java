package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MenuProf extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.prof_menu);
   }
   
   public void alterarCadastro(View v) {
	   Intent i;
	   i = new Intent(this, AlterarCadastro.class);
	   i.putExtra("tipo", "prof");
	   startActivity(i);
   }
 
   public void cadastrarSeminario(View v) {
	   Intent i;
	   i = new Intent(this, CadastroSeminario.class);
	   startActivity(i);

   }


   public void listarSeminarios(View v){

   }
   
}
