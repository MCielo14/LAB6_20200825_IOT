package com.example.lab6_20200825_iot.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.lab6_20200825_iot.R;

import java.util.Calendar;
// Para que seleccione el mes se hizo con ayuda de ChatGPT
public class MonthYearPickerDialog extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year, month, calendar.get(Calendar.DAY_OF_MONTH));

        try {
            ((View) dialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android"))).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialog;
    }
}
