package com.example.lecturer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LecStudent extends AppCompatActivity {

    private Button buttonStudent;
    private Button buttonLecturer;
    private Button buttonAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lec_student);

        buttonStudent = findViewById(R.id.buttonStudent);
        buttonLecturer = findViewById(R.id.buttonLecturer);
        buttonAdmin = findViewById(R.id.buttonAdmin);

        buttonStudent.setOnClickListener(v -> {
            startActivity(new Intent(LecStudent.this, SignInStudentActivity.class));
        });
        buttonLecturer.setOnClickListener(v -> {
            startActivity(new Intent(LecStudent.this, SignInLecturerActivity.class));
            finish();
        });

        buttonAdmin.setOnClickListener(v -> {
            startActivity(new Intent(LecStudent.this, SignInAdminActivity.class));
        });

    }
}