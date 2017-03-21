package fabianleven.cristianmilapallas.valenbisi;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fabi on 21.03.2017.
 */

public class PartesDBHelper extends SQLiteOpenHelper {

    public PartesDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // so far only one version exists
    }

    public void insertParte(Parte parte) {

    }

    public void updateParte(Parte parte) {

    }

    public void deleteParte(int parteId) {

    }

    public Cursor partesByStation(Parada station) {
        return null;
    }

    public Parte parteById(int parteId) {
        return null;
    }
}
