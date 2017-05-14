// Referência:
// https://dragaosemchama.com.br/2015/05/programacao-bluetooth-no-android/

package br.usp.ime.aet.SeminarioApp;

import android.bluetooth.BluetoothSocket;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;

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
        byte bDados[];

        try {
            byte[] buffer = new byte[1024];
            InputStream leitura = btSocket.getInputStream();
            int lidos = leitura.read(buffer);
            btSocket.close();
            bDados = new byte[lidos];

            for (int i = 0; i < lidos; i++)
                bDados[i] = buffer[i];
        }
        catch (IOException e) {
            ui.mensagemSimples(ui.pegarString(R.string.falha),
                    ui.pegarString(R.string.falha_btooth_prof));
            return;
        }

        final String nusp = new String(bDados);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("nusp", nusp);
        params.put("seminar_id", idSeminario);
        String url = "attendence/submit";

        new Servidor(ui.getTela(), new Servidor.Callback() {
            @Override
            void sucesso() {
                ui.mensagemSimples(ui.pegarString(R.string.conf_pres_titulo),
                        ui.pegarString(R.string.conf_pres_nusp) + " " + nusp + " " +
                                ui.pegarString(R.string.conf_sucesso) + "!");
            }
        }).post(url, params, true);
    }

}
