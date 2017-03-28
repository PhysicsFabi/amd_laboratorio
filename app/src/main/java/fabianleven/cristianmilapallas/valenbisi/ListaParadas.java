package fabianleven.cristianmilapallas.valenbisi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListaParadas extends AppCompatActivity {

    public static final String LOG_TAG = "Valenbisi";
    public static final String STATION_KEY = "station";
    private static final String VALENBISI_URL = "http://mapas.valencia.es/lanzadera/opendata/Valenbisi/JSON";
    private AdapterParada adapterParada;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_paradas);
        listView = (ListView) findViewById(R.id.station_list);
        new HTTPConnector().execute(VALENBISI_URL);
    }

    private void initListView(ArrayList<Parada> paradas) {
        adapterParada = new AdapterParada(getApplicationContext(), paradas);
        listView.setAdapter(adapterParada);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getApplicationContext(), DetalleParada.class);
                Parada parada = (Parada) adapterParada.getItem(position);
                // parada is serializable
                in.putExtra(STATION_KEY, parada);
                startActivity(in);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapterParada!=null)
            adapterParada.updateParadasFromDatabase();
    }

    public class HTTPConnector extends AsyncTask<String, Void, ArrayList<Parada>> {

        @Override
        protected ArrayList doInBackground(String... params) {
            ArrayList paradas = new ArrayList<Parada>();
            String url = params[0];
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];

            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");

                // Add request header
                con.setRequestProperty(
                        "user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/55.0.2883.87 Safari/537.36");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("accept-language", "es");
                con.connect();

                int responseCode = con.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP Error code: " + responseCode);
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                int n;

                while ((n = in.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                in.close();
            } catch (UnsupportedEncodingException e) {
                Log.e(ListaParadas.LOG_TAG, "Error while connecting to Valenbisi service.", e);
            } catch (IOException e) {
                Log.e(ListaParadas.LOG_TAG, "Error while connecting to Valenbisi service.", e);
            }

            JSONObject json;
            try {
                json = new JSONObject(writer.toString());
            } catch (JSONException e) {
                Log.e(ListaParadas.LOG_TAG, "Error while parsing received JSON.", e);
                json = new JSONObject();
            }

            return paradasFromJSON(json);
        }

        @Override
        protected void onPostExecute(ArrayList<Parada> arrayList) {
            initListView(arrayList);
        }

        private ArrayList<Parada> paradasFromJSON(JSONObject object) {
            ArrayList<Parada> paradas = new ArrayList<>();
            try {
                JSONArray paradasJSON = object.getJSONArray("features");

                for(int i = 0; i< paradasJSON.length(); i++) {
                    JSONObject paradaJSON = paradasJSON.getJSONObject(i);
                    JSONObject paradaJSONProps = paradaJSON.getJSONObject("properties");
                    JSONObject paradaJSONGeometry = paradaJSON.getJSONObject("geometry");
                    JSONArray paradaJSONCoordinates = paradaJSONGeometry.getJSONArray("coordinates");
                    paradas.add(new Parada(
                            paradaJSONProps.getString("name"),
                            paradaJSONProps.getInt("number"),
                            paradaJSONProps.getString("address"),
                            paradaJSONProps.optInt("total", -1),
                            paradaJSONProps.optInt("free", -1),
                            paradaJSONProps.optInt("available", -1),
                            paradaJSONCoordinates.getDouble(0), //latitude
                            paradaJSONCoordinates.getDouble(1), // longitude
                            0 // amount of partes set later
                    ));
                }
            } catch (JSONException e) {
                Log.e(ListaParadas.LOG_TAG, "Error while parsing json file.", e);
            }
            return paradas;
        }
    }
}
