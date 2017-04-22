package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.util.UUID;

import android.util.Log;

public class ThreadEscutaProfessor extends Thread {

   // Para o módulo do aluno informar na conexão
   public static final String uuid = "67186568-26ad-11e7-93ae-92361f002671";
   
   private ComunicacaoThreadUI ui;
   private BluetoothServerSocket btServerSocket;
   
   public ThreadEscutaProfessor(ComunicacaoThreadUI ui) {
      this.ui = ui;
   }
   
   @Override
   public void run() {
      try {
         Log.d("X", "Professor começando a escuta");
         BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
         Log.d("X", "Peguei o adapter");
         btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord(
            "Seminários", UUID.fromString(uuid));
         Log.d("X", "Criei o ServerSocket");
         
         // Para com uma exceção provocada pelo fechamento do ServerSocket
         while (true) {
            Log.d("X", "Aguardando um aluno...");
            BluetoothSocket btSocket = btServerSocket.accept();
            Log.d("X", "Aluno conectado :)");
            Thread recebimento = new ThreadProfessorRecebe(btSocket, ui);
            recebimento.start();
            Log.d("X", "Disparei o recebimento");
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
         ui.mensagemSimples("ERRO!", ex.getMessage());
         Log.d("X", "Erro ao cancelar o ServerSocket!", ex);
      }
   }
   
}
