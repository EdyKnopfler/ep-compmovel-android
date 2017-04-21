package br.usp.ime.aet.SeminarioApp;

import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.HashMap;
import android.os.Bundle;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;

public class AlunoConfirmBluetooth extends TelaBluetooth {
   
   private ListView listaDispositivos;
   private ArrayAdapter<String> listaAdapter;
   private HashMap<String, String> enderecos;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.aluno_confirm_bluetooth);
      listaDispositivos = (ListView) findViewById(R.id.lista_dispositivos);
      listaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
      listaDispositivos.setAdapter(listaAdapter);
      enderecos = new HashMap<String, String>();
   }
   
   @Override
   protected void acaoBluetooth() {
      IntentFilter filtro = new IntentFilter(BluetoothDevice.ACTION_FOUND);
      registerReceiver(descoberta, filtro);
      getBtAdapter().startDiscovery();
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
   protected void acaoDesconexao() {
      unregisterReceiver(descoberta);
      getBtAdapter().cancelDiscovery();
   }

}
