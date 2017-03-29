package fabianleven.cristianmilapallas.valenbisi;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static fabianleven.cristianmilapallas.valenbisi.ListaParadas.HTTPConnectorResult.*;

public class ListaParadas extends AppCompatActivity {

    @SuppressWarnings("WeakerAccess")
    public static final String LOG_TAG = "Valenbisi";
    public static final String STATION_KEY = "station";
    private static final String VALENBISI_URL = "http://mapas.valencia.es/lanzadera/opendata/Valenbisi/JSON";

    private ArrayList<Parada> paradas;
    private AdapterParada adapterParada;
    private ListView paradasLV;
    private SwipeRefreshLayout paradasRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_paradas);
        initViews();
        fetchDataFromServer();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateParadasFromDatabase();
        if(adapterParada!=null)
            adapterParada.notifyDataSetChanged();
    }

    private void initViews() {
        paradasLV = (ListView) findViewById(R.id.station_list);
        paradasLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getApplicationContext(), DetalleParada.class);
                Parada parada = (Parada) adapterParada.getItem(position);
                // parada is serializable
                in.putExtra(STATION_KEY, parada);
                startActivity(in);
            }
        });

        paradasRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        paradasRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDataFromServer();
            }
        });
    }

    private void fetchDataFromServer() {
        paradasRefreshLayout.setRefreshing(true);
        new HTTPConnector().execute(VALENBISI_URL);
    }

    private void setParadas(ArrayList<Parada> paradas) {
        this.paradas = paradas;
        updateParadasFromDatabase();
        initListViewAdapter();
    }

    private void initListViewAdapter() {
        adapterParada = new AdapterParada(getApplicationContext(), paradas);
        paradasLV.setAdapter(adapterParada);
    }

    private void onDataRequestFinished(ArrayList<Parada> paradas, HTTPConnectorResult result) {
        switch (result) {
            case SUCCESS:
                setParadas(paradas);
                Toast.makeText(getApplicationContext(), R.string.ListaParada_fetchData_success, Toast.LENGTH_SHORT).show();
                break;
            case ERROR_CONNECTION_FAILURE:
            case ERROR_PARSING_FAILURE:
                Toast.makeText(getApplicationContext(), R.string.ListaParada_fetchData_failure, Toast.LENGTH_SHORT).show();
                break;
        }
        paradasRefreshLayout.setRefreshing(false);
    }

    /**
     * Retrieves the amount of tickets for each station from the database and refreshes the data.
     * Also calls notifyDataSetChanged(), hence the ListView this adapter belongs to will be refreshed.
     */
    private void updateParadasFromDatabase() {
        if(paradas==null)
            return;
        for (Parada station: paradas) {
            Cursor c = PartesDBHelper.getInstance(getApplicationContext()).partesByStation(station);
            station.partes = c.getCount();
            c.close();
        }
    }

    enum HTTPConnectorResult {
        SUCCESS,
        ERROR_CONNECTION_FAILURE,
        ERROR_PARSING_FAILURE
    }

    public class HTTPConnector extends AsyncTask<String, Void, ArrayList<Parada>> {

        HTTPConnectorResult result = SUCCESS;

        @Override
        protected ArrayList<Parada> doInBackground(String... params) {
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
            } catch (IOException e) {
                Log.e(ListaParadas.LOG_TAG, "Error while connecting to Valenbisi service.", e);
                result = ERROR_CONNECTION_FAILURE;
                return null;
            }

            JSONObject json;
            try {
                json = new JSONObject(writer.toString());
            } catch (JSONException e) {
                Log.e(ListaParadas.LOG_TAG, "Error while parsing received JSON.", e);
                result = ERROR_CONNECTION_FAILURE;
                return null;
            }

            ArrayList<Parada> paradas = paradasFromJSON(json);
            result = SUCCESS;
            return paradas;
        }

        private ArrayList<Parada> paradasFromJSON(JSONObject object) {
            ArrayList<Parada> paradas = new ArrayList<>();
            try {
                JSONArray paradasJSON = object.getJSONArray("features");

                for(int i = 0; i< paradasJSON.length(); i++) {
                    JSONObject paradaJSON = paradasJSON.getJSONObject(i);
                    JSONObject paradaJSONProps = paradaJSON.getJSONObject("properties");
                    JSONObject paradaJSONGeometry = paradaJSON.getJSONObject("geometry");
                    JSONArray paradaJSONCoordinatesUTM = paradaJSONGeometry.getJSONArray("coordinates");
                    Pair<Double,Double> paradaJSONCoordinatesLatLon = UTM2Deg(
                            30,
                            'S',
                            paradaJSONCoordinatesUTM.getDouble(0),
                            paradaJSONCoordinatesUTM.getDouble(1)
                    );
                    paradas.add(new Parada(
                            paradaJSONProps.getString("name"),
                            paradaJSONProps.getInt("number"),
                            paradaJSONProps.getString("address"),
                            paradaJSONProps.optInt("total", -1),
                            paradaJSONProps.optInt("free", -1),
                            paradaJSONProps.optInt("available", -1),
                            paradaJSONCoordinatesLatLon.first, //latitude
                            paradaJSONCoordinatesLatLon.second // longitude
                    ));
                }
            } catch (JSONException e) {
                Log.e(ListaParadas.LOG_TAG, "Error while parsing json file.", e);
                result = ERROR_PARSING_FAILURE;
                return null;
            }

            Collections.sort(paradas,new Comparator<Parada>() {
                @Override
                public int compare(Parada parada1, Parada parada2) {
                    return parada1.address.compareToIgnoreCase(parada2.address);
                }
            });
            return paradas;
        }

        @Override
        protected void onPostExecute(ArrayList<Parada> paradas) {
            onDataRequestFinished(paradas, result);
        }

        private Pair<Double, Double> UTM2Deg(int zone_number, char zone_letter, double easting, double northing) {
            double longitude;
            double latitude;
            double north;

            if(zone_letter>'M')
                north = northing;
            else
                north = northing - 10000000;

            longitude=(north/6366197.724/0.9996+(1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)-0.006739496742*Math.sin(north/6366197.724/0.9996)*Math.cos(north/6366197.724/0.9996)*(Math.atan(Math.cos(Math.atan(( Math.exp((easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*( 1 -  0.006739496742*Math.pow((easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996 )/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996 - 0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996 )*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996)*3/2)*(Math.atan(Math.cos(Math.atan((Math.exp((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996))*180/Math.PI;
            longitude=Math.round(longitude*10000000);
            longitude=longitude/10000000;
            latitude=Math.atan((Math.exp((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*( north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2* north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3)) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))*180/Math.PI+zone_number*6-183;
            latitude=Math.round(latitude*10000000);
            latitude=latitude/10000000;
            return new Pair<>(latitude, longitude);
        }
    }
}
