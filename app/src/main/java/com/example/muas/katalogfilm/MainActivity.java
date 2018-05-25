package com.example.muas.katalogfilm;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muas.katalogfilm.data.db.FilmAdapter;
import com.example.muas.katalogfilm.data.db.model.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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

    FilmAdapter filmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCariFilm = (EditText) findViewById(R.id.edt_cariFilm);

        rvListFilm = (RecyclerView) findViewById(R.id.rv_list_film);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvListFilm.setLayoutManager(linearLayoutManager);
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

                    for (int i=0; i<jsonArray.length();i++){
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
}
