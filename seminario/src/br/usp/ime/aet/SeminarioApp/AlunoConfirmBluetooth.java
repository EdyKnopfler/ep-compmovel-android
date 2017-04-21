package br.usp.ime.aet.SeminarioApp;

import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.HashMap;
import android.os.Bundle;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.widget.AdapterView;
import static android.widget.AdapterView.OnItemClickListener;

import android.util.Log;

public class AlunoConfirmBluetooth extends TelaBluetooth
                                   implements OnItemClickListener {
   
   private ListView listaDispositivos;
   private ArrayAdapter<String> listaAdapter;
   private LinearLayout nenhumEncontrado;
   private HashMap<String, String> enderecos;
   

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.aluno_confirm_bluetooth);
      listaDispositivos = (ListView) findViewById(R.id.lista_dispositivos);
      listaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
      listaDispositivos.setAdapter(listaAdapter);
      listaDispositivos.setOnItemClickListener(this);
      nenhumEncontrado = (LinearLayout) findViewById(R.id.nenhum_disp_encontrado);
      enderecos = new HashMap<String, String>();
   }
   
   @Override
   protected void acaoBluetooth() {
      IntentFilter filtro = new IntentFilter();
      filtro.addAction(BluetoothDevice.ACTION_FOUND);
      filtro.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
      registerReceiver(descoberta, filtro);
      getBtAdapter().startDiscovery();
   }
   
   private final BroadcastReceiver descoberta = new BroadcastReceiver() {
      public void onReceive(Context context, Intent intent) {
         String acao = intent.getAction();
         
         if (BluetoothDevice.ACTION_FOUND.equals(acao)) {
            nenhumEncontrado.setVisibility(View.INVISIBLE);
            BluetoothDevice disp = 
                  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            enderecos.put(disp.getName(), disp.getAddress());
            listaAdapter.add(disp.getName());
         }
         else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(acao)) {
            if (enderecos.size() == 0)
               nenhumEncontrado.setVisibility(View.VISIBLE);
         }
      }
      
   };
   
   public void tentarNovamente(View v) {
      getBtAdapter().startDiscovery();
   }
   
   // Estes parâmetros significam isto mesmo?
   public void onItemClick(AdapterView<?> lista, View item, int pos, long id) {
      String dispositivo = (String) listaDispositivos.getItemAtPosition(pos);
      ConexaoAluno conexao = new ConexaoAluno(enderecos.get(dispositivo));
      
      try {
         // depois!
      }
      catch (Exception ex) {
         Toast.makeText(this, R.string.conexao_falhou, Toast.LENGTH_LONG)
              .show();
         Log.d("X", "Conexão falhou!", ex);
      }
   }
   
   @Override
   protected void acaoDesconexao() {
      unregisterReceiver(descoberta);
      getBtAdapter().cancelDiscovery();
   }

}
