package com.uts.proyecto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ViewNoteDialogFragment extends DialogFragment {

    private static final String ARG_NOTE = "note";

    private Note note;

    public static ViewNoteDialogFragment newInstance(Note note) {
        ViewNoteDialogFragment fragment = new ViewNoteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_view_note, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.card_dialog);
        }
        view.setBackground(null);

        if (getArguments() != null) {
            note = (Note) getArguments().getSerializable(ARG_NOTE);
        }

        if (note == null) dismiss();


        ((TextView) view.findViewById(R.id.viewNoteTitle)).setText(note.getTitle());
        ((TextView) view.findViewById(R.id.viewNoteContent)).setText(note.getContent());


        // Botón cerrar
        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());

        return view;
    }
}


