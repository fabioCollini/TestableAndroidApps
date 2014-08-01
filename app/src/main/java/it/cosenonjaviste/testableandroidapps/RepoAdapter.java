package it.cosenonjaviste.testableandroidapps;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.cosenonjaviste.testableandroidapps.model.Repo;

public class RepoAdapter extends BaseAdapter {

    public static final String REPOS = "repos";

    private List<Repo> repos = new ArrayList<Repo>();

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
        Picasso.with(context).load(repo.getOwner().getAvatar()).into(rowWrapper.image);
        return convertView;
    }

    public void reloadData(List<Repo> repos) {
        this.repos = repos;
        notifyDataSetChanged();
    }

    public void loadFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Parcelable parcelable = savedInstanceState.getParcelable(REPOS);
            repos = Parcels.unwrap(parcelable);
        }
    }

    public void saveInBundle(Bundle outState) {
        outState.putParcelable(REPOS, Parcels.wrap(repos));
    }

    static class RowWrapper {
        @InjectView(R.id.image) ImageView image;
        @InjectView(R.id.text) TextView text;
    }
}
