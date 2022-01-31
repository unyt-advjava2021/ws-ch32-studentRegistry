/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eltonb.ws.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elton.ballhysa
 */
public class StudentData {
    
    private int id;
    private String name;
    private String surname;
    private int totalCredits;
    private double gpa;
    private List<CourseData> courses;
    
    public StudentData() {
        courses = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public List<CourseData> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseData> courses) {
        this.courses = courses;
    }
    
    
    
}
