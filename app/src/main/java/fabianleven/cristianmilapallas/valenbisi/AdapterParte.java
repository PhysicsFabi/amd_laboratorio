package fabianleven.cristianmilapallas.valenbisi;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.EnumMap;


/**
 * Created by Fabi on 21.03.2017.
 */

public class AdapterParte extends CursorAdapter {

    public AdapterParte(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.parte_list_entry, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTV = (TextView) view.findViewById(R.id.parte_list_name);
        StatusColorDotView statusSCDV = (StatusColorDotView) view.findViewById(R.id.parte_list_status);
        Parte parte = PartesDBHelper.parteFromCursor(cursor);
        nameTV.setText(parte.getName());
        statusSCDV.setStatus(parte.getStatus());
    }
}
