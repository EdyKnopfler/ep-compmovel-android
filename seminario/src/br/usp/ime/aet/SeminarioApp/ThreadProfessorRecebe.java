package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothSocket;
import java.io.InputStream;

import android.util.Log;

public class ThreadProfessorRecebe extends Thread {

   private BluetoothSocket btSocket;
   private ComunicacaoThreadUI ui;

   public ThreadProfessorRecebe(BluetoothSocket socket, ComunicacaoThreadUI ui) {
      this.btSocket = socket;
      this.ui = ui;
   }
   
   @Override
   public void run() {
      try {
         InputStream leitura = btSocket.getInputStream();
         byte[] buffer = new byte[1024];
         int lidos = leitura.read(buffer);
         byte[] bDados = new byte[lidos];
         
         for (int i = 0; i < lidos; i++)
            bDados[i] = buffer[i];
         
         btSocket.close();
         String nusp = new String(bDados);
         ui.mensagemSimples("Confirmação de Presença!",
            "Recebida confirmação NUSP " + nusp);
      }
      catch (Exception ex) {
         ui.mensagemSimples("ERRO!", ex.getMessage());
         Log.d("X", "Leitura falhou!", ex);
      }
   }

}
