package it.cosenonjaviste.testableandroidapps.base;

import android.app.Activity;
import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;

import org.mockito.MockitoAnnotations;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.AppModule;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxUtils;
import rx.schedulers.Schedulers;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;

public class BaseActivityTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    public BaseActivityTest(Class<T> activityClass) {
        super(activityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        setupDexmaker();

        MockitoAnnotations.initMocks(this);

        final EspressoExecutor espressoExecutor = EspressoExecutor.newCachedThreadPool();

        ObjectGraphHolder.forceObjectGraphCreator(new ObjectGraphCreator() {
            @Override public ObjectGraph create(Application app) {
                Object[] testModules = getTestModules();
                Object[] modules = new Object[testModules.length + 1];
                modules[0] = new AppModule(app);
                System.arraycopy(testModules, 0, modules, 1, testModules.length);
                return ObjectGraph.create(modules);
            }
        });

        registerIdlingResources(espressoExecutor);

        RxUtils.setIo(Schedulers.from(espressoExecutor));

        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    protected Object[] getTestModules() {
        return new Object[0];
    }

    /**
     * Workaround for Mockito and JB-MR2 incompatibility to avoid
     * java.lang.IllegalArgumentException: dexcache == null
     *
     * @see <a href="https://code.google.com/p/dexmaker/issues/detail?id=2">
     * https://code.google.com/p/dexmaker/issues/detail?id=2</a>
     */
    private void setupDexmaker() {
        // Explicitly set the Dexmaker cache, so tests that use mockito work
        final String dexCache = getInstrumentation().getTargetContext().getCacheDir().getPath();
        System.setProperty("dexmaker.dexcache", dexCache);
    }
}
