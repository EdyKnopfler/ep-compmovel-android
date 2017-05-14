// Referência:
// http://stackoverflow.com/questions/2050263/using-zxing-to-create-an-android-barcode-scanning-app

package br.usp.ime.aet.SeminarioApp;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.app.AlertDialog;
import java.util.HashMap;

public class MenuAluno extends Activity {
    private String nusp, idSem;

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

    public void listarSeminarios(View v) {
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
                idSem = contents;
                postPresenca();
            }
            else {
                showDialog(R.string.falha,
                        getString(R.string.falha_operacao));
            }
        }
    }

    private void postPresenca() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("nusp", nusp);
        params.put("seminar_id", idSem);
        new Servidor(this, new Resposta()).post("attendence/submit", params, true);
    }

    private class Resposta extends Servidor.Callback {
        @Override
        void sucesso() {
            showDialog(R.string.sucesso, getString(R.string.conf_sucesso));
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
