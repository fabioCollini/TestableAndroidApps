package it.cosenonjaviste.testableandroidapps.base;

import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fabiocollini on 08/09/14.
 */
public class TestCache {
    public static void main(String[] args) throws InterruptedException {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).doOnEach(new Action1<Notification<? super Long>>() {
            @Override public void call(Notification<? super Long> notification) {
                System.out.println("on each " + notification);
            }
        }).take(10).subscribeOn(Schedulers.io()).cache(1);
        System.out.println("Create");
        Thread.sleep(1000);

        Subscription subscription = observable.subscribe(new Action1<Long>() {
            @Override public void call(Long aLong) {
                System.out.println("Next " + aLong);
            }
        });

        Thread.sleep(500);
        System.out.println("Unsubscribe...");
        subscription.unsubscribe();

        Thread.sleep(2500);

//        System.out.println("Subscribe...");
//
//        observable.subscribe(new Action1<Long>() {
//            @Override public void call(Long aLong) {
//                System.out.println("Next " + aLong);
//            }
//        });
        Thread.sleep(10000);
    }
}
