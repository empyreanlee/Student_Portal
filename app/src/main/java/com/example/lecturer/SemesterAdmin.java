package com.example.lecturer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SemesterAdmin extends AppCompatActivity {

    private EditText editTextRegNo, editTextAssignment1, editTextAssignment2, cat1, cat2, exam, editTextTotal;
    private EditText course;
    private Button buttonSubmit, button;

    private FirebaseFirestore db;
    private ImageView iconButton1;
    private ImageView iconButton2;
    private TextView textViewTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semester1);

        db = FirebaseFirestore.getInstance();

        course = findViewById(R.id.course);
        editTextRegNo = findViewById(R.id.editTextRegNo);
        editTextAssignment1 = findViewById(R.id.editTextAssignment1);
        editTextAssignment2 = findViewById(R.id.editTextAssignment2);
        cat1 = findViewById(R.id.cat1);
        cat2 = findViewById(R.id.cat2);
        exam = findViewById(R.id.exam);
        textViewTotal = findViewById(R.id.textViewTotal);
        button = findViewById(R.id.buttonGenerateTotal);
        editTextTotal = findViewById(R.id.editTextTotal);

        button.setOnClickListener(v -> {
            int assignment1 = Integer.parseInt(editTextAssignment1.getText().toString());
            int assignment2 = Integer.parseInt(editTextAssignment2.getText().toString());
            int cat1Value = Integer.parseInt(cat1.getText().toString());
            int cat2Value = Integer.parseInt(cat2.getText().toString());
            int examValue = Integer.parseInt(exam.getText().toString());
            int totalPercentage = calculateTotal(assignment1, assignment2, cat1Value, cat2Value, examValue);

            updateTotalUI(totalPercentage);

        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lecturer");


        iconButton1 = findViewById(R.id.iconButton1);
        iconButton1.setSelected(true);

        iconButton1.setOnClickListener(v -> {
            Intent intent = new Intent(SemesterAdmin.this, SemesterAdmin.class);
            startActivity(intent);
        });
        iconButton2 = findViewById(R.id.iconButton2);
        iconButton2.setOnClickListener(v -> {
            Intent intent = new Intent(SemesterAdmin.this, Semester2.class);
            startActivity(intent);
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String lecturerId = user.getUid();
        }
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(v -> {
            String regNo = editTextRegNo.getText().toString();
            String courseValue = course.getText().toString();
            int assignment1 = Integer.parseInt(editTextAssignment1.getText().toString());
            int assignment2 = Integer.parseInt(editTextAssignment2.getText().toString());
            int cat1Value = Integer.parseInt(cat1.getText().toString());
            int cat2Value = Integer.parseInt(cat2.getText().toString());
            int examValue = Integer.parseInt(exam.getText().toString());
            int total = Integer.parseInt(editTextTotal.getText().toString());

            pushMarksToFirestore(regNo, courseValue, assignment1, assignment2, cat1Value, cat2Value, examValue, total);
        });
    }

    private void updateTotalUI(int totalPercentage) {
        // Update the TextView with the calculated total
        textViewTotal.setText("Total: " + totalPercentage + "%");
    }

    private int calculateTotal(int assignment1, int assignment2, int cat1, int cat2, int exam) {

        double assignmentPercentage = ((double) (assignment1 + assignment2) / 2);
        double catPercentage = ((double) (cat1 + cat2) / 2);
        double examPercentage = ((double) (exam));

        double totalPercentage = assignmentPercentage + catPercentage + examPercentage;

        return (int) Math.round(totalPercentage);
    }

    private void pushMarksToFirestore(String regNo, String courseName, int assignment1, int assignment2, int cat1, int cat2, int exam, int total) {
        // Reference to the student's document based on regNo field
        CollectionReference usersCollection = db.collection("users");
        Query query = usersCollection.whereEqualTo("regNo", regNo);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    DocumentReference studentDocRef = documentSnapshot.getReference();

                    CollectionReference semestersCollection = studentDocRef.collection("semesters");

                    DocumentReference semesterDocRef = semestersCollection.document("semester");

                    semesterDocRef.get().addOnCompleteListener(semesterTask -> {
                        if (semesterTask.isSuccessful()) {
                            DocumentSnapshot semesterSnapshot = semesterTask.getResult();

                            if (semesterSnapshot != null && semesterSnapshot.exists()) {
                                // Semester document exists, check if the entered courseName is in the registeredCourses array
                                List<String> registeredCourses = (List<String>) semesterSnapshot.get("registeredCourses");

                                if (registeredCourses != null && registeredCourses.contains(courseName)) {
                                    CollectionReference marksCollection = semesterDocRef.collection("marks");

                                    DocumentReference marksDocRef = marksCollection.document(courseName);

                                    marksDocRef.get().addOnCompleteListener(marksTask -> {
                                        if (marksTask.isSuccessful()) {
                                            DocumentSnapshot marksSnapshot = marksTask.getResult();

                                            if (marksSnapshot != null && marksSnapshot.exists()) {
                                                // Marks for this course already exist, update the existing marks
                                                marksDocRef.update("assignment1", assignment1,
                                                                "assignment2", assignment2,
                                                                "cat1", cat1,
                                                                "cat2", cat2,
                                                                "exam", exam,
                                                                "total", total)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Log.d("Firestore", "Marks updated successfully");

                                                            // Marks updated successfully
                                                            Toast.makeText(SemesterAdmin.this, "Marks updated successfully", Toast.LENGTH_SHORT).show();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.d("Firestore", "Failed to update marks");
                                                            // Handle the error
                                                            Toast.makeText(SemesterAdmin.this, "Error updating marks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                            } else {
                                                // Marks for this course do not exist, add new marks
                                                Marks marks = new Marks(assignment1, assignment2, cat1, cat2, exam, total);
                                                marksDocRef.set(marks)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Log.d("Firestore", "Marks added successfully");

                                                            // Marks added successfully
                                                            Toast.makeText(SemesterAdmin.this, "Marks added successfully", Toast.LENGTH_SHORT).show();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.d("Firestore", "Failed to add marks");
                                                            // Handle the error
                                                            Toast.makeText(SemesterAdmin.this, "Error adding marks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        } else {
                                            // Handle the error
                                            Toast.makeText(SemesterAdmin.this, "Error fetching marks details: " + marksTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(SemesterAdmin.this, "Invalid courseName", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(SemesterAdmin.this, "Error fetching semester details: " + semesterTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SemesterAdmin.this, "Student not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SemesterAdmin.this, "Error fetching student details: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}