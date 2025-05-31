package com.uts.proyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    List<ScheduleDay> lista;

    public ScheduleAdapter(List<ScheduleDay> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        ScheduleDay h = lista.get(position);
        holder.texto.setText(h.getDay() + " - " + h.getStartHour() + " a " + h.getEndHour());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView texto;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            texto = itemView.findViewById(R.id.textSchedule);
        }
    }
}
