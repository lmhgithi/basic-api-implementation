package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer userId;
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int vote;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userId")
    private List<RsEntity> rsEntity;
}
