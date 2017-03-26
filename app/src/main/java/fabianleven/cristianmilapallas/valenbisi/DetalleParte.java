package fabianleven.cristianmilapallas.valenbisi;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleParte extends AppCompatActivity {

    private Parte parte;
    private int stationId;
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
            parte = new Parte();
            stationId = getIntent().getIntExtra(DetalleParada.KEY_STATION_ID, 0);
            parte.setStationId(stationId);
            setUpLayoutForNewParte();
        } else {
            parte = dbHelper.parteById(parteId);
            stationId = parte.getStationId();
            setUpLayoutForUpdateParte();
        }
    }

    private void setUpLayoutForUpdateParte() {
        setTitle(R.string.DetalleParte_title_update);
        fielFieldsFromParte();
        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateParte();
            }
        });

        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteParte();
            }
        });
    }

    private void setUpLayoutForNewParte() {
        setTitle(R.string.DetalleParte_title_new);
        updateBt.setImageResource(android.R.drawable.ic_input_add);
        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewParte();
            }
        });

        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetalleParte.this.getApplicationContext(), R.string.DetalleParte_creation_deleted, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void createNewParte() {
        parte.setStationId(stationId);
        switch (fillParteFromFields()) {
            case ERROR_NAME_EMPTY:
                Toast.makeText(DetalleParte.this.getApplicationContext(), R.string.DetalleParte_creation_error_empty_name, Toast.LENGTH_SHORT).show();
                return;
            case SUCCESS:
                long insertion_result = dbHelper.insertParte(parte);
                if(insertion_result<=-1) {
                    Toast.makeText(DetalleParte.this.getApplicationContext(), R.string.DetalleParte_creation_error_db, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(DetalleParte.this.getApplicationContext(), R.string.DetalleParte_creation_success, Toast.LENGTH_SHORT).show();
                finish();
        }
    }

    private void deleteParte() {
        if(!dbHelper.deleteParte(Integer.parseInt(parte.getId()))) {
            Toast.makeText(DetalleParte.this.getApplicationContext(), R.string.DetalleParte_delete_error_db, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(DetalleParte.this.getApplicationContext(), R.string.DetalleParte_delete_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateParte() {
        switch (fillParteFromFields()) {
            case ERROR_NAME_EMPTY:
                Toast.makeText(DetalleParte.this.getApplicationContext(), R.string.DetalleParte_creation_error_empty_name, Toast.LENGTH_SHORT).show();
                return;
            case SUCCESS:
                if(!dbHelper.updateParte(parte)) {
                    Toast.makeText(DetalleParte.this.getApplicationContext(), R.string.DetalleParte_update_error_db, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(DetalleParte.this.getApplicationContext(), R.string.DetalleParte_update_success, Toast.LENGTH_SHORT).show();
                finish();
        }
    }

    private void fielFieldsFromParte() {
        nameTE.setText(parte.getName());
        descriptionTE.setText(parte.getDescription());
        statusSp.setSelection(parte.getStatus().ordinal());
        typeSp.setSelection(parte.getType().ordinal());
    }

    private enum FILL_PARTE_RESULT {
        SUCCESS,
        ERROR_NAME_EMPTY
    }

    private FILL_PARTE_RESULT fillParteFromFields() {
        String name = nameTE.getText().toString();
        if(name.trim().isEmpty()) {
            return FILL_PARTE_RESULT.ERROR_NAME_EMPTY;
        }
        parte.setName(name);
        parte.setDescription(descriptionTE.getText().toString());
        parte.setStatus((Parte.STATUS) statusSp.getSelectedItem());
        parte.setType((Parte.TYPE) typeSp.getSelectedItem());
        return FILL_PARTE_RESULT.SUCCESS;
    }
}
