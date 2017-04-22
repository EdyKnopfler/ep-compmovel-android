package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothSocket;
import java.io.InputStream;
import java.util.HashMap;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;

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
         Log.d("X", "Professor iniciando recebimento...");
         byte[] buffer = new byte[1024];
         Log.d("X", "Aloquei um buffer");
         InputStream leitura = btSocket.getInputStream();
         Log.d("X", "Peguei o InputStream");
         int lidos = leitura.read(buffer);
         Log.d("X", "Chupei a informação");
         btSocket.close();
         Log.d("X", "Fechei o socket");
         byte[] bDados = new byte[lidos];
         Log.d("X", "Aloquei do tamanho exato");
         
         for (int i = 0; i < lidos; i++)
            bDados[i] = buffer[i];
         Log.d("X", "Copiei os dados");
         
         String nusp = new String(bDados);
         Log.d("X", "Criei a String, NUSP = " + nusp);
         
         
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("nusp", nusp);
			// params.put("seminar_id", semId);  // Tem que enviar o ID do Seminário :P
         Log.d("X", "Enviando requisição para o servidor...");
			String resposta = HttpRequest
			      .post(Consts.SERVIDOR + "attendence/submit")
					.form(params)
					.body();
         Log.d("X", "Resposta chegou!");

			JSONObject json = new JSONObject(resposta);
			Log.d("X", "Success = " + json.getString("success"));
         
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
         Log.d("X", "Leitura falhou!", ex);
      }
   }

}
