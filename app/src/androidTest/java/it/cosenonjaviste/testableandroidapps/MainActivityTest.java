package it.cosenonjaviste.testableandroidapps;

import android.widget.ListView;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;

public class MainActivityTest extends BaseActivityTest {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        ObjectGraphHolder.forceObjectGraphCreator(app -> ObjectGraph.create(new AppModule(app), new WelcomeDialogManagerTestModule()));
        super.setUp();
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
