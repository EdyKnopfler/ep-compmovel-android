package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;

public abstract class TelaBluetooth extends Activity {
   
   private static final int ACAO_HABILITAR = 1;
   
   private boolean habilitadoAnterior, habilitado;
   private BluetoothAdapter btAdapter;
   
   // O que as telas com Bluetooth farão? 
   protected abstract void acaoBluetooth();
   
   // Alguém precisa liberar algum recurso?
   protected abstract void acaoDesconexao();
   
   // Acesso ao Bluetooth adapter
   protected BluetoothAdapter getBtAdapter() {
      return btAdapter;
   }
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      habilitarBluetooth();
   }
   
   private void habilitarBluetooth() {
      btAdapter = BluetoothAdapter.getDefaultAdapter();
      
      if (btAdapter == null) {
         Toast.makeText(this, getResources().getString(R.string.bluetooth_nao_detect), 
                        Toast.LENGTH_LONG).show();
         finish();
      }
      
      habilitadoAnterior = btAdapter.isEnabled();
      
      if (!habilitadoAnterior) {
         Intent habilitar = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
         startActivityForResult(habilitar, ACAO_HABILITAR);
      }
      else {
         habilitado = true;
         acaoBluetooth();
      }
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == ACAO_HABILITAR) {
         if (resultCode == RESULT_OK) {
            habilitado = true;
            acaoBluetooth();
         }
         else
            finish();
      }
   }
   
   @Override
   protected void onDestroy() {
      if (btAdapter.isEnabled()) {
         acaoDesconexao();
      
         if (habilitado && !habilitadoAnterior)
            btAdapter.disable();
      }
      
      super.onDestroy();
   }

}
