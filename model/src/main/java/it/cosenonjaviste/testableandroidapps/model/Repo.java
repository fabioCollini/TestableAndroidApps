package it.cosenonjaviste.testableandroidapps.model;

import com.google.gson.annotations.SerializedName;

public class Repo {
    long id;

    String name;

    String description;

    @SerializedName("html_url") String url;

    Owner owner;

    boolean starred;

    boolean updating;

    public Repo() {
    }

    public Repo(long id, String name, Owner owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return owner.getName() + " - " + name + (starred ? " *" : "");
    }

    public void toggleStar() {
        starred = !starred;
    }

    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }
}
