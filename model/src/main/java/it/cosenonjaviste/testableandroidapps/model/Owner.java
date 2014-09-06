package it.cosenonjaviste.testableandroidapps.model;

import com.google.gson.annotations.SerializedName;

public class Owner {
    long id;

    @SerializedName("login") String name;

    @SerializedName("avatar_url") String avatar;

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
