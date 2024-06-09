package com.example.lab6_20200825_iot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab6_20200825_iot.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarIngreso extends AppCompatActivity {

    private EditText descripcionEditText, montoEditText;
    private TextView fechaTextView, horaTextView, tituloTextView;
    private Button guardarButton;
    private FirebaseFirestore db;
    private String ingresoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ingreso);

        tituloTextView = findViewById(R.id.titulonuevo);
        descripcionEditText = findViewById(R.id.descricionnuevo);
        montoEditText = findViewById(R.id.montoid);
        fechaTextView = findViewById(R.id.fechaedi);
        horaTextView = findViewById(R.id.horaedi);
        guardarButton = findViewById(R.id.Guardarbutton);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            ingresoId = intent.getStringExtra("ingresoId");
            tituloTextView.setText(intent.getStringExtra("titulo"));
            descripcionEditText.setText(intent.getStringExtra("descripcion"));
            montoEditText.setText(String.valueOf(intent.getDoubleExtra("monto", 0)));
            fechaTextView.setText(intent.getStringExtra("fecha"));
            horaTextView.setText(intent.getStringExtra("hora"));
            Log.d("EditarIngreso", "Editing ingreso with ID: " + ingresoId);
        }

        guardarButton.setOnClickListener(v -> guardarCambiosIngreso());
    }

    private void guardarCambiosIngreso() {
        if (ingresoId == null) {
            Log.e("EditarIngreso", "Ingreso ID is null");
            Toast.makeText(this, "No se pudo identificar el ingreso", Toast.LENGTH_SHORT).show();
            return;
        }

        String descripcion = descripcionEditText.getText().toString();
        double monto = Double.parseDouble(montoEditText.getText().toString());

        Map<String, Object> ingreso = new HashMap<>();
        ingreso.put("descripcion", descripcion);
        ingreso.put("monto", monto);

        db.collection("ingresos").document(ingresoId)
                .update(ingreso)
                .addOnSuccessListener(aVoid -> {
                    Log.d("EditarIngreso", "Ingreso updated successfully");
                    Toast.makeText(EditarIngreso.this, "Ingreso cambiado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarIngreso.this, ListaIngreso.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("EditarIngreso", "Error updating ingreso", e);
                    Toast.makeText(EditarIngreso.this, "No se pudo verificar el ingreso", Toast.LENGTH_SHORT).show();
                });
    }
}
