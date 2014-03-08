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
}
