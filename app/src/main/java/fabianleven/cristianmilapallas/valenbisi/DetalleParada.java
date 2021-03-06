package fabianleven.cristianmilapallas.valenbisi;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class DetalleParada extends AppCompatActivity {
    public static final String KEY_PARTE_ID = "parteId";
    public static final String KEY_STATION_ID = "stationId";

    private TextView numeroTV;
    private TextView addressTV;
    private TextView totalSlotsTV;
    private TextView availableBikesTV;
    private TextView freeSlotsTV;
    private TextView coordinatesTV;
    private ListView incidentsLV;
    private ImageButton openMapBt;
    private ImageButton addIncidentBt;
    private PartesDBHelper db;
    private AdapterParte partesAdapter;

    private Parada parada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_parada);
        parada = (Parada) getIntent().getSerializableExtra(ListaParadas.STATION_KEY);
        db = PartesDBHelper.getInstance(getApplicationContext());
        partesAdapter = new AdapterParte(getApplicationContext(), db.partesByStation(parada));

        setTitle(parada.name);
        findAllViews();
        setFields();
        setUpParteList();
        openMapBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });
        addIncidentBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addParte();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        partesAdapter.changeCursor(db.partesByStation(parada));
    }

    @Override
    protected void onStop() {
        super.onStop();
        partesAdapter.getCursor().close();
    }

    private void findAllViews() {
        numeroTV = (TextView) findViewById(R.id.detalle_number);
        addressTV = (TextView) findViewById(R.id.detalle_address);
        totalSlotsTV = (TextView) findViewById(R.id.detalle_total);
        availableBikesTV = (TextView) findViewById(R.id.detalle_available);
        freeSlotsTV = (TextView) findViewById(R.id.detalle_freeslots);
        coordinatesTV = (TextView) findViewById(R.id.detalle_coordinates);
        incidentsLV = (ListView) findViewById(R.id.detalle_incidents);
        openMapBt = (ImageButton) findViewById(R.id.detalle_openmap);
        addIncidentBt = (ImageButton) findViewById(R.id.detalle_addincident);
    }

    private void setFields() {
        numeroTV.setText(String.valueOf(parada.number));
        addressTV.setText(parada.address);
        String totalSlots_string = parada.totalSlots == -1 ? getString(R.string.DetalleParada_no_hay_data) : String.valueOf(parada.totalSlots);
        totalSlotsTV.setText(totalSlots_string);
        String availableSlots_string = parada.availableBikes == -1 ? getString(R.string.DetalleParada_no_hay_data) : String.valueOf(parada.availableBikes);
        availableBikesTV.setText(availableSlots_string);
        String freeSlots_string = parada.freeSlots == -1 ? getString(R.string.DetalleParada_no_hay_data) : String.valueOf(parada.freeSlots);
        freeSlotsTV.setText(freeSlots_string);
        String coordinates_as_string = parada.coordinates.latitude + ", " + parada.coordinates.longitude;
        coordinatesTV.setText(coordinates_as_string);
    }

    private void setUpParteList() {
        incidentsLV.setAdapter(partesAdapter);
        incidentsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = partesAdapter.getCursor();
                cursor.moveToPosition(position);
                Intent in = new Intent(getApplicationContext(), DetalleParte.class);
                in.putExtra(KEY_PARTE_ID, PartesDBHelper.parteFromCursor(cursor).getId());
                startActivity(in);
            }
        });
    }

    private void addParte() {
        Intent in = new Intent(getApplicationContext(), DetalleParte.class);
        in.putExtra(KEY_STATION_ID, parada.number);
        startActivity(in);
    }

    private void openMap() {
        Uri gmmIntentUri;
        gmmIntentUri = Uri.parse("geo:0,0?q="+parada.coordinates.latitude+","+parada.coordinates.longitude+"("+parada.address+")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }


}
