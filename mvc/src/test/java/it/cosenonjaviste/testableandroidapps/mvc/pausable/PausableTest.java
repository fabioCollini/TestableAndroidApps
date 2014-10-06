package it.cosenonjaviste.testableandroidapps.mvc.pausable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import it.cosenonjaviste.testableandroidapps.mvc.base.PausableSubscription;
import it.cosenonjaviste.testableandroidapps.mvc.base.PausableSubscriptions;
import rx.Observable;
import rx.functions.Action1;
import rx.observers.Observers;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static org.junit.Assert.assertEquals;

/**
 * Created by fabiocollini on 05/10/14.
 */
public class PausableTest {
    @Test public void testPauseResume() {
        TestScheduler testScheduler = Schedulers.test();
        List<Long> list = new ArrayList<>();
        Observable<Long> observable = Observable.interval(500, TimeUnit.MILLISECONDS, testScheduler).limit(5);
        PausableSubscription pausableSubscription = PausableSubscriptions.subscribe(observable, Observers.create((Action1<Long>) list::add));
        sleep(testScheduler, 800);
        pausableSubscription.pause();
        pausableSubscription.resume();
        sleep(testScheduler, 2000);
        assertEquals(Arrays.asList(0L, 1L, 2L, 3L, 4L), list);
    }

    @Test public void testPauseCompletedResume() {
        TestScheduler testScheduler = Schedulers.test();
        List<Long> list = new ArrayList<>();
        Observable<Long> observable = Observable.interval(500, TimeUnit.MILLISECONDS, testScheduler).limit(5);
        PausableSubscription pausableSubscription = PausableSubscriptions.subscribe(observable, Observers.create((Action1<Long>) list::add));
        sleep(testScheduler, 800);
        pausableSubscription.pause();
        sleep(testScheduler, 2000);
        pausableSubscription.resume();
        assertEquals(Arrays.asList(0L, 1L, 2L, 3L, 4L), list);
    }

    @Test public void testPauseSleepResume() {
        TestScheduler testScheduler = Schedulers.test();
        List<Long> list = new ArrayList<>();
        Observable<Long> observable = Observable.interval(500, TimeUnit.MILLISECONDS, testScheduler).limit(5);
        PausableSubscription pausableSubscription = PausableSubscriptions.subscribe(observable, Observers.create((Action1<Long>) list::add));
        sleep(testScheduler, 800);
        pausableSubscription.pause();
        sleep(testScheduler, 900);
        pausableSubscription.resume();
        sleep(testScheduler, 1000);
        assertEquals(Arrays.asList(0L, 1L, 2L, 3L, 4L), list);
    }

    @Test public void testReplay() {
        TestScheduler testScheduler = Schedulers.test();
        List<Long> list1 = new ArrayList<>();
        List<Long> list2 = new ArrayList<>();
        Observable<Long> observable = Observable.interval(500, TimeUnit.MILLISECONDS, testScheduler).limit(5).replay().refCount();
        PausableSubscription pausableSubscription1 = PausableSubscriptions.subscribe(observable, Observers.create((Action1<Long>) list1::add));
        PausableSubscription pausableSubscription2 = PausableSubscriptions.subscribe(observable, Observers.create((Action1<Long>) list2::add));
        sleep(testScheduler, 800);
        pausableSubscription1.pause();
        sleep(testScheduler, 900);
        pausableSubscription1.resume();
        sleep(testScheduler, 1000);
        assertEquals(Arrays.asList(0L, 1L, 2L, 3L, 4L), list1);
        assertEquals(Arrays.asList(0L, 1L, 2L, 3L, 4L), list2);
    }

    @Test public void testReplayDestroy() {
        TestScheduler testScheduler = Schedulers.test();
        List<Long> list1 = new ArrayList<>();
        List<Long> list2 = new ArrayList<>();
        Observable<Long> observable = Observable.interval(500, TimeUnit.MILLISECONDS, testScheduler).limit(5).replay().refCount();
        PausableSubscription pausableSubscription1 = PausableSubscriptions.subscribe(observable, Observers.create((Action1<Long>) list1::add));
        PausableSubscription pausableSubscription2 = PausableSubscriptions.subscribe(observable, Observers.create((Action1<Long>) list2::add));
        sleep(testScheduler, 800);
        pausableSubscription1.destroy();
        sleep(testScheduler, 900);
        pausableSubscription2.pause();
        pausableSubscription2.resume();
        sleep(testScheduler, 1000);
        assertEquals(Arrays.asList(0L), list1);
        assertEquals(Arrays.asList(0L, 1L, 2L, 3L, 4L), list2);
    }

    @Test public void testDestroy() {
        TestScheduler testScheduler = Schedulers.test();
        List<Long> list = new ArrayList<>();
        Observable<Long> observable = Observable.interval(500, TimeUnit.MILLISECONDS, testScheduler).limit(5);
        PausableSubscription pausableSubscription = PausableSubscriptions.subscribe(observable, Observers.create((Action1<Long>) list::add));
        sleep(testScheduler, 800);
        pausableSubscription.destroy();
        sleep(testScheduler, 2000);
        assertEquals(Arrays.asList(0L), list);
    }

    private void sleep(TestScheduler testScheduler, int i) {
        testScheduler.advanceTimeBy(i, TimeUnit.MILLISECONDS);
    }

}
