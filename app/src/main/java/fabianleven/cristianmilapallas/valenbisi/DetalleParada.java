package fabianleven.cristianmilapallas.valenbisi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetalleParada extends AppCompatActivity {
    private TextView numeroTV;
    private TextView addressTV;
    private TextView totalSlotsTV;
    private TextView availableBikesTV;
    private TextView freeSlotsTV;
    private TextView coordinatesTV;
    private ImageButton openMapBt;
    private ImageButton addIncidentBt;
    private Parada parada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_parada);
        parada = (Parada) getIntent().getSerializableExtra(ListaParadas.STATION_KEY);
        setTitle(parada.name);
        numeroTV = (TextView) findViewById(R.id.detalle_number);
        addressTV = (TextView) findViewById(R.id.detalle_address);
        totalSlotsTV = (TextView) findViewById(R.id.detalle_total);
        availableBikesTV = (TextView) findViewById(R.id.detalle_available);
        freeSlotsTV = (TextView) findViewById(R.id.detalle_freeslots);
        coordinatesTV = (TextView) findViewById(R.id.detalle_coordinates);
        openMapBt = (ImageButton) findViewById(R.id.detalle_openmap);
        addIncidentBt = (ImageButton) findViewById(R.id.detalle_addincident);

        numeroTV.setText(String.valueOf(parada.number));
        addressTV.setText(parada.address);
        totalSlotsTV.setText(String.valueOf(parada.totalSlots));
        availableBikesTV.setText(String.valueOf(parada.availableBikes));
        freeSlotsTV.setText(String.valueOf(parada.freeSlots));
        String coordinates_as_string = parada.coordinates.latitude + ", " + parada.coordinates.longitude;
        coordinatesTV.setText(coordinates_as_string);


        openMapBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri;
                gmmIntentUri = Uri.parse("geo:0,0?q="+parada.coordinates.latitude+","+parada.coordinates.longitude+"("+parada.address+")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }
}
