package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.util.UUID;

import android.util.Log;

public class ThreadEscutaProfessor extends Thread {

   // Para o módulo do aluno informar na conexão
   public static final String uuid = "67186568-26ad-11e7-93ae-92361f002671";
   
   private BluetoothServerSocket btServerSocket;
   
   @Override
   public void run() {
      try {
         BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
         btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord(
            "Seminários", UUID.fromString(uuid));
         
         // Para com uma exceção provocada pelo fechamento do ServerSocket
         while (true) {
            BluetoothSocket btSocket = btServerSocket.accept();
            Thread recebimento = new ThreadProfessorRecebe(btSocket);
            recebimento.start();
         }
      }
      catch (Exception ex) {
         Log.d("X", "ESCUTA PAROU :) !");
      }
   }
   
   public void parar() {
      try {
         Log.d("X", "Vou mandar a escuta parar");
         btServerSocket.close();
      }
      catch (Exception ex) {
         Log.d("X", "Erro ao cancelar o ServerSocket!", ex);
      }
   }
   
}
