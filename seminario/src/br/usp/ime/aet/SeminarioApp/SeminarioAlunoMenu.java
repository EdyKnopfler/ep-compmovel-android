package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.HashMap;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;
import org.json.JSONArray;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.util.Log;

public class SeminarioAlunoMenu extends Activity {

	private static final int ALTERACAO_SEMINARIO = 222;
	private static final String LOG = "sam";

	private String id, nome;
	private TextView tvNome;
	private ListView lista;
	private ArrayAdapter adapter;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.seminario_aluno_menu);
      id = getIntent().getStringExtra("id");
      nome = getIntent().getStringExtra("nome");
   }

   public void comprovarPresencaBlueTooth() {

   }

   public void comprovarPresencaQRCode(){

   }
}
