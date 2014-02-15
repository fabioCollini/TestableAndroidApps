package it.cosenonjaviste.testableandroidapps;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fabiocollini on 09/02/14.
 */
public class Repo implements Parcelable {
    private long id;
    private String name;
    private String description;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Repo createFromParcel(Parcel in) {
            Repo repo = new Repo();
            repo.id = in.readLong();
            repo.name = in.readString();
            repo.description = in.readString();
            return repo;
        }

        public Repo[] newArray(int size) {
            return new Repo[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }
}
