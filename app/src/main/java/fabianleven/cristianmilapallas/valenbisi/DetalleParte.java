package fabianleven.cristianmilapallas.valenbisi;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

        String parteId = getIntent().getStringExtra(DetalleParada.KEY_PARTE_ID);
        dbHelper = new PartesDBHelper(getApplicationContext());

        nameTE = (TextView) findViewById(R.id.detalle_parte_name);
        descriptionTE = (TextView) findViewById(R.id.detalle_parte_description);
        statusSp = (Spinner) findViewById(R.id.detalle_parte_status);
        typeSp = (Spinner) findViewById(R.id.detalle_parte_type);
        deleteBt = (FloatingActionButton) findViewById(R.id.detalle_parte_delete);
        updateBt = (FloatingActionButton) findViewById(R.id.detalle_parte_confirm);

        statusSp.setAdapter(new ArrayAdapter<Parte.STATUS>(this, android.R.layout.simple_spinner_item, Parte.STATUS.values()));
        typeSp.setAdapter(new ArrayAdapter<Parte.TYPE>(this, android.R.layout.simple_spinner_item, Parte.TYPE.values()));


        if(parteId==null) {
            setTitle(R.string.DetalleParte_title_new);
            updateBt.setImageResource(android.R.drawable.ic_input_add);
            // create new One
            updateBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Parte.STATUS status = (Parte.STATUS) statusSp.getSelectedItem();
                    int stationid = getIntent().getIntExtra("stationId", 0);
                    System.out.println("station id click: " + stationid);
                    parte = new Parte("", nameTE.getText().toString(), descriptionTE.getText().toString(),
                                        stationid,
                                        (Parte.STATUS) statusSp.getSelectedItem(), (Parte.TYPE) typeSp.getSelectedItem());

                    System.err.println(dbHelper.insertParte(parte));
                    finish();
                }
            });

            deleteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            setTitle(R.string.DetalleParte_title_update);
            parte = dbHelper.parteById(parteId);
            // update old
            nameTE.setText(parte.getName());
            descriptionTE.setText(parte.getDescription());
            statusSp.setSelection(parte.getStatus().ordinal());
            typeSp.setSelection(parte.getType().ordinal());

            updateBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.updateParte(parte);

                    finish();
                }
            });

            deleteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHelper.deleteParte(Integer.parseInt(parte.getId()));

                    finish();
                }
            });
        }
    }
}
