package models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "users")
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int localID;
    private String username;
    private String description;
    private String uid;
    private String image;

    @Ignore
    public User(String username, String description, String uid) {
        this.username = username;
        this.description = description;
        this.uid = uid;
    }

    public User() {
    }

    protected User(Parcel in) {
        localID = in.readInt();
        username = in.readString();
        description = in.readString();
        uid = in.readString();
        image = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return uid;
    }

    public void setId(String id) {
        this.uid = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", description='" + description + '\'' +
                ", id='" + uid + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(localID);
        dest.writeString(username);
        dest.writeString(description);
        dest.writeString(uid);
        dest.writeString(image);
    }
}
