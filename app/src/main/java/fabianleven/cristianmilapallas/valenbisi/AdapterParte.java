package fabianleven.cristianmilapallas.valenbisi;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Fabi on 21.03.2017.
 */

public class AdapterParte extends CursorAdapter {

    static class ViewHolder {
        TextView nameTV;
        ImageView statusIV;
    }

    public AdapterParte(Context context, Cursor c) {
        super(context, c, 0);
    }

    /* example para mirar
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        AdapterParada.ViewHolder holder = null;

        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.parada_list_entry, null);
            holder = new AdapterParada.ViewHolder();
            holder.number = (TextView) v.findViewById(R.id.paradaviewnumber);
            holder.address = (TextView) v.findViewById(R.id.paradaviewaddress);
            holder.partes = (TextView) v.findViewById(R.id.paradaviewpartes);
            v.setTag(holder);
        } else {
            holder = (AdapterParada.ViewHolder) v.getTag();
        }

        Parada parada = this.paradas.get(position);
        holder.number.setText(Integer.toString(parada.number));
        holder.address.setText(parada.address);
        holder.partes.setText("0");

        return v;
    }
    */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

}
