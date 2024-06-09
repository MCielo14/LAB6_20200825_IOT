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
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab6_20200825_iot.R;
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
        String descripcion = descripcionEditText.getText().toString();
        double monto = Double.parseDouble(montoEditText.getText().toString());
        String fecha = fechaSeleccionada; // debes actualizar esto con la fecha seleccionada
        String hora = horaSeleccionada; // debes actualizar esto con la hora seleccionada
        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> ingreso = new HashMap<>();
        ingreso.put("titulo", titulo);
        ingreso.put("descripcion", descripcion);
        ingreso.put("monto", monto);
        ingreso.put("fecha", fecha);
        ingreso.put("hora", hora);
        ingreso.put("userId", userId);

        db.collection("ingresos").add(ingreso)
                .addOnSuccessListener(documentReference -> {
                    // Redirigir a ListaIngreso
                    Intent intent = new Intent(CrearIngreso.this, ListaIngreso.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Maneja el error
                });
    }
}
