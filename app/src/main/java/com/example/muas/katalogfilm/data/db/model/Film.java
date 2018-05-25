package com.example.muas.katalogfilm.data.db.model;


public class Film {


    private String gambar_film;
    private String original_title;
    private String overview;
    private String release_date;


    public Film(String gambar_film, String original_title, String overview, String release_date) {
        this.gambar_film = gambar_film;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
    }

    public Film() {
    }

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
}
