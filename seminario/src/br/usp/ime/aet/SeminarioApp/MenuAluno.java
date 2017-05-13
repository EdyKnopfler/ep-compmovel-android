
package br.usp.ime.aet.SeminarioApp;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.app.AlertDialog;
import java.util.HashMap;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;

import android.util.Log;

public class MenuAluno extends Activity {
	private final static	String LOG = "QR";
	private String nusp, idSem, nomeSem, url;

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

   public void comprovarPresencaBlueTooth(View v) {
		Intent i = new Intent(this, AlunoConfirmBluetooth.class);
		i.putExtra("nusp", nusp);
		startActivity(i);
   }

   public void comprovarPresencaQRCode(View v){
	IntentIntegrator intentIntegrator = new IntentIntegrator(this); // where this is activity 
	intentIntegrator.initiateScan(IntentIntegrator.QR_CODE_TYPES); // or QR_CODE_TYPES if you need to scan QR	
   }
@Override
public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    IntentResult result = 
        IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    if (result != null) {
        String contents = result.getContents();

        if (contents != null) {
		idSem = contents;
		postPresenca();

        } else {
            showDialog(R.string.falha,
                getString(R.string.falha_operacao));
        }
    }
}

private void postPresenca() {

	HashMap<String, String> params = new HashMap<String, String>();
	params.put("nusp", nusp);
	params.put("seminar_id", idSem);
	String url = Consts.SERVIDOR + "attendence/submit";

	try {
		String resposta = HttpRequest.post(url).form(params).body();
		JSONObject json = new JSONObject(resposta);

		if (json.getString("success").equals("true")){

            		showDialog(R.string.sucesso, getString(R.string.conf_sucesso));
		}
		else{
            		showDialog(R.string.falha, getString(R.string.falha_operacao));
		}
	}
	catch (Exception ex) {
		Log.d(LOG, "Deu pau");
	}
}

private void showDialog(int title, CharSequence message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(title);
    builder.setMessage(message);
    builder.setPositiveButton(R.string.ok, null);
    builder.show();
}


}
