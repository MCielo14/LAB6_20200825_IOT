package com.example.lab6_20200825_iot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab6_20200825_iot.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarEgreso extends AppCompatActivity {
    private EditText descripcionEditText, montoEditText;
    private TextView fechaTextView, horaTextView, tituloTextView;
    private Button guardarButton;
    private FirebaseFirestore db;
    private String egresoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_egreso);

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
            Intent intent1 = new Intent(EditarEgreso.this, ListaIngreso.class);
            startActivity(intent1);
        });
        egreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(EditarEgreso.this, ListarEgreso.class);
            startActivity(intent1);
        });
        resumen.setOnClickListener(v -> {
            Intent intent1 = new Intent(EditarEgreso.this, Resumen.class);
            startActivity(intent1);
        });
        cerrarSesionButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(EditarEgreso.this)
                    .addOnCompleteListener(task -> {
                        Credentials.getClient(EditarEgreso.this).disableAutoSignIn();
                        Intent loginIntent = new Intent(EditarEgreso.this, MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    });
        });


        Intent intent = getIntent();
        if (intent != null) {
            egresoId = intent.getStringExtra("egresoId");
            tituloTextView.setText(intent.getStringExtra("titulo"));
            descripcionEditText.setText(intent.getStringExtra("descripcion"));
            montoEditText.setText(String.valueOf(intent.getDoubleExtra("monto", 0)));
            fechaTextView.setText(intent.getStringExtra("fecha"));
            horaTextView.setText(intent.getStringExtra("hora"));
            Log.d("EditarIngreso", "Editar egreso con ID: " + egresoId);
        }

        guardarButton.setOnClickListener(v -> guardarCambiosIngreso());
    }

    private void guardarCambiosIngreso() {
        if (egresoId == null) {
            Log.e("EditarIngreso", "Egreso ID es nulo");
            Toast.makeText(this, "No se pudo identificar el egreso", Toast.LENGTH_SHORT).show();
            return;
        }

        String descripcion = descripcionEditText.getText().toString();
        double monto = Double.parseDouble(montoEditText.getText().toString());

        Map<String, Object> egreso = new HashMap<>();
        egreso.put("descripcion", descripcion);
        egreso.put("monto", monto);

        db.collection("egresos").document(egresoId)
                .update(egreso)
                .addOnSuccessListener(aVoid -> {
                    Log.d("EditarIngreso", "Egreso actualizado ");
                    Toast.makeText(EditarEgreso.this, "Egreso cambiado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarEgreso.this, ListarEgreso.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("EditarIngreso", "Error actualizando egreso", e);
                    Toast.makeText(EditarEgreso.this, "No se pudo verificar el egreso", Toast.LENGTH_SHORT).show();
                });
    }
}