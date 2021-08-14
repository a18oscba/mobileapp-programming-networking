package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.google.android.material.snackbar.Snackbar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    ArrayList<Mountain> mountainsList;
    ArrayAdapter<Mountain> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mountainsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item_textview, mountainsList);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String message = "Berget " + mountainsList.get(i).getName() + " finns i " + mountainsList.get(i).getLocation() +
                        " och är " + mountainsList.get(i).getSize() + " stort.";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");



    }
    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String json) {
            try {
                mountainsList.clear();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String ID = jsonObject.getString("ID");
                    String name = jsonObject.getString("name");
                    String location = jsonObject.getString("location");
                    String size = jsonObject.getString("size");
                    Mountain mountain = new Mountain(ID, name, location, size);
                    mountainsList.add(mountain);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("tag","E:"+e.getMessage());
            }
        }
    }
}
