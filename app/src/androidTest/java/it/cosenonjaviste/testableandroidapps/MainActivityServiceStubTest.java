package it.cosenonjaviste.testableandroidapps;

import android.app.Application;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.widget.ListView;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphCreator;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;

public class MainActivityServiceStubTest extends BaseActivityTest {

    public MainActivityServiceStubTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        Instrumentation instrumentation = getInstrumentation();
        final Resources resources = instrumentation.getContext().getResources();
        ObjectGraphHolder.forceObjectGraphCreator(new ObjectGraphCreator() {
            @Override public ObjectGraph create(Application app) {
                return ObjectGraph.create(new AppModule(app), new WelcomeDialogManagerTestModule(), new GitHubServiceTestModule(resources));
            }
        });
        super.setUp();
    }

    public void testSearch() {
        solo.typeText(solo.getEditText(0), "abc");

        solo.clickOnImageButton(0);

        ListView list = (ListView) solo.getView(R.id.list);

        waitForVisibleView(list, 5000);

        assertEquals(4, list.getAdapter().getCount());

        solo.clickInList(3);
    }
}