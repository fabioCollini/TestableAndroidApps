package it.cosenonjaviste.testableandroidapps;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphCreator;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;
import it.cosenonjaviste.testableandroidapps.utils.Clock;
import it.cosenonjaviste.testableandroidapps.utils.DatePrefsSaver;

public class WelcomeDialogManagerMorningTest extends BaseActivityTest {

    public WelcomeDialogManagerMorningTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        ObjectGraphHolder.forceObjectGraphCreator(new ObjectGraphCreator() {
            @Override public ObjectGraph create(Application app) {
                return ObjectGraph.create(new AppModule(app), new TestModule());
            }
        });
        super.setUp();
    }

    public void testMorning() {
        assertTrue(solo.waitForDialogToOpen(1000));
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
