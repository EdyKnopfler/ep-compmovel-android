package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.util.UUID;
import java.io.OutputStream;

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
         OutputStream saida = btSocket.getOutputStream();
         saida.write(nusp.getBytes());
         btSocket.close();
         ui.mensagemSimples(ui.pegarString(R.string.conf_pres_titulo),
                            ui.pegarString(R.string.conf_env_prof) + ": " + nusp);
         ui.fecharTela();
      }
      catch (Exception ex) {
         ui.mensagemSimples("ERRO!", ui.pegarString(R.string.btooth_falha_aluno));
      }
   }

}
