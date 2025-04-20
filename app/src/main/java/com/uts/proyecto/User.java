package com.uts.proyecto;

public class User {
    public String name;

    // Empty constructor required for Firebase
    public User() {
    }
    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
