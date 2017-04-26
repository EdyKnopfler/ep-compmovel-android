package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MenuAluno extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.aluno_menu);
   }
   
   public void alterarCadastro(View v) {
	   Intent i;
	   i = new Intent(this, AlterarCadastro.class);
	   i.putExtra("tipo", "aluno");
	   startActivity(i);
   }

   public void listarSeminarios(View v){
	   Intent i;
	   i = new Intent(this, ListarSeminarios.class);
	   startActivity(i);

   }
   
}
