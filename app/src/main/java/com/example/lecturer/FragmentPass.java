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

public class FragmentPass extends Fragment implements MyPagerAdapter1.TitledFragment {

    private Button passbutton;
    private CommonAdapter commonAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentpass, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPassList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        passbutton = view.findViewById(R.id.fragment1Button);
        List<Student> passItemList = new ArrayList<>();
        commonAdapter = new CommonAdapter(passItemList);
        recyclerView.setAdapter(commonAdapter);

        passbutton.setOnClickListener(v -> generatePassList());

        return view;
    }


    private void generatePassList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentSemester = "semester";

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Student> passList = new ArrayList<>();

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
                                            boolean isPass = true;

                                            for (QueryDocumentSnapshot courseDoc : semesterTask.getResult()) {
                                                if (courseDoc.contains("total") && courseDoc.getLong("total") != null) {
                                                    int totalMarks = courseDoc.getLong("total").intValue();

                                                    if (totalMarks < 40) {
                                                        isPass = false;
                                                        break;
                                                    }
                                                } else {
                                                    isPass = false;
                                                    break;
                                                }
                                            }
                                            if (isPass) {
                                                passList.add(new Student(regNo, name));
                                            }

                                            updateRecyclerView(passList);
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

    private void updateRecyclerView(List<Student> passList) {
        getActivity().runOnUiThread(() -> {
            commonAdapter.clearItems();
            commonAdapter.setItems(passList);
            commonAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public CharSequence getTitle() {
        return "Pass List";
    }
}
