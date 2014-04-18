package it.cosenonjaviste.testableandroidapps;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.model.Repo;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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


        closeSoftKeyboard();

        onView(withId(R.id.reload))
                .check(matches(not(isDisplayed())));

        onData(is(instanceOf(Repo.class))).atPosition(3)
                .perform(click());
    }
}
