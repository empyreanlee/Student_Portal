package com.example.lecturer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<String> courses;

    public CourseAdapter(List<String> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        String course = courses.get(position);
        holder.bind(course, position);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void updateCourses(List<String> newCourses) {
        courses.clear();
        courses.addAll(newCourses);
        notifyDataSetChanged();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView[] courseTextViews;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTextViews = new TextView[]{
                    itemView.findViewById(R.id.textViewCourse1),
                    itemView.findViewById(R.id.textViewCourse2),
                    itemView.findViewById(R.id.textViewCourse3),
                    itemView.findViewById(R.id.textViewCourse4),
                    itemView.findViewById(R.id.textViewCourse5)
            };
        }

        public void bind(String course, int position) {
            courseTextViews[position].setText(course);
            courseTextViews[position].setVisibility(View.VISIBLE);
        }
    }
}
