package it.cosenonjaviste.testableandroidapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private ListView listView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Repo> repos = intent.getParcelableArrayListExtra(SearchService.REPOS);
            if (repos != null) {
                listView.setAdapter(new ArrayAdapter<Repo>(MainActivity.this, android.R.layout.simple_list_item_1, repos));
            }
        }
    };
    private EditText query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        query = (EditText) findViewById(R.id.query);

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchService.class);
                intent.putExtra(SearchService.QUERY, query.getText().toString());
                startService(intent);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(SearchService.EVENT_NAME));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }
}
