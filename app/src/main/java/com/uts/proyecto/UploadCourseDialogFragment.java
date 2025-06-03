package com.uts.proyecto;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UploadCourseDialogFragment extends DialogFragment {

    FirebaseUser user;
    List<ScheduleDay> scheduleDays = new ArrayList<>();
    ScheduleAdapter scheduleAdapter;
    private EditText textCourseName, textCourseCode, textClassroom, textProfessor, textProfessorContact;
    private Button saveButton;
    private Button cancelButton;
    private OnCourseSavedListener courseSavedListener;
    private String selectedColor = "#FFFFFF"; // valor por defecto

    private View colorRed;
    private View colorBlue;
    private View colorGreen;
    private View colorYellow;
    private View colorPink;
    private View colorBrown;
    private View colorPurple;
    private View colorCyan;
    private View colorOrange;
    private View colorGray;


    public UploadCourseDialogFragment() {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_upload_course, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.card_dialog);
        }
        view.setBackground(null);

        user = FirebaseAuth.getInstance().getCurrentUser();


        textCourseName = view.findViewById(R.id.textCourseName);
        textCourseCode = view.findViewById(R.id.textCourseCode);
        textClassroom = view.findViewById(R.id.textClassroom);
        textProfessor = view.findViewById(R.id.textProfessor);
        textProfessorContact = view.findViewById(R.id.textProfessorContact);
        saveButton = view.findViewById(R.id.saveCourseButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        // Color seleccionado
        colorRed = view.findViewById(R.id.colorRed);
        colorBlue = view.findViewById(R.id.colorBlue);
        colorGreen = view.findViewById(R.id.colorGreen);
        colorYellow = view.findViewById(R.id.colorYellow);
        colorPink = view.findViewById(R.id.colorPink);
        colorBrown = view.findViewById(R.id.colorBrown);
        colorPurple = view.findViewById(R.id.colorPurple);
        colorCyan = view.findViewById(R.id.colorCyan);
        colorOrange = view.findViewById(R.id.colorOrange);
        colorGray = view.findViewById(R.id.colorGray);

        colorRed.setOnClickListener(v -> {
            selectedColor = "#40FF0000";
            clearColorSelected();
            colorRed.setBackground(getResources().getDrawable(R.drawable.custom_edittext));

        });
        colorBlue.setOnClickListener(v -> {
            selectedColor = "#400073FF";
            clearColorSelected();
            colorBlue.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        });
        colorGreen.setOnClickListener(v -> {
            selectedColor = "#4059FF00";
            clearColorSelected();
            colorGreen.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        });
        colorYellow.setOnClickListener(v -> {
            selectedColor = "#40FFBF00";
            clearColorSelected();
            colorYellow.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        });
        colorPink.setOnClickListener(v -> {
            selectedColor = "#40FF00DD";
            clearColorSelected();
            colorPink.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        });
        colorBrown.setOnClickListener(v -> {
            selectedColor = "#406E4B25";
            clearColorSelected();
            colorBrown.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        });
        colorPurple.setOnClickListener(v -> {
            selectedColor = "#408000FF";
            clearColorSelected();
            colorPurple.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        });
        colorCyan.setOnClickListener(v -> {
            selectedColor = "#4000FF88";
            clearColorSelected();
            colorCyan.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        });
        colorOrange.setOnClickListener(v -> {
            selectedColor = "#40FF5900";
            clearColorSelected();
            colorOrange.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        });
        colorGray.setOnClickListener(v -> {
            selectedColor = "#40818181";
            clearColorSelected();
            colorGray.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        });

        Spinner spinner = view.findViewById(R.id.spinnerDay);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                Arrays.asList("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"));
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dayAdapter);

        // Time pickers
        TextView textStart = view.findViewById(R.id.textStartTime);
        TextView textEnd = view.findViewById(R.id.textEndTime);

        textStart.setOnClickListener(v -> showTimePicker(textStart));
        textEnd.setOnClickListener(v -> showTimePicker(textEnd));

        // Botón agregar horario
        view.findViewById(R.id.btnAgregarHorario).setOnClickListener(v -> {
            String dia = spinner.getSelectedItem().toString();
            String inicio = textStart.getText().toString();
            String fin = textEnd.getText().toString();

            if (!inicio.contains(":") || !fin.contains(":")) {
                Toast.makeText(view.getContext(), "Seleccione horas válidas", Toast.LENGTH_SHORT).show();
                return;
            }

            scheduleDays.add(new ScheduleDay(dia, inicio, fin));
            scheduleAdapter.notifyDataSetChanged();
        });

        // Configuración del RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHorarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        scheduleAdapter = new ScheduleAdapter(scheduleDays);
        recyclerView.setAdapter(scheduleAdapter);


        saveButton.setOnClickListener(v -> {
            String name = textCourseName.getText().toString().trim();
            String code = textCourseCode.getText().toString().trim();
            String classroom = textClassroom.getText().toString().trim();
            String professor = textProfessor.getText().toString().trim();
            String professorContact = textProfessorContact.getText().toString().trim();
            Schedule schedule = new Schedule(scheduleDays);
            String color = selectedColor;
            // ...

            View rootView = requireActivity().findViewById(android.R.id.content);
            if (name.isEmpty() || code.isEmpty()) {
                Snackbar.make(view.getRootView(), "El nombre y el código del curso son obligatorios", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.danger))
                        .setTextColor(getResources().getColor(R.color.black))
                        .show();
                return;
            }


            if (scheduleDays.isEmpty()) {
                Snackbar.make(view.getRootView(), "Debes agregar al menos un horario", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.danger))
                        .setTextColor(getResources().getColor(R.color.black))
                        .show();
                return;
            }

            if (user != null) {
                String uid = user.getUid();
                DatabaseReference coursesRef = FirebaseDatabase.getInstance()
                        .getReference("courses")
                        .child(uid);

                String courseId;
                courseId = coursesRef.push().getKey();

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("courses")
                        .child(user.getUid())
                        .child(courseId);

                Course courseData = new Course(courseId, name, code, classroom, professor, professorContact, schedule, color);

                ref.setValue(courseData)
                        .addOnFailureListener(e -> {
                            Snackbar.make(v, "Hubo un error al guardar la materia", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(getResources().getColor(R.color.danger))
                                    .setTextColor(getResources().getColor(R.color.black))
                                    .show();
                        });
                if (courseSavedListener != null) {
                    courseSavedListener.onCourseSaved();
                }
                dismiss();
            } else {
                Snackbar.make(v, "Usuario no Autenticado", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.danger))
                        .setTextColor(getResources().getColor(R.color.black))
                        .show();
            }
        });

        cancelButton.setOnClickListener(v -> {
            dismiss(); // Cierra el modal
        });

        return view;
    }

    public void setOnCourseSavedListener(OnCourseSavedListener listener) {
        this.courseSavedListener = listener;
    }

    private void showTimePicker(TextView targetView) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        new TimePickerDialog(getContext(), (view, hourOfDay, minute1) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
            targetView.setText(time);
        }, hour, minute, true).show();
    }

    public void clearColorSelected() {
        colorBlue.setBackground(getResources().getDrawable(R.color.course_blue));
        colorGreen.setBackground(getResources().getDrawable(R.color.course_green));
        colorYellow.setBackground(getResources().getDrawable(R.color.course_yellow));
        colorPink.setBackground(getResources().getDrawable(R.color.course_pink));
        colorBrown.setBackground(getResources().getDrawable(R.color.course_brown));
        colorPurple.setBackground(getResources().getDrawable(R.color.course_purple));
        colorCyan.setBackground(getResources().getDrawable(R.color.course_cyan));
        colorOrange.setBackground(getResources().getDrawable(R.color.course_orange));
        colorGray.setBackground(getResources().getDrawable(R.color.course_gray));
        colorRed.setBackground(getResources().getDrawable(R.color.course_red));
    }

    public interface OnCourseSavedListener {
        void onCourseSaved();
    }

}

