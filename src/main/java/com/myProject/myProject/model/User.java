package com.myProject.myProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    private String roles;
    private String fullName;

    @OneToMany(mappedBy = "user")
    private List<MyLog> myLogs;


}
