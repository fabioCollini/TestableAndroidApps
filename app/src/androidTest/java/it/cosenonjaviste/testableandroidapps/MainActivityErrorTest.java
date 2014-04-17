package it.cosenonjaviste.testableandroidapps;

import android.app.Instrumentation;
import android.content.res.Resources;

import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;

import org.mockito.Mock;

import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.share.ShareHelper;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class MainActivityErrorTest extends BaseActivityTest<MainActivity> {

    @Mock ShareHelper shareHelper;

    public MainActivityErrorTest() {
        super(MainActivity.class);
    }

    @Override protected Object[] getTestModules() {
        Instrumentation instrumentation = getInstrumentation();
        final Resources resources = instrumentation.getContext().getResources();

        return new Object[]{new WelcomeDialogManagerTestModule(),
                new GitHubServiceErrorTestModule(resources),
                new ShareHelperTestModule(shareHelper)};
    }

    public void testSearch() {
        onView(withId(R.id.query))
                .perform(ViewActions.typeText("abc"));

        onView(withId(R.id.search))
                .perform(click());

        onView(withId(R.id.reload))
                .perform(click());

        closeSoftKeyboard();

        onData(is(instanceOf(Repo.class))).inAdapterView(withId(R.id.list)).atPosition(3)
                .perform(click());

        verify(shareHelper).share(anyString(), anyString());
    }
}