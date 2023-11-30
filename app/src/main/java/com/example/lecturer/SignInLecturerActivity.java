package com.example.lecturer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInLecturerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText Email;
    private EditText Password;
    private Button button;
    private FirebaseFirestore db;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_lecturer);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        button = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signupText);

        signUp.setOnClickListener(v ->{
            Intent loginIntent = new Intent(getApplicationContext(), SignUpLecturerActivity.class);
            startActivity(loginIntent);

        });


        button.setOnClickListener(v -> signInUser());
    }

    private void signInUser() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        // Check if the user is already logged in
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            if (!isLecturerEmail(email)) {
                // If the email domain is not valid for a lecturer, show an error
                Toast.makeText(SignInLecturerActivity.this, "Invalid email domain for lecturer", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, signInTask -> {
                        if (signInTask.isSuccessful()) {

                            // Query Firestore to check if the user is a lecturer
                            DocumentReference userDocRef = db.collection("users").document(userId);

                            userDocRef.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // User is a lecturer and has already registered details, redirect to the main activity
                                        Intent intent = new Intent(SignInLecturerActivity.this, Semester1.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // User is a lecturer but has not registered details, redirect to the lecturer registration activity
                                        Intent intent = new Intent(SignInLecturerActivity.this, LecReg.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    // Handle failure
                                    Toast.makeText(SignInLecturerActivity.this, "Error checking lecturer details", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign-in fails, display a message to the user.
                            Toast.makeText(SignInLecturerActivity.this, "Sign-in failed: " + signInTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


// Check if the email belongs to a lecturer
            private boolean isLecturerEmail (String email){
                // Modify this logic based on how you structure lecturer emails
                return email.endsWith("@university.com");
            }
        }

