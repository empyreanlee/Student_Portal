package com.example.lecturer;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private String regNo;

    private List<String> failedCourses;

    public Student(String name, String regNo) {
        this.name = name;
        this.regNo = regNo;

        this.failedCourses = new ArrayList<>();
    }

    public Student(String name, String regNo, List<String> failedCourses) {
        this.name = name;
        this.regNo = regNo;

        this.failedCourses = failedCourses;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }



    public List<String> getFailedCourses() {
        return failedCourses;
    }

    public void setFailedCourses(List<String> failedCourses) {
        this.failedCourses = failedCourses;
    }
}
