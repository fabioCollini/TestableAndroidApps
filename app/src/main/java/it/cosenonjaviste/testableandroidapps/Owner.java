package it.cosenonjaviste.testableandroidapps;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fabiocollini on 25/02/14.
 */
public class Owner implements Parcelable {
    private long id;

    @SerializedName("login")
    private String name;

    @SerializedName("avatar_url")
    private String avatar;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(avatar);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Owner createFromParcel(Parcel in) {
            Owner owner = new Owner();
            owner.id = in.readLong();
            owner.name = in.readString();
            owner.avatar = in.readString();
            return owner;
        }

        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };
}
