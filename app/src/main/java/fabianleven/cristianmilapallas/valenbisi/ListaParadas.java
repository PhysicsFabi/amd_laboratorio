package fabianleven.cristianmilapallas.valenbisi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ListaParadas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = (ListView) findViewById(R.id.station_list);
        listView.setAdapter(new AdapterParada());
        setContentView(R.layout.activity_lista_paradas);
    }
}
