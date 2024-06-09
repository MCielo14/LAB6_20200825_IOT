package com.example.lab6_20200825_iot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_20200825_iot.R;
import com.example.lab6_20200825_iot.adapters.EgresoAdapter;
import com.example.lab6_20200825_iot.adapters.IngresoAdapter;
import com.example.lab6_20200825_iot.data.Egreso;
import com.example.lab6_20200825_iot.data.Ingreso;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListarEgreso extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EgresoAdapter egresoAdapter;
    private List<Egreso> egresoList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_egresos);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        egresoList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_egresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        egresoAdapter = new EgresoAdapter(this, egresoList);
        recyclerView.setAdapter(egresoAdapter);

        cargarEgresos();

        Button cerrarSesionButton = findViewById(R.id.cerrarsession);
        Button nuevoIngreso = findViewById(R.id.crearNuevoIngreso);
        Button ingreso =findViewById(R.id.ingresosbutton);
        Button egreso =findViewById(R.id.egresosbutton);
        Button resumen = findViewById(R.id.resumenbutton);
        ingreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(ListarEgreso.this, ListaIngreso.class);
            startActivity(intent1);
        });
        egreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(ListarEgreso.this, ListarEgreso.class);
            startActivity(intent1);
        });
        resumen.setOnClickListener(v -> {
            Intent intent1 = new Intent(ListarEgreso.this, Resumen.class);
            startActivity(intent1);
        });

        nuevoIngreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(ListarEgreso.this, CrearEgreso.class);
            startActivity(intent1);
        });
        cerrarSesionButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(ListarEgreso.this)
                    .addOnCompleteListener(task -> {
                        Credentials.getClient(ListarEgreso.this).disableAutoSignIn();
                        Intent loginIntent = new Intent(ListarEgreso.this, MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    });
        });


        // Verifica si un ingreso fue eliminado
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("deleted", false)) {
            cargarEgresos();
        }
    }

    private void cargarEgresos() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("egresos")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        egresoList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Egreso egreso = document.toObject(Egreso.class);
                            egreso.setId(document.getId()); // Asigna el ID del documento
                            Log.d("ListaEgreso", "Documento cargado: " + document.getId());
                            egresoList.add(egreso);
                        }
                        egresoAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("ListaEgreso", "Error al cargar documentos", task.getException());
                    }
                });
    }

}