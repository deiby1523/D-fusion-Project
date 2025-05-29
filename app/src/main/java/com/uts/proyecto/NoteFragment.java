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

public class NoteFragment extends Fragment {

    FirebaseUser user;
    FloatingActionButton noteFab;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    NoteAdapter.NoteActionListener listener;
    List<Note> listaNotas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        noteAdapter = new NoteAdapter(listaNotas, new NoteAdapter.NoteActionListener() {


            @Override
            public void onEdit(Note nota) {
                // Puedes reutilizar el UploadNoteDialogFragment para edición
                UploadNoteDialogFragment dialog = new UploadNoteDialogFragment(nota); // necesitas agregar constructor con nota
                dialog.setOnNoteSavedListener(() -> {
                    cargarNotasDesdeFirebase();
                    Toast.makeText(requireContext(), "Nota actualizada", Toast.LENGTH_SHORT).show();
                });
                dialog.show(getChildFragmentManager(), "EditNoteDialog");

            }

            @Override
            public void onDelete(Note nota) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("notes")
                            .child(user.getUid())
                            .child(nota.getId()); // asegúrate que cada nota tenga su ID

                    ref.removeValue().addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Nota eliminada", Toast.LENGTH_SHORT).show();
                    });
                    cargarNotasDesdeFirebase();
                }
            }
        });


        recyclerView.setAdapter(noteAdapter);


        noteFab = view.findViewById(R.id.NoteFab);
        noteFab.setOnClickListener(v -> {
            UploadNoteDialogFragment dialog = new UploadNoteDialogFragment();
            dialog.setOnNoteSavedListener(() -> {
                Toast.makeText(requireContext(), "Nota guardada correctamente", Toast.LENGTH_SHORT).show();
                cargarNotasDesdeFirebase();
            });
            dialog.show(getChildFragmentManager(), "UploadNoteDialog");
        });
        cargarNotasDesdeFirebase();

        return view;
    }

    private void cargarNotasDesdeFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notes").child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaNotas.clear();
                for (DataSnapshot notaSnap : snapshot.getChildren()) {
                    Note nota = notaSnap.getValue(Note.class);
                    if (nota != null) {
                        listaNotas.add(nota);
                    }
                }
                noteAdapter.setNotas(listaNotas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar notas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}