package it.cosenonjaviste.testableandroidapps.mvc;

import it.cosenonjaviste.testableandroidapps.mvc.base.ContextBinder;
import it.cosenonjaviste.testableandroidapps.mvc.base.ObservableQueue;
import it.cosenonjaviste.testableandroidapps.mvc.base.RxMvcController;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fabiocollini on 05/10/14.
 */
public class MultiListController extends RxMvcController<MultiListModel> {
    private String name;

    protected MultiListController(ContextBinder contextBinder, String name) {
        super(contextBinder);
        this.name = name;
    }

    @Override public String toString() {
        return "KillController{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override protected MultiListModel createModel() {
        return new MultiListModel();
    }

    @Override protected Subscription initSubscriptions() {
        CompositeSubscription subscription = new CompositeSubscription();
        subscription.add(queue1.subscribe(model.getKey(), (item, observable) -> {
//            System.out.println("Subscribe observable 1 " + observable + " controller " + KillController.this);
            return observable.subscribe((t1) -> {
                System.out.println("Add " + t1 + " in list 1");
                model.list1.add(t1);
            });
        }));
        subscription.add(queue2.subscribe(model.getKey(), (item, observable) -> {
//            System.out.println("Subscribe observable 2 " + observable + " controller " + KillController.this);
            return observable.subscribe((t1) -> {
                System.out.println("Add " + t1 + " in list 2");
                model.list2.add(t1);
            });
        }));
        return subscription;
    }

    public static ObservableQueue<Integer> queue1 = new ObservableQueue<>();
    public static ObservableQueue<Integer> queue2 = new ObservableQueue<>();

    public void call1(PublishSubject<Integer> subject) {
        queue1.onNext(null, contextBinder.bindObservable(subject));
    }

    public void call2(PublishSubject<Integer> subject) {
        queue2.onNext(null, contextBinder.bindObservable(subject));
    }

    @Override public void destroy() {
        super.destroy();
        queue1.pause(model.getKey());
        queue2.pause(model.getKey());
    }
}
