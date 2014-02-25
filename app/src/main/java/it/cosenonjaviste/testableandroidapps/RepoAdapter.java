package it.cosenonjaviste.testableandroidapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icicle;

/**
 * Created by fabiocollini on 15/02/14.
 */
public class RepoAdapter extends BaseAdapter {

    @Icicle ArrayList<Repo> repos = new ArrayList<Repo>();

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

    public void reloadData(ArrayList<Repo> repos) {
        this.repos = repos;
        notifyDataSetChanged();
    }

    static class RowWrapper {
        @InjectView(R.id.image) ImageView image;
        @InjectView(R.id.text) TextView text;
    }
}
