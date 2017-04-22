package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothSocket;
import java.io.InputStream;

import android.util.Log;

public class ThreadProfessorRecebe extends Thread {

   private BluetoothSocket btSocket;

   public ThreadProfessorRecebe(BluetoothSocket socket) {
      this.btSocket = socket;
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
         String sDados = new String(bDados);
         String[] vDados = sDados.split(" ");
         String nusp = vDados[0];
         String semId = vDados[1];
         Log.d("X", "Recebido com sucesso! " + nusp + " " + semId);
      }
      catch (Exception ex) {
         Log.d("X", "Leitura falhou!", ex);
      }
   }

}
