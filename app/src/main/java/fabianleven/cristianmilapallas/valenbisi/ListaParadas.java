package fabianleven.cristianmilapallas.valenbisi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListaParadas extends AppCompatActivity {

    public static final String logTag = "Valenbisi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_paradas);
        ListView listView = (ListView) findViewById(R.id.station_list);
        listView.setAdapter(new AdapterParada(getApplicationContext()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent in = new Intent(getApplicationContext(), DetalleParada.class);
            startActivity(in);
        }
    });
    }
}
