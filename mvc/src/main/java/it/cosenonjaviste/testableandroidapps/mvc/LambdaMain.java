package it.cosenonjaviste.testableandroidapps.mvc;

import rx.Observable;

/**
 * Created by fabiocollini on 15/09/14.
 */
public class LambdaMain {
    public static void main(String[] args) {
        Observable.just(1, 2, 3, 4, 5).filter(i -> i % 2 == 0).map(i -> i * 2).reduce((i1, i2) -> i1 + i2).forEach(System.out::println);
    }
}
