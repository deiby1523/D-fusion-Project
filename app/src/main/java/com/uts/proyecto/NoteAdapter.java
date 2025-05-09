package com.uts.proyecto;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notas;

    private NoteActionListener listener;

    public NoteAdapter(List<Note> notas, NoteActionListener listener) {
        this.notas = notas;
        this.listener = listener;
    }

    public NoteAdapter(List<Note> listaNotas) {
    }

    public void setNotas(List<Note> nuevasNotas) {
        this.notas = nuevasNotas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nota, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note nota = notas.get(position);
        holder.titulo.setText(nota.getTitle());
        holder.contenido.setText(nota.getContent());

        // Animación de entrada desde abajo (slide up)
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_anim);
        holder.itemView.startAnimation(animation);

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.menu_nota_item);
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_edit) {
                    listener.onEdit(nota);
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    listener.onDelete(nota);
                    return true;
                }
                return false;
            });
            popup.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notas != null ? notas.size() : 0;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, contenido;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tituloNota);
            contenido = itemView.findViewById(R.id.contenidoNota);
        }
    }

    public interface NoteActionListener {
        void onEdit(Note nota);
        void onDelete(Note nota);
    }
}