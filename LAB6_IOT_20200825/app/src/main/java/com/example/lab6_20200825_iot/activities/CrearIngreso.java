package com.example.lab6_20200825_iot.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.lab6_20200825_iot.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CrearIngreso extends AppCompatActivity {

    private EditText tituloEditText, descripcionEditText, montoEditText;
    private Button fechaButton, horaButton, guardarButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String fechaSeleccionada, horaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ingreso);

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
            Intent intent1 = new Intent(CrearIngreso.this, ListaIngreso.class);
            startActivity(intent1);
        });
        egreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(CrearIngreso.this, ListarEgreso.class);
            startActivity(intent1);
        });
        resumen.setOnClickListener(v -> {
            Intent intent1 = new Intent(CrearIngreso.this, Resumen.class);
            startActivity(intent1);
        });
        cerrarSesionButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(CrearIngreso.this)
                    .addOnCompleteListener(task -> {
                        Credentials.getClient(CrearIngreso.this).disableAutoSignIn();
                        Intent loginIntent = new Intent(CrearIngreso.this, MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    });
        });
    }
    // Se crea el metodo seleccionarFecha con ayuda de CHATGPT
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
    // Se crea el metodo seleccionar hora con ayuda de CHATGPT

    private void seleccionarHora() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CrearIngreso.this, new TimePickerDialog.OnTimeSetListener() {
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
        // Se eliminan los espacios en blanco para la desripcion y no tiene validacion para que soporte que la descripcion pueda estar vacia
        String descripcion = descripcionEditText.getText().toString().trim();
        String montoStr = montoEditText.getText().toString().trim();
        String fecha = fechaSeleccionada;
        String hora = horaSeleccionada;
        String userId = auth.getCurrentUser().getUid();

     // Se valia que el monto no este vacío
        if (montoStr.isEmpty()) {
            Toast.makeText(this, "El monto no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }
    // Se valida que el monto se un número decimal
        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El monto debe ser un número decimal válido", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> ingreso = new HashMap<>();
        ingreso.put("titulo", titulo);
        ingreso.put("descripcion", descripcion);
        ingreso.put("monto", monto);
        ingreso.put("fecha", fecha);
        ingreso.put("hora", hora);
        ingreso.put("userId", userId);

        db.collection("ingresos").add(ingreso)
                .addOnSuccessListener(documentReference -> {
                    Intent intent = new Intent(CrearIngreso.this, ListaIngreso.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar el ingreso", Toast.LENGTH_SHORT).show();
                });
    }

}
