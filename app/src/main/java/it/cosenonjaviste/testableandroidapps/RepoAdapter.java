package it.cosenonjaviste.testableandroidapps;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.cosenonjaviste.testableandroidapps.model.Repo;

public class RepoAdapter extends BaseAdapter {

    public static final String REPOS = "repos";

    private List<Repo> repos = new ArrayList<Repo>();

    private Set<Long> updatingRepos = new HashSet<Long>();

    private Context context;

    public RepoAdapter(Context context) {
        this.context = context;
    }

    @Override public int getCount() {
        return repos.size();
    }

    @Override public Repo getItem(int position) {
        return repos.get(position);
    }

    @Override public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.repo_row, parent, false);
            RowWrapper rowWrapper = new RowWrapper();
            ButterKnife.inject(rowWrapper, convertView);
            convertView.setTag(rowWrapper);
        }
        RowWrapper rowWrapper = (RowWrapper) convertView.getTag();
        Repo repo = getItem(position);
        rowWrapper.text.setText(repo.toString());
        rowWrapper.text.setTypeface(null, updatingRepos.contains(repo.getId()) ? Typeface.ITALIC : Typeface.NORMAL);
        Picasso.with(context).load(repo.getOwner().getAvatar()).into(rowWrapper.image);
        return convertView;
    }

    public void reloadData(List<Repo> repos, Set<Long> updatingRepos) {
        this.repos = repos;
        this.updatingRepos = updatingRepos;
        notifyDataSetChanged();
    }

    static class RowWrapper {
        @InjectView(R.id.image) ImageView image;
        @InjectView(R.id.text) TextView text;
    }
}
