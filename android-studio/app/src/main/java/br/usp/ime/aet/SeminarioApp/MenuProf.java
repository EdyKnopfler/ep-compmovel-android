package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MenuProf extends Activity {

    private String nusp;

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.prof_menu);
       nusp = getIntent().getStringExtra("nusp");
   }

   public void alterarCadastro(View v) {
	   Intent i;
	   i = new Intent(this, AlterarCadastro.class);
	   i.putExtra("tipo", "prof");
       i.putExtra("nusp", nusp);
	   startActivity(i);
   }

   public void cadastrarSeminario(View v) {
	   Intent i;
	   i = new Intent(this, CadastroSeminario.class);
	   startActivity(i);
   }

   public void listarSeminarios(View v){
	   Intent i;
	   i = new Intent(this, ListarSeminarios.class);
       i.putExtra("tipo", "prof");
	   startActivity(i);
   }

}
