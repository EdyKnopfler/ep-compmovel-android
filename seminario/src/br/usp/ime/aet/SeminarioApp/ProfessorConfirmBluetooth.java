package br.usp.ime.aet.SeminarioApp;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.util.Log;

public class ProfessorConfirmBluetooth extends TelaBluetooth {
   
   private static final int ACAO_VISIBILIDADE = 2;
   
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
      Log.d("X", "Ativando visibilidade...");
      boolean visivelAnterior = getBtAdapter().getScanMode() == 
                        BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE;
                        
      if (!visivelAnterior) {
         Log.d("X", "Estávamos invisíveis!");
         Intent visivel = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
         visivel.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
         Log.d("X", "Vou pedir confirmação");
         startActivityForResult(visivel, ACAO_VISIBILIDADE);
      }
      else {
         Log.d("X", "Estávamos visíveis!");
         visivel = true;
         iniciarEscuta();
      }
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      
      if (requestCode == ACAO_VISIBILIDADE) {
         if (resultCode == 1) {
            Log.d("X", "Agora estamos visíveis!");
            visivel = true;
            iniciarEscuta();
         }
         else {
            Log.d("X", "Resolveu não permitir");
            finish();
         }
      }
   }
   
   private void iniciarEscuta() {
      Log.d("X", "Vou iniciar a escuta");
      escuta = new ThreadEscutaProfessor(new ComunicacaoThreadUI(this));
      Log.d("X", "Criei a thread");
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
