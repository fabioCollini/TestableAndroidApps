package it.cosenonjaviste.testableandroidapps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.base.EndlessObserver;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;
import it.cosenonjaviste.testableandroidapps.base.RxRetainedFragment;
import it.cosenonjaviste.testableandroidapps.base.RxUtils;
import it.cosenonjaviste.testableandroidapps.model.Owner;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.share.ShareHelper;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

@ParcelClasses({@ParcelClass(RepoResponse.class), @ParcelClass(Repo.class), @ParcelClass(Owner.class)})
public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.list) ListView listView;

    @InjectView(R.id.query) EditText query;

    @InjectView(R.id.progress) View progress;

    @InjectView(R.id.reload) View reload;

    @Inject WelcomeDialogManager welcomeDialogManager;

    @Inject RepoService repoService;

    @Inject ShareHelper shareHelper;

    private RepoAdapter repoAdapter;

    private RxRetainedFragment<List<Repo>> fragment;

    private Observer<List<Repo>> observer = new Observer<List<Repo>>() {
        @Override public void onCompleted() {
        }

        @Override public void onError(Throwable e) {
            reload.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }

        @Override public void onNext(List<Repo> repos) {
            repoAdapter.reloadData(repos);
            listView.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    };
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph appObjectGraph = ObjectGraphHolder.getObjectGraph(getApplication());
        ObjectGraph activityObjectGraph = appObjectGraph.plus(new ActivityModule(this));
        activityObjectGraph.inject(this);

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        repoAdapter = new RepoAdapter(this);
        repoAdapter.loadFromBundle(savedInstanceState);

        listView.setAdapter(repoAdapter);

        welcomeDialogManager.showDialogIfNeeded();

        fragment = RxRetainedFragment.getFragment(this, "rxRetained");
    }

    @OnItemClick(R.id.list) void shareItem(int position) {
        Repo repo = repoAdapter.getItem(position);
//        repoService.toggleStar(repo);
        shareHelper.share(repo.getName(), repo.getName() + " " + repo.getUrl());
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        repoAdapter.saveInBundle(outState);
    }

    @Override protected void onStart() {
        super.onStart();
        Subscription s = fragment.reconnectObservable(observer);
        if (!s.isUnsubscribed()) {
            showProgress();
            subscriptions.add(s);
        }

        subscriptions.add(repoService.subscribe(new EndlessObserver<Repo>() {
            @Override public void onNext(Repo repo) {
                repoAdapter.notifyDataSetChanged();
            }

            @Override public void onNextError(Throwable t) {
                repoAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override protected void onStop() {
        subscriptions.unsubscribe();
        super.onStop();
    }

    @OnEditorAction(R.id.query) boolean onSearch(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            executeSearch();
            return true;
        }
        return false;
    }

    @OnClick({R.id.search, R.id.reload}) void executeSearch() {
        showProgress();

        String queryString = query.getText().toString();

        Observable<List<Repo>> observable = RxUtils.background(this, repoService.listRepos(queryString));
        subscriptions.add(fragment.connectObservable(observable, observer));
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }

    @Module(injects = MainActivity.class, addsTo = AppModule.class)
    public static class ActivityModule {
        private FragmentActivity activity;

        public ActivityModule(FragmentActivity activity) {
            this.activity = activity;
        }

        @Provides @Singleton public FragmentActivity provideActivity() {
            return activity;
        }
    }
}
