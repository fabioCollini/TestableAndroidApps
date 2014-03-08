package it.cosenonjaviste.testableandroidapps;

import android.widget.ListView;

import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;

public class MainActivityTest extends BaseActivityTest<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testSearch() {
        solo.typeText(solo.getEditText(0), "abc");

        solo.clickOnImageButton(0);

        ListView list = (ListView) solo.getView(R.id.list);

        waitForVisibleView(list, 5000);

        assertEquals(30, list.getAdapter().getCount());

        solo.clickInList(3);
    }
}
