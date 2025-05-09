package com.uts.proyecto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadNoteDialogFragment extends DialogFragment {

    FirebaseUser user;
    private EditText textNoteTitle, textNoteContent;
    private Button saveButton;
    private Button cancelButton;
    private Note notaParaEditar;

    private OnNoteSavedListener noteSavedListener;


    public UploadNoteDialogFragment(Note nota) {
        this.notaParaEditar = nota;
    }

    public UploadNoteDialogFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_upload_note, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.card_dialog);
        }

        view.setBackground(null);

        user = FirebaseAuth.getInstance().getCurrentUser();


        textNoteTitle = view.findViewById(R.id.textNoteTitle);
        textNoteContent = view.findViewById(R.id.textNoteContent);
        saveButton = view.findViewById(R.id.saveNoteButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        if (notaParaEditar != null) {
            textNoteTitle.setText(notaParaEditar.getTitle());
            textNoteContent.setText(notaParaEditar.getContent());
        }

        saveButton.setOnClickListener(v -> {
            String title = textNoteTitle.getText().toString().trim();
            String content = textNoteContent.getText().toString().trim();

            if (title.isEmpty()) {
                Snackbar.make(v, "El título de la nota es obligatorio", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.danger))
                        .setTextColor(getResources().getColor(R.color.black))
                        .show();
                return;
            }

            if (user != null) {
                String uid = user.getUid();
                DatabaseReference notesRef = FirebaseDatabase.getInstance()
                        .getReference("notes")
                        .child(uid);


                String noteId;
                if (notaParaEditar != null) noteId = notaParaEditar.getId();
                else noteId = notesRef.push().getKey();


                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("notes")
                        .child(user.getUid())
                        .child(noteId);

                Note noteData = new Note(noteId, title, content, System.currentTimeMillis());

                ref.setValue(noteData)
                        .addOnFailureListener(e -> {
                            Snackbar.make(v, "Hubo un error al guardar la nota", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(getResources().getColor(R.color.danger))
                                    .setTextColor(getResources().getColor(R.color.black))
                                    .show();
                        });
                if (noteSavedListener != null) {
                    noteSavedListener.onNoteSaved();
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

    public void setOnNoteSavedListener(OnNoteSavedListener listener) {
        this.noteSavedListener = listener;
    }

    public interface OnNoteSavedListener {
        void onNoteSaved();
    }
}

