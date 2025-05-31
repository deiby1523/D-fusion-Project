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

public class CourseFragment extends Fragment {

    FirebaseUser user;
    FloatingActionButton courseFab;
    RecyclerView recyclerView;
    CourseAdapter courseAdapter;
    CourseAdapter.CourseActionListener listener;
    List<Course> listaCourses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseAdapter = new CourseAdapter(listaCourses, new CourseAdapter.CourseActionListener() {


            @Override
            public void onEdit(Course nota) {
                // Puedes reutilizar el UploadCourseDialogFragment para edición
//                UploadCourseDialogFragment dialog = new UploadCourseDialogFragment(nota); // necesitas agregar constructor con nota
//                dialog.setOnCourseSavedListener(() -> {
//                    cargarCoursesDesdeFirebase();
//                    Toast.makeText(requireContext(), "Nota actualizada", Toast.LENGTH_SHORT).show();
//                });
//                dialog.show(getChildFragmentManager(), "EditCourseDialog");

            }

            @Override
            public void onDelete(Course course) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("courses")
                            .child(user.getUid())
                            .child(course.getId());
                    ref.removeValue().addOnSuccessListener(aVoid -> {
//                        Toast.makeText(getContext(), "Materia eliminada", Toast.LENGTH_SHORT).show();
                    });
                    cargarCoursesDesdeFirebase();
                }
            }
        });

        recyclerView.setAdapter(courseAdapter);

        courseFab = view.findViewById(R.id.courseFab);
        courseFab.setOnClickListener(v -> {
            UploadCourseDialogFragment dialog = new UploadCourseDialogFragment();
            dialog.setOnCourseSavedListener(() -> {
                Toast.makeText(requireContext(), "Materia guardada correctamente", Toast.LENGTH_SHORT).show();
                cargarCoursesDesdeFirebase();
            });
            dialog.show(getChildFragmentManager(), "UploadCourseDialog");
        });
        cargarCoursesDesdeFirebase();

        return view;
    }

    private void cargarCoursesDesdeFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("courses").child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCourses.clear();
                for (DataSnapshot coursesnap : snapshot.getChildren()) {
                    Course course = coursesnap.getValue(Course.class);
                    if (course != null) {
                        listaCourses.add(course);
                    }
                }
                courseAdapter.setCourses(listaCourses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar courses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}