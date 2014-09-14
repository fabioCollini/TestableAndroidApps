package it.cosenonjaviste.testableandroidapps.mvc;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cosenonjaviste.testableandroidapps.model.Repo;

/**
 * Created by fabiocollini on 12/09/14.
 */
@Parcel
public class RepoListModel {

    List<Repo> repos = new ArrayList<Repo>();

    Set<Long> updatingRepos = new HashSet<Long>();

    boolean progressVisible;

    boolean reloadVisible;

    private String exceptionMessage;

    public List<Repo> getRepos() {
        return repos;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    public Set<Long> getUpdatingRepos() {
        return updatingRepos;
    }

    public boolean isProgressVisible() {
        return progressVisible;
    }

    public void setProgressVisible(boolean progressVisible) {
        this.progressVisible = progressVisible;
    }

    public boolean isReloadVisible() {
        return reloadVisible;
    }

    public void setReloadVisible(boolean reloadVisible) {
        this.reloadVisible = reloadVisible;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override public String toString() {
        return "RepoListModel{" +
                "repos=" + repos +
                ", updatingRepos=" + updatingRepos +
                ", progressVisible=" + progressVisible +
                ", reloadVisible=" + reloadVisible +
                '}';
    }
}
