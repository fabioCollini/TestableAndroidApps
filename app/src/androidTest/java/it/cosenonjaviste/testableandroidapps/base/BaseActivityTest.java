package it.cosenonjaviste.testableandroidapps.base;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.Espresso;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.AppModule;
import it.cosenonjaviste.testableandroidapps.service.SearchService;

public class BaseActivityTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    public BaseActivityTest(Class<T> activityClass) {
        super(activityClass);
    }

    public void setUp() throws Exception {
        super.setUp();

        ObjectGraphHolder.forceObjectGraphCreator(new ObjectGraphCreator() {
            @Override public ObjectGraph create() {
                Object[] testModules = getTestModules();
                Object[] modules = new Object[testModules.length + 1];
                modules[0] = new AppModule();
                System.arraycopy(testModules, 0, modules, 1, testModules.length);
                return ObjectGraph.create(modules);
            }
        });

        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();

        Context context = getInstrumentation().getTargetContext();
        IntentServiceIdlingResource idlingResource = new IntentServiceIdlingResource(context, SearchService.class);
        Espresso.registerIdlingResources(idlingResource);
    }

    protected Object[] getTestModules() {
        return new Object[0];
    }
}
