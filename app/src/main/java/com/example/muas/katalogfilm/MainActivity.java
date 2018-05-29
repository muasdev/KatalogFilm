package com.example.muas.katalogfilm;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.muas.katalogfilm.data.db.FetchDataFilm;
import com.example.muas.katalogfilm.data.db.FilmAdapter;
import com.example.muas.katalogfilm.data.db.model.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_cariFilm)
    EditText edtCariFilm;
    @BindView(R.id.btn_CariFilm)
    Button btnCariFilm;
    @BindView(R.id.rv_list_film)
    RecyclerView rvListFilm;

    final String BASE_FILM_URI =
            "https://api.themoviedb.org/3/search/movie?api_key=a91db70d304c21ebc5320b123953a915";
    final String PARAM_QUERY = "query";

    final String API_KEY =
            "https://api.themoviedb.org/3/movie/popular?api_key=a91db70d304c21ebc5320b123953a915&language=en-US&page=1";


    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCariFilm = (EditText) findViewById(R.id.edt_cariFilm);

        rvListFilm = (RecyclerView) findViewById(R.id.rv_list_film);


        /*simpleDateFormat = new SimpleDateFormat("EEE");
        Date = simpleDateFormat.format(calendar.getTime());*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvListFilm.setLayoutManager(linearLayoutManager);

        new getDataFilm().execute(API_KEY);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_CariFilm)
    public void onViewClicked(View view) {
        final ProgressDialog progressDialog =
                new ProgressDialog(this);
        progressDialog.setMessage("harap tunggu . . .");
        progressDialog.show();

        String cariFilm = edtCariFilm.getText()
                .toString().trim();


        Uri uri = Uri
                .parse(BASE_FILM_URI)
                .buildUpon()
                .appendQueryParameter(PARAM_QUERY,
                        cariFilm)
                .build();

        String url = uri.toString();

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "coba lagi yah hehe",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                final ArrayList<Film> filmArrayList = new ArrayList<>();
                String dataJson = response
                        .body()
                        .string();
                try {
                    JSONObject jsonObject = new JSONObject(dataJson);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FilmAdapter filmAdapter = new
                                FilmAdapter(getApplicationContext(),
                                filmArrayList);
                        rvListFilm.setAdapter(filmAdapter);
                    }
                });
            }
        });
    }

    //bikin asynctask
    private class getDataFilm extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();

                return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {

                jsonObject = new JSONObject(s);

                final ArrayList<Film> movieList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {

//Reading JSON object at 'i'th position of JSON Array
                    JSONObject object = jsonArray.getJSONObject(i);


                    /*calendar = Calendar.getInstance();
                    GregorianCalendar hari = new GregorianCalendar(TimeZone.getTimeZone(waktu));
                    int hariRilis = hari.get(calendar.DAY_OF_WEEK);
                    String dayOfTheWeek = (String) DateFormat.format("EEE", hariRilis); // Thursday*/

                    /*ubah format DATE waktu rilis film dari API*/
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = simpleDateFormat.parse(object.getString("release_date"));

                    simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
                    String output1 = simpleDateFormat.format(date1); //

                    Film filmDetail = new Film();
                    filmDetail.setOriginal_title(object.getString("original_title"));
                    filmDetail.setOverview(object.getString("overview"));
                    filmDetail.setRelease_date(output1);
                    filmDetail.setGambar_film(object.getString("poster_path"));
                    movieList.add(filmDetail);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FilmAdapter filmAdapter = new
                                    FilmAdapter(getApplicationContext(),
                                    movieList);
                            rvListFilm.setAdapter(filmAdapter);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                Log.d("pesan error", "onPostExecute: gagal convert waktu");
            }
        }
    }

}
