package it.cosenonjaviste.testableandroidapps.mvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cosenonjaviste.testableandroidapps.model.Repo;

public class RepoListModel {

    List<Repo> repos = new ArrayList<>();

    Set<Long> updatingRepos = new HashSet<>();

    boolean reloadVisible;

    public List<Repo> getRepos() {
        return repos;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    public Set<Long> getUpdatingRepos() {
        return updatingRepos;
    }

    public boolean isReloadVisible() {
        return reloadVisible;
    }

    public void setReloadVisible(boolean reloadVisible) {
        this.reloadVisible = reloadVisible;
    }
}
