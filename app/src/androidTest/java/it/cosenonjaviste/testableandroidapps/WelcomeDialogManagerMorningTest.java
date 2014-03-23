package it.cosenonjaviste.testableandroidapps;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.utils.Clock;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

public class WelcomeDialogManagerMorningTest extends BaseActivityTest<MainActivity> {

    public WelcomeDialogManagerMorningTest() {
        super(MainActivity.class);
    }

    @Override protected Object[] getTestModules() {
        return new Object[]{new TestModule()};
    }

    public void testMorning() {
        onView(withText(R.string.welcome))
                .check(matches(isDisplayed()));
    }

    @Module(overrides = true, library = true)
    public class TestModule {
        @Provides @Singleton
        public Clock provideClock() {
            return new ClockStub(7, 22);
        }

        @Provides @Singleton
        public DatePrefsSaver provideDatePrefsSaver() {
            return new DatePrefsSaverStub(false);
        }
    }
}
