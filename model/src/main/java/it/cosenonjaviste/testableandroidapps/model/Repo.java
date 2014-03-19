package it.cosenonjaviste.testableandroidapps.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Repo {
    long id;

    String name;

    String description;

    @SerializedName("html_url") String url;

    Owner owner;

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
        return owner.getName() + " - " + name;
    }
}
