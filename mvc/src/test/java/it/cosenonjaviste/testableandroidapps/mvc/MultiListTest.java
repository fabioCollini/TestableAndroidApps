package it.cosenonjaviste.testableandroidapps.mvc;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import rx.subjects.PublishSubject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by fabiocollini on 05/10/14.
 */
public class MultiListTest {

    private MultiListController controller;
    private MultiListModel model;
    private MultiListModel model2;
    private CloneObjectSaver objectSaver;
    private TestContextBinder contextBinder;

    @Before
    public void setup() {
        contextBinder = new TestContextBinder();

        objectSaver = new CloneObjectSaver();
    }

    @Test public void testActivityKilled() {
        go("testActivityKilled",
                new int[]{1}, new int[]{2, 3},
                new int[]{}, new int[]{20, 30},
                new int[]{100}, new int[]{200, 300},
                Arrays.asList(1, 100), Arrays.asList(2, 3, 20, 30, 200, 300)
        );
    }

    @Test public void testBefore() {
        go("testBefore",
                new int[]{4}, new int[]{5, 6},
                new int[]{}, new int[]{},
                new int[]{}, new int[]{},
                Arrays.asList(4), Arrays.asList(5, 6)
        );
    }

    @Test public void testAfter() {
        go("testAfter",
                new int[]{}, new int[]{},
                new int[]{}, new int[]{},
                new int[]{1}, new int[]{2, 3},
                Arrays.asList(1), Arrays.asList(2, 3)
        );
    }

    @Test public void testSaved() {
        go("testSaved",
                new int[]{}, new int[]{},
                new int[]{21}, new int[]{20, 30},
                new int[]{}, new int[]{},
                Arrays.asList(21), Arrays.asList(20, 30)
        );
    }

    @Test public void testNoDuplicates() {
        go("testNoDuplicates",
                new int[]{1}, new int[]{},
                new int[]{}, new int[]{},
                new int[]{}, new int[]{},
                Arrays.asList(1), Arrays.asList()
        );
    }

    private void go(String name, int[] before1, int[] before2, int[] saved1, int[] saved2, int[] after1, int[] after2, List<Integer> expect1, List<Integer> expect2) {
        controller = new MultiListController(contextBinder, "Controller 1 " + name) {
            @Override protected MultiListModel createModel() {
                MultiListTest.this.model = super.createModel();
                return MultiListTest.this.model;
            }
        };

        controller.loadFromBundle(objectSaver);
        controller.subscribe(null);

        PublishSubject<Integer> subject1 = PublishSubject.<Integer>create();
        PublishSubject<Integer> subject2 = PublishSubject.<Integer>create();

        controller.call1(subject1);
        controller.call2(subject2);

        execute(subject1, before1);
        execute(subject2, before2);

        controller.unsubscribeView();
        controller.destroy();
        controller.saveInBundle(objectSaver);

        execute(subject1, saved1);
        execute(subject2, saved2);

        MultiListController controller1 = new MultiListController(contextBinder, "Controller 2 " + name);
        controller1.loadFromBundle(objectSaver);
        controller1.subscribe(null);


        model2 = objectSaver.loadFromBundle();

        execute(subject1, after1);
        execute(subject2, after2);

        controller1.unsubscribeView();
        controller1.destroy();
        subject1.onCompleted();
        subject2.onCompleted();

        assertEquals(expect1, model2.list1);
        assertEquals(expect2, model2.list2);

        MultiListController.queue1.destroyCache("key");
        MultiListController.queue2.destroyCache("key");

        assertTrue(MultiListController.queue1.isCacheEmpty());
        assertEquals(0, MultiListController.queue1.getRunningObservableCount());
        assertTrue(MultiListController.queue2.isCacheEmpty());
        assertEquals(0, MultiListController.queue2.getRunningObservableCount());
    }

    private void execute(PublishSubject<Integer> subject, int[] values) {
        for (int aBefore1 : values) {
            subject.onNext(aBefore1);
        }
    }
}
