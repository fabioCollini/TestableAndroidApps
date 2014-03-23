package it.cosenonjaviste.testableandroidapps.base;

import android.app.Activity;
import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.AppModule;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;

public class BaseActivityTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    public BaseActivityTest(Class<T> activityClass) {
        super(activityClass);
    }

    public void setUp() throws Exception {
        super.setUp();

        final EspressoExecutor espressoExecutor = EspressoExecutor.newCachedThreadPool();

        ObjectGraphHolder.forceObjectGraphCreator(new ObjectGraphCreator() {
            @Override public ObjectGraph create(Application app) {
                Object[] testModules = getTestModules();
                Object[] modules = new Object[testModules.length + 2];
                modules[0] = new AppModule(app);
                modules[1] = new EspressoExecutorTestModule(espressoExecutor);
                System.arraycopy(testModules, 0, modules, 2, testModules.length);
                return ObjectGraph.create(modules);
            }
        });

        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();

        registerIdlingResources(espressoExecutor);
    }

    protected Object[] getTestModules() {
        return new Object[0];
    }
}
