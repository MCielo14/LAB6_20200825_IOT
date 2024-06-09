package com.example.lab6_20200825_iot.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab6_20200825_iot.R;
import com.example.lab6_20200825_iot.activities.DetallesEgreso;
import com.example.lab6_20200825_iot.activities.DetallesIngreso;
import com.example.lab6_20200825_iot.data.Egreso;
import java.util.List;

public class EgresoAdapter extends RecyclerView.Adapter<EgresoAdapter.ViewHolder> {

    private Context context;
    private List<Egreso> egresoList;

    public EgresoAdapter(Context context, List<Egreso> ingresoList) {
        this.context = context;
        this.egresoList = ingresoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.egreso_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Egreso egreso = egresoList.get(position);
        if (egreso != null) {
            Log.d("IngresoAdapter", "Binding data for egresoId: " + egreso.getId());
            holder.tituloTextView.setText(egreso.getTitulo());
            holder.montoTextView.setText(String.valueOf(egreso.getMonto()));
            holder.fechaTextView.setText(egreso.getFecha());
            holder.horaTextView.setText(egreso.getHora());

            holder.detallesButton.setOnClickListener(v -> {
                Log.d("EgresoAdapter", "Detalles button clicked for egresoId: " + egreso.getId());
                Intent intent = new Intent(context, DetallesEgreso.class);
                intent.putExtra("egresoId", egreso.getId());
                intent.putExtra("titulo", egreso.getTitulo());
                intent.putExtra("descripcion", egreso.getDescripcion());
                intent.putExtra("monto", egreso.getMonto());
                intent.putExtra("fecha", egreso.getFecha());
                intent.putExtra("hora", egreso.getHora());
                context.startActivity(intent);
            });
        } else {
            Log.e("EgresoAdapter", "Egreso is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return egresoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView, montoTextView, fechaTextView, horaTextView;
        Button detallesButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.titulodetalle);
            montoTextView = itemView.findViewById(R.id.montoDetalle);
            fechaTextView = itemView.findViewById(R.id.fechadetalles);
            horaTextView = itemView.findViewById(R.id.horadetalles);
            detallesButton = itemView.findViewById(R.id.detallesButton);
        }
    }
}
