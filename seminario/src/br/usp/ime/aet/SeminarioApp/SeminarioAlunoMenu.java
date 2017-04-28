package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import android.util.Log;

public class SeminarioAlunoMenu extends Activity {

	private String nuspAluno, idSem, nomeSem;
	private TextView tvNome;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.seminario_aluno_menu);
      idSem = getIntent().getStringExtra("id");
      nomeSem = getIntent().getStringExtra("nome");
		nuspAluno = getIntent().getStringExtra("nusp");
		tvNome = (TextView) findViewById(R.id.nome_sem);
		tvNome.setText(nomeSem);
   }

   public void comprovarPresencaBlueTooth() {
		Intent i = new Intent(this, AlunoConfirmBluetooth.class);
		i.putExtra("nusp", nuspAluno);
		startActivity(i);
   }

   public void comprovarPresencaQRCode(){

   }
}
