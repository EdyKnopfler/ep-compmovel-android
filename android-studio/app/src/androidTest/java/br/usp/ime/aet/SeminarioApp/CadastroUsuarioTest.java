package br.usp.ime.aet.SeminarioApp;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CadastroUsuarioTest {

    @Rule
    public IntentsTestRule<CadastroUsuario> rule = new IntentsTestRule<>(CadastroUsuario.class);

    @Mock
    private AcessoWeb acessoWeb;

    @Mock
    private Cache cache;

    private void configura(boolean sucesso) {
        // Dados iniciais
        CadastroUsuario tela = (CadastroUsuario) rule.getActivity();
        tela.getIntent().putExtra("tipo", "aluno");

        // Mock do servidor
        MockitoAnnotations.initMocks(this);
        if (sucesso)
            when(acessoWeb.post()).thenReturn("{\"success\": \"" + sucesso + "\"}");
        else
            when(acessoWeb.post()).thenThrow(new RuntimeException());
        tela.setAcessoWeb(acessoWeb);
        tela.setCache(cache);

        // Digita
        onView(withId(R.id.name))
                .perform(typeText("usuario fake"), closeSoftKeyboard());
        onView(withId(R.id.nusp))
                .perform(typeText("666"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("666"), closeSoftKeyboard());

        // Clica
        onView(withId(R.id.confirmar)).perform(click());
    }

    @Test
    public void cadastroSucesso() {
        configura(true);

        // NÃO deve permanecer na tela
        try {
            intending(hasComponent(CadastroUsuario.class.getName()));
            fail();
        }
        catch (Error e) {} // :)
    }

    @Test
    public void cadastroFalha() {
        configura(false);
        intending(hasComponent(hasClassName(CadastroUsuario.class.getName())));
        // Deve cachear
        verify(cache, times(1)).salvar();
    }

}
