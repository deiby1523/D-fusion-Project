package com.uts.proyecto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ViewCourseDialogFragment extends DialogFragment {

    private static final String ARG_COURSE = "course";

    private Course course;

    public static ViewCourseDialogFragment newInstance(Course course) {
        ViewCourseDialogFragment fragment = new ViewCourseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE, course); // Asegúrate de que `Course` implemente Serializable o Parcelable
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_view_course, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.card_dialog);
        }
        view.setBackground(null);

        if (getArguments() != null) {
            course = (Course) getArguments().getSerializable(ARG_COURSE);
        }

        if (course == null) dismiss();


        ((TextView) view.findViewById(R.id.viewCourseName)).setText(course.getName());
        ((TextView) view.findViewById(R.id.viewCourseCode)).setText(course.getCourseCode());
        ((TextView) view.findViewById(R.id.viewClassroom)).setText(course.getClassroom());
        ((TextView) view.findViewById(R.id.viewProfessor)).setText(course.getProfessor());
        ((TextView) view.findViewById(R.id.viewProfessorContact)).setText(course.getProfessorContact());

        // Mostrar horarios
        TextView scheduleView = view.findViewById(R.id.viewSchedule);
        StringBuilder scheduleText = new StringBuilder();
        for (ScheduleDay day : course.getSchedule().getDays()) {
            scheduleText.append("• ").append(day.getDay())
                    .append(" ").append(day.getStartHour())
                    .append(" - ").append(day.getEndHour())
                    .append("\n");
        }
        scheduleView.setText(scheduleText.toString());

        // Botón cerrar
        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());

        return view;
    }
}

