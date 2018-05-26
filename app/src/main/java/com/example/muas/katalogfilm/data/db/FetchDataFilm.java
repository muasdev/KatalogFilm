package com.example.muas.katalogfilm.data.db;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muas.katalogfilm.R;
import com.example.muas.katalogfilm.data.db.model.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchDataFilm extends AsyncTask<String, Void, String>{

    private ImageView imageFilm;
    private TextView judulFilm;
    private TextView overview;
    private TextView release_date;

    public FetchDataFilm(ImageView imageFilm, TextView judulFilm, TextView overview, TextView release_date) {
        this.imageFilm = imageFilm;
        this.judulFilm = judulFilm;
        this.overview = overview;
        this.release_date = release_date;
    }

    public FetchDataFilm() {

    }


    @Override
    protected String doInBackground(String... params) {
        // Get the search string
        String queryString = params[0];


        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        // Attempt to query the Books API.
        try {
            // Base URI for the Books API.
            final String BASE_FILM_URI =
                    "https://api.themoviedb.org/3/search/movie?api_key=a91db70d304c21ebc5320b123953a915";
            final String PARAM_QUERY = "query"; // Parameter for the search string.
            final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
            final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

            // Build up your query URI, limiting results to 10 items and printed books.
            Uri builtURI = Uri.parse(BASE_FILM_URI).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .build();

            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                // return null;
                return null;
            }
            bookJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            e.printStackTrace();

            // Close the connections.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        return bookJSONString;
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON and updates the Views.
     *
     * @param s Result from the doInBackground method containing the raw JSON response,
     *          or null if it failed.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        final ArrayList<Film> filmArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject
                    .getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectFilm = jsonArray
                        .getJSONObject(i);

                String gambarFilm = objectFilm
                        .get("poster_path")
                        .toString();

                String judulFilm = objectFilm
                        .get("original_title")
                        .toString();

                String descFilm = objectFilm
                        .getString("overview")
                        .toString();

                String rilisFilm = objectFilm
                        .getString("release_date")
                        .toString();

                Film film = new Film(gambarFilm,
                        judulFilm,
                        descFilm,
                        rilisFilm);
                filmArrayList.add(film);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FilmAdapter filmAdapter = new
                        FilmAdapter(getApplicationContext(),
                        filmArrayList);
                rvListFilm.setAdapter(filmAdapter);
            }
        });*/
    }
}
