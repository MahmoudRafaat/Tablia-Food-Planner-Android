package com.example.tablia.data.auth.models;

public class User {
    private String id;
    private String fullName;
    private String email;
    private String profilePicture;

    public User() {
    }

    public User(String id, String fullName, String email, String profilePicture) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}