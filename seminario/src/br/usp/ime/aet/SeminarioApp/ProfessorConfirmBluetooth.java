package br.usp.ime.aet.SeminarioApp;

import android.os.Bundle;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.util.Log;

public class ProfessorConfirmBluetooth extends TelaBluetooth {
   
   private static final int ACAO_VISIBILIDADE = 1;
   private static final int ACAO_INVISIBILIDADE = 2;
   
   private boolean visivel;
   private ThreadEscutaProfessor escuta;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.prof_confirm_bluetooth);
   }
   
   @Override
   protected void acaoBluetooth() {
      ativarVisibilidade();
   }
   
   private void ativarVisibilidade() {
      boolean visivelAnterior = getBtAdapter().getScanMode() == 
                        BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE;
                        
      if (!visivelAnterior) {
         Intent visivel = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
         visivel.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
         startActivityForResult(visivel, ACAO_VISIBILIDADE);
      }
      else {
         visivel = true;
         iniciarEscuta();
      }
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      
      if (requestCode == ACAO_VISIBILIDADE) {
         if (resultCode == RESULT_OK) {
            visivel = true;
            iniciarEscuta();
         }
         else
            finish();
      }
   }
   
   private void iniciarEscuta() {
      ((TextView) findViewById(R.id.pronto_receber)).setText(
            getResources().getString(R.string.esperando_alunos));
      escuta = new ThreadEscutaProfessor(new ComunicacaoThreadUI(this));
      escuta.start();
   }
   
   @Override
   protected void acaoDesconexao() {
      Log.d("X", "Professor fechando a tela");
      if (visivel) {
         Log.d("X", "Estamos visíveis!");
         escuta.parar();
         Log.d("X", "Mandei parar a escuta");
      }
   }
   
}
