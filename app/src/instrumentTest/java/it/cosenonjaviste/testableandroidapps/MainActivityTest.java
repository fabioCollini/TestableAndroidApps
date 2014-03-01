package it.cosenonjaviste.testableandroidapps;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by fabiocollini on 28/02/14.
 */
@SmallTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();

        registerIdlingResources();
    }

    public void testSimpleClickAndCheckText() {
        onView(withId(R.id.query))
                .perform(ViewActions.typeText("abc"));

        onView(withId(R.id.search))
                .perform(ViewActions.click());

        onData(is(instanceOf(Repo.class))).atPosition(0).check(matches(isDisplayed()));

//        onView(withId(R.id.list)).check(matches(withChildCount(is(greaterThan(0))))); // Because the previous instruction makes sure we have data

//        Espresso.onData(Matchers.anything()).check(ViewAssertions.matches(ViewMatchers.withText("aaaaaaaaaaaaa")));

//        onView(ViewMatchers.withId(R.id.list))
//                .check(ViewAssertions.matches(ViewMatchers.withText("Hello Espresso!")));
    }
}
