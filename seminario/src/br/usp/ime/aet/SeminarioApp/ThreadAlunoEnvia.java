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
         BluetoothDevice disp = btAdapter.getRemoteDevice(enderecoProf);
         btSocket = disp.createRfcommSocketToServiceRecord(UUID.fromString(
               ThreadEscutaProfessor.uuid));
         btSocket.connect();
         OutputStream saida = btSocket.getOutputStream();
         saida.write(nusp.getBytes());
         btSocket.close();
         ui.mensagemSimples("Confirmação de Presença", "Enviada com sucesso!");
      }
      catch (Exception ex) {
         ui.mensagemSimples("ERRO!", ex.getMessage());
         Log.e("X", "Envio falhou!", ex);
      }
   }
   
}
