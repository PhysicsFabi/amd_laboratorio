package fabianleven.cristianmilapallas.valenbisi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class AdapterParada extends BaseAdapter {
    private ArrayList<Parada> paradas;
    Context context;
    PartesDBHelper partesDataBase;

    static class ViewHolder {
        TextView number;
        TextView address;
        TextView partes;
    }

    public AdapterParada(Context c) {
        context = c;
        partesDataBase = new PartesDBHelper(c);
        Init();
    }

    public void Init() {
        InputStream is = context.getResources().openRawResource(R.raw.paradasvalenbici);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
           Log.e(ListaParadas.logTag, "Error while reading json file.", e);
        } catch (IOException e) {
            Log.e(ListaParadas.logTag, "Error while reading json file.", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(ListaParadas.logTag, "Error while reading json file.", e);
            }
        }

        try {
            initParadasFromJSON(new JSONObject(writer.toString()));
        } catch (JSONException e) {
            Log.e(ListaParadas.logTag, "Error while reading json file.", e);
        }
    }


    private void initParadasFromJSON(JSONObject object) {
       this.paradas = new ArrayList<Parada>();
        try {
            JSONArray paradasJSON = object.getJSONArray("features");

            for(int i = 0; i< paradasJSON.length(); i++) {
                JSONObject paradaJSON = paradasJSON.getJSONObject(i);
                JSONObject paradaJSONProps = paradaJSON.getJSONObject("properties");
                JSONObject paradaJSONGeometry = paradaJSON.getJSONObject("geometry");
                JSONArray paradaJSONCoordinates = paradaJSONGeometry.getJSONArray("coordinates");
                this.paradas.add(new Parada(
                        paradaJSONProps.getString("name"),
                        paradaJSONProps.getInt("number"),
                        paradaJSONProps.getString("address"),
                        paradaJSONProps.optInt("total", -1),
                        paradaJSONProps.optInt("free", -1),
                        paradaJSONProps.optInt("available", -1),
                        paradaJSONCoordinates.getDouble(0), //latitude
                        paradaJSONCoordinates.getDouble(1) // longitude
                ));
            }
        } catch (JSONException e) {
            Log.e(ListaParadas.logTag, "Error while parsing json file.", e);
        }
    }

    @Override
    public int getCount() {
        return paradas.size();
    }

    @Override
    public Object getItem(int position) {
        return paradas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return paradas.get(position).number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;

        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.parada_list_entry, null);
            holder = new ViewHolder();
            holder.number = (TextView) v.findViewById(R.id.paradaviewnumber);
            holder.address = (TextView) v.findViewById(R.id.paradaviewaddress);
            holder.partes = (TextView) v.findViewById(R.id.paradaviewpartes);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }


        Parada parada = this.paradas.get(position);
        holder.number.setText(Integer.toString(parada.number));
        holder.address.setText(parada.address);
        holder.partes.setText(String.valueOf(partesDataBase.partesByStation(parada).getCount()));

        return v;
    }
}
