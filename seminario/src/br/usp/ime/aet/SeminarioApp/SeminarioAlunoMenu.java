
package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import android.util.Log;

public class SeminarioAlunoMenu extends Activity {

	private final static	String LOG = "QR";
	private String nusp, idSem, nomeSem, url;
	private TextView tvNome;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.seminario_aluno_menu);
      //idSem = getIntent().getStringExtra("id");
      nomeSem = getIntent().getStringExtra("nome");
		nusp = getIntent().getStringExtra("nusp");
		tvNome = (TextView) findViewById(R.id.nome_sem);
		tvNome.setText(nomeSem);
   }

}
