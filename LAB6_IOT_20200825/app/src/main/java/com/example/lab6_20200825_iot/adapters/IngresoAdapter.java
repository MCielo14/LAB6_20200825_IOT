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
import com.example.lab6_20200825_iot.activities.DetallesIngreso;
import com.example.lab6_20200825_iot.data.Ingreso;
import java.util.List;

public class IngresoAdapter extends RecyclerView.Adapter<IngresoAdapter.ViewHolder> {

    private Context context;
    private List<Ingreso> ingresoList;

    public IngresoAdapter(Context context, List<Ingreso> ingresoList) {
        this.context = context;
        this.ingresoList = ingresoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingreso_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingreso ingreso = ingresoList.get(position);
        if (ingreso != null) {
            Log.d("IngresoAdapter", "ingresoId: " + ingreso.getId());
            holder.tituloTextView.setText(ingreso.getTitulo());
            holder.montoTextView.setText(String.valueOf(ingreso.getMonto()));
            holder.fechaTextView.setText(ingreso.getFecha());
            holder.horaTextView.setText(ingreso.getHora());

            holder.detallesButton.setOnClickListener(v -> {
                Log.d("IngresoAdapter", "Detalles ingresoId: " + ingreso.getId());
                Intent intent = new Intent(context, DetallesIngreso.class);
                intent.putExtra("ingresoId", ingreso.getId());
                intent.putExtra("titulo", ingreso.getTitulo());
                intent.putExtra("descripcion", ingreso.getDescripcion());
                intent.putExtra("monto", ingreso.getMonto());
                intent.putExtra("fecha", ingreso.getFecha());
                intent.putExtra("hora", ingreso.getHora());
                context.startActivity(intent);
            });
        } else {
            Log.e("IngresoAdapter", "Ingreso nulo: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return ingresoList.size();
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
