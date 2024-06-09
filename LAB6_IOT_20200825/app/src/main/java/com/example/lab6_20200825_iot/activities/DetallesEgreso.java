package com.example.lab6_20200825_iot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

public class DetallesEgreso extends AppCompatActivity {

    private TextView tituloTextView, descripcionTextView, montoTextView, fechaTextView, horaTextView;
    private Button editarButton, borrarButton;
    private FirebaseFirestore db;
    private String egresoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_egreso);

        tituloTextView = findViewById(R.id.titulodetalle);
        descripcionTextView = findViewById(R.id.descriciondetalle);
        montoTextView = findViewById(R.id.montoDetalle);
        fechaTextView = findViewById(R.id.FechaDetalle);
        horaTextView = findViewById(R.id.HoraVencimientoDetalle);
        editarButton = findViewById(R.id.Editarbutton);
        borrarButton = findViewById(R.id.BorrarButton);
        Button ingreso =findViewById(R.id.ingresosbutton);
        Button egreso =findViewById(R.id.egresosbutton);
        Button resumen = findViewById(R.id.resumenbutton);
        Button cerrarSesionButton = findViewById(R.id.cerrarsession);
        ingreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetallesEgreso.this, ListaIngreso.class);
            startActivity(intent1);
        });
        egreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetallesEgreso.this, ListarEgreso.class);
            startActivity(intent1);
        });
        resumen.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetallesEgreso.this, Resumen.class);
            startActivity(intent1);
        });
        cerrarSesionButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(DetallesEgreso.this)
                    .addOnCompleteListener(task -> {
                        Credentials.getClient(DetallesEgreso.this).disableAutoSignIn();
                        Intent loginIntent = new Intent(DetallesEgreso.this, MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    });
        });

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            egresoId = intent.getStringExtra("egresoId");
            tituloTextView.setText(intent.getStringExtra("titulo"));
            descripcionTextView.setText(intent.getStringExtra("descripcion"));
            montoTextView.setText(String.valueOf(intent.getDoubleExtra("monto", 0)));
            fechaTextView.setText(intent.getStringExtra("fecha"));
            horaTextView.setText(intent.getStringExtra("hora"));
        }

        editarButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(DetallesEgreso.this, EditarEgreso.class);
            editIntent.putExtra("egresoId", egresoId);
            editIntent.putExtra("titulo", tituloTextView.getText().toString());
            editIntent.putExtra("descripcion", descripcionTextView.getText().toString());
            editIntent.putExtra("monto", Double.parseDouble(montoTextView.getText().toString()));
            editIntent.putExtra("fecha", fechaTextView.getText().toString());
            editIntent.putExtra("hora", horaTextView.getText().toString());
            startActivity(editIntent);
        });

        borrarButton.setOnClickListener(v -> {
            db.collection("egresos").document(egresoId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Intent listIntent = new Intent(DetallesEgreso.this, ListarEgreso.class);
                        listIntent.putExtra("deleted", true);
                        startActivity(listIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(DetallesEgreso.this, "No se pudo eliminar el egreso", Toast.LENGTH_SHORT).show();
                    });
        });
    }}