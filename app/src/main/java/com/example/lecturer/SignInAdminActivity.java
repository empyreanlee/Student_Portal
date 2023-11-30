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

public class SignInAdminActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText Email;
    private EditText Password;
    private Button button;
    private FirebaseFirestore db;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_student);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        button = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signupText);


        signUp.setOnClickListener(v ->{
            Intent loginIntent = new Intent(getApplicationContext(), SignUpAdminActivity.class);
            startActivity(loginIntent);

        });

        button.setOnClickListener(v -> signInUser());
    }

    private void signInUser() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String userId = mAuth.getCurrentUser().getUid();

        // Query Firestore to check if the student has registered for courses
        DocumentReference studentDocRef = db.collection("users").document(userId);

                    // If not registered, proceed with email/password sign-in
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, signInTask -> {
                                if (signInTask.isSuccessful()) {
                                    // Sign-in success, update UI or navigate to the next screen
                                    Intent intent = new Intent(SignInAdminActivity.this, AdminPage.class);
                                    startActivity(intent);

                                } else {
                                    // If sign-in fails, display a message to the user.
                                    Toast.makeText(SignInAdminActivity.this, "Sign-in failed: " + signInTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

