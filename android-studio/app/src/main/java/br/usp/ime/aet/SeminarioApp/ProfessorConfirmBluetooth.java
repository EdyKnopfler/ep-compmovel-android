package br.usp.ime.aet.SeminarioApp;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class ProfessorConfirmBluetooth extends TelaBluetooth {

   private static final int ACAO_VISIBILIDADE = 2;

   private String idSeminario;
   private boolean visivel;
   private ThreadEscutaProfessor escuta;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.prof_confirm_bluetooth);
      idSeminario = getIntent().getStringExtra("id_seminario");
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
         if (resultCode == 1) {
            visivel = true;
            iniciarEscuta();
         }
         else {
            finish();
         }
      }
   }

   private void iniciarEscuta() {
      escuta = new ThreadEscutaProfessor(idSeminario, new ComunicacaoThreadUI(this));
      escuta.start();
   }

   @Override
   protected void acaoDesconexao() {
      if (visivel)
         escuta.parar();
   }

}
