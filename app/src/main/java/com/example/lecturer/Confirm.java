package com.example.lecturer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Confirm extends AppCompatActivity {

    private Button buttonClick;
    private EditText editTextName;
    private EditText editTextRegNo;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerViewCourses;
    private CourseAdapter courseAdapter;
    private ImageView iconButton1;
    private ImageView iconButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonClick = findViewById(R.id.buttonClick);
        editTextName = findViewById(R.id.editTextName);
        editTextRegNo = findViewById(R.id.editTextRegNo);
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);

        iconButton1 = findViewById(R.id.iconButton1);
        iconButton1.setSelected(true);

        iconButton1.setOnClickListener(v -> {
            Intent intent = new Intent(Confirm.this, Confirm.class);
            startActivity(intent);
        });
        iconButton2 = findViewById(R.id.iconButton2);
        iconButton2.setOnClickListener(v -> {
            Intent intent = new Intent(Confirm.this, Confirm2.class);
            startActivity(intent);
        });

        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter(new ArrayList<>()); // Initialize with an empty list
        recyclerViewCourses.setAdapter(courseAdapter);

        buttonClick.setOnClickListener(v -> {
            String regNo = editTextRegNo.getText().toString();

            if (TextUtils.isEmpty(regNo)) {
                Toast.makeText(Confirm.this, "Please enter registration number", Toast.LENGTH_SHORT).show();
                return;
            }

            fetchStudentDetails(regNo);
        });
    }

    private void fetchStudentDetails(String regNo) {
        db.collection("users")
                .whereEqualTo("regNo", regNo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming there's only one document matching the regNo
                                String name = document.getString("name");
                                String regNo = document.getString("regNo");
                                Toast.makeText(Confirm.this, "Welcome, " + name  , Toast.LENGTH_SHORT).show();

                                // Fetch the registered courses
                                fetchRegisteredCourses(document.getId());
                            }
                        } else {
                            // Handle query failure
                            Toast.makeText(Confirm.this, "Error fetching student details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchRegisteredCourses(String userId) {
        db.collection("users")
                .document(userId)
                .collection("semesters")
                .document("semester")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot semesterDocument = task.getResult();
                            if (semesterDocument != null && semesterDocument.exists()) {
                                Log.d("Firestore", "Semester Document: " + semesterDocument.getData());
                                List<String> registeredCourses = (List<String>) semesterDocument.get("registeredCourses");
                                if (registeredCourses != null  && !registeredCourses.isEmpty()) {
                                    Log.d("Firestore", "Registered Courses: " + registeredCourses);
                                    // Do something with the registered courses
                                    Toast.makeText(Confirm.this, "Registered Courses: " + registeredCourses.toString(), Toast.LENGTH_LONG).show();
                                    courseAdapter.updateCourses(registeredCourses);

                                } else {
                                    Log.d("Firestore", "Registered Courses is null or empty");

                                    Toast.makeText(Confirm.this, "No registered courses found for this semester", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("Firestore", "Semester Document does not exist or is empty");
                                Toast.makeText(Confirm.this, "Semester document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Confirm.this, "Error fetching registered courses", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
