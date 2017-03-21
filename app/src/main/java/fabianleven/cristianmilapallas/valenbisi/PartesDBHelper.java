package fabianleven.cristianmilapallas.valenbisi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fabi on 21.03.2017.
 */

public class PartesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "valenbisi";
    private static int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "parte";
    private static final String COLUMN_NAME_PRIMARY_KEY = "id";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_DESCRIPTION = "description";
    private static final String COLUMN_NAME_STATION_ID = "station_id";
    private static final String COLUMN_NAME_STATUS = "status";
    private static final String COLUMN_NAME_TYPE = "type";

    public PartesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // assumes sqlite ver. > 3
        String sql =
            "CREATE TABLE " + TABLE_NAME + " ( " +
            COLUMN_NAME_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME_NAME + " TEXT, " +
            COLUMN_NAME_DESCRIPTION + " TEXT, " +
            COLUMN_NAME_STATION_ID + " INTEGER, " +
            COLUMN_NAME_STATUS + " INTEGER, " +
            COLUMN_NAME_TYPE + " INTEGER )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // so far only one version exists
    }

    /**
     * Creates key-value-pairs for a ticket. The pirmary key column is NOT included.
     *
     * @param parte
     * @return column name - column content pairs for a ticket
     */
    private ContentValues contentValuesByParte(Parte parte) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, parte.getName());
        values.put(COLUMN_NAME_DESCRIPTION, parte.getDescription());
        values.put(COLUMN_NAME_STATION_ID, parte.getStationId());
        values.put(COLUMN_NAME_STATUS, parte.getStatus().getVal());
        values.put(COLUMN_NAME_TYPE, parte.getType().getVal());
        return values;
    }

    public static Parte parteFromCursor(Cursor c) {
        final int COLUMN_ID_PRIMARY_KEY = c.getColumnIndex(COLUMN_NAME_PRIMARY_KEY);
        final int COLUMN_ID_NAME = c.getColumnIndex(COLUMN_NAME_NAME);
        final int COLUMN_ID_DESCRIPTION = c.getColumnIndex(COLUMN_NAME_DESCRIPTION);
        final int COLUMN_ID_STATION_ID = c.getColumnIndex(COLUMN_NAME_STATION_ID);
        final int COLUMN_ID_STATUS = c.getColumnIndex(COLUMN_NAME_STATUS);
        final int COLUMN_ID_TYPE = c.getColumnIndex(COLUMN_NAME_TYPE);
        return new Parte(
                c.getString(COLUMN_ID_PRIMARY_KEY),
                c.getString(COLUMN_ID_NAME),
                c.getString(COLUMN_ID_DESCRIPTION),
                c.getInt(COLUMN_ID_STATION_ID),
                Parte.STATUS.values()[c.getInt(COLUMN_ID_STATUS)],
                Parte.TYPE.values()[c.getInt(COLUMN_ID_TYPE)]);
    }

    /**
     * Inserts a ticket in the database.
     *
     * @param parte The tickt to be inserted
     * @return the ID of the newly inserted ticket, or -1 if an error occurred
     */
    public long insertParte(Parte parte) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insertOrThrow(TABLE_NAME, null, contentValuesByParte(parte));
    }

    /**
     * Updates a ticket in the database.
     *
     * @param parte the ticket t be updated
     * @return true - success, false - failure
     */
    public boolean updateParte(Parte parte) {
        String filter = COLUMN_NAME_PRIMARY_KEY + "=" + parte.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_NAME, contentValuesByParte(parte), filter, null) > 0;
    }

    /**
     * Deletes a ticket in the database.
     *
     * @param parteId the id of the ticket to be deleted
     * @return true - success, false - failure
     */
    public boolean deleteParte(int parteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filter = COLUMN_NAME_PRIMARY_KEY + "=" + parteId;
        return db.delete(TABLE_NAME, filter, null) > 0;
    }

    public Cursor partesByStation(Parada station) {
        String filter = COLUMN_NAME_STATION_ID + "=" + station.number;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    /**
     * Returns the ticket with the given id.
     *
     * @param parteId the ticket id
     * @return the ticket or nullm if the given ticket id does not exist
     */
    public Parte parteById(int parteId) {
        String filter = COLUMN_NAME_PRIMARY_KEY + "=" + parteId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if(!c.moveToFirst())
            return null;
        else {
            return parteFromCursor(c);
        }
    }
}
