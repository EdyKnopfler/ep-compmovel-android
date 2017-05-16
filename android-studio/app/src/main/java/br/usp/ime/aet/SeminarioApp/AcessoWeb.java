// Referência:
// https://github.com/joaobmonteiro/livro-android/blob/master/06-TwitterSearch-1/src/br/com/casadocodigo/twittersearch/TwitterSearchActivity.java

package br.usp.ime.aet.SeminarioApp;

import com.github.kevinsawicki.http.HttpRequest;
import java.util.HashMap;

/** Encapsula as chamadas ao servidor de forma a ser possível mockar */
public class AcessoWeb {

    private String url;
    private HashMap<String, String> params;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public String get() {
        String json = HttpRequest.get(url).body();
        return json;
    }

    public String post() {
        String json = HttpRequest.post(url).form(params).body();
        return json;
    }

}
