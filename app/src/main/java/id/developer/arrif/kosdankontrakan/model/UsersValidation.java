package id.developer.arrif.kosdankontrakan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UsersValidation implements Parcelable {
    private String userName;
    private String message;

    public UsersValidation() {
    }

    protected UsersValidation(Parcel in) {
        userName = in.readString();
        message = in.readString();
    }

    public static final Creator<UsersValidation> CREATOR = new Creator<UsersValidation>() {
        @Override
        public UsersValidation createFromParcel(Parcel in) {
            return new UsersValidation(in);
        }

        @Override
        public UsersValidation[] newArray(int size) {
            return new UsersValidation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(message);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Creator<UsersValidation> getCREATOR() {
        return CREATOR;
    }
}
