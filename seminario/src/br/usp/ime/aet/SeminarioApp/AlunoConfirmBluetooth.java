package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import java.util.Set;
import java.util.HashMap;

import android.util.Log;


public class AlunoConfirmBluetooth extends Activity {
   
   private static final int HABILITADO = 1;
   
   // Recebe mensagens de outras threads
   private boolean habilitadoAnterior;
   private TextView mensagens;
   private ListView listaDispositivos;
   private BluetoothAdapter btAdapter;
   private ArrayAdapter<String> listaAdapter;
   private HashMap<String, String> enderecos;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.aluno_confirm_bluetooth);
      mensagens = (TextView) findViewById(R.id.mensagens);
      listaDispositivos = (ListView) findViewById(R.id.lista_dispositivos);
      
      // Estava faltando ISTO
      listaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
      listaDispositivos.setAdapter(listaAdapter);
      
      enderecos = new HashMap<String, String>();
      ativarBluetooth();
   }
   
   private void ativarBluetooth() {
      btAdapter = BluetoothAdapter.getDefaultAdapter();
      
      if (btAdapter == null) {
         mensagens.setText("Hardware Bluetooth não foi detectado...");
         return;
      }
      
      habilitadoAnterior = btAdapter.isEnabled();
      
      if (!habilitadoAnterior) {
         Intent habilitar = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
         startActivityForResult(habilitar, HABILITADO);
      }

      // Preciso deste else? Não posso requerer a activity mesmo já estando habilitado?
      // (1 só código para os 2 casos) -- TESTAR!
      else {
         IntentFilter filtro = new IntentFilter(BluetoothDevice.ACTION_FOUND);
         registerReceiver(descoberta, filtro);
         btAdapter.startDiscovery();
      }
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == HABILITADO) {
         if (resultCode == RESULT_OK) {
            IntentFilter filtro = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(descoberta, filtro);
            btAdapter.startDiscovery();
         }
         else
            finish();
      }
   }
   
   private final BroadcastReceiver descoberta = new BroadcastReceiver() {
      public void onReceive(Context context, Intent intent) {
         String acao = intent.getAction();
         
         if (BluetoothDevice.ACTION_FOUND.equals(acao)) {
            BluetoothDevice disp = 
                  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            enderecos.put(disp.getName(), disp.getAddress());
            listaAdapter.add(disp.getName());
         }
      }
      
   };
   
   @Override
   protected void onDestroy() {
      super.onDestroy();
      
      if (btAdapter.isEnabled()) {
         unregisterReceiver(descoberta);
      
         if (habilitadoAnterior) {
            btAdapter.disable();
         }
      }
   }

}
