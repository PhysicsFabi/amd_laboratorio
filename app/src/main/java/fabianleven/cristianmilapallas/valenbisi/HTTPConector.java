package fabianleven.cristianmilapallas.valenbisi;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Cristian on 28/03/2017.
 */

public class HTTPConector extends AsyncTask<String, void, ArrayList> {

    @Override
    protected ArrayList doInBackground(String... params) {
        ArrayList paradas = new ArrayList<Parada>();
        String url = "http://mapas.valencia.es/lanzadera/opendata/Valenbisi/JSON";
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            // Add request header
            con.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)" +
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

