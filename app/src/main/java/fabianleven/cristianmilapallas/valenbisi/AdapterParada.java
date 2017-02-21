package fabianleven.cristianmilapallas.valenbisi;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

/**
 * Created by Fabi on 21.02.2017.
 */

public class AdapterParada extends BaseAdapter {
    private ArrayList<Parada> paradas;
    Context context;

    static class ViewHolder {
        TextView number;
        TextView address;
        TextView partes;
    }

    public void AdapterParadas (Context c) {
        context = c;
        Init();
    }

    public void Init() {
        InputStream is = context.getResources().openRawResource(R.raw.paradasvalenbisi);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;

            while((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try {
            JSONObject jsonObject = new JSONObject(writer.toString());
            this.paradas = paradasFromJSON(jsonObject);
        } catch (JSONException e) {
            this.paradas = new ArrayList<Parada>();
        }
    }


    private ArrayList<Parada> paradasFromJSON(JSONObject object) {
        return null;
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
    public View getView(int position, View convertVIew, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;

        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.listparadaview, null);
            holder = new ViewHolder();
            holder.number = (TextView) v.findViewById(R.id.paradaviewnumber);
            holder.address = (TextView) v.findViewById(R.id.paradaviewaddress);
            holder.partes = (TextView) v.findViewById(R.id.paradaviewpartes);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // Fill holder with the station info that is at 'position' in the ArrayList.

        return null;
    }
}
