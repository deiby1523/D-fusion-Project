package com.uts.proyecto;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UploadTaskDialogFragment extends DialogFragment {

    private EditText textTaskTitle, textTaskDueDate, textTaskDescription;
    private Spinner spinnerCursos;
    private Button saveButton, cancelButton;

    private FirebaseUser user;
    private Task tareaParaEditar;
    private OnTaskSavedListener taskSavedListener;

    private List<Course> listaCursos = new ArrayList<>();
    private List<String> nombresCursos = new ArrayList<>();

    public UploadTaskDialogFragment() {}

    public UploadTaskDialogFragment(Task tarea) {
        this.tareaParaEditar = tarea;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_upload_task, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.card_dialog);
        }

        view.setBackground(null);

        user = FirebaseAuth.getInstance().getCurrentUser();

        textTaskTitle = view.findViewById(R.id.textTaskTitle);
        textTaskDueDate = view.findViewById(R.id.textDueDate);
        textTaskDescription = view.findViewById(R.id.textTaskDescription);
        spinnerCursos = view.findViewById(R.id.spinnerCourses);
        saveButton = view.findViewById(R.id.saveTaskButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        textTaskDueDate.setFocusable(false); // Evita que aparezca el teclado
        textTaskDueDate.setOnClickListener(v -> mostrarDatePicker());

        cargarCursos();

        if (tareaParaEditar != null) {
            textTaskTitle.setText(tareaParaEditar.getTitle());
            textTaskDueDate.setText(tareaParaEditar.getDueDate());
            textTaskDescription.setText(tareaParaEditar.getDescription());
        }

        saveButton.setOnClickListener(v -> {
            String titulo = textTaskTitle.getText().toString().trim();
            String fecha = textTaskDueDate.getText().toString().trim();
            String descripcion = textTaskDescription.getText().toString().trim();
            int cursoPos = spinnerCursos.getSelectedItemPosition();

            if (titulo.isEmpty()) {
                mostrarError(v, "El título de la tarea es obligatorio");
                return;
            }

            if (fecha.isEmpty()) {
                mostrarError(v, "La fecha de entrega es obligatoria");
                return;
            }

            if (cursoPos == -1 || cursoPos >= listaCursos.size()) {
                mostrarError(v, "Selecciona un curso válido");
                return;
            }

            Course cursoSeleccionado = listaCursos.get(cursoPos);

            if (user != null) {
                String uid = user.getUid();
                DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("tasks").child(uid);
                String taskId = tareaParaEditar != null ? tareaParaEditar.getTaskId() : taskRef.push().getKey();
                String courseId = cursoSeleccionado.getId();
                String courseName = cursoSeleccionado.getName();
                String courseColor = cursoSeleccionado.getColor();

                Task tarea = new Task(
                        taskId,
                        courseId,
                        courseName,
                        courseColor,
                        titulo,
                        descripcion,
                        fecha,
                        false
                );

                taskRef.child(taskId).setValue(tarea).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (taskSavedListener != null) taskSavedListener.onTaskSaved();
                        dismiss();
                    } else {
                        mostrarError(v, "Error al guardar la tarea");
                    }
                });
            } else {
                mostrarError(v, "Usuario no autenticado");
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private void mostrarError(View v, String mensaje) {
        Snackbar.make(v, mensaje, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getResources().getColor(R.color.danger))
                .setTextColor(getResources().getColor(R.color.black))
                .show();
    }

    private void cargarCursos() {
        if (user == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("courses")
                .child(user.getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCursos.clear();
                nombresCursos.clear();
                for (DataSnapshot cursoSnap : snapshot.getChildren()) {
                    Course curso = cursoSnap.getValue(Course.class);
                    if (curso != null) {
                        listaCursos.add(curso);
                        nombresCursos.add(curso.getName());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, nombresCursos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCursos.setAdapter(adapter);

                if (tareaParaEditar != null) {
                    for (int i = 0; i < listaCursos.size(); i++) {
                        if (listaCursos.get(i).getId().equals(tareaParaEditar.getCourseId())) {
                            spinnerCursos.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(requireView(), "Error al cargar cursos", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setOnTaskSavedListener(OnTaskSavedListener listener) {
        this.taskSavedListener = listener;
    }

    public interface OnTaskSavedListener {
        void onTaskSaved();
    }

    private void mostrarDatePicker() {
        final Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int day = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, yearSelected, monthSelected, daySelected) -> {
                    // Ajusta el formato como desees: dd/MM/yyyy
                    String fechaSeleccionada = String.format("%02d/%02d/%04d", daySelected, monthSelected + 1, yearSelected);
                    textTaskDueDate.setText(fechaSeleccionada);
                }, year, month, day);

        datePickerDialog.show();
    }

}
