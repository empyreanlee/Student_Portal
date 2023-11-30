package com.example.lecturer;

public class Marks {
    private int assignment1;
    private int assignment2;
    private int cat1;
    private int cat2;
    private int exam;
    private int total;


    // Default constructor required for Firestore deserialization
    public Marks() {}

    public Marks(int assignment1, int assignment2, int cat1, int cat2, int exam, int total){
        this.assignment1 = assignment1;
        this.assignment2 = assignment2;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.exam = exam;
        this.total = total ;
    }

    public int getAssignment1() {
        return assignment1;
    }

    public void setAssignment1(int assignment1) {
        this.assignment1 = assignment1;
    }

    public int getAssignment2() {
        return assignment2;
    }

    public void setAssignment2(int assignment2) {
        this.assignment2 = assignment2;
    }

    public int getCat1() {
        return cat1;
    }

    public void setCat1(int cat1) {
        this.cat1 = cat1;
    }

    public int getCat2() {
        return cat2;
    }

    public void setCat2(int cat2) {
        this.cat2 = cat2;
    }

    public int getExam() {
        return exam;
    }

    public void setExam(int exam) {
        this.exam = exam;
    }

    public int getTotal(){return total;}

    public void setTotal(int total) {
        this.total = total;
    }
}
