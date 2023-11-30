package com.example.lecturer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LecReg extends AppCompatActivity {
    private EditText editTextEmployeeId, editTextName, editTextCourseName, editTextRole;
    private Button buttonRegister;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lec_reg);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextEmployeeId = findViewById(R.id.editTextEmployeeId);
        editTextName = findViewById(R.id.editTextName);
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextRole = findViewById(R.id.editTextRole);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(v -> registerLecturer());
    }

    private void registerLecturer() {
        // Get values from input fields
        String employeeId = editTextEmployeeId.getText().toString();
        String name = editTextName.getText().toString();
        String courseName = editTextCourseName.getText().toString();
        String role = editTextRole.getText().toString();

        // TODO: Perform registration logic, e.g., store data in Firestore
        String userId = mAuth.getCurrentUser().getUid();
        CollectionReference usersCollection = db.collection("users");
        DocumentReference lecturerDocRef = usersCollection.document(userId);

        // Create a Lecturer object
        Lecturer lecturer = new Lecturer(employeeId, name, courseName, role);

        // Set the data in Firestore
        lecturerDocRef.set(lecturer)
                .addOnSuccessListener(aVoid -> {
                    // Document successfully written
                    Toast.makeText(this, "Lecturer registered successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LecReg.this, Semester1.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(this, "Error adding lecturer information", Toast.LENGTH_SHORT).show();
                });
    }
}
