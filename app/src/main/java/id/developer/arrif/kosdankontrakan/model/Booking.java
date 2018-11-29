package id.developer.arrif.kosdankontrakan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Booking implements Parcelable {
    private String postingId;
    private String userId;
    private String namaTempat;
    private String namaUser;

    public Booking() {
    }

    protected Booking(Parcel in) {
        postingId = in.readString();
        userId = in.readString();
        namaTempat = in.readString();
        namaUser = in.readString();
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(postingId);
        parcel.writeString(userId);
        parcel.writeString(namaTempat);
        parcel.writeString(namaUser);
    }

    public String getPostingId() {
        return postingId;
    }

    public void setPostingId(String postingId) {
        this.postingId = postingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNamaTempat() {
        return namaTempat;
    }

    public void setNamaTempat(String namaTempat) {
        this.namaTempat = namaTempat;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public static Creator<Booking> getCREATOR() {
        return CREATOR;
    }
}
