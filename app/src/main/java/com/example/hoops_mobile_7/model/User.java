package com.example.hoops_mobile_7.model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String age;
    private String gender;

    public User(String email, String password, String age, String gender) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getAge() { return age; }
    public String getGender() { return gender; }
}
