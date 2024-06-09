package com.example.lab6_20200825_iot.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CrearEgreso extends AppCompatActivity {

    private EditText tituloEditText, descripcionEditText, montoEditText;
    private Button fechaButton, horaButton, guardarButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String fechaSeleccionada, horaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_egreso);

        tituloEditText = findViewById(R.id.titulonuevo);
        descripcionEditText = findViewById(R.id.descricionnuevo);
        montoEditText = findViewById(R.id.montoid);
        fechaButton = findViewById(R.id.button);
        horaButton = findViewById(R.id.buttonhoravencimientonuevo);
        guardarButton = findViewById(R.id.Guardarbutton);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fechaButton.setOnClickListener(v -> seleccionarFecha());
        horaButton.setOnClickListener(v -> seleccionarHora());
        guardarButton.setOnClickListener(v -> guardarIngreso());
        Button ingreso =findViewById(R.id.ingresosbutton);
        Button egreso =findViewById(R.id.egresosbutton);
        Button resumen = findViewById(R.id.resumenbutton);
        Button cerrarSesionButton = findViewById(R.id.cerrarsession);
        ingreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(CrearEgreso.this, ListaIngreso.class);
            startActivity(intent1);
        });
        egreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(CrearEgreso.this, ListarEgreso.class);
            startActivity(intent1);
        });
        resumen.setOnClickListener(v -> {
            Intent intent1 = new Intent(CrearEgreso.this, Resumen.class);
            startActivity(intent1);
        });
        cerrarSesionButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(CrearEgreso.this)
                    .addOnCompleteListener(task -> {
                        Credentials.getClient(CrearEgreso.this).disableAutoSignIn();
                        Intent loginIntent = new Intent(CrearEgreso.this, MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    });
        });
    }

    private void seleccionarFecha() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        fechaButton.setText(fechaSeleccionada);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void seleccionarHora() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CrearEgreso.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                horaSeleccionada = selectedHour + ":" + selectedMinute;
                horaButton.setText(horaSeleccionada);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void guardarIngreso() {
        String titulo = tituloEditText.getText().toString();
        String descripcion = descripcionEditText.getText().toString();
        double monto = Double.parseDouble(montoEditText.getText().toString());
        String fecha = fechaSeleccionada; // debes actualizar esto con la fecha seleccionada
        String hora = horaSeleccionada; // debes actualizar esto con la hora seleccionada
        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> egreso = new HashMap<>();
        egreso.put("titulo", titulo);
        egreso.put("descripcion", descripcion);
        egreso.put("monto", monto);
        egreso.put("fecha", fecha);
        egreso.put("hora", hora);
        egreso.put("userId", userId);

        db.collection("egresos").add(egreso)
                .addOnSuccessListener(documentReference -> {
                    // Redirigir a ListaIngreso
                    Intent intent = new Intent(CrearEgreso.this, ListarEgreso.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Maneja el error
                });
    }
}