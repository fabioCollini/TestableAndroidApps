package it.cosenonjaviste.testableandroidapps;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.model.Repo;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class MainActivityTest extends BaseActivityTest<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override protected Object[] getTestModules() {
        return new Object[]{new WelcomeDialogManagerTestModule()};
    }

    public void testSearch() {
        onView(withId(R.id.query))
                .perform(ViewActions.typeText("abc"));

        onView(withId(R.id.search))
                .perform(click());

        onData(is(instanceOf(Repo.class))).inAdapterView(withId(R.id.list)).atPosition(3)
                .perform(click());
    }
}
