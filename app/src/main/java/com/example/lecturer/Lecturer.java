package com.example.lecturer;

public class Lecturer {
    private String employeeId;
    private String name;
    private String courseName;
    private String role;

    public Lecturer() {
    }

    public Lecturer(String employeeId, String name, String courseName, String role) {
        this.employeeId = employeeId;
        this.name = name;
        this.courseName = courseName;
        this.role = role;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
