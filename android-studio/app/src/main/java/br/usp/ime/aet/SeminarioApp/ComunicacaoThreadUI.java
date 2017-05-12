package br.usp.ime.aet.SeminarioApp;

import android.app.Activity;
import android.app.AlertDialog;

// Auxílio para threads poderem interagir com a UI
public class ComunicacaoThreadUI {

   private Activity tela;

   // Que tela está rodando?
   public ComunicacaoThreadUI(Activity tela) {
      this.tela = tela;
   }

   public Activity getTela() {
      return tela;
   }

   public void mensagemSimples(final String titulo, final String mensagem) {
      tela.runOnUiThread(new Runnable() {
         @Override
         public void run() {
            AlertDialog.Builder alert = new AlertDialog.Builder(tela);
            alert.setTitle(titulo);
            alert.setMessage(mensagem);
            alert.setPositiveButton("OK", null);
            alert.show();
         }
      });
   }

   public void fecharTela() {
      tela.runOnUiThread(new Runnable() {
         @Override
         public void run() {
            tela.finish();
         }
      });
   }

   public String pegarString(int id) {
       return tela.getResources().getString(id);
   }

   public void rodarNaUI(Runnable codigo) {
       tela.runOnUiThread(codigo);
   }

}
