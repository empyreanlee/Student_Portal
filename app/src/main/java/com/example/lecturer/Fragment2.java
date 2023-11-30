package com.example.lecturer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;



public class Fragment2 extends Fragment implements MyPagerAdapter.TitledFragment {

    private Button buttonRegister, button;
    private EditText editTextName;
    private EditText editTextRegNo;
    private CheckBox checkboxAnimation, checkboxBlockchain, checkboxVR, checkboxCyberSecurity, checkboxMachine, checkboxSystem, checkboxCompilerDesign;
    private DatabaseReference studentsRef;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for Fragment2
        View view = inflater.inflate(R.layout.fragment2, container, false);



        FirebaseApp.initializeApp(requireContext()); // Use requireContext() instead of this

        mAuth = FirebaseAuth.getInstance();


        buttonRegister = view.findViewById(R.id.buttonRegister);
        editTextName = ((StudentReg) requireActivity()).getEditTextName();
        editTextRegNo = ((StudentReg) requireActivity()).getEditTextRegNo();


        checkboxAnimation = view.findViewById(R.id.checkboxAnimation);
        checkboxBlockchain = view.findViewById(R.id.checkboxBlockchain);
        checkboxVR = view.findViewById(R.id.checkboxVR);
        checkboxCyberSecurity = view.findViewById(R.id.checkboxCyberSecurity);
        checkboxMachine = view.findViewById(R.id.checkboxMachine);
        checkboxSystem = view.findViewById(R.id.checkboxSystem);
        checkboxCompilerDesign = view.findViewById(R.id.checkboxCompilerDesign);

        buttonRegister.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String regNo = editTextRegNo.getText().toString();

            ArrayList<String> selectedCourses = new ArrayList<>();

            if (checkboxAnimation.isChecked()) selectedCourses.add("Animation");
            if (checkboxBlockchain.isChecked()) selectedCourses.add("Blockchain");
            if (checkboxVR.isChecked()) selectedCourses.add("VR");
            if (checkboxCyberSecurity.isChecked()) selectedCourses.add("CyberSecurity");
            if (checkboxMachine.isChecked()) selectedCourses.add("Machine");
            if (checkboxSystem.isChecked()) selectedCourses.add("System");
            if (checkboxCompilerDesign.isChecked()) selectedCourses.add("CompilerDesign");

            if (selectedCourses.size() > 5) {
                Toast.makeText(getActivity(), "Please select at most 5 courses", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(regNo) || selectedCourses.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = mAuth.getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersCollection = db.collection("users");
            DocumentReference studentDocRef = usersCollection.document(userId);
            CollectionReference semestersCollection = studentDocRef.collection("semesters");

            Student student = new Student(name, regNo);

            // Set the student information
            studentDocRef.set(student)
                    .addOnSuccessListener(aVoid -> {
                        // Document successfully written
                        Toast.makeText(getActivity(), "Student information added for " + name, Toast.LENGTH_SHORT).show();


                        // Create a SemesterData object and push it to Firestore
                        SemesterData semesterData = new SemesterData(2, selectedCourses);

                        semestersCollection.document("Semester")
                                .set(semesterData)
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(getActivity(), "Registration successful for Semester 2. ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), Confirm.class);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure
                                    Toast.makeText(getActivity(), "Error registering for Semester 2", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(getActivity(), "Error adding student information", Toast.LENGTH_SHORT).show();
                    });
        });

        return view ;
    }




    @Override
    public CharSequence getTitle() {
        return "Semester 2";
    }
}
