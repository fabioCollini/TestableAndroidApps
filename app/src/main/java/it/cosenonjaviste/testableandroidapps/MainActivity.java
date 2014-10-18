package it.cosenonjaviste.testableandroidapps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;
import it.cosenonjaviste.testableandroidapps.base.RxMvpActivity;
import it.cosenonjaviste.testableandroidapps.model.Owner;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.model.RepoService;
import it.cosenonjaviste.testableandroidapps.mvc.EventType;
import it.cosenonjaviste.testableandroidapps.mvc.ModelEvent;
import it.cosenonjaviste.testableandroidapps.mvc.RepoListModel;
import it.cosenonjaviste.testableandroidapps.mvc.RepoListPresenter;
import it.cosenonjaviste.testableandroidapps.mvc.base.Navigator;
import it.cosenonjaviste.testableandroidapps.share.ShareHelper;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

@ParcelClasses({@ParcelClass(RepoResponse.class), @ParcelClass(Repo.class), @ParcelClass(Owner.class), @ParcelClass(RepoListModel.class)})
public class MainActivity extends RxMvpActivity<RepoListPresenter, RepoListModel> {

    @InjectView(R.id.list) ListView listView;

    @InjectView(R.id.query) EditText query;

    @InjectView(R.id.progress) View progress;

    @InjectView(R.id.reload) View reload;

    @Inject WelcomeDialogManager welcomeDialogManager;

    @Inject Provider<RepoListPresenter> presenterProvider;

    @Inject ShareHelper shareHelper;

    private RepoAdapter repoAdapter;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override protected void onCreate(Bundle savedInstanceState) {
        ObjectGraph appObjectGraph = ObjectGraphHolder.getObjectGraph(getApplication());
        ObjectGraph activityObjectGraph = appObjectGraph.plus(new ActivityModule(this));
        activityObjectGraph.inject(this);

        super.onCreate(savedInstanceState);

        repoAdapter = new RepoAdapter(this);

        listView.setAdapter(repoAdapter);

        welcomeDialogManager.showDialogIfNeeded();
    }

    @Override protected void subscribeToModelUpdates(Observable<ModelEvent<RepoListModel>> updates) {
        subscriptions.add(
                updates
                        .filter(e -> e.isExtraEmpty() && e.getType() == EventType.START_LOADING)
                        .subscribe(e -> showProgress())
        );
        subscriptions.add(
                updates
                        .filter(e -> e.isExtraEmpty() && e.getType() != EventType.START_LOADING)
                        .map(ModelEvent::getModel)
                        .subscribe(this::updateView)
        );

        Observable<ModelEvent<RepoListModel>> reposEvents = updates.filter(e -> e.getExtra() instanceof Repo);

        subscriptions.add(
                reposEvents
                        .filter(e -> e.getType() == EventType.START_LOADING)
                        .map(ModelEvent::getExtra)
                        .ofType(Repo.class)
                        .map(Repo::getId)
                        .subscribe(repoAdapter::startUpdatingRepo)
        );
        subscriptions.add(
                reposEvents
                        .filter(e -> e.getType() == EventType.END_LOADING || e.getType() == EventType.ERROR)
                        .map(ModelEvent::getExtra)
                        .ofType(Repo.class)
                        .map(Repo::getId)
                        .subscribe(repoAdapter::endUpdatingRepo)
        );
        subscriptions.add(
                reposEvents
                        .filter(e -> e.getType() == EventType.END_LOADING)
                        .map(ModelEvent::getModel)
                        .subscribe(this::updateView)
        );
        subscriptions.add(
                updates
                        .filter(e -> e.getType() == EventType.ERROR)
                        .map(ModelEvent::getThrowable)
                        .subscribe(t -> Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show())
        );
    }

    @Override public void onStop() {
        super.onStop();
        subscriptions.unsubscribe();
        subscriptions = new CompositeSubscription();
    }

    @Override protected Navigator getNavigator() {
        return null;
    }

    @OnItemClick(R.id.list) void shareItem(int position) {
        Repo repo = repoAdapter.getItem(position);
        presenter.toggleStar(repo);
//        shareHelper.share(repo.getName(), repo.getName() + " " + repo.getUrl());
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override protected RepoListPresenter createPresenter() {
        return presenterProvider.get();
    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }

    public void updateView(RepoListModel model) {
        if (model.isReloadVisible()) {
            reload.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            reload.setVisibility(View.GONE);
        }
        repoAdapter.reloadData(model.getRepos());
    }

    @OnEditorAction(R.id.query) boolean onSearch(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            executeSearch();
            return true;
        }
        return false;
    }

    @OnClick({R.id.search, R.id.reload}) void executeSearch() {
        String queryString = query.getText().toString();
        presenter.listRepos(queryString);
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

        @Provides @Singleton RepoListPresenter providePresenter(RepoService repoService) {
            return new RepoListPresenter(repoService);
        }
    }
}
