package fabianleven.cristianmilapallas.valenbisi;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

public class DetalleParte extends AppCompatActivity {

    private Parte parte;
    private PartesDBHelper dbHelper;

    private TextView nameTE, descriptionTE;
    private Spinner statusSp, typeSp;
    private FloatingActionButton deleteBt, updateBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_parte);

        updateBt = (FloatingActionButton) findViewById(R.id.detalle_parte_confirm);

        String parteId = getIntent().getStringExtra(DetalleParada.KEY_PARTE_ID);
        dbHelper = new PartesDBHelper(getApplicationContext());
        if(parteId==null) {
            setTitle(R.string.DetalleParte_title_new);
            updateBt.setImageResource(android.R.drawable.ic_input_add);
            // create new One
        } else {
            setTitle(R.string.DetalleParte_title_update);
            parte = dbHelper.parteById(parteId);
            // update old
        }
    }
}
