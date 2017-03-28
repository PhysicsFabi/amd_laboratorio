package fabianleven.cristianmilapallas.valenbisi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class AdapterParada extends BaseAdapter {
    private final ArrayList<Parada> paradas;
    private final Context context;

    static class ViewHolder {
        TextView number;
        TextView address;
        TextView partes;
    }

    public AdapterParada(Context c, ArrayList<Parada> paradas) {
        context = c;
        this.paradas = paradas;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            v = View.inflate(context, R.layout.parada_list_entry, null);
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
        holder.partes.setText(String.valueOf(parada.partes));

        return v;
    }
}
