package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothSocket;
import java.io.InputStream;
import java.util.HashMap;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;

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
         byte[] buffer = new byte[1024];
         InputStream leitura = btSocket.getInputStream();
         int lidos = leitura.read(buffer);
         btSocket.close();
         byte[] bDados = new byte[lidos];

         for (int i = 0; i < lidos; i++)
            bDados[i] = buffer[i];

         String nusp = new String(bDados);

  			HashMap<String, String> params = new HashMap<String, String>();
  			params.put("nusp", nusp);
  			// params.put("seminar_id", semId);  // Tem que enviar o ID do Seminário :P
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
         ui.mensagemSimples("ERRO!", ex.getMessage());
      }
   }

}
