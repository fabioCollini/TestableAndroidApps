package it.cosenonjaviste.testableandroidapps;

import android.widget.ListView;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphCreator;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;

public class MainActivityTest extends BaseActivityTest {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        ObjectGraphHolder.forceObjectGraphCreator(new ObjectGraphCreator() {
            @Override public ObjectGraph create() {
                return ObjectGraph.create(new AppModule(), new WelcomeDialogManagerTestModule());
            }
        });
    }

    public void testSearch() {
        solo.typeText(solo.getEditText(0), "abc");

        solo.clickOnImageButton(0);

        ListView list = (ListView) solo.getView(R.id.list);

        waitForVisibleView(list, 5000);

        assertEquals(30, list.getAdapter().getCount());
    }
}
