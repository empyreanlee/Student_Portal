package com.example.lecturer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpStudentActivity extends AppCompatActivity {

    private EditText Name, Email, Password;

    private TextView textView, textView2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_student);

        Name = findViewById(R.id.editTextName);
        Email = findViewById(R.id.editTextEmail);
        Password = findViewById(R.id.editTextPassword);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);



        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(v -> signUpUser());
        textView2.setOnClickListener(v -> {
            Intent loginIntent = new Intent(getApplicationContext(), SignInStudentActivity.class);
            startActivity(loginIntent);
        });
    }

    private void signUpUser() {
        String name = Name.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-up success, update UI or navigate to the next screen
                        Intent intent = new Intent(SignUpStudentActivity.this, SignInStudentActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignUpStudentActivity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign-up fails, display a message to the user.
                        Toast.makeText(SignUpStudentActivity.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
