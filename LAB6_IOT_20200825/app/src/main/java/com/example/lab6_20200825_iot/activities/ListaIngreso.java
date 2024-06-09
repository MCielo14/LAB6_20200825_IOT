package com.example.lab6_20200825_iot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab6_20200825_iot.R;
import com.example.lab6_20200825_iot.adapters.IngresoAdapter;
import com.example.lab6_20200825_iot.data.Ingreso;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ListaIngreso extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IngresoAdapter ingresoAdapter;
    private List<Ingreso> ingresoList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_ingreso);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        ingresoList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_ingresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingresoAdapter = new IngresoAdapter(this, ingresoList);
        recyclerView.setAdapter(ingresoAdapter);

        cargarIngresos();

        Button cerrarSesionButton = findViewById(R.id.cerrarsession);
        Button nuevoIngreso = findViewById(R.id.crearNuevoIngreso);
        nuevoIngreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(ListaIngreso.this, CrearIngreso.class);
            startActivity(intent1);
        });
        cerrarSesionButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(ListaIngreso.this)
                    .addOnCompleteListener(task -> {
                        Credentials.getClient(ListaIngreso.this).disableAutoSignIn();
                        Intent loginIntent = new Intent(ListaIngreso.this, MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    });
        });


        // Verifica si un ingreso fue eliminado
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("deleted", false)) {
            cargarIngresos();
        }
    }

    private void cargarIngresos() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("ingresos")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ingresoList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ingreso ingreso = document.toObject(Ingreso.class);
                            ingreso.setId(document.getId()); // Asigna el ID del documento
                            Log.d("ListaIngreso", "Documento cargado: " + document.getId());
                            ingresoList.add(ingreso);
                        }
                        ingresoAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("ListaIngreso", "Error al cargar documentos", task.getException());
                    }
                });
    }

}
