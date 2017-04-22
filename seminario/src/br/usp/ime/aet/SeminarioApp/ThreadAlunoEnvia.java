package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.util.UUID;
import java.io.OutputStream;

import android.util.Log;

public class ThreadAlunoEnvia extends Thread {
   
   private String enderecoProf;
   private BluetoothSocket btSocket;
   private String nusp, semId;
   
   public ThreadAlunoEnvia(String enderecoProf, String nusp, String semId) {
      this.enderecoProf = enderecoProf;
      this.nusp = nusp;
      this.semId = semId;
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
         saida.write((nusp + " " + semId).getBytes());
         btSocket.close();
         Log.e("X", "Enviado com sucesso!");
      }
      catch (Exception ex) {
         Log.e("X", "Envio falhou!", ex);
      }
   }
   
}
