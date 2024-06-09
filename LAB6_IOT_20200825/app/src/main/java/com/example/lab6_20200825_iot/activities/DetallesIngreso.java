package com.example.lab6_20200825_iot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab6_20200825_iot.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetallesIngreso extends AppCompatActivity {

    private TextView tituloTextView, descripcionTextView, montoTextView, fechaTextView, horaTextView;
    private Button editarButton, borrarButton;
    private FirebaseFirestore db;
    private String ingresoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ingreso);

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
            Intent intent1 = new Intent(DetallesIngreso.this, ListaIngreso.class);
            startActivity(intent1);
        });
        egreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetallesIngreso.this, ListarEgreso.class);
            startActivity(intent1);
        });
        resumen.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetallesIngreso.this, Resumen.class);
            startActivity(intent1);
        });
        cerrarSesionButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(DetallesIngreso.this)
                    .addOnCompleteListener(task -> {
                        Credentials.getClient(DetallesIngreso.this).disableAutoSignIn();
                        Intent loginIntent = new Intent(DetallesIngreso.this, MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    });
        });

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            ingresoId = intent.getStringExtra("ingresoId");
            tituloTextView.setText(intent.getStringExtra("titulo"));
            descripcionTextView.setText(intent.getStringExtra("descripcion"));
            montoTextView.setText(String.valueOf(intent.getDoubleExtra("monto", 0)));
            fechaTextView.setText(intent.getStringExtra("fecha"));
            horaTextView.setText(intent.getStringExtra("hora"));
        }

        editarButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(DetallesIngreso.this, EditarIngreso.class);
            editIntent.putExtra("ingresoId", ingresoId);
            editIntent.putExtra("titulo", tituloTextView.getText().toString());
            editIntent.putExtra("descripcion", descripcionTextView.getText().toString());
            editIntent.putExtra("monto", Double.parseDouble(montoTextView.getText().toString()));
            editIntent.putExtra("fecha", fechaTextView.getText().toString());
            editIntent.putExtra("hora", horaTextView.getText().toString());
            startActivity(editIntent);
        });

        borrarButton.setOnClickListener(v -> {
            db.collection("ingresos").document(ingresoId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Intent listIntent = new Intent(DetallesIngreso.this, ListaIngreso.class);
                        listIntent.putExtra("deleted", true);
                        startActivity(listIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(DetallesIngreso.this, "No se pudo eliminar el ingreso", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
