package fabianleven.cristianmilapallas.valenbisi;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

public class DetalleParte extends AppCompatActivity {

    private Parte parte;
    private TextView name, description;
    private Spinner status, type;
    private FloatingActionButton delete, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_parte);

        parte = getIntent().getExtras(DetalleParada.KEY_PARTE_ID);

        if (parte != null) {
            name = (TextView) findViewById(R.id.detalle_parte_);

        } else {

        }


    }
}
