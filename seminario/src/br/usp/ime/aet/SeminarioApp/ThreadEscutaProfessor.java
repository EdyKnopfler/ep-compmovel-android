package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.util.UUID;

public class ThreadEscutaProfessor extends Thread {

   private String idSeminario;
   private ComunicacaoThreadUI ui;
   private BluetoothServerSocket btServerSocket;

   public ThreadEscutaProfessor(String idSeminario, ComunicacaoThreadUI ui) {
      this.idSeminario = idSeminario;
      this.ui = ui;
   }

   @Override
   public void run() {
      try {
         BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
         btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord(
            "Seminários", UUID.fromString(Consts.uuid));

         while (true) {
            BluetoothSocket btSocket = btServerSocket.accept();
            Thread recebimento = new ThreadProfessorRecebe(btSocket, idSeminario, ui);
            recebimento.start();
         }
      }
      catch (Exception ex) {
         // Ao sair da tela, o ServerSocket é fechado e o loop para
      }
   }

   public void parar() {
      try {
         btServerSocket.close();
      }
      catch (Exception ex) {
         ui.mensagemSimples("ERRO!", ex.getMessage());
      }
   }

}
