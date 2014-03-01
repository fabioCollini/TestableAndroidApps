package it.cosenonjaviste.testableandroidapps;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testSearch() {
        solo.typeText(solo.getEditText(0), "abc");

        solo.clickOnImageButton(0);

        ListView list = (ListView) solo.getView(R.id.list);

        waitForVisibleView(list, 5000);

        assertEquals(30, list.getAdapter().getCount());
    }

    private void waitForVisibleView(View view, int timeout) {
        long start = System.currentTimeMillis();
        while (!view.isShown()) {
            if ((System.currentTimeMillis() - start) > timeout) {
                fail("View " + view.getId() + " not visible");
            }
            solo.sleep(100);
        }
    }
}
