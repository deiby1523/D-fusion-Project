package com.uts.proyecto;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tareas;
    private TaskActionListener listener;

    public TaskAdapter(List<Task> tareas, TaskActionListener listener) {
        this.tareas = tareas;
        this.listener = listener;
    }

    public void setTareas(List<Task> nuevasTareas) {
        this.tareas = nuevasTareas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tareas.get(position);
        holder.titulo.setText(task.getTitle());
        holder.fechaEntrega.setText(task.getDueDate());
        holder.curso.setText(task.getCourseName());
        int color = Color.parseColor(task.getCourseColor());

        int colorConAlfa = ColorUtils.setAlphaComponent(color, 255); // Alfa de 0-255 (255 = opaco, 0 = totalmente transparente)

        int colorOscurecido = ColorHelper.reducirBrillo(colorConAlfa, 0.80f); // Reduce brillo al 85%


        holder.curso.setTextColor(colorOscurecido);

        // Animación al mostrar el item
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_anim);
        holder.itemView.startAnimation(animation);

        holder.optionsBtn.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.menu_nota_item);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_edit) {
                    listener.onEdit(task);
                    return true;
                } else if (item.getItemId() == R.id.menu_delete) {
                    listener.onDelete(task);
                    return true;
                }
                return false;
            });
            popup.show();
        });

        holder.itemView.setOnClickListener(v -> {
            ViewTaskDialogFragment dialog = ViewTaskDialogFragment.newInstance(task);
            dialog.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), "view_task");
        });
    }

    @Override
    public int getItemCount() {
        return tareas != null ? tareas.size() : 0;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, fechaEntrega, curso;
        ImageButton optionsBtn;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textTaskTitle);
            fechaEntrega = itemView.findViewById(R.id.textDueDate);
            curso = itemView.findViewById(R.id.textTaskCourse);
            optionsBtn = itemView.findViewById(R.id.optionsButtonTask);
        }
    }

    public interface TaskActionListener {
        void onEdit(Task tarea);
        void onDelete(Task tarea);
    }
}
