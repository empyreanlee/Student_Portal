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

public class FragmentMiss extends Fragment implements MyPagerAdapter1.TitledFragment {
    private CommonAdapter commonAdapter;
    private Button buttonmissing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the common layout for the fragment
        View view = inflater.inflate(R.layout.fragmentmissing, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPassList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Student> missingItemList = new ArrayList<>();
        commonAdapter = new CommonAdapter(missingItemList);
        recyclerView.setAdapter(commonAdapter);

        buttonmissing = view.findViewById(R.id.buttonMissing);
        buttonmissing.setOnClickListener(v -> generateMissingList());

        return view;
    }
    private void generateMissingList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentSemester = "semester";

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Student> missingList = new ArrayList<>();

                        for (QueryDocumentSnapshot studentDoc : task.getResult()) {
                            String regNo = studentDoc.getString("regNo");
                            String name = studentDoc.getString("name");

                            db.collection("users")
                                    .document(studentDoc.getId())
                                    .collection("semesters")
                                    .document(currentSemester)
                                    .collection("marks")
                                    .get()
                                    .addOnCompleteListener(semesterTask -> {
                                        if (semesterTask.isSuccessful()) {
                                            boolean hasMissingMarks = false;

                                            for (QueryDocumentSnapshot courseDoc : semesterTask.getResult()) {
                                                // Check if any course has missing marks (assignment, cats, or exam)
                                                if (!courseDoc.contains("assignment1") || courseDoc.getLong("assignment1") == null
                                                        || !courseDoc.contains("assignment2") || courseDoc.getLong("assignment2") == null
                                                        || !courseDoc.contains("cat1") || courseDoc.getLong("cat1") == null
                                                        || !courseDoc.contains("cat2") || courseDoc.getLong("cat2") == null
                                                        || !courseDoc.contains("exam") || courseDoc.getLong("exam") == null) {
                                                    hasMissingMarks = true;
                                                    break;
                                                }
                                            }

                                            if (hasMissingMarks) {
                                                missingList.add(new Student(regNo, name));
                                            }

                                            updateRecyclerView(missingList);
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
        private void updateRecyclerView(List<Student> missingList) {
            getActivity().runOnUiThread(() -> {
                commonAdapter.clearItems();
                commonAdapter.setItems(missingList);
                commonAdapter.notifyDataSetChanged();
            });
        }

    @Override
    public CharSequence getTitle() {
        return "Missing";
    }
}
