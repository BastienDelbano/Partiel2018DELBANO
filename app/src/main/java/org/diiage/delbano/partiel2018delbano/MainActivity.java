package org.diiage.delbano.partiel2018delbano;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity THIS = this;

        String baseUrlApi = getResources().getString(R.string.url_api);
        URL baseUrl = null;
        try {
            baseUrl = new URL(baseUrlApi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        @SuppressLint("StaticFieldLeak") AsyncTask<URL,Integer,ArrayList<Release>> task = new AsyncTask<URL, Integer, ArrayList<Release>>() {
            @Override
            protected ArrayList<Release> doInBackground(URL... urls) {

                ArrayList<Release> releases = new ArrayList<>();
                try {

                    InputStream inputStream = urls[0].openStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    //Initialisation d'un StringBuilder pour stocker le contenu distant
                    StringBuilder stringBuilder = new StringBuilder();
                    String lineBuffer = null;
                    while ((lineBuffer = bufferedReader.readLine()) != null){
                        stringBuilder.append(lineBuffer);
                    }

                    String data = stringBuilder.toString();
                    JSONArray jsonArray = new JSONArray(data);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONParser jsonParser = new JSONParser();
                        releases.add(jsonParser.JSONToRelease(jsonObject));
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("EXCEPTION", e.getLocalizedMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("EXCEPTION", e.getLocalizedMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("EXCEPTION", e.getLocalizedMessage());
                }
                return releases;
            }

            @Override
            protected void onPostExecute(ArrayList<Release> releases){
                super.onPostExecute(releases);
                ListView lstRelease = findViewById(R.id.lstReleaseTitle);
                ReleaseAdapter ra = new ReleaseAdapter(THIS, releases);
                lstRelease.setAdapter(ra);

                ReleaseDbHelper helper = new ReleaseDbHelper(THIS);
                SQLiteDatabase db = helper.getWritableDatabase();

                for (Release release : releases){
                    long idLastRelease = helper.insertRelease(db, release);
                }
            }
        }.execute(baseUrl);
    }
}
