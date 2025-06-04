package com.uts.proyecto;

import java.io.Serializable;

public class Task implements Serializable {
    private String taskId;
    private String courseId;
    private String courseName;
    private String courseColor;

    private String title;
    private String description;
    private String dueDate;
    private boolean completed;

    public Task() {
    }

    public Task(String taskId, String courseId, String courseName, String courseColor, String title, String description, String dueDate, boolean completed) {
        this.taskId = taskId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseColor = courseColor;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseColor() {
        return courseColor;
    }

    public void setCourseColor(String courseColor) {
        this.courseColor = courseColor;
    }
}
