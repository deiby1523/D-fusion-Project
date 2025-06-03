package com.uts.proyecto;

import java.io.Serializable;

public class Course implements Serializable {
    private String id;
    private String name;
    private String courseCode;
    private String classroom;
    private String professor;
    private String professorContact;
    private Schedule schedule;
    private String color;

    public Course() {} // Constructor vacío para Firebase

    public Course(String id, String name, String courseCode, String classroom, String professor, String professorContact, Schedule schedule, String color) {
        this.id = id;
        this.name = name;
        this.courseCode = courseCode;
        this.classroom = classroom;
        this.professor = professor;
        this.professorContact = professorContact;
        this.schedule = schedule;
        this.color = color;
    }

    public Course(String id, String name, String courseCode, String classroom, String professor, String professorContact, Schedule schedule) {
        this.id = id;
        this.name = name;
        this.courseCode = courseCode;
        this.classroom = classroom;
        this.professor = professor;
        this.professorContact = professorContact;
        this.schedule = schedule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getProfessorContact() {
        return professorContact;
    }

    public void setProfessorContact(String professorContact) {
        this.professorContact = professorContact;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

