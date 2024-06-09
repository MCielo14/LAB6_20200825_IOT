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
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.firebase.auth.FirebaseAuth;
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
        Button ingreso =findViewById(R.id.ingresosbutton);
        Button egreso =findViewById(R.id.egresosbutton);
        Button resumen = findViewById(R.id.resumenbutton);
        Button cerrarSesionButton = findViewById(R.id.cerrarsession);
        ingreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(EditarIngreso.this, ListaIngreso.class);
            startActivity(intent1);
        });
        egreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(EditarIngreso.this, ListarEgreso.class);
            startActivity(intent1);
        });
        resumen.setOnClickListener(v -> {
            Intent intent1 = new Intent(EditarIngreso.this, Resumen.class);
            startActivity(intent1);
        });
        cerrarSesionButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(EditarIngreso.this)
                    .addOnCompleteListener(task -> {
                        Credentials.getClient(EditarIngreso.this).disableAutoSignIn();
                        Intent loginIntent = new Intent(EditarIngreso.this, MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    });
        });


        Intent intent = getIntent();
        if (intent != null) {
            ingresoId = intent.getStringExtra("ingresoId");
            tituloTextView.setText(intent.getStringExtra("titulo"));
            descripcionEditText.setText(intent.getStringExtra("descripcion"));
            montoEditText.setText(String.valueOf(intent.getDoubleExtra("monto", 0)));
            fechaTextView.setText(intent.getStringExtra("fecha"));
            horaTextView.setText(intent.getStringExtra("hora"));
            Log.d("EditarIngreso", "Editar ingreso con ID: " + ingresoId);
        }

        guardarButton.setOnClickListener(v -> guardarCambiosIngreso());
    }

    private void guardarCambiosIngreso() {
        if (ingresoId == null) {
            Log.e("EditarIngreso", "Ingreso ID válido el actual es nulo");
            Toast.makeText(this, "No se pudo identificar el ingreso", Toast.LENGTH_SHORT).show();
            return;
        }

        String descripcion = descripcionEditText.getText().toString();
        double monto = Double.parseDouble(montoEditText.getText().toString());
        // Se añade un hash map para añadir una clave y un valor, esto se usa para actualizar los datos de la bd
        Map<String, Object> ingreso = new HashMap<>();
        ingreso.put("descripcion", descripcion);
        ingreso.put("monto", monto);

        db.collection("ingresos").document(ingresoId)
                .update(ingreso)
                .addOnSuccessListener(aVoid -> {
                    Log.d("EditarIngreso", "Ingreso cambiado");
                    Toast.makeText(EditarIngreso.this, "Ingreso cambiado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarIngreso.this, ListaIngreso.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("EditarIngreso", "Error actualizando ingreso", e);
                    Toast.makeText(EditarIngreso.this, "No se pudo verificar el ingreso", Toast.LENGTH_SHORT).show();
                });
    }
}
