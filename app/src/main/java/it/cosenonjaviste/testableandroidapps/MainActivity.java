package it.cosenonjaviste.testableandroidapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import it.cosenonjaviste.testableandroidapps.base.ObjectGraphHolder;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.service.SearchService;
import it.cosenonjaviste.testableandroidapps.share.ShareHelper;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.list) ListView listView;

    @InjectView(R.id.query) EditText query;

    @InjectView(R.id.progress) View progress;

    @InjectView(R.id.reload) View reload;

    @Inject WelcomeDialogManager welcomeDialogManager;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RepoResponse repos = Parcels.unwrap(intent.getParcelableExtra(SearchService.REPOS));
            if (repos != null) {
                repoAdapter.reloadData(repos.getItems());
                listView.setVisibility(View.VISIBLE);
            } else {
                reload.setVisibility(View.VISIBLE);
            }
            progress.setVisibility(View.GONE);
        }
    };

    private RepoAdapter repoAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraphHolder.inject(getApplication(), this);

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        repoAdapter = new RepoAdapter(this);
        repoAdapter.loadFromBundle(savedInstanceState);

        listView.setAdapter(repoAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(SearchService.EVENT_NAME));

        welcomeDialogManager.showDialogIfNeeded(this);
    }

    @OnItemClick(R.id.list) void shareItem(int position) {
        Repo repo = repoAdapter.getItem(position);
        ShareHelper.share(this, repo.getName(), repo.getName() + " " + repo.getUrl());
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        repoAdapter.saveInBundle(outState);
    }

    @Override protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }

    @OnEditorAction(R.id.query) boolean onSearch(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            executeSearch();
            return true;
        }
        return false;
    }

    @OnClick({R.id.search, R.id.reload}) void executeSearch() {
        progress.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        Intent intent = new Intent(MainActivity.this, SearchService.class);
        intent.putExtra(SearchService.QUERY, query.getText().toString());
        startService(intent);
    }
}
