package com.example.lecturer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentFail extends Fragment implements MyPagerAdapter1.TitledFragment {
    private CommonAdapter commonAdapter;
    private Button failButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentfail, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPassList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Student> failItemList = new ArrayList<>();
        commonAdapter = new CommonAdapter(failItemList);
        recyclerView.setAdapter(commonAdapter);

        failButton = view.findViewById(R.id.fragment2Button);
        failButton.setOnClickListener(v -> generateFailList());

        return view;
    }

    private void generateFailList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentSemester = "semester";

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Student> failList = new ArrayList<>();

                        for (QueryDocumentSnapshot studentDoc : task.getResult()) {
                            String regNo = studentDoc.getString("regNo");
                            String name = studentDoc.getString("name");
                            List<String> failedCourses = new ArrayList<>();

                            db.collection("users")
                                    .document(studentDoc.getId())
                                    .collection("semesters")
                                    .document(currentSemester)
                                    .collection("marks")
                                    .get()
                                    .addOnCompleteListener(semesterTask -> {
                                        if (semesterTask.isSuccessful()) {
                                            for (QueryDocumentSnapshot courseDoc : semesterTask.getResult()) {
                                                if (courseDoc.contains("total") && courseDoc.getLong("total") != null) {
                                                    int totalMarks = courseDoc.getLong("total").intValue();

                                                    if (totalMarks < 40) {
                                                        failedCourses.add(courseDoc.getId());
                                                    }
                                                }
                                            }

                                            if (!failedCourses.isEmpty()) {
                                                failList.add(new Student(regNo, name, failedCourses));
                                            }

                                            updateRecyclerView(failList);
                                        } else {
                                            // Handle the error
                                            // Note: You may want to add proper error handling
                                        }
                                    });
                        }
                    } else {
                        // Handle the error
                        // Note: You may want to add proper error handling
                    }
                });
    }

    private void updateRecyclerView(List<Student> failList) {
        getActivity().runOnUiThread(() -> {
            commonAdapter.clearItems();
            commonAdapter.setItems(failList);
            commonAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public CharSequence getTitle() {
        return "Fail List";
    }
}
