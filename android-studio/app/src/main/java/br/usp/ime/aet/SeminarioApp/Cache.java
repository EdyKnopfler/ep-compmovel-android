package br.usp.ime.aet.SeminarioApp;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URLDecoder;

public class Cache {

   public static class PostPendente {
      public String url, strParams;
      public HashMap<String, String> params;
   }

   private SharedPreferences config;

   public Cache(Context context) {
      this.config = context.getSharedPreferences(
         "br.usp.ime.aet.SeminarioApp.CACHE", Context.MODE_PRIVATE);
   }

   public void salvar(HashMap<String, String> params, String url) {
      try {
         SharedPreferences.Editor editor = config.edit();
         editor.putString(urlEncode(params), url);
         editor.commit();
      }
      catch (UnsupportedEncodingException e) {
      }
   }

   public ArrayList<PostPendente> postsPendentes() {
      try {
         ArrayList<PostPendente> posts = new ArrayList<PostPendente>();
         Map<String, ?> configs = config.getAll();

         for (String strParams : configs.keySet()) {
            PostPendente post = new PostPendente();
            post.url = (String) configs.get(strParams);
            post.strParams = strParams;
            post.params = urlDecode(strParams);
            posts.add(post);
         }

         return posts;
      }
      catch (UnsupportedEncodingException e) {
         return null;
      }
   }

   public void remover(String strParams) {
      SharedPreferences.Editor editor = config.edit();
      editor.remove(strParams);
      editor.commit();
   }

   private String urlEncode(HashMap<String, String> params) throws UnsupportedEncodingException {
      StringBuilder sb = new StringBuilder();

      // Evitando conflitos de posts com parâmetros iguais!
      sb.append("__hora=").append(Calendar.getInstance().getTimeInMillis());

      for (String p : params.keySet())
         sb.append("&").append(p).append("=")
           .append(URLEncoder.encode(params.get(p), "UTF-8"));

      return sb.toString();
   }

   private HashMap<String, String> urlDecode(String str) throws UnsupportedEncodingException {
      HashMap<String, String> params = new HashMap<String, String>();
      String[] pares = str.split("&");

      for (String p : pares) {
         String[] partes = p.split("=");
         if (partes[0].equals("__hora")) continue;
         params.put(partes[0], URLDecoder.decode(partes[1], "UTF-8"));
      }

      return params;
   }

}
