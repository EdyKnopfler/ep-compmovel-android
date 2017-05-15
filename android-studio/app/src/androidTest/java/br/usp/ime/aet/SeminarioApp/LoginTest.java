package br.usp.ime.aet.SeminarioApp;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import com.github.kevinsawicki.http.HttpRequest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpRequest.class)
public class LoginTest {

    @Rule
    public IntentsTestRule<LoginActivity> rule = new IntentsTestRule<>(LoginActivity.class);

    @Mock
    private HttpRequest request;

    @Test
    public void tentaLogar() {

        // Mock do servidor
        when(request.body()).thenReturn("{\"success\": \"true\"}");
        PowerMockito.mockStatic(HttpRequest.class);
        when(HttpRequest.post(Consts.SERVIDOR + "login/teacher")).thenReturn(request);

        // Dados iniciais
        rule.getActivity().getIntent().putExtra("tipo", "prof");

        // Digita
        onView(withId(R.id.nusp))
                .perform(typeText("nusp fake"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("nusp fake"), closeSoftKeyboard());

        // Clica
        onView(withId(R.id.confirmar)).perform(click());

        // A outra tela abriu?
        intended(hasComponent(MenuProf.class.getName()));

    }

}
