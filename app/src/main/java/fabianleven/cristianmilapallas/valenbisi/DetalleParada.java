package fabianleven.cristianmilapallas.valenbisi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetalleParada extends AppCompatActivity {
    private TextView numeroTV;
    private TextView addressTV;
    private TextView totalSlotsTV;
    private TextView availableBikesTV;
    private TextView freeSlotsTV;
    private TextView coordinatesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_parada);
        Parada parada = (Parada) getIntent().getSerializableExtra(ListaParadas.STATION_KEY);
        setTitle(parada.name);
        numeroTV = (TextView) findViewById(0);
        addressTV = (TextView) findViewById(0);
        totalSlotsTV = (TextView) findViewById(0);
        availableBikesTV = (TextView) findViewById(0);
        freeSlotsTV = (TextView) findViewById(0);
        coordinatesTV = (TextView) findViewById(0);

        numeroTV.setText(String.valueOf(parada.number));
        addressTV.setText(parada.address);
        totalSlotsTV.setText(String.valueOf(parada.totalSlots));
        availableBikesTV.setText(String.valueOf(parada.availableBikes));
        freeSlotsTV.setText(String.valueOf(parada.freeSlots));
        String coordinates_as_string = parada.coordinates.latitude + ", " + parada.coordinates.longitude;
        coordinatesTV.setText(coordinates_as_string);
    }
}
