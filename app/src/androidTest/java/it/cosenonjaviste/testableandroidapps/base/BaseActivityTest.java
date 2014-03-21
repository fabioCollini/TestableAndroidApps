package it.cosenonjaviste.testableandroidapps.base;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;

public class BaseActivityTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {
    protected Solo solo;

    public BaseActivityTest(Class<T> activityClass) {
        super(activityClass);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        setupDexmaker();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    protected void waitForVisibleView(View view, int timeout) {
        long start = System.currentTimeMillis();
        while (!view.isShown()) {
            if ((System.currentTimeMillis() - start) > timeout) {
                fail("View " + view.getId() + " not visible");
            }
            solo.sleep(100);
        }
    }

    /**
     * Workaround for Mockito and JB-MR2 incompatibility to avoid
     * java.lang.IllegalArgumentException: dexcache == null
     *
     * @see <a href="https://code.google.com/p/dexmaker/issues/detail?id=2">
     *     https://code.google.com/p/dexmaker/issues/detail?id=2</a>
     */
    private void setupDexmaker() {
        // Explicitly set the Dexmaker cache, so tests that use mockito work
        final String dexCache = getInstrumentation().getTargetContext().getCacheDir().getPath();
        System.setProperty("dexmaker.dexcache", dexCache);
    }
}
