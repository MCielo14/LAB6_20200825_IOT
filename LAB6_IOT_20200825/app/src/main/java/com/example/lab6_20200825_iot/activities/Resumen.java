package com.example.lab6_20200825_iot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab6_20200825_iot.R;
import com.example.lab6_20200825_iot.data.Egreso;
import com.example.lab6_20200825_iot.data.Ingreso;
import com.firebase.ui.auth.AuthUI;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Resumen extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private Button buttonSelectMonth;
    private Button cerrarSessionButton;
    private FirebaseFirestore db;
    private String currentUserId;
    private TextView textViewMes;
// La configuración del resumen  usando pieChart y barChart se hizo con ayuda de ChatGPT
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);
        buttonSelectMonth = findViewById(R.id.buttonSelectMonth);
        cerrarSessionButton = findViewById(R.id.cerrarsession);
        textViewMes = findViewById(R.id.textViewMes);
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Button ingreso =findViewById(R.id.ingresosbutton);
        Button egreso =findViewById(R.id.egresosbutton);
        Button resumen = findViewById(R.id.resumenbutton);
        ingreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(Resumen.this, ListaIngreso.class);
            startActivity(intent1);
        });
        egreso.setOnClickListener(v -> {
            Intent intent1 = new Intent(Resumen.this, ListarEgreso.class);
            startActivity(intent1);
        });
        resumen.setOnClickListener(v -> {
            Intent intent1 = new Intent(Resumen.this, Resumen.class);
            startActivity(intent1);
        });


        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        textViewMes.setText("Mes: " + currentMonth + "/" + currentYear);
        updateCharts(currentMonth, currentYear);

        buttonSelectMonth.setOnClickListener(view -> {
            MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog();
            monthYearPickerDialog.setListener((datePicker, year, month, dayOfMonth) -> {
                int selectedMonth = month + 1;
                textViewMes.setText("Mes: " + selectedMonth + "/" + year);
                updateCharts(selectedMonth, year);
            });
            monthYearPickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
        });

        cerrarSessionButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(Resumen.this)
                    .addOnCompleteListener(task -> {
                        Intent loginIntent = new Intent(Resumen.this, MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    });
        });
    }

    private void updateCharts(int month, int year) {
        db.collection("ingresos")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final double[] totalIngresos = {0};
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ingreso ingreso = document.toObject(Ingreso.class);
                            totalIngresos[0] += ingreso.getMonto();
                        }

                        db.collection("egresos")
                                .whereEqualTo("userId", currentUserId)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        final double[] totalEgresos = {0};
                                        for (QueryDocumentSnapshot document : task2.getResult()) {
                                            Egreso egreso = document.toObject(Egreso.class);
                                            totalEgresos[0] += egreso.getMonto();
                                        }

                                        List<PieEntry> pieEntries = new ArrayList<>();
                                        pieEntries.add(new PieEntry((float) totalEgresos[0], "Egresos"));
                                        pieEntries.add(new PieEntry((float) totalIngresos[0], "Ingresos"));

                                        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Egresos vs Ingresos");
                                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                        PieData pieData = new PieData(pieDataSet);
                                        pieChart.setData(pieData);
                                        pieChart.invalidate();

                                        List<BarEntry> barEntries = new ArrayList<>();
                                        barEntries.add(new BarEntry(0, (float) totalIngresos[0]));
                                        barEntries.add(new BarEntry(1, (float) totalEgresos[0]));

                                        BarDataSet barDataSet = new BarDataSet(barEntries, "Ingresos/Egresos");
                                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                        BarData barData = new BarData(barDataSet);
                                        barChart.setData(barData);
                                        barChart.invalidate();
                                    } else {
                                        Toast.makeText(Resumen.this, "Error obteniendo datos de egresos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Resumen.this, "Error obteniendo datos de ingresos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
