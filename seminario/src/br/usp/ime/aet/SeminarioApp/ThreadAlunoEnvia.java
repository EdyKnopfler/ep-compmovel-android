package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.util.UUID;
import java.io.OutputStream;

import android.util.Log;

public class ThreadAlunoEnvia extends Thread {

   private String enderecoProf, nusp;
   private ComunicacaoThreadUI ui;
   private BluetoothSocket btSocket;

   public ThreadAlunoEnvia(String enderecoProf, String nusp, ComunicacaoThreadUI ui) {
      this.enderecoProf = enderecoProf;
      this.nusp = nusp;
      this.ui = ui;
   }

   @Override
   public void run() {
      try {
         BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
         BluetoothDevice disp = btAdapter.getRemoteDevice(enderecoProf);
         btSocket = disp.createRfcommSocketToServiceRecord(UUID.fromString(
               Consts.uuid));
         btSocket.connect();
         Log.d("X", "conectei");
         OutputStream saida = btSocket.getOutputStream();
         saida.write(nusp.getBytes());
         Log.d("X", "enviei " + nusp);
         //btSocket.close();
         //Log.d("X", "fechei o socket");
         ui.mensagemSimples(ui.pegarString(R.string.conf_pres_titulo),
                            ui.pegarString(R.string.conf_env_prof) + ": " + nusp);
         ui.fecharTela();
      }
      catch (Exception ex) {
         Log.d("X", "DEU XABLÁU!\n", ex);
         ui.mensagemSimples("ERRO!", ui.pegarString(R.string.btooth_falha_aluno));
      }
   }

}
