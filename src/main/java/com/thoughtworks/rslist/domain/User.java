package com.thoughtworks.rslist.domain;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class User {
    @Size(max = 8)
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int vote = 10;

    public User(String name, String gender, int age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }
}
