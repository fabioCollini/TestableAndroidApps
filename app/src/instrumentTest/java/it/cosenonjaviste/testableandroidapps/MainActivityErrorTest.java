package it.cosenonjaviste.testableandroidapps;

import android.app.Application;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.view.View;
import android.widget.ListView;

import dagger.ObjectGraph;
import it.cosenonjaviste.testableandroidapps.base.BaseActivityTest;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphCreator;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;

public class MainActivityErrorTest extends BaseActivityTest {

    public MainActivityErrorTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        Instrumentation instrumentation = getInstrumentation();
        final Resources resources = instrumentation.getContext().getResources();
        ObjectGraphHolder.forceObjectGraphCreator(new ObjectGraphCreator() {
            @Override public ObjectGraph create(Application app) {
                return ObjectGraph.create(new AppModule(app), new WelcomeDialogManagerTestModule(), new GitHubServiceErrorTestModule(resources));
            }
        });
        super.setUp();
    }

    public void testSearch() {
        solo.typeText(solo.getEditText(0), "abc");

        solo.clickOnImageButton(0);

        View reloadButton = solo.getView(R.id.reload);

        waitForVisibleView(reloadButton, 5000);

        solo.clickOnView(reloadButton);

        ListView list = (ListView) solo.getView(R.id.list);

        waitForVisibleView(list, 5000);

        assertEquals(4, list.getAdapter().getCount());

        solo.clickInList(3);
    }
}