package it.cosenonjaviste.testableandroidapps;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import it.cosenonjaviste.testableandroidapps.model.Repo;
import it.cosenonjaviste.testableandroidapps.model.RepoResponse;
import it.cosenonjaviste.testableandroidapps.share.ShareHelper;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {
    public static final String REPOS = "repos";

    private ArrayList<Repo> repos = new ArrayList<Repo>();

    public RepoAdapter() {
        setHasStableIds(true);
    }

    @Override public long getItemId(int position) {
        return repos.get(position).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.image) ImageView image;
        @InjectView(R.id.text) TextView text;
        private Repo repo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void popupate(Repo repo) {
            this.repo = repo;
            text.setText(repo.toString());
            Picasso.with(image.getContext()).load(repo.getOwner().getAvatar()).into(image);
        }

        @OnClick(R.id.layout) void onClick(View v) {
            ShareHelper.share(v.getContext(), repo.getName(), repo.getName() + " " + repo.getUrl());
        }
    }

    @Override
    public RepoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder rowWrapper, int position) {
        Repo repo = repos.get(position);
        rowWrapper.popupate(repo);
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public void reloadData(ArrayList<Repo> items) {
        this.repos = items;
        notifyDataSetChanged();
    }

    public void loadFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Parcelable parcelable = savedInstanceState.getParcelable(REPOS);
            RepoResponse response = Parcels.unwrap(parcelable);
            repos = response.getItems();
        }
    }

    public void saveInBundle(Bundle outState) {
        outState.putParcelable(REPOS, Parcels.wrap(new RepoResponse(repos)));
    }

}