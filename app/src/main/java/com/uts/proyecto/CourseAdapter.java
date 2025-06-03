package com.uts.proyecto;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courses;
    private CourseActionListener listener;

    public CourseAdapter(List<Course> courses, CourseActionListener listener) {
        this.courses = courses;
        this.listener = listener;
    }

    public void setCourses(List<Course> newCourses) {
        this.courses = newCourses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.courseCard.setBackgroundColor(Color.parseColor(course.getColor()));
        holder.nombreCourse.setText(course.getName());
        holder.codigoCurso.setText(String.format("Grupo: %s", course.getCourseCode()));
        holder.salon.setText(String.format("Salon: %s", course.getClassroom()));

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_anim);
        holder.itemView.startAnimation(animation);

        holder.optionsBtn.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.menu_course_item);
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_delete_course) {
                    listener.onDelete(course);
                    return true;
                }
                return false;
            });
            popup.show();
        });

        holder.itemView.setOnClickListener(v -> {
            ViewCourseDialogFragment dialog = ViewCourseDialogFragment.newInstance(course);
            dialog.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), "view_course");
        });
    }

    @Override
    public int getItemCount() {
        return courses != null ? courses.size() : 0;
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView nombreCourse, codigoCurso, salon;
        ImageButton optionsBtn;

        LinearLayout courseCard;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreCourse = itemView.findViewById(R.id.tvNombreCourse);
            codigoCurso = itemView.findViewById(R.id.tvCodigoCurso);
            salon = itemView.findViewById(R.id.tvSalon);
            optionsBtn = itemView.findViewById(R.id.btnOpcionesCourse);
            courseCard = itemView.findViewById(R.id.courseCard);
        }
    }

    public interface CourseActionListener {
        void onEdit(Course course);
        void onDelete(Course course);
    }
}

