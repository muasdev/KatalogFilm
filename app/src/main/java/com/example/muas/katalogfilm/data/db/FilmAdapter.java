package com.example.muas.katalogfilm.data.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.muas.katalogfilm.R;
import com.example.muas.katalogfilm.data.db.model.Film;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmMovieHolder> {

    private Context context;
    private ArrayList<Film> films;

    public FilmAdapter(Context context, ArrayList<Film> films) {
        this.context = context;
        this.films = films;
    }

    @NonNull
    @Override
    public FilmAdapter.FilmMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_movie, parent, false);
        FilmMovieHolder filmMovieHolder = new FilmMovieHolder(view);
        return filmMovieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FilmAdapter.FilmMovieHolder holder, int position) {

        String gambarFilm = films.get(position)
                .getGambar_film();
        Glide.with(context)
                .load("http://image.tmdb.org/t/p/w50/"+gambarFilm)
                .into(holder.imageFilm);

        holder.judulFilm
                .setText(films
                        .get(position)
                        .getOriginal_title());

        holder.overview
                .setText(films
                        .get(position)
                        .getOverview());

        holder.release_date
                .setText(films
                        .get(position)
                        .getRelease_date());

    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public class FilmMovieHolder extends RecyclerView.ViewHolder {

        ImageView imageFilm;
        TextView judulFilm;
        TextView overview;
        TextView release_date;

        public FilmMovieHolder(View itemView) {
            super(itemView);
            imageFilm = (ImageView) itemView.findViewById(R.id.iv_gambarFilm);
            judulFilm = (TextView) itemView.findViewById(R.id.tv_judulFilm);
            overview = (TextView) itemView.findViewById(R.id.tv_deskFilm);
            release_date = (TextView) itemView.findViewById(R.id.tv_rilisFilm);
        }
    }
}
