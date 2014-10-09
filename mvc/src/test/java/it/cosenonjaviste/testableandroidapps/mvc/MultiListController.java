package it.cosenonjaviste.testableandroidapps.mvc;

import it.cosenonjaviste.testableandroidapps.mvc.base.PresenterArgs;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvpPresenter;
import rx.subjects.PublishSubject;

public class MultiListController extends RxMvpPresenter<MultiListModel> {

    @Override protected MultiListModel createModel(PresenterArgs args) {
        return new MultiListModel();
    }

    public void call1(PublishSubject<Integer> subject) {
        subscribePausable(subject, (t1) -> {
            System.out.println("Add " + t1 + " in list 1");
            model.list1.add(t1);
        }, Throwable::printStackTrace);
    }

    public void call2(PublishSubject<Integer> subject) {
        subscribePausable(subject, (t1) -> {
            System.out.println("Add " + t1 + " in list 2");
            model.list2.add(t1);
        }, Throwable::printStackTrace);
    }
}
