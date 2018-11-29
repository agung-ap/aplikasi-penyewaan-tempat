package id.developer.arrif.kosdankontrakan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Posting implements Parcelable {
    private String key;
    private String imageUrl;
    private String namaTempat;
    private String alamatTempat;
    private String hargaTempat;
    private String deskripsiTempat;

    public Posting() {
    }

    protected Posting(Parcel in) {
        key = in.readString();
        imageUrl = in.readString();
        namaTempat = in.readString();
        alamatTempat = in.readString();
        hargaTempat = in.readString();
        deskripsiTempat = in.readString();
    }

    public static final Creator<Posting> CREATOR = new Creator<Posting>() {
        @Override
        public Posting createFromParcel(Parcel in) {
            return new Posting(in);
        }

        @Override
        public Posting[] newArray(int size) {
            return new Posting[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNamaTempat() {
        return namaTempat;
    }

    public void setNamaTempat(String namaTempat) {
        this.namaTempat = namaTempat;
    }

    public String getAlamatTempat() {
        return alamatTempat;
    }

    public void setAlamatTempat(String alamatTempat) {
        this.alamatTempat = alamatTempat;
    }

    public String getHargaTempat() {
        return hargaTempat;
    }

    public void setHargaTempat(String hargaTempat) {
        this.hargaTempat = hargaTempat;
    }

    public String getDeskripsiTempat() {
        return deskripsiTempat;
    }

    public void setDeskripsiTempat(String deskripsiTempat) {
        this.deskripsiTempat = deskripsiTempat;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(imageUrl);
        parcel.writeString(namaTempat);
        parcel.writeString(alamatTempat);
        parcel.writeString(hargaTempat);
        parcel.writeString(deskripsiTempat);
    }
}
