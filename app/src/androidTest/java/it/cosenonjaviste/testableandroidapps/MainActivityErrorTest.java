package it.cosenonjaviste.testableandroidapps;

import android.app.Instrumentation;
import android.content.res.Resources;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.model.Repo;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class MainActivityErrorTest extends BaseActivityTest<MainActivity> {

    public MainActivityErrorTest() {
        super(MainActivity.class);
    }

    @Override protected Object[] getTestModules() {
        Instrumentation instrumentation = getInstrumentation();
        final Resources resources = instrumentation.getContext().getResources();

        return new Object[]{new WelcomeDialogManagerTestModule(),
                new ClientErrorStubTestModule(resources)
        };
    }

    public void testSearch() {
        onView(withId(R.id.query))
                .perform(ViewActions.typeText("abc"));

        onView(withId(R.id.search))
                .perform(click());

        closeSoftKeyboard();

        onView(withId(R.id.reload))
                .perform(click());

        onData(is(instanceOf(Repo.class))).atPosition(3)
                .perform(click());
    }
}