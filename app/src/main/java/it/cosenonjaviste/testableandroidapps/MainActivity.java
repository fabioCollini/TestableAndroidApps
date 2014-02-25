package it.cosenonjaviste.testableandroidapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import icepick.Icepick;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.list) ListView listView;

    @InjectView(R.id.query) EditText query;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Repo> repos = intent.getParcelableArrayListExtra(SearchService.REPOS);
            if (repos != null) {
                repoAdapter.reloadData(repos);
            }
        }
    };

    private RepoAdapter repoAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        repoAdapter = new RepoAdapter(this);
        Icepick.restoreInstanceState(repoAdapter, savedInstanceState);

        listView.setAdapter(repoAdapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(SearchService.EVENT_NAME));
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(repoAdapter, outState);
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

    @OnClick(R.id.search) void executeSearch() {
        Intent intent = new Intent(MainActivity.this, SearchService.class);
        intent.putExtra(SearchService.QUERY, query.getText().toString());
        startService(intent);
    }
}
