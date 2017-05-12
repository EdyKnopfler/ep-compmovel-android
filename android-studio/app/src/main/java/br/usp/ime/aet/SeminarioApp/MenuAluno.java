package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MenuAluno extends Activity {

   private String nusp;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.aluno_menu);
      nusp = getIntent().getStringExtra("nusp");
   }

   public void alterarCadastro(View v) {
	   Intent i;
	   i = new Intent(this, AlterarCadastro.class);
	   i.putExtra("tipo", "aluno");
      i.putExtra("nusp", nusp);
	   startActivity(i);
   }

   public void listarSeminarios(View v){
	   Intent i;
	   i = new Intent(this, ListarSeminarios.class);
      i.putExtra("tipo", "aluno");
      i.putExtra("nusp", nusp);
	   startActivity(i);
   }

   public void seminariosQueAssisti(View v) {
		Intent i = new Intent(this, SeminariosDoAluno.class);
		i.putExtra("nusp", nusp);
		startActivity(i);
	}

}