package it.cosenonjaviste.testableandroidapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.service.SearchService;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.list) RecyclerView recyclerView;

    @InjectView(R.id.query) EditText query;

    @InjectView(R.id.progress) View progress;

    @InjectView(R.id.reload) View reload;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RepoResponse repos = Parcels.unwrap(intent.getParcelableExtra(SearchService.REPOS));
            if (repos != null) {
                repoAdapter.reloadData(repos.getItems());
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                reload.setVisibility(View.VISIBLE);
            }
            progress.setVisibility(View.GONE);
        }
    };

    private RepoAdapter repoAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        repoAdapter = new RepoAdapter();
        repoAdapter.loadFromBundle(savedInstanceState);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(repoAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(SearchService.EVENT_NAME));

        WelcomeDialog.showDialogIfNeeded(this);
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
        recyclerView.setVisibility(View.GONE);
        Intent intent = new Intent(MainActivity.this, SearchService.class);
        intent.putExtra(SearchService.QUERY, query.getText().toString());
        startService(intent);
    }
}
