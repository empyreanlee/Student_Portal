package com.example.lecturer;

import java.util.ArrayList;

public class SemesterData {
    private String name;
    private String regNo;
    private int semester;
    private ArrayList<String> registeredCourses;

    public SemesterData() {
        // Default constructor required for Firestore
    }

    public SemesterData(int semester, ArrayList<String> registeredCourses) {
        this.semester = semester;
        this.registeredCourses = registeredCourses;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public ArrayList<String> getRegisteredCourses() {
        return registeredCourses;
    }

    public void setRegisteredCourses(ArrayList<String> registeredCourses) {
        this.registeredCourses = registeredCourses;
    }
}
