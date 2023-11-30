package com.example.lecturer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class Semester2 extends AppCompatActivity {

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

        button.setOnClickListener(v ->{
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
        getSupportActionBar().setTitle("Lecturer 2");

        iconButton1 = findViewById(R.id.iconButton1);
        iconButton1.setOnClickListener(v -> {
            Intent intent = new Intent(Semester2.this, Semester1.class);
            startActivity(intent);
        });

        iconButton2 = findViewById(R.id.iconButton2);
        iconButton2.setSelected(true);
        iconButton2.setOnClickListener(v -> {
            Intent intent = new Intent(Semester2.this, Semester2.class);
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

            pushMarksToFirestore(regNo, courseValue, assignment1, assignment2, cat1Value, cat2Value, examValue,total);
        });
    }
    private void updateTotalUI(int totalPercentage) {
        // Update the TextView with the calculated total
        textViewTotal.setText("Total: " + totalPercentage+ "%");
    }
    private int calculateTotal(int assignment1, int assignment2, int cat1, int cat2, int exam) {
        int assignmentWeight = 10;
        int catWeight = 20;
        int examWeight = 70;


        double assignmentPercentage =((double)(assignment1 + assignment2)/2);
        double catPercentage = ((double)(cat1 + cat2)/2);
        double examPercentage = ((double)(exam));

        double  totalPercentage = assignmentPercentage + catPercentage + examPercentage;

        return(int) Math.round(totalPercentage);
    }

    private void pushMarksToFirestore(String regNo, String courseName, int assignment1, int assignment2, int cat1, int cat2, int exam, int total) {

        // Reference to the student's document based on regNo field
        CollectionReference usersCollection = db.collection("users");
        Query query = usersCollection.whereEqualTo("regNo", regNo);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                // Check if the document exists (assuming regNo is unique)
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    DocumentReference studentDocRef = documentSnapshot.getReference();

                    // Reference to the semester document
                    CollectionReference semestersCollection = studentDocRef.collection("semesters");

                    // Get the current semester or create a new one (you can modify this logic)
                    DocumentReference semesterDocRef = semestersCollection.document("Semester");

                    semesterDocRef.get().addOnCompleteListener(semesterTask -> {
                        if (semesterTask.isSuccessful()) {
                            DocumentSnapshot semesterSnapshot = semesterTask.getResult();

                            if (semesterSnapshot != null && semesterSnapshot.exists()) {
                                // Semester document exists, check if the entered courseName is in the registeredCourses array
                                List<String> registeredCourses = (List<String>) semesterSnapshot.get("registeredCourses");

                                if (registeredCourses != null && registeredCourses.contains(courseName)) {
                                    // The entered courseName is valid, proceed to add or update marks

                                    // Reference to the marks subcollection
                                    CollectionReference marksCollection = semesterDocRef.collection("marks");

                                    // Check if marks for this course already exist
                                    DocumentReference marksDocRef = marksCollection.document(courseName);

                                    marksDocRef.get().addOnCompleteListener(marksTask -> {
                                        if (marksTask.isSuccessful()) {
                                            DocumentSnapshot marksSnapshot = marksTask.getResult();

                                            if (marksSnapshot != null && marksSnapshot.exists()) {
                                                // Marks for this course already exist, prevent editing
                                                Toast.makeText(Semester2.this, "Marks for this course already exist. Editing not allowed.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Marks for this course don't exist, proceed to add marks

                                                // Create a Marks object (a model class representing the marks)
                                                Marks marks = new Marks(assignment1, assignment2, cat1, cat2, exam, total);

                                                // Set the marks in the document
                                                marksDocRef.set(marks)
                                                        .addOnSuccessListener(aVoid -> {
                                                            // Marks added successfully
                                                            Toast.makeText(Semester2.this, "Marks added successfully", Toast.LENGTH_SHORT).show();
                                                            Log.d("Firestore", "Total" + total );
                                                            navigateToNextActivity();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Handle the error
                                                            Toast.makeText(Semester2.this, "Error adding marks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        } else {
                                            // Handle the error
                                            Toast.makeText(Semester2.this, "Error fetching marks details: " + marksTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // The entered courseName is not in the registeredCourses array
                                    Toast.makeText(Semester2.this, "Invalid courseName", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            // Handle the error
                            Toast.makeText(Semester2.this, "Error fetching semester details: " + semesterTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle the case where the document is not found
                    Toast.makeText(Semester2.this, "Student not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the error
                Toast.makeText(Semester2.this, "Error fetching student details: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void navigateToNextActivity() {
        Intent intent = new Intent(this, LecStudent.class);

        startActivity(intent);
        finish();
    }
}