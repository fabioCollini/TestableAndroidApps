package it.cosenonjaviste.testableandroidapps.mvc;

import java.util.ArrayList;
import java.util.List;

import it.cosenonjaviste.testableandroidapps.model.Repo;

public class RepoListModel {

    List<Repo> repos = new ArrayList<>();

    boolean reloadVisible;

    public List<Repo> getRepos() {
        return repos;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    public boolean isReloadVisible() {
        return reloadVisible;
    }

    public void setReloadVisible(boolean reloadVisible) {
        this.reloadVisible = reloadVisible;
    }
}
