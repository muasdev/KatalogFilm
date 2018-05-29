package com.example.muas.katalogfilm.data.db.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Film implements Parcelable{


    private String gambar_film;
    private String original_title;
    private String overview;
    private String release_date;

    public Film() {
    }

    public Film(String gambar_film, String original_title, String overview, String release_date) {
        this.gambar_film = gambar_film;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
    }

    protected Film(Parcel in) {
        gambar_film = in.readString();
        original_title = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    public String getGambar_film() {
        return gambar_film;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setGambar_film(String gambar_film) {
        this.gambar_film = gambar_film;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gambar_film);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
    }
}
