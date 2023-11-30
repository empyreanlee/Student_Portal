package com.example.lecturer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {

    private List<Student> itemList;

    public CommonAdapter(List<Student> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = itemList.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView[] itemTextViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextViews = new TextView[]{
                    itemView.findViewById(R.id.textView1),
                    itemView.findViewById(R.id.textView2),
                    itemView.findViewById(R.id.textView3),
                    itemView.findViewById(R.id.textView4),
                    itemView.findViewById(R.id.textView5)
            };
        }

        public void bind(Student student) {
            // Set relevant information from the Student object to TextViews
            itemTextViews[0].setText(student.getRegNo());
        }
    }
    public void clearItems() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public void setItems(List<Student> newItems) {
        itemList.clear();
        itemList.addAll(newItems);
        notifyDataSetChanged();
    }
}
