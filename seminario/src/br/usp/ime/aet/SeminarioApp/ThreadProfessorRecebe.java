package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothSocket;
import java.io.InputStream;
import java.util.HashMap;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;

import android.util.Log;

public class ThreadProfessorRecebe extends Thread {

   private BluetoothSocket btSocket;
   private String idSeminario;
   private ComunicacaoThreadUI ui;

   public ThreadProfessorRecebe(BluetoothSocket socket, String idSeminario,
                                ComunicacaoThreadUI ui) {
      this.btSocket = socket;
      this.idSeminario = idSeminario;
      this.ui = ui;
   }

   @Override
   public void run() {
      try {
         byte[] buffer = new byte[1024];
         InputStream leitura = btSocket.getInputStream();
         int lidos = leitura.read(buffer);
         Log.d("X", "recebi");
         btSocket.close();
         Log.d("X", "fechei o socket");
         byte[] bDados = new byte[lidos];

         for (int i = 0; i < lidos; i++)
            bDados[i] = buffer[i];

         String nusp = new String(bDados);
         Log.d("X", "nusp = " + nusp);

  			HashMap<String, String> params = new HashMap<String, String>();
  			params.put("nusp", nusp);
  			params.put("seminar_id", idSeminario);
  			String resposta = HttpRequest
  			      .post(Consts.SERVIDOR + "attendence/submit")
  					.form(params)
  					.body();

  			JSONObject json = new JSONObject(resposta);

         if (json.getString("success").equals("true"))
            ui.mensagemSimples(ui.pegarString(R.string.conf_pres_titulo),
                  ui.pegarString(R.string.conf_pres_nusp) + " " + nusp + " " +
                  ui.pegarString(R.string.conf_sucesso) + "!");
         else
            ui.mensagemSimples(ui.pegarString(R.string.conf_pres_titulo),
                  ui.pegarString(R.string.conf_pres_nusp) + " " + nusp + " " +
                  ui.pegarString(R.string.falhou_mensagem) + ":\n" +
                  json.getString("message"));
      }
      catch (Exception ex) {
         Log.d("X", "DEU XABLÁU!\n", ex);
         ui.mensagemSimples("ERRO!", ex.getMessage());
      }
   }

}
