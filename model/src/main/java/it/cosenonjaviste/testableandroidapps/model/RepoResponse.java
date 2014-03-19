package it.cosenonjaviste.testableandroidapps.model;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class RepoResponse {
    ArrayList<Repo> items;

    public RepoResponse() {
    }

    public RepoResponse(ArrayList<Repo> items) {
        this.items = items;
    }

    public ArrayList<Repo> getItems() {
        return items;
    }
}
