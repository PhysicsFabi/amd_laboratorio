package fabianleven.cristianmilapallas.valenbisi;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
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
        ImageView statusTV;
    }

    public AdapterParte(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Shouldn't it be R.layour.parte_list_entry inside the inflate() method? not sure.
        return LayoutInflater.from(context).inflate(R.layout.parada_list_entry, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = null;

        // Debugging code.
        if(view.findViewById(R.id.parte_list_name) == null) {
            System.err.println("NO REFERENCE");
        } else {
            System.err.println("WHAT!?");
        }
        // Code down below throws NullPointerException. Something's wrong retrieving "R.id.parte_list_name".

        //holder.nameTV = (TextView) view.findViewById(R.id.parte_list_name);
        //holder.statusTV = (ImageView) view.findViewById(R.id.parte_list_status);
        //Parte parte = PartesDBHelper.parteFromCursor(cursor);
        //holder.nameTV.setText(parte.getName());
        //TODO set color according to status
    }

}
