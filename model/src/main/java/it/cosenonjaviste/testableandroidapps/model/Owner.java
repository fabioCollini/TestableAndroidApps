package it.cosenonjaviste.testableandroidapps.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Owner {
    long id;

    @SerializedName("login") String name;

    @SerializedName("avatar_url") String avatar;

    public Owner() {
    }

    public Owner(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
