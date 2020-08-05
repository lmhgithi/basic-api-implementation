package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class User {
    @Size(max = 8)
    @NotNull
    @JsonProperty("user_name")
    private String name;

    @NotNull
    @JsonProperty("user_gender")
    private String gender;

    @Max(100)
    @Min(18)
    @JsonProperty("user_age")
    private int age;

    @Email
    @JsonProperty("user_email")
    private String email;

    @Pattern(regexp = "1[0-9]{10}")
    @JsonProperty("user_phone")
    private String phone;

    private int vote = 10;

    public User(String name, String gender, int age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public User(User user){
        this.name = user.name;
        this.gender = user.gender;
        this.age = user.age;
        this.email = user.email;
        this.phone = user.phone;
    }
}
