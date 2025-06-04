package com.uts.proyecto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    FirebaseUser user;
    FloatingActionButton taskFab;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    List<Task> listaTareas = new ArrayList<>();

    TaskAdapter.TaskActionListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taskAdapter = new TaskAdapter(listaTareas, new TaskAdapter.TaskActionListener() {
            @Override
            public void onEdit(Task tarea) {
                UploadTaskDialogFragment dialog = new UploadTaskDialogFragment(tarea); // Constructor con tarea
                dialog.setOnTaskSavedListener(() -> {
                    cargarTareasDesdeFirebase();
                    Toast.makeText(requireContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show();
                });
                dialog.show(getChildFragmentManager(), "EditTaskDialog");
            }

            @Override
            public void onDelete(Task tarea) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("tasks")
                            .child(user.getUid())
                            .child(tarea.getTaskId());

                    ref.removeValue().addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show();
                        cargarTareasDesdeFirebase();
                    });
                }
            }
        });

        recyclerView.setAdapter(taskAdapter);

        taskFab = view.findViewById(R.id.TaskFab);
        taskFab.setOnClickListener(v -> {
            UploadTaskDialogFragment dialog = new UploadTaskDialogFragment();
            dialog.setOnTaskSavedListener(() -> {
                Toast.makeText(requireContext(), "Tarea guardada correctamente", Toast.LENGTH_SHORT).show();
                cargarTareasDesdeFirebase();
            });
            dialog.show(getChildFragmentManager(), "UploadTaskDialog");
        });

        cargarTareasDesdeFirebase();

        return view;
    }

    private void cargarTareasDesdeFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("tasks")
                .child(user.getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaTareas.clear();
                for (DataSnapshot tareaSnap : snapshot.getChildren()) {
                    Task tarea = tareaSnap.getValue(Task.class);
                    if (tarea != null) {
                        listaTareas.add(tarea);
                    }
                }
                taskAdapter.setTareas(listaTareas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar tareas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
