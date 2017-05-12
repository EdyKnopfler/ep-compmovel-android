
package br.usp.ime.aet.SeminarioApp;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.app.AlertDialog;

import android.util.Log;

public class SeminarioAlunoMenu extends Activity {

    private final static String LOG = "QR";
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

    public void comprovarPresencaBlueTooth(View v) {
        Intent i = new Intent(this, AlunoConfirmBluetooth.class);
        i.putExtra("nusp", nuspAluno);
        startActivity(i);
    }

    public void comprovarPresencaQRCode(View v) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                showDialog(R.string.sucesso, result.toString());
            } else {
                showDialog(R.string.falha,
                        getString(R.string.falha_operacao));
            }
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
