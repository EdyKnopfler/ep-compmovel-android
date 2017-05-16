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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public IntentsTestRule<LoginActivity> rule = new IntentsTestRule<>(LoginActivity.class);

    @Mock
    private AcessoWeb acessoWeb;

    private void configura(String sucesso, String tipo) {
        // Dados iniciais
        LoginActivity tela = (LoginActivity) rule.getActivity();
        tela.getIntent().putExtra("tipo", tipo);

        // Mock do servidor
        MockitoAnnotations.initMocks(this);
        when(acessoWeb.post()).thenReturn("{\"success\": \"" + sucesso + "\"}");
        tela.setAcessoWeb(acessoWeb);

        // Digita
        onView(withId(R.id.nusp))
                .perform(typeText("nusp fake"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("password fake"), closeSoftKeyboard());

        // Clica
        onView(withId(R.id.confirmar)).perform(click());
    }

    @Test
    public void loginProfessorSucesso() {
        configura("true", "prof");
        intended(hasComponent(MenuProf.class.getName()));
    }

    @Test
    public void loginProfessorFalha() {
        configura("false", "prof");

        // A tela NÃO deve abrir
        try {
            intended(hasComponent(MenuProf.class.getName()));
            fail();
        }
        catch (Error e) {}
    }

    @Test
    public void loginAlunoSucesso() {
        configura("true", "aluno");
        intended(hasComponent(MenuAluno.class.getName()));
    }

    @Test
    public void loginAlunoFalha() {
        configura("false", "aluno");

        // A tela NÃO deve abrir
        try {
            intended(hasComponent(MenuAluno.class.getName()));
            fail();
        }
        catch (Error e) {}
    }

}
