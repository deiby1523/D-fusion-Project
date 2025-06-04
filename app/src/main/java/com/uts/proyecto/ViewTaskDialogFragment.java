package com.uts.proyecto;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.DialogFragment;

public class ViewTaskDialogFragment extends DialogFragment {

    private static final String ARG_TASK = "task";

    private Task task;

    public static ViewTaskDialogFragment newInstance(Task task) {
        ViewTaskDialogFragment fragment = new ViewTaskDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_view_task, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.card_dialog);
        }
        view.setBackground(null);

        if (getArguments() != null) {
            task = (Task) getArguments().getSerializable(ARG_TASK);
        }

        if (task == null) dismiss();


        ((TextView) view.findViewById(R.id.viewTaskTitle)).setText(task.getTitle());
        ((TextView) view.findViewById(R.id.viewCourse)).setText(task.getCourseName());
        int color = Color.parseColor(task.getCourseColor());

        int colorConAlfa = ColorUtils.setAlphaComponent(color, 255); // Alfa de 0-255 (255 = opaco, 0 = totalmente transparente)

        int colorOscurecido = ColorHelper.reducirBrillo(colorConAlfa, 0.80f); // Reduce brillo al 85%

        ((TextView) view.findViewById(R.id.viewCourse)).setTextColor(colorOscurecido);

        ((TextView) view.findViewById(R.id.viewDueDate)).setText(task.getDueDate());
        ((TextView) view.findViewById(R.id.viewTaskDescription)).setText(task.getDescription());

        // Botón cerrar
        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());

        return view;
    }
}


