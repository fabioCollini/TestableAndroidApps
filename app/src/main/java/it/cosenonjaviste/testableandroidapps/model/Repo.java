package it.cosenonjaviste.testableandroidapps.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fabiocollini on 09/02/14.
 */
public class Repo implements Parcelable {
    private long id;
    private String name;
    private String description;
    private Owner owner;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(owner, 0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Repo createFromParcel(Parcel in) {
            Repo repo = new Repo();
            repo.id = in.readLong();
            repo.name = in.readString();
            repo.description = in.readString();
            repo.owner = in.readParcelable(Repo.class.getClassLoader());
            return repo;
        }

        public Repo[] newArray(int size) {
            return new Repo[size];
        }
    };

    @Override
    public String toString() {
        return owner.getName() + " - " + name;
    }
}
