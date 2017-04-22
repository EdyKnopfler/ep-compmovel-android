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
         Log.e("X", "Aluno vai enviar...");
         BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
         Log.e("X", "Peguei o adapter");
         BluetoothDevice disp = btAdapter.getRemoteDevice(enderecoProf);
         Log.e("X", "Peguei o dispositivo");
         btSocket = disp.createRfcommSocketToServiceRecord(UUID.fromString(
               ThreadEscutaProfessor.uuid));
         Log.e("X", "Peguei o socket");
         btSocket.connect();
         Log.e("X", "Conectei :)");
         OutputStream saida = btSocket.getOutputStream();
         Log.e("X", "Peguei o OutputStream");
         saida.write(nusp.getBytes());
         Log.e("X", "Mandei o NUSP");
         btSocket.close();
         Log.e("X", "Fechei o socket");
         ui.mensagemSimples(ui.pegarString(R.string.conf_pres_titulo), 
                            ui.pegarString(R.string.conf_env_prof) + ": " + nusp);
         Log.e("X", "Mostrei o alerta");
         ui.fecharTela();
         Log.e("X", "Fechei a tela");
      }
      catch (Exception ex) {
         ui.mensagemSimples("ERRO!", ex.getMessage());
         Log.e("X", "Envio falhou!", ex);
      }
   }
   
}
